package com.kt.worktimetrackermanager.presentation.components.chart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.parse
import com.kt.worktimetrackermanager.core.ext.parseDate
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enum.Period
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.presentation.components.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.stacked
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import timber.log.Timber
import java.text.DecimalFormat

@Composable
fun AttendanceEachTime(
    modifier: Modifier = Modifier,
    data: List<AttendanceRecord>,
    period: Period,
    action: (Period) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
            .padding(MaterialTheme.padding.small)
    ) {
        SingleChoiceSegmentedButtonRow {
            var selected by remember { mutableStateOf(period) }
            Period.entries.fastForEachIndexed { index, period ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = Period.entries.size
                    ),
                    selected = index == selected.ordinal,
                    onClick = {
                        selected = period
                        action(period)
                    }
                ) {
                    Text(
                        text = period.name
                    )
                }
            }
        }

        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(data) {
            Timber.d(data.toString())
            if (data.isNotEmpty()) {
                modelProducer.runTransaction {
                    columnSeries {
                        series(
                            List(data.size) {
                                data[it].fullAttendance
                            }
                        )
                        series(
                            List(data.size) {
                                data[it].partialAttendance
                            }
                        )
                        series(
                            List(data.size) {
                                data[it].absenceAttendance
                            }
                        )
                    }
                    extras {
                        it[LegendLabelKey] = y.toSet()
                    }
                }
            } else {
                modelProducer.runTransaction {
                    columnSeries {
                        series(
                            List(1) { 0 }
                        )
//                        series(
//                            List(1) { 0 }
//                        )
//                        series(
//                            List(1) { 0 }
//                        )
                    }
                    extras {
                        it[LegendLabelKey] = y.toSet()
                    }
                }
            }
        }
        val bottomValueFormatter =
            if (data.isNotEmpty()) {
                when (period) {
                    Period.DAILY -> {
                        CartesianValueFormatter { _, x, _ ->
                            data[x.toInt() % data.size].end.toLocalDate().parse()
                        }
                    }

                    Period.WEEKLY -> {
                        CartesianValueFormatter { _, x, _ ->
                            data[x.toInt() % data.size].end.toLocalDate().parse()
                        }
                    }

                    Period.MONTHLY -> CartesianValueFormatter { _, x, _ ->
                        data[x.toInt() % data.size].end.toLocalDate().parse()
                    }
                }
            } else {
                CartesianValueFormatter.Default
            }
        AttendanceEachTimeChart(
            modelProducer = modelProducer,
            bottomValueFormatter = bottomValueFormatter
        )
    }
}

@Composable
private fun AttendanceEachTimeChart(
    modelProducer: CartesianChartModelProducer,
    bottomValueFormatter: CartesianValueFormatter,
) {
    val chartColors = listOf<Color>(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )
    val legendItemLabelComponent = rememberTextComponent(vicoTheme.textColor)

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                        ColumnCartesianLayer.ColumnProvider.series(
                            chartColors.map { color ->
                                rememberLineComponent(fill = fill(color), thickness = 16.dp)
                            },
                        ),
                    columnCollectionSpacing = 32.dp,
                    mergeMode = { ColumnCartesianLayer.MergeMode.stacked() },
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        valueFormatter = StartAxisValueFormatter,
                        itemPlacer = StartAxisItemPlacer,
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                        valueFormatter = bottomValueFormatter
                    ),
                marker = rememberMarker(MarkerValueFormatter),
                layerPadding = {
                    cartesianLayerPadding(
                        scalableStart = 16.dp,
                        scalableEnd = 16.dp,
                    )
                },
                legend =
                    rememberHorizontalLegend(
                        items = { extraStore ->
                            extraStore[LegendLabelKey].forEachIndexed { index, label ->
                                add(
                                    LegendItem(
                                        shapeComponent(
                                            fill(chartColors[index]),
                                            CorneredShape.Pill,
                                        ),
                                        legendItemLabelComponent,
                                        label,
                                    ),
                                )
                            }
                        },
                        padding = insets(top = 16.dp),
                    ),
            ),
        modelProducer = modelProducer,
        modifier = Modifier.height(252.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true),
        placeholder = {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        },
    )
}

private val y =
    listOf(
        "Full",
        "Partial",
        "Absence",
    )

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val YDecimalFormat = DecimalFormat("#")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 1.0 })
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

//Fake Data