package com.kt.worktimetrackermanager.core.presentation.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.secondaryItemAlpha
import com.kt.worktimetrackermanager.core.presentation.ui.components.ActionButton
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import kotlinx.collections.immutable.ImmutableList

data class EmptyBoxAction(
    @StringRes val stringRes: Int,
    @DrawableRes val icon: Int,
    val onClick: () -> Unit,
)

@Composable
fun EmptyBox(
    @StringRes stringRes: Int,
    @StringRes descRes: Int,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    actions: ImmutableList<EmptyBoxAction>? = null,
) {
    EmptyBox(
        message = stringResource(stringRes),
        desc = stringResource(descRes),
        iconRes = iconRes,
        modifier = modifier,
        actions = actions
    )
}

@Composable
private fun EmptyBox(
    message: String,
    desc: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    actions: ImmutableList<EmptyBoxAction>? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.padding.large)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = "Empty Box Icon",
            modifier = Modifier
                .size(172.dp)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
//                .paddingFromBaseline(top = MaterialTheme.padding.large)
        )

        Text(
            text = desc,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .secondaryItemAlpha()
//                .paddingFromBaseline(bottom = MaterialTheme.padding.large)
        )

        if (!actions.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            ) {
                actions.fastForEach {
                    ActionButton(
                        modifier = Modifier.weight(1f),
                        title = stringResource(it.stringRes),
                        icon = it.icon,
                        onClick = it.onClick,
                    )
                }
            }
        }
    }
}