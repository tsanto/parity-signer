package io.parity.signer.screens.keydetails.exportprivatekey

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.NetworkCardModel
import io.parity.signer.components.base.BottomSheetHeader
import io.parity.signer.components.sharedcomponents.CircularCountDownTimer
import io.parity.signer.components.sharedcomponents.KeyCard
import io.parity.signer.components.sharedcomponents.KeyCardModel
import io.parity.signer.domain.DisableScreenshots
import io.parity.signer.domain.EmptyNavigator
import io.parity.signer.domain.Navigator
import io.parity.signer.domain.intoImageBitmap
import io.parity.signer.screens.keydetails.exportprivatekey.PrivateKeyExportModel.Companion.SHOW_PRIVATE_KEY_TIMEOUT
import io.parity.signer.ui.helpers.PreviewData
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.appliedStroke
import io.parity.signer.ui.theme.fill6
import io.parity.signer.uniffi.encodeToQr
import kotlinx.coroutines.runBlocking

@Composable
fun PrivateKeyExportBottomSheet(
    model: PrivateKeyExportModel,
    navigator: Navigator,
) {
	val sidePadding = 24.dp
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		BottomSheetHeader(stringResource(R.string.export_private_key_title)) {
			navigator.backAction()
		}
		//scrollable part if doesn't fit into screen
		Column(
			modifier = Modifier
				.verticalScroll(rememberScrollState())
				.weight(weight = 1f, fill = false)
				.padding(start = sidePadding, end = sidePadding)
		) {
			val qrRounding = dimensionResource(id = R.dimen.qrShapeCornerRadius)
			val plateShape =
				RoundedCornerShape(qrRounding, qrRounding, qrRounding, qrRounding)
			Column(
				modifier = Modifier
					.clip(plateShape)
					.border(
						BorderStroke(1.dp, MaterialTheme.colors.appliedStroke),
						plateShape
					)
					.background(MaterialTheme.colors.fill6, plateShape)
			) {
				Box(
					modifier = Modifier
						.fillMaxWidth(1f)
						.aspectRatio(1.1f)
						.background(
							Color.White,
							RoundedCornerShape(qrRounding)
						),
					contentAlignment = Alignment.Center,
				) {
					val qrImage = remember { runBlocking { encodeToQr(model.qrData, true) } }
					Image(
						bitmap = qrImage.intoImageBitmap(),
						contentDescription = stringResource(R.string.qr_with_address_to_scan_description),
						contentScale = ContentScale.Fit,
						modifier = Modifier.size(264.dp)
					)
				}
				KeyCard(model.keyCard)
			}
			//autohide component
			val timerText = stringResource(R.string.export_private_key_timer_label)
			CircularCountDownTimer(
				SHOW_PRIVATE_KEY_TIMEOUT,
				timerText
			) { navigator.backAction() }
		}
	}

	DisableScreenshots()
}

class PrivateKeyExportModel(
	val qrData: List<UByte>,
	val keyCard: KeyCardModel,
	val network: NetworkCardModel,
) {
	companion object {
		const val SHOW_PRIVATE_KEY_TIMEOUT = 60 //seconds

		fun createMock(): PrivateKeyExportModel = PrivateKeyExportModel(
			qrData = PreviewData.exampleQRData,
			keyCard = KeyCardModel.createStub(),
			network = NetworkCardModel("Polkadot", "NetworkLogo")
		)
	}
}

@Preview(
	name = "light", group = "themes", uiMode = UI_MODE_NIGHT_NO,
	showBackground = true, backgroundColor = 0xFFFFFFFF,
)
@Preview(
	name = "dark", group = "themes", uiMode = UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewPrivateKeyExportBottomSheet() {
	SignerNewTheme {
		PrivateKeyExportBottomSheet(
			model = PrivateKeyExportModel.createMock(),
			EmptyNavigator()
		)
	}
}

