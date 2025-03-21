package com.kt.worktimetrackermanager.presentation.components.chart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun AttendanceRateChart(
    modifier: Modifier = Modifier,
    title: String,
    attendanceRecord: AttendanceRecord,
) {
    val context = LocalContext.current

    val fullColor = MaterialTheme.colorScheme.primary
    val partialColor = MaterialTheme.colorScheme.secondaryContainer
    val absenceColor = MaterialTheme.colorScheme.tertiaryContainer

    var data by remember {
        mutableStateOf(
            listOf(
                Pie(
                    label = context.getString(R.string.label_full_attendance),
                    data = 12.0,
                    color = fullColor
                ),
                Pie(
                    label = context.getString(R.string.label_partial_attendance),
                    data = 12.0,
                    color = partialColor
                ),
                Pie(
                    label = context.getString(R.string.label_absence_attendance),
                    data = 12.0,
                    color = absenceColor
                )
            )
        )
    }


    PieChart(
        modifier = Modifier.size(200.dp),
        data = data,
        selectedScale = 1.2f,
        scaleAnimEnterSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300),
        style = Pie.Style.Fill
    )
}