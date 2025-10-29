package com.example.w06 // ✅ 수정: 프로젝트 이름에 맞게 패키지 변경 완료

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.* // mutableIntStateOf 사용을 위해 *로 변경
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
// TODO: 아래 import는 프로젝트의 실제 테마 파일에 맞게 변경해야 합니다.
import com.example.w06.ui.theme.W06Theme // ✅ 수정: W06Theme으로 변경

// 알림 답장 처리를 위한 액션 상수
const val ACTION_REPLY = "com.example.w06.ACTION_REPLY" // ✅ 수정: 패키지 이름 반영
const val KEY_TEXT_REPLY = "key_text_reply" // ✅ 수정: 파일 레벨 상수로 선언 (지역 변수 경고 제거)


/**
 * 앱의 진입점 (Activity)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // TODO: 만약 테마 이름이 W06Theme이 아니라면, 여기서 실제 테마 이름으로 변경해야 합니다.
            W06Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleGameScreen() // 최종 통합 게임 화면
                }
            }
        }
    }
}

/**
 * 데이터 클래스: 개별 버블의 상태를 저장합니다.
 */
data class Bubble(
    val id: Int,
    var position: Offset,
    val radius: Float,
    val color: Color,
    val creationTime: Long = System.currentTimeMillis(),
    val velocityX: Float = 0f,
    val velocityY: Float = 0f
)

/**
 * 게임 상태를 관리하는 클래스
 */
class GameState {
    var bubbles by mutableStateOf<List<Bubble>>(emptyList())
    var score by mutableIntStateOf(0) // ✅ 수정: mutableIntStateOf로 변경
    var isGameOver by mutableStateOf(false)
    var gameTime by mutableIntStateOf(30) // ✅ 수정: mutableIntStateOf로 변경
}

/**
 * Step 5: 모든 기능 통합 (게임 화면의 메인 컴포저블)
 */
@Composable
fun BubbleGameScreen() {
    val context = LocalContext.current
    val gameState = remember { GameState() }
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    // --- 진동 설정 ---
    // ✅ 수정: VIBRATE 권한 체크 경고를 Suppress (AndroidManifest에서 권한 요청함)
    @SuppressLint("MissingPermission")
    fun playPopEffect() {
        // API 레벨에 따른 Vibrator 객체 획득 (S(31) 버전 이상부터 VibratorManager 사용)
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator.hasVibrator()) {
            // API 26 이상부터 VibrationEffect 사용
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50)
            }
        }
    }

    // --- 알림 권한 요청 및 표시 로직 ---
    // 런타임 알림 권한 요청을 위한 ActivityResultLauncher
    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한 승인 시 알림 표시
            showNotification(context, gameState.score)
        } else {
            Log.w("BubbleGame", "Notification permission denied.")
            Toast.makeText(context, "알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 알림 권한을 확인하고 요청하는 함수
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 실행
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // 이미 권한이 있다면 바로 알림 표시
                showNotification(context, gameState.score)
            }
        } else {
            // Tiramisu 이전 버전에서는 권한 요청 없이 바로 알림 표시
            showNotification(context, gameState.score)
        }
    }

    // --- 게임 루프 및 물리 엔진 ---
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = gameState.isGameOver) {
        if (!gameState.isGameOver) {
            // 1. 게임 타이머 코루틴 (1초마다 실행)
            launch {
                while (gameState.gameTime > 0 && !gameState.isGameOver) {
                    delay(1000)
                    gameState.gameTime--

                    // 3초가 지난 버블 자동 제거 (생존 시간 관리)
                    val currentTime = System.currentTimeMillis()
                    gameState.bubbles = gameState.bubbles.filter { currentTime - it.creationTime < 3000 }
                }
                if (!gameState.isGameOver) {
                    gameState.isGameOver = true // 시간 초과로 게임 오버
                }
            }

            // 2. 버블 물리 및 생성 코루틴 (약 60 FPS)
            launch {
                while (!gameState.isGameOver) {
                    delay(16) // 약 60 FPS 유지를 위한 딜레이

                    // 버블 생성: 0.2f 확률로 새 버블 생성 시도 (최대 15개)
                    if (canvasSize != androidx.compose.ui.geometry.Size.Zero && Random.nextFloat() < 0.2f && gameState.bubbles.size < 15) {
                        gameState.bubbles = gameState.bubbles + Bubble(
                            id = Random.nextInt(),
                            position = Offset(
                                x = Random.nextFloat() * canvasSize.width,
                                y = Random.nextFloat() * canvasSize.height
                            ),
                            radius = Random.nextFloat() * 50 + 50,
                            color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 200),
                            velocityX = Random.nextFloat() * 10 - 5,
                            velocityY = Random.nextFloat() * 10 - 5
                        )
                    }

                    // 버블 위치 업데이트 (물리 엔진)
                    gameState.bubbles = gameState.bubbles.map { bubble ->
                        var newX = bubble.position.x + bubble.velocityX
                        var newY = bubble.position.y + bubble.velocityY

                        var newVelocityX = bubble.velocityX
                        var newVelocityY = bubble.velocityY

                        // 화면 경계 처리 (튕기기)
                        if (newX - bubble.radius < 0 || newX + bubble.radius > canvasSize.width) {
                            newVelocityX *= -1
                            newX = newX.coerceIn(bubble.radius, canvasSize.width - bubble.radius)
                        }
                        if (newY - bubble.radius < 0 || newY + bubble.radius > canvasSize.height) {
                            newVelocityY *= -1
                            newY = newY.coerceIn(bubble.radius, canvasSize.height - bubble.radius)
                        }
                        bubble.copy(position = Offset(newX, newY), velocityX = newVelocityX, velocityY = newVelocityY)
                    }
                }
            }
        }
    }

    // --- UI 레이아웃 ---
    Box(modifier = Modifier.fillMaxSize()) {
        // 캔버스: 버블을 그리고 터치 이벤트를 처리하는 영역
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { newSize ->
                    canvasSize = newSize.toSize() // Canvas 크기 저장
                }
                .pointerInput(gameState.isGameOver) {
                    if (!gameState.isGameOver) {
                        detectTapGestures { offset ->
                            coroutineScope.launch {
                                // 터치된 버블 찾기 (가장 나중에 그려진 버블부터 확인)
                                val tappedBubble = gameState.bubbles.lastOrNull { bubble ->
                                    val distance = sqrt((offset.x - bubble.position.x).pow(2) + (offset.y - bubble.position.y).pow(2))
                                    distance <= bubble.radius
                                }
                                if (tappedBubble != null) {
                                    gameState.bubbles = gameState.bubbles - tappedBubble // 버블 제거
                                    gameState.score++ // 점수 증가
                                    playPopEffect() // 진동 발생
                                }
                            }
                        }
                    }
                }
        ) {
            // 모든 버블 그리기
            gameState.bubbles.forEach { bubble ->
                drawCircle(color = bubble.color, radius = bubble.radius, center = bubble.position)
            }
        }

        // UI 요소 (점수, 시간, 다이얼로그)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 상단 상태 표시줄
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Score: ${gameState.score}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Time: ${gameState.gameTime}s", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        // 게임 오버 다이얼로그
        if (gameState.isGameOver) {
            AlertDialog(
                onDismissRequest = { /* 차단적 다이얼로그이므로 아무것도 하지 않음 */ },
                title = { Text("게임 종료!") },
                text = { Text("최종 점수: ${gameState.score}점") },
                confirmButton = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 결과 알림 받기 버튼 (권한 요청 포함)
                        Button(onClick = { requestNotificationPermission() }) {
                            Text("결과 알림 받기")
                        }
                        // 다시 시작 버튼
                        TextButton(
                            onClick = {
                                // 상태 초기화
                                gameState.score = 0
                                gameState.bubbles = emptyList()
                                gameState.gameTime = 30
                                gameState.isGameOver = false
                            }
                        ) {
                            Text("다시 시작")
                        }
                        // 게임 종료 버튼
                        TextButton(
                            onClick = {
                                (context as? ComponentActivity)?.finish()
                            }
                        ) {
                            Text("게임 종료")
                        }
                    }
                }
            )
        }
    }
}

/**
 * 시스템 알림을 생성하고 표시하는 함수
 */
fun showNotification(context: Context, score: Int) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "bubble-game-channel"
    val channelName = "Bubble Game Channel"

    // 1. 알림 채널 생성 (API 26+ 필요)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "버블 게임 결과 알림 채널입니다."
            setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(uri, audioAttributes)
            enableVibration(true)
        }
        manager.createNotificationChannel(channel)
    }

    // 알림 탭 시 실행할 Intent 설정
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    // 2. 알림 빌더 생성
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("버블 게임 결과")
        .setContentText("최종 점수: ${score}점! 다시 플레이하시겠어요?")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // 3. RemoteInput (답장 기능) 설정
    val replyLabel = "메시지 보내기"
    val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
        setLabel(replyLabel)
        build()
    }

    // 답장 액션이 실행될 때 호출될 BroadcastReceiver 설정
    val replyIntent = Intent(context, ReplyReceiver::class.java).apply {
        action = ACTION_REPLY
    }
    val replyPendingIntent = PendingIntent.getBroadcast(
        context, 30, replyIntent, PendingIntent.FLAG_MUTABLE
    )

    val replyAction: NotificationCompat.Action =
        NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "답장",
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

    builder.addAction(replyAction)

    // 4. 알림 발생
    manager.notify(11, builder.build())
}


/**
 * 알림의 답장을 처리하는 BroadcastReceiver
 */
class ReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 사용자가 입력한 텍스트를 가져옵니다.
        val replyTxt = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY) // ✅ 수정: 상수 사용
        Log.d("BubbleGame-Reply", "Received Reply: $replyTxt")

        // 알림을 닫습니다.
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(11)

        // 사용자에게 토스트 메시지로 확인
        Toast.makeText(context, "답변이 저장되었습니다: $replyTxt", Toast.LENGTH_LONG).show()
    }
}


@Preview(showBackground = true, name = "Bubble Game Final")
@Composable
fun BubbleGameFinalPreview() {
    W06Theme { // TODO: 만약 테마 이름이 W06Theme이 아니라면, 여기서 실제 테마 이름으로 변경해야 합니다.
        BubbleGameScreen()
    }
}
