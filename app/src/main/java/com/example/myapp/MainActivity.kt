package com.example.myapp

// package com.example.week06 <-- 본인 패키지 이름은 그대로 두세요!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// 데이터 클래스 (총 볼륨 volume 추가됨)
data class WorkoutRecord(
    val id: Long,
    val name: String,
    val weight: String,
    val sets: String,
    val reps: String,
    val volume: Int // 총 볼륨 (무게 * 세트 * 횟수)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFAFAFA)) {
                    WorkoutApp()
                }
            }
        }
    }
}

@Composable
fun WorkoutApp() {
    // 입력 변수들
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }

    // 리스트 변수
    val workoutList = remember { mutableStateListOf<WorkoutRecord>() }

    // [기능2] 타이머 변수 (60초)
    var timerSeconds by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // 타이머 로직 (1초마다 감소)
    LaunchedEffect(isTimerRunning, timerSeconds) {
        if (isTimerRunning && timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        } else if (timerSeconds == 0) {
            isTimerRunning = false
        }
    }

    // [기능3] 오늘 날짜 가져오기
    val currentDate = remember {
        SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA).format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- [기능3] 상단 대시보드 (날짜 & 요약) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = currentDate, fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = "오늘 총 ${workoutList.sumOf { it.sets.toInt() }}세트 완료! 🔥",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
            }
        }

        // --- [기능2] 휴식 타이머 ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isTimerRunning) {
                Text(
                    text = "휴식 중: ${timerSeconds}초",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            } else {
                Button(
                    onClick = {
                        timerSeconds = 60 // 60초 시작
                        isTimerRunning = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("휴식 타이머 (60초)")
                }
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // 입력창 영역
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("종목 이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = weight, onValueChange = { weight = it },
                label = { Text("kg") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = sets, onValueChange = { sets = it },
                label = { Text("세트") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = reps, onValueChange = { reps = it },
                label = { Text("회") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && weight.isNotEmpty() && sets.isNotEmpty() && reps.isNotEmpty()) {
                    // [기능1] 볼륨 계산 로직
                    val w = weight.toIntOrNull() ?: 0
                    val s = sets.toIntOrNull() ?: 0
                    val r = reps.toIntOrNull() ?: 0
                    val totalVolume = w * s * r

                    val newRecord = WorkoutRecord(
                        id = System.currentTimeMillis(),
                        name = name, weight = weight, sets = sets, reps = reps,
                        volume = totalVolume // 볼륨 저장
                    )
                    workoutList.add(0, newRecord)

                    // 입력창 초기화
                    name = ""
                    weight = ""
                    sets = ""
                    reps = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("기록 추가 +", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 리스트 영역
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(workoutList) { record ->
                WorkoutCard(record = record, onDelete = { workoutList.remove(record) })
            }
        }
    }
}

@Composable
fun WorkoutCard(record: WorkoutRecord, onDelete: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = record.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${record.weight}kg × ${record.sets}세트 × ${record.reps}회")
                // [기능1 표시] 총 볼륨 보여주기
                Text(
                    text = "총 볼륨: ${record.volume}kg",
                    fontSize = 12.sp,
                    color = Color(0xFF673AB7),
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.LightGray)
            }
        }
    }
}