package com.kt.worktimetrackermanager.presentation.components.chart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord.Companion.isNoneData
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun AttendanceRateChart(
    modifier: Modifier = Modifier,
    title: String,
    attendanceRecord: AttendanceRecord,
) {
    val context = LocalContext.current

    val pieColors = listOf<Color>(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )

    var data by remember {
        mutableStateOf(
            listOf(
                Pie(
                    label = context.getString(R.string.label_full_attendance),
                    data = attendanceRecord.fullAttendance.toDouble(),
                    color = pieColors[0]
                ),
                Pie(
                    label = context.getString(R.string.label_partial_attendance),
                    data = attendanceRecord.partialAttendance.toDouble(),
                    color = pieColors[1]
                ),
                Pie(
                    label = context.getString(R.string.label_absence_attendance),
                    data = attendanceRecord.absenceAttendance.toDouble(),
                    color = pieColors[2]
                )
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
            .padding(MaterialTheme.padding.small)
    ) {
        when {
            attendanceRecord.isNoneData() -> {
                Image(
                    painter = painterResource(R.drawable.img_nodata),
                    contentDescription = null
                )
            }

            else -> {
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
        }

        data.fastForEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.padding.mediumSmall)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(it.color)
                        .size(12.dp)
                )
                Text(text = it.label!!, style = MaterialTheme.typography.bodySmall)
                Text(text = it.data.toInt().toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }

}