package com.leoxtech.iot

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {

    private lateinit var txtHumidity: TextView
    private lateinit var txtTemperature: TextView
    private lateinit var txtSSID: TextView
    private lateinit var txtStatus: TextView
    private lateinit var txtLPGLvl: TextView
    private lateinit var txtH2Lvl: TextView
    private lateinit var txtCH4Lvl: TextView
    private lateinit var txtCOLvl: TextView
    private lateinit var txtAlcoholLvl: TextView
    private lateinit var txtMQ135COLvl: TextView
    private lateinit var txtMQ135AlcoholLvl: TextView
    private lateinit var txtMQ135CO2Lvl: TextView
    private lateinit var txtMQ135TolueneLvl: TextView
    private lateinit var txtMQ135NH4Lvl: TextView
    private lateinit var txtMQ135AcetoneLvl: TextView
    private lateinit var txtDataNotFound: TextView
    private lateinit var layoutMain: LinearLayout

    private lateinit var switchElectricEquipments: MaterialSwitch
    private lateinit var txtElectricEquipmentsStatus: TextView
    private lateinit var imgElectricEquipments: ImageView

    private lateinit var switchAirBlower: MaterialSwitch
    private lateinit var txtAirBlowerStatus: TextView
    private lateinit var imgAirBlower: ImageView
    private lateinit var imgWIFI: ImageView

    private lateinit var cardViewMQ6AllTimeData: MaterialCardView
    private lateinit var cardViewMQ135AllTimeData: MaterialCardView
    private lateinit var cardViewDHT11Chart: MaterialCardView
    private lateinit var cardViewSwitchPanel: MaterialCardView

    private lateinit var dbRef: DatabaseReference
    private lateinit var dialog: AlertDialog

    private val CHANNEL_ID = "CHANNEL_ID"
    private val notificationID = 101

    private var isSend = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        dialogBox()

        getFirebaseData()

        updateFirebaseSwitchPanel()

        clickListeners()

        createNotificationChannel()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(titleMessage: String, descriptionMessage: String, shortDescriptionMessage: String) {

        val intent: Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(titleMessage)
            .setContentText(shortDescriptionMessage)
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(descriptionMessage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(notificationID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(android.app.NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun clickListeners() {
        cardViewMQ6AllTimeData.setOnClickListener {
            startActivity(Intent(this, LPGChart::class.java))
        }

        cardViewMQ135AllTimeData.setOnClickListener {
            startActivity(Intent(this, AirQualityChart::class.java))
        }

        cardViewSwitchPanel.setOnClickListener {
            startActivity(Intent(this, SwitchPanel::class.java))
        }

        cardViewDHT11Chart.setOnClickListener {
            startActivity(Intent(this, DHT11Chart::class.java))
        }
    }

    private fun updateFirebaseSwitchPanel() {
        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("SWITCH_PANEL").child("RELAYS")

        switchElectricEquipments.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbRef.child("ALL_ELECTRONIC_EQUIP").setValue("ON")
                imgElectricEquipments.setImageResource(R.drawable.connected)
                txtElectricEquipmentsStatus.text = "Equipments are ON"
            } else {
                dbRef.child("ALL_ELECTRONIC_EQUIP").setValue("OFF")
                switchElectricEquipments.thumbIconDrawable = null
                imgElectricEquipments.setImageResource(R.drawable.disconnected)
                txtElectricEquipmentsStatus.text = "Equipments are OFF"
            }
        }

        switchAirBlower.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbRef.child("AIR_BLOWER").setValue("ON")
                imgAirBlower.setImageResource(R.drawable.fan)
                txtAirBlowerStatus.text = "Air Blower is ON"
            } else {
                dbRef.child("AIR_BLOWER").setValue("OFF")
                switchAirBlower.thumbIconDrawable = null
                imgAirBlower.setImageResource(R.drawable.air_quality)
                txtAirBlowerStatus.text = "Air Blower is OFF"
            }
        }
    }

    private fun getFirebaseData() {
        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT")
        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    txtSSID.text = "Connected to: ${dataSnapshot.child("CONNECTION").child("SSID").value.toString()}"
                    imgWIFI.visibility = View.VISIBLE
                    txtHumidity.text = dataSnapshot.child("CURRENT_VALUES").child("DHT11_SENSOR").child("HUMIDITY").value.toString()
                    txtTemperature.text = dataSnapshot.child("CURRENT_VALUES").child("DHT11_SENSOR").child("TEMPERATURE").value.toString()
                    txtLPGLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("LPG").value.toString()
                    txtH2Lvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("H2").value.toString()
                    txtCH4Lvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("CH4").value.toString()
                    txtCOLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("CO").value.toString()
                    txtAlcoholLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("ALCOHOL").value.toString()

                    txtMQ135COLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("CO").value.toString()
                    txtMQ135AlcoholLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("ALCOHOL").value.toString()
                    txtMQ135CO2Lvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("CO2").value.toString()
                    txtMQ135TolueneLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("TOLUENE").value.toString()
                    txtMQ135NH4Lvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("NH4").value.toString()
                    txtMQ135AcetoneLvl.text = dataSnapshot.child("CURRENT_VALUES").child("MQ_135_SENSOR").child("ACETONE").value.toString()

                    val statusAllElectronicEquipments = dataSnapshot.child("SWITCH_PANEL").child("RELAYS").child("ALL_ELECTRONIC_EQUIP").value.toString()
                    val statusAirBlower = dataSnapshot.child("SWITCH_PANEL").child("RELAYS").child("AIR_BLOWER").value.toString()

                    updateSwitchPanel(statusAllElectronicEquipments, statusAirBlower)

                    // LPG Level Notification Trigger (MQ-6 Sensor)
                    if (dataSnapshot.child("DETECTION").child("GAS").child("LPG").value.toString() >= "YES"){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            sendNotification("LPG Level is High", "We regret to inform you that the detected gas levels are exceeded your threshold gas levels. (LPG Level is ${dataSnapshot.child("CURRENT_VALUES").child("MQ_6_SENSOR").child("LPG").value.toString()}) This is a critical situation that requires immediate attention.", "Gas Levels are Exceeded.")
                        }
                    }

                }else{
                    txtDataNotFound.visibility = View.VISIBLE
                    layoutMain.visibility = View.INVISIBLE
                    dialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateSwitchPanel(statusAllElectronicEquipments: String, statusAirBlower: String) {
        if (statusAllElectronicEquipments == "ON") {
            switchElectricEquipments.isChecked = true
            imgElectricEquipments.setImageResource(R.drawable.connected)
            txtElectricEquipmentsStatus.text = "Equipments are ON"
        } else {
            switchElectricEquipments.isChecked = false
            imgElectricEquipments.setImageResource(R.drawable.disconnected)
            txtElectricEquipmentsStatus.text = "Equipments are OFF"
        }

        if (statusAirBlower == "ON"){
            switchAirBlower.isChecked = true
            imgAirBlower.setImageResource(R.drawable.fan)
            txtAirBlowerStatus.text = "Air blower is ON"
        } else {
            switchAirBlower.isChecked = false
            imgAirBlower.setImageResource(R.drawable.air_quality)
            txtAirBlowerStatus.text = "Air blower is OFF"
        }

        dialog.dismiss()
    }

    private fun initialize() {
        txtHumidity = findViewById(R.id.txtHumidity)
        txtTemperature = findViewById(R.id.txtTemperature)
        txtSSID = findViewById(R.id.txtSSID)
        txtStatus = findViewById(R.id.txtStatus)
        txtLPGLvl = findViewById(R.id.txtLPGLvl)
        txtH2Lvl = findViewById(R.id.txtH2Lvl)
        txtCH4Lvl = findViewById(R.id.txtCH4Lvl)
        txtCOLvl = findViewById(R.id.txtCOLvl)
        txtAlcoholLvl = findViewById(R.id.txtAlcoholLvl)
        switchElectricEquipments = findViewById(R.id.switchElectricEquipments)
        txtElectricEquipmentsStatus = findViewById(R.id.txtElectricEquipmentsStatus)
        imgElectricEquipments = findViewById(R.id.imgElectricEquipments)
        switchAirBlower = findViewById(R.id.switchAirBlower)
        txtAirBlowerStatus = findViewById(R.id.txtAirBlowerStatus)
        imgAirBlower = findViewById(R.id.imgAirBlower)
        txtDataNotFound = findViewById(R.id.txtDataNotFound)
        layoutMain = findViewById(R.id.layoutMain)
        txtMQ135COLvl = findViewById(R.id.txtMQ135COLvl)
        txtMQ135AlcoholLvl = findViewById(R.id.txtMQ135AlcoholLvl)
        txtMQ135CO2Lvl = findViewById(R.id.txtMQ135CO2Lvl)
        txtMQ135TolueneLvl = findViewById(R.id.txtMQ135TolueneLvl)
        txtMQ135NH4Lvl = findViewById(R.id.txtMQ135NH4Lvl)
        txtMQ135AcetoneLvl = findViewById(R.id.txtMQ135AcetoneLvl)
        cardViewMQ6AllTimeData = findViewById(R.id.cardViewMQ6AllTimeData)
        cardViewMQ135AllTimeData = findViewById(R.id.cardViewMQ135AllTimeData)
        imgWIFI = findViewById(R.id.imgWIFI)
        cardViewSwitchPanel = findViewById(R.id.cardViewSwitchPanel)
        cardViewDHT11Chart = findViewById(R.id.cardViewDHT11Chart)
    }

    private fun dialogBox() {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setView(R.layout.progress_dialog)
        }.create().also {
            dialog = it
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }
    }
}