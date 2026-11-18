package com.ava.ai.presentation.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.ava.ai.data.datastore.PreferencesManager

@Composable
fun SettingsScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    var avaActive by remember { mutableStateOf(false) }
    var wakeWordEnabled by remember { mutableStateOf(true) }
    var ttsEnabled by remember { mutableStateOf(true) }
    var autoReadSms by remember { mutableStateOf(false) }
    var floatingBubble by remember { mutableStateOf(false) }
    var autoStart by remember { mutableStateOf(false) }
    var rootAccessEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        prefs.avaActive.collect { avaActive = it }
    }

    LaunchedEffect(Unit) {
        prefs.wakeWordEnabled.collect { wakeWordEnabled = it }
    }

    LaunchedEffect(Unit) {
        prefs.ttsEnabled.collect { ttsEnabled = it }
    }

    LaunchedEffect(Unit) {
        prefs.autoReadSms.collect { autoReadSms = it }
    }

    LaunchedEffect(Unit) {
        prefs.floatingBubbleEnabled.collect { floatingBubble = it }
    }

    LaunchedEffect(Unit) {
        prefs.autoStartOnBoot.collect { autoStart = it }
    }

    LaunchedEffect(Unit) {
        prefs.rootAccessEnabled.collect { rootAccessEnabled = it }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("General Settings", style = MaterialTheme.typography.titleLarge)
        }

        item {
            SettingCard {
                SettingSwitchRow(
                    title = "AVA Active",
                    subtitle = "Enable AVA assistant",
                    icon = Icons.Default.Power,
                    checked = avaActive,
                    onCheckedChange = {
                        avaActive = it
                        scope.launch { prefs.setMayaActive(it) }
                    }
                )
            }
        }

        item {
            Text(
                "Voice Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SettingCard {
                Column {
                    SettingSwitchRow(
                        title = "Wake Word Detection",
                        subtitle = "Listen for Hey Ava",
                        icon = Icons.Default.RecordVoiceOver,
                        checked = wakeWordEnabled,
                        onCheckedChange = {
                            wakeWordEnabled = it
                            scope.launch { prefs.setWakeWordEnabled(it) }
                        }
                    )

                    HorizontalDivider()

                    SettingSwitchRow(
                        title = "Text-to-Speech",
                        subtitle = "Enable voice responses",
                        icon = Icons.Default.VolumeUp,
                        checked = ttsEnabled,
                        onCheckedChange = {
                            ttsEnabled = it
                            scope.launch { prefs.setTtsEnabled(it) }
                        }
                    )
                }
            }
        }

        item {
            Text(
                "AI Provider",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SettingCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Current Provider")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("OpenAI GPT-4")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Change Provider")
                    }
                }
            }
        }

        item {
            Text(
                "Automation",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SettingCard {
                Column {
                    SettingSwitchRow(
                        title = "Auto-read SMS",
                        subtitle = "Read incoming SMS aloud",
                        icon = Icons.Default.Message,
                        checked = autoReadSms,
                        onCheckedChange = {
                            autoReadSms = it
                            scope.launch { prefs.setAutoReadSms(it) }
                        }
                    )

                    HorizontalDivider()

                    SettingSwitchRow(
                        title = "Floating Bubble",
                        subtitle = "Show floating assistant bubble",
                        icon = Icons.Default.BubbleChart,
                        checked = floatingBubble,
                        onCheckedChange = {
                            floatingBubble = it
                            scope.launch {
                                prefs.setFloatingBubbleEnabled(it)
                            }
                        }
                    )

                    HorizontalDivider()

                    SettingSwitchRow(
                        title = "Auto-start on Boot",
                        subtitle = "Start AVA when device boots",
                        icon = Icons.Default.PowerSettingsNew,
                        checked = autoStart,
                        onCheckedChange = {
                            autoStart = it
                            scope.launch {
                                prefs.setAutoStartOnBoot(it)
                            }
                        }
                    )
                }
            }
        }

        item {
            Text(
                "Advanced",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            SettingCard {
                Column {
                    SettingSwitchRow(
                        title = "Root Access",
                        subtitle = "Enable root shell access",
                        icon = Icons.Default.Security,
                        checked = rootAccessEnabled,
                        onCheckedChange = {
                            rootAccessEnabled = it
                            scope.launch {
                                prefs.setRootAccessEnabled(it)
                            }
                        }
                    )

                    HorizontalDivider()

                    SettingClickableRow(
                        title = "Accessibility Service",
                        subtitle = "Configure UI automation",
                        icon = Icons.Default.Accessibility,
                        onClick = {
                            openSystemSettings(
                                context,
                                Settings.ACTION_ACCESSIBILITY_SETTINGS
                            )
                        }
                    )

                    HorizontalDivider()

                    SettingClickableRow(
                        title = "Notification Access",
                        subtitle = "Configure notification listening",
                        icon = Icons.Default.Notifications,
                        onClick = {
                            openSystemSettings(
                                context,
                                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun openSystemSettings(context: Context, action: String) {
    try {
        context.startActivity(Intent(action))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun SettingCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        content()
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingClickableRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate")
        }
    }
}