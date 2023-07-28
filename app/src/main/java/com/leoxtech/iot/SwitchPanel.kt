package com.leoxtech.iot

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SwitchPanel : AppCompatActivity() {

    private lateinit var switchElectricEquipments: MaterialSwitch
    private lateinit var txtElectricEquipmentsStatus: TextView
    private lateinit var imgElectricEquipments: ImageView

    private lateinit var switchAirBlower: MaterialSwitch
    private lateinit var txtAirBlowerStatus: TextView
    private lateinit var imgAirBlower: ImageView

    private lateinit var dbRef: DatabaseReference
    private lateinit var dialog: AlertDialog

    private lateinit var cardViewBack: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_panel)

        initialize()

        dialogBox()

        getFirebaseData()

        updateFirebaseSwitchPanel()

        clickListeners()

    }

    private fun clickListeners() {
        cardViewBack.setOnClickListener {
            finish()
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
                    var statusAllElectronicEquipments = dataSnapshot.child("SWITCH_PANEL").child("RELAYS").child("ALL_ELECTRONIC_EQUIP").value.toString()
                    var statusAirBlower = dataSnapshot.child("SWITCH_PANEL").child("RELAYS").child("AIR_BLOWER").value.toString()

                    updateSwitchPanel(statusAllElectronicEquipments, statusAirBlower)

                }else{
                    dialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SwitchPanel, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
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
        switchElectricEquipments = findViewById(R.id.switchElectricEquipments)
        txtElectricEquipmentsStatus = findViewById(R.id.txtElectricEquipmentsStatus)
        imgElectricEquipments = findViewById(R.id.imgElectricEquipments)
        switchAirBlower = findViewById(R.id.switchAirBlower)
        txtAirBlowerStatus = findViewById(R.id.txtAirBlowerStatus)
        imgAirBlower = findViewById(R.id.imgAirBlower)
        cardViewBack = findViewById(R.id.cardViewBack)
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