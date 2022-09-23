package com.aliucord.manager.ui.dialog

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.aliucord.manager.R

@Composable
fun ManageStorageDialog() {
    var manageStorageGranted by remember { mutableStateOf(Environment.isExternalStorageManager()) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !manageStorageGranted) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (Environment.isExternalStorageManager())
                        manageStorageGranted = true
                }

                Button(
                    onClick = {
                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            .setData("package:com.aliucord.manager".toUri())
                            .let { launcher.launch(it) }
                    },
                ) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            title = { Text(stringResource(R.string.permissions_grant)) },
            text = { Text(stringResource(R.string.permissions_grant_body)) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}
