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
import com.google.firebase.database.*

class AirQualityChart : AppCompatActivity() {

    private lateinit var lineChartCOChart: LineChart
    private lateinit var lineChartAlcoholChart: LineChart
    private lateinit var lineChartCO2Chart: LineChart
    private lateinit var lineChartTolueneChart: LineChart
    private lateinit var lineChartNH4Chart: LineChart
    private lateinit var lineChartAcetoneChart: LineChart
    private lateinit var cardViewBack: MaterialCardView

    private lateinit var dbRef: DatabaseReference
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_quality_chart)

        initializeComponents()

        dialogBox()

        getFirebaseData()

        clickListeners()

    }

    private fun clickListeners() {
        cardViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getFirebaseData() {

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("CO")
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
                val dataSetCO = LineDataSet(entries, "CO")
                dataSetCO.color = resources.getColor(R.color.color6)
                dataSetCO.valueTextColor = resources.getColor(R.color.black)
                dataSetCO.lineWidth = 4f
                dataSetCO.valueTextSize = 14f

                val lineData = LineData(dataSetCO)
                lineChartCOChart.data = lineData
                lineChartCOChart.invalidate()
                lineChartCOChart.notifyDataSetChanged()
                lineChartCOChart.setDrawGridBackground(false)
                lineChartCOChart.axisLeft.setDrawGridLines(false);
                lineChartCOChart.xAxis.setDrawGridLines(false);
                lineChartCOChart.axisRight.setDrawGridLines(false);
                lineChartCOChart.isAutoScaleMinMaxEnabled = true
                lineChartCOChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("ALCOHOL")
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
                val dataSetAlcohol = LineDataSet(entries, "Alcohol")
                dataSetAlcohol.color = resources.getColor(R.color.app_color1)
                dataSetAlcohol.valueTextColor = resources.getColor(R.color.black)
                dataSetAlcohol.lineWidth = 4f
                dataSetAlcohol.valueTextSize = 14f

                val lineData = LineData(dataSetAlcohol)
                lineChartAlcoholChart.data = lineData
                lineChartAlcoholChart.invalidate()
                lineChartAlcoholChart.notifyDataSetChanged()
                lineChartAlcoholChart.setDrawGridBackground(false)
                lineChartAlcoholChart.axisLeft.setDrawGridLines(false);
                lineChartAlcoholChart.xAxis.setDrawGridLines(false);
                lineChartAlcoholChart.axisRight.setDrawGridLines(false);
                lineChartAlcoholChart.isAutoScaleMinMaxEnabled = true
                lineChartAlcoholChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("CO2")
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
                val dataSetCO2 = LineDataSet(entries, "CO2")
                dataSetCO2.color = resources.getColor(R.color.light_blue_900)
                dataSetCO2.valueTextColor = resources.getColor(R.color.black)
                dataSetCO2.lineWidth = 4f
                dataSetCO2.valueTextSize = 14f

                val lineData = LineData(dataSetCO2)
                lineChartCO2Chart.data = lineData
                lineChartCO2Chart.invalidate()
                lineChartCO2Chart.notifyDataSetChanged()
                lineChartCO2Chart.setDrawGridBackground(false)
                lineChartCO2Chart.axisLeft.setDrawGridLines(false);
                lineChartCO2Chart.xAxis.setDrawGridLines(false);
                lineChartCO2Chart.axisRight.setDrawGridLines(false);
                lineChartCO2Chart.isAutoScaleMinMaxEnabled = true
                lineChartCO2Chart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("TOLUENE")
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
                val dataSetToluene = LineDataSet(entries, "Toluene")
                dataSetToluene.color = resources.getColor(R.color.color8)
                dataSetToluene.valueTextColor = resources.getColor(R.color.black)
                dataSetToluene.lineWidth = 4f
                dataSetToluene.valueTextSize = 14f

                val lineData = LineData(dataSetToluene)
                lineChartTolueneChart.data = lineData
                lineChartTolueneChart.invalidate()
                lineChartTolueneChart.notifyDataSetChanged()
                lineChartTolueneChart.setDrawGridBackground(false)
                lineChartTolueneChart.axisLeft.setDrawGridLines(false);
                lineChartTolueneChart.xAxis.setDrawGridLines(false);
                lineChartTolueneChart.axisRight.setDrawGridLines(false);
                lineChartTolueneChart.isAutoScaleMinMaxEnabled = true
                lineChartTolueneChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("NH4")
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
                val dataSetNH4 = LineDataSet(entries, "NH4")
                dataSetNH4.color = resources.getColor(R.color.app_color5)
                dataSetNH4.valueTextColor = resources.getColor(R.color.black)
                dataSetNH4.lineWidth = 4f
                dataSetNH4.valueTextSize = 14f

                val lineData = LineData(dataSetNH4)
                lineChartNH4Chart.data = lineData
                lineChartNH4Chart.invalidate()
                lineChartNH4Chart.notifyDataSetChanged()
                lineChartNH4Chart.setDrawGridBackground(false)
                lineChartNH4Chart.axisLeft.setDrawGridLines(false);
                lineChartNH4Chart.xAxis.setDrawGridLines(false);
                lineChartNH4Chart.axisRight.setDrawGridLines(false);
                lineChartNH4Chart.isAutoScaleMinMaxEnabled = true
                lineChartNH4Chart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_135_SENSOR").child("ACETONE")
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
                val dataSetAcetone = LineDataSet(entries, "Acetone")
                dataSetAcetone.color = resources.getColor(R.color.app_color5)
                dataSetAcetone.valueTextColor = resources.getColor(R.color.black)
                dataSetAcetone.lineWidth = 4f
                dataSetAcetone.valueTextSize = 14f

                val lineData = LineData(dataSetAcetone)
                lineChartAcetoneChart.data = lineData
                lineChartAcetoneChart.invalidate()
                lineChartAcetoneChart.notifyDataSetChanged()
                lineChartAcetoneChart.setDrawGridBackground(false)
                lineChartAcetoneChart.axisLeft.setDrawGridLines(false);
                lineChartAcetoneChart.xAxis.setDrawGridLines(false);
                lineChartAcetoneChart.axisRight.setDrawGridLines(false);
                lineChartAcetoneChart.isAutoScaleMinMaxEnabled = true
                lineChartAcetoneChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AirQualityChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.dismiss()

    }

    private fun initializeComponents() {
        lineChartCOChart = findViewById(R.id.lineChartCOChart)
        lineChartAlcoholChart = findViewById(R.id.lineChartAlcoholChart)
        lineChartCO2Chart = findViewById(R.id.lineChartCO2Chart)
        lineChartTolueneChart = findViewById(R.id.lineChartTolueneChart)
        lineChartNH4Chart = findViewById(R.id.lineChartNH4Chart)
        lineChartAcetoneChart = findViewById(R.id.lineChartAcetoneChart)
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