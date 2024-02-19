package com.example.mc_assignment_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mc_assignment_1.ui.theme.MC_assignment_1Theme
import com.google.mlkit.vision.text.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MC_assignment_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    var stops by remember { mutableStateOf(
        listOf(
            Stop("Stop 1", 0),
            Stop("Stop 2", 10),
            Stop("Stop 3", 5),
            Stop("Stop 4", 20),
            Stop("Stop 5", 15),
            Stop("Stop 6", 8),
            Stop("Stop 7", 30),
            Stop("Stop 8", 25),
            Stop("Stop 9", 12),
            Stop("Stop 10", 18),
            Stop("Stop 11", 40),
            Stop("Stop 12", 35),
            Stop("Stop 13", 28),
            Stop("Stop 14", 45),
            Stop("Stop 15", 50),
            Stop("Stop 16", 22),
            Stop("Stop 17", 17),
            Stop("Stop 18", 14),
            Stop("Stop 19", 9),
            Stop("Stop 20", 1)
        )
    ) }
    var currentStopIndex by remember { mutableStateOf(0) }
    var isMiles by remember { mutableStateOf(false) }
    var isLazyList by remember { mutableStateOf(true) }

    var isScrollable by remember { mutableStateOf(true) }

    var circularIndex = currentStopIndex % stops.size
    if(!isScrollable){
        circularIndex = currentStopIndex % 10
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            Switch(
                checked = isScrollable,
                onCheckedChange = { isScrollable = it },
//                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
            Text(
                text = if (isScrollable) "Scrollable" else "Non-scrollable",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.h6

            )
            Button(onClick = { currentStopIndex++ }) {
                Text("Next")
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(color = Color(0xffccefff), shape = MaterialTheme.shapes.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total Distance Covered:",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (isMiles) "${calculateTotalDistance(stops, circularIndex, isScrollable).distanceCoveredInMiles().roundTo2Decimal()} miles" else "${calculateTotalDistance(stops, circularIndex, isScrollable).distanceCovered} km",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Switch(
                    checked = isMiles,
                    onCheckedChange = { isMiles = it },
//                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total Distance Left:",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = if (isMiles) "${calculateTotalDistance(stops, circularIndex, isScrollable).distanceLeftInMiles().roundTo2Decimal()} miles" else "${calculateTotalDistance(stops, circularIndex, isScrollable).distanceLeft} km",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Switch(
                    checked = isMiles,
                    onCheckedChange = { isMiles = it },
//                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                )
            }
        }

        ProgressDetails(
            stops = stops,
            currentStopIndex = circularIndex,
            isMiles = isMiles,
            isScrollable = isScrollable // Pass isScrollable parameter here
        )



        if (isLazyList) {
            if (isScrollable) {
                LazyStopList(
                    stops = stops,
                    currentStopIndex = circularIndex,
                    isMiles = isMiles
                )
            } else {
                NonScrollableStopList(
                    stops = stops,
                    currentStopIndex = circularIndex,
                    isMiles = isMiles
                )
            }
        } else {
            NormalStopList(
                stops = stops,
                currentStopIndex = circularIndex,
                isMiles = isMiles
            )
        }
    }
}

@Composable
fun NonScrollableStopList(stops: List<Stop>, currentStopIndex: Int, isMiles: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        stops.forEachIndexed { index, stop ->
            StopItem(stop = stop, isCurrentStop = index == currentStopIndex, isMiles)
        }
    }
}


data class Stop(val name: String, val distanceToPreviousStop: Int)

data class TotalDistance(val distanceCovered: Int, val distanceLeft: Int) {
    fun distanceCoveredInMiles(): Double {
        return distanceCovered * 0.621371
    }

    fun distanceLeftInMiles(): Double {
        return distanceLeft * 0.621371
    }
}

fun calculateTotalDistance(stops: List<Stop>, currentStopIndex: Int, isScrollable: Boolean): TotalDistance {
    var distanceCovered = 0
    var distanceLeft = 0

    if (isScrollable) {
        for (i in 0..currentStopIndex) {
            distanceCovered += stops[i].distanceToPreviousStop
        }
        for (i in currentStopIndex + 1 until stops.size) {
            distanceLeft += stops[i].distanceToPreviousStop
        }
    } else {
        for (i in 0..currentStopIndex) {
            distanceCovered += stops[i].distanceToPreviousStop
        }
        for (i in currentStopIndex + 1 until minOf(stops.size, 10)) {
            distanceLeft += stops[i].distanceToPreviousStop
        }
    }

    return TotalDistance(distanceCovered, distanceLeft)
}

@Composable
fun ProgressDetails(
    stops: List<Stop>,
    currentStopIndex: Int,
    isMiles: Boolean,
    isScrollable: Boolean
) {
    val totalDistance: TotalDistance
    val progressPercentage: Float

    if (isScrollable) {
        totalDistance = if (isMiles) calculateTotalDistance(stops, currentStopIndex, isScrollable) else calculateTotalDistance(stops, currentStopIndex, isScrollable)
        progressPercentage = if (isMiles) {
            ((totalDistance.distanceCoveredInMiles() / (totalDistance.distanceCoveredInMiles() + totalDistance.distanceLeftInMiles())) * 100).toFloat()
        } else {
            ((totalDistance.distanceCovered.toFloat() / (totalDistance.distanceCovered + totalDistance.distanceLeft)) * 100).toFloat()
        }
    } else {
        totalDistance = if (isMiles) calculateTotalDistance(stops.take(10), currentStopIndex, isScrollable) else calculateTotalDistance(stops.take(10), currentStopIndex, isScrollable)
        val distanceCovered = if (isMiles) totalDistance.distanceCoveredInMiles() else totalDistance.distanceCovered
        val distanceLeft = if (isMiles) totalDistance.distanceLeftInMiles() else totalDistance.distanceLeft
        progressPercentage = if (totalDistance.distanceCovered + totalDistance.distanceLeft == 0) 0f else (distanceCovered.toFloat() / (distanceCovered.toFloat() + distanceLeft.toFloat())) * 100
    }

    val isLastStop = currentStopIndex == stops.size - 1
    val progress = if (isLastStop) 1f else progressPercentage / 100

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Text(
            text = "PROGRESS: ${"%.2f".format(progressPercentage)}%",
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LazyStopList(stops: List<Stop>, currentStopIndex: Int, isMiles: Boolean) {
    LazyColumn(
        state = rememberLazyListState(currentStopIndex),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(stops) { index, stop ->
            StopItem(stop = stop, isCurrentStop = index == currentStopIndex, isMiles = isMiles)
        }
    }
}

@Composable
fun NormalStopList(stops: List<Stop>, currentStopIndex: Int, isMiles: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        stops.forEachIndexed { index, stop ->
            StopItem(stop = stop, isCurrentStop = index == currentStopIndex, isMiles = isMiles)
        }
    }
}

@Composable
fun StopItem(stop: Stop, isCurrentStop: Boolean, isMiles: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (isCurrentStop) Color(0xffffff99) else Color.Transparent)
    ) {
        val distance = if (isMiles) "${(stop.distanceToPreviousStop * 0.621371).roundTo2Decimal()} miles" else "${stop.distanceToPreviousStop} km"
        Text(
            text = "${stop.name} - $distance",
            fontSize = 24.sp,
            modifier = Modifier.padding(0.dp)
        )
    }
}

fun Double.roundTo2Decimal(): Double {
    return (this * 100).toInt() / 100.0
}
