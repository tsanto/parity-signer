package io.parity.signer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.parity.signer.domain.BASE58_STYLE_ABBREVIATE
import io.parity.signer.domain.abbreviateString
import io.parity.signer.ui.theme.*

@Deprecated("Use new KeySetItem for new screens")
@Composable
fun SeedCard(
	seedName: String,
	identicon: ImageContent,
	base58: String = "",
	showAddress: Boolean = false,
	multiselectMode: Boolean = false,
	selected: Boolean = false,
	swiped: Boolean = false,
	increment: (Int) -> Unit = {},
	delete: () -> Unit = {},
) {
	Surface(
		shape = MaterialTheme.shapes.medium,
		color = MaterialTheme.colors.Bg200,
		modifier = Modifier.heightIn(47.dp).padding(8.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Box(contentAlignment = Alignment.BottomEnd) {
				IdentIcon(identicon)
				if (multiselectMode) {
					if(selected) {
						Icon(Icons.Default.CheckCircle, "Not multiselected", tint = MaterialTheme.colors.Action400)
					} else {
						Icon(Icons.Outlined.Circle, "Multiselected", tint = MaterialTheme.colors.Action400)
					}
				}
			}
			Spacer(modifier = Modifier.width(10.dp))
			Column {
				Text(
					seedName,
					color = MaterialTheme.colors.Text600,
					style = MaterialTheme.typography.subtitle1
				)
				if (showAddress) {
					Text(
						base58.abbreviateString(BASE58_STYLE_ABBREVIATE),
						color = MaterialTheme.colors.Text400,
						style = CryptoTypography.body2
					)
				}
			}
			if (swiped) {
				Spacer(Modifier.weight(1f))
				SwipedButtons(increment, delete)
			}
		}
	}
}
