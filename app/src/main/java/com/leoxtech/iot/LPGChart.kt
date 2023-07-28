package com.leoxtech.iot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.*

class LPGChart : AppCompatActivity() {

    private lateinit var lineChartLPGChart: LineChart
    private lateinit var lineChartH2Chart: LineChart
    private lateinit var lineChartCH4Chart: LineChart
    private lateinit var lineChartCOChart: LineChart
    private lateinit var lineChartAlcoholChart: LineChart
    private lateinit var cardViewBack: MaterialCardView

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lpgchart)

        initializeComponents()

        getFirebaseData()

        clickListeners()

    }

    private fun clickListeners() {
        cardViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getFirebaseData() {

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_6_LPG_GAS").child("LPG")
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
                val dataSetLPG = LineDataSet(entries, "LPG")
                dataSetLPG.color = resources.getColor(R.color.color6)
                dataSetLPG.valueTextColor = resources.getColor(R.color.black)
                dataSetLPG.lineWidth = 4f
                dataSetLPG.valueTextSize = 14f

                val lineData = LineData(dataSetLPG)
                lineChartLPGChart.data = lineData
                lineChartLPGChart.invalidate()
                lineChartLPGChart.notifyDataSetChanged()
                lineChartLPGChart.setDrawGridBackground(false)
                lineChartLPGChart.axisLeft.setDrawGridLines(false);
                lineChartLPGChart.xAxis.setDrawGridLines(false);
                lineChartLPGChart.axisRight.setDrawGridLines(false);
                lineChartLPGChart.isAutoScaleMinMaxEnabled = true
                lineChartLPGChart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LPGChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_6_LPG_GAS").child("H2")
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
                val dataSetH2 = LineDataSet(entries, "H2")
                dataSetH2.color = resources.getColor(R.color.app_color1)
                dataSetH2.valueTextColor = resources.getColor(R.color.black)
                dataSetH2.lineWidth = 4f
                dataSetH2.valueTextSize = 14f

                val lineData = LineData(dataSetH2)
                lineChartH2Chart.data = lineData
                lineChartH2Chart.invalidate()
                lineChartH2Chart.notifyDataSetChanged()
                lineChartH2Chart.setDrawGridBackground(false)
                lineChartH2Chart.axisLeft.setDrawGridLines(false);
                lineChartH2Chart.xAxis.setDrawGridLines(false);
                lineChartH2Chart.axisRight.setDrawGridLines(false);
                lineChartH2Chart.isAutoScaleMinMaxEnabled = true
                lineChartH2Chart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LPGChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_6_LPG_GAS").child("CH4")
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
                val dataSetCH4 = LineDataSet(entries, "CH4")
                dataSetCH4.color = resources.getColor(R.color.light_blue_900)
                dataSetCH4.valueTextColor = resources.getColor(R.color.black)
                dataSetCH4.lineWidth = 4f
                dataSetCH4.valueTextSize = 14f

                val lineData = LineData(dataSetCH4)
                lineChartCH4Chart.data = lineData
                lineChartCH4Chart.invalidate()
                lineChartCH4Chart.notifyDataSetChanged()
                lineChartCH4Chart.setDrawGridBackground(false)
                lineChartCH4Chart.axisLeft.setDrawGridLines(false);
                lineChartCH4Chart.xAxis.setDrawGridLines(false);
                lineChartCH4Chart.axisRight.setDrawGridLines(false);
                lineChartCH4Chart.isAutoScaleMinMaxEnabled = true
                lineChartCH4Chart.description.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LPGChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_6_LPG_GAS").child("CO")
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
                dataSetCO.color = resources.getColor(R.color.color8)
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
                Toast.makeText(this@LPGChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("GAS_AIR_PROJECT").child("All_VALUES").child("MQ_SENSORS").child("MQ_6_LPG_GAS").child("ALCOHOL")
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
                val dataSetAlcohol = LineDataSet(entries, "ALCOHOL")
                dataSetAlcohol.color = resources.getColor(R.color.app_color5)
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
                Toast.makeText(this@LPGChart, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initializeComponents() {
        lineChartLPGChart = findViewById(R.id.lineChartLPGChart)
        lineChartH2Chart = findViewById(R.id.lineChartH2Chart)
        lineChartCH4Chart = findViewById(R.id.lineChartCH4Chart)
        lineChartCOChart = findViewById(R.id.lineChartCOChart)
        lineChartAlcoholChart = findViewById(R.id.lineChartAlcoholChart)
        cardViewBack = findViewById(R.id.cardViewBack)
    }
}