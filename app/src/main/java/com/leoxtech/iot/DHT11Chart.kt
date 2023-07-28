package com.leoxtech.iot

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DHT11Chart : AppCompatActivity() {

    private lateinit var lineChartTemperatureChart: LineChart
    private lateinit var lineChartHumidityChart: LineChart

    private lateinit var cardViewBack: MaterialCardView

    private lateinit var dbRef: DatabaseReference
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dht11_chart)

        initializeComponents()

        dialogBox()

        getFirebaseData()

        clickListeners()

    }

    private fun getFirebaseData() {
        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("DHT11_SENSOR").child("TEMPERATURE")
        dbRef.limitToLast(10).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get data from Firebase and set that data into line chart view mikephil library
                val entries = ArrayList<Entry>()
                var i = 0
                for (data in snapshot.children) {
                    val value = data.getValue(Float::class.java)
                    entries.add(Entry(i.toFloat(), value!!))
                    i++
                }
                val dataSetTemperature = LineDataSet(entries, "Temperature")
                dataSetTemperature.color = resources.getColor(R.color.color6)
                dataSetTemperature.valueTextColor = resources.getColor(R.color.black)
                dataSetTemperature.lineWidth = 4f
                dataSetTemperature.valueTextSize = 14f

                val lineTemperature = LineData(dataSetTemperature)
                lineChartTemperatureChart.data = lineTemperature
                lineChartTemperatureChart.invalidate()
                lineChartTemperatureChart.notifyDataSetChanged()
                lineChartTemperatureChart.setDrawGridBackground(false)
                lineChartTemperatureChart.axisLeft.setDrawGridLines(false);
                lineChartTemperatureChart.xAxis.setDrawGridLines(false);
                lineChartTemperatureChart.axisRight.setDrawGridLines(false);
                lineChartTemperatureChart.isAutoScaleMinMaxEnabled = true
                lineChartTemperatureChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DHT11Chart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("DHT11_SENSOR").child("HUMIDITY")
        dbRef.limitToLast(10).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get data from Firebase and set that data into line chart view mikephil library
                val entries = ArrayList<Entry>()
                var i = 0
                for (data in snapshot.children) {
                    val value = data.getValue(Float::class.java)
                    entries.add(Entry(i.toFloat(), value!!))
                    i++
                }
                val dataSetHumidity = LineDataSet(entries, "Humidity")
                dataSetHumidity.color = resources.getColor(R.color.color11)
                dataSetHumidity.valueTextColor = resources.getColor(R.color.black)
                dataSetHumidity.lineWidth = 4f
                dataSetHumidity.valueTextSize = 14f

                val lineHumidity = LineData(dataSetHumidity)
                lineChartHumidityChart.data = lineHumidity
                lineChartHumidityChart.invalidate()
                lineChartHumidityChart.notifyDataSetChanged()
                lineChartHumidityChart.setDrawGridBackground(false)
                lineChartHumidityChart.axisLeft.setDrawGridLines(false);
                lineChartHumidityChart.xAxis.setDrawGridLines(false);
                lineChartHumidityChart.axisRight.setDrawGridLines(false);
                lineChartHumidityChart.isAutoScaleMinMaxEnabled = true
                lineChartHumidityChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DHT11Chart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.dismiss()
    }

    private fun initializeComponents() {
        lineChartTemperatureChart = findViewById(R.id.lineChartTemperatureChart)
        lineChartHumidityChart = findViewById(R.id.lineChartHumidityChart)
        cardViewBack = findViewById(R.id.cardViewBack)
    }

    private fun clickListeners() {
        cardViewBack.setOnClickListener {
            onBackPressed()
        }
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