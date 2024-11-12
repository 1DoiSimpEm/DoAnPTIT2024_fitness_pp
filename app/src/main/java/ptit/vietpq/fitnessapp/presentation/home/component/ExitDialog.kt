package ptit.vietpq.fitnessapp.presentation.home.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ptit.vietpq.fitnessapp.R

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(R.string.exit)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_exit)) },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        onConfirm()
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

@Preview
@Composable
private fun ExitDialogPreview() {
    ExitDialog(
        onConfirm = {},
        onDismiss = {}
    )
}
