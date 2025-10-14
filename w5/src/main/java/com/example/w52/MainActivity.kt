package com.example.w52

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { StopWatchScreen() } }
    }
}

/** 중앙에 시간, 그 아래 Start/Stop/Reset 버튼 3개 */
@Composable
fun StopWatchScreen() {
    var timeInMs by remember { mutableLongStateOf(0L) }   // 경과 시간(ms)
    var running  by remember { mutableStateOf(false) }     // 실행 중 여부

    // running이 true일 때만 10ms마다 시간 증가
    LaunchedEffect(running) {
        if (running) {
            while (true) {
                delay(10L)
                timeInMs += 10L
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 00:01:23 형식(분:초:센티초)
        Text(
            text = formatTime(timeInMs),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { running = true })  { Text("Start") }
            Spacer(Modifier.width(12.dp))
            Button(onClick = { running = false }) { Text("Stop") }
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                running = false
                timeInMs = 0L
            }) { Text("Reset") }
        }
    }
}

/** 00:MM:SS:CC(여기서는 MM:SS:CC만 사용) */
private fun formatTime(ms: Long): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    val centis  = (ms % 1000) / 10
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, centis)
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    MaterialTheme { StopWatchScreen() }
}

