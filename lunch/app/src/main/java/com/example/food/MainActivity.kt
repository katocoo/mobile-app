package com.example.food  // ⬅️ 여기만 네 프로젝트 원래 package 로 바꿔줘

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// 점심 메뉴 데이터
data class MenuItem(
    val name: String,
    val category: String,
    val desc: String,
    val emoji: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // 프로젝트에서 따로 Theme 안 쓰고 MaterialTheme 바로 사용
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LunchRouletteApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchRouletteApp() {
    // 50개 정도의 메뉴 리스트
    val menuList = remember {
        listOf(
            // 한식
            MenuItem("김치찌개", "한식", "따뜻한 국물 한입으로 기운 UP!", "🍲"),
            MenuItem("된장찌개", "한식", "구수하고 건강한 맛!", "🥘"),
            MenuItem("비빔밥", "한식", "색색 고명과 고추장 조화!", "🍚"),
            MenuItem("불고기", "한식", "단짠단짠 밥도둑!", "🥩"),
            MenuItem("제육볶음", "한식", "매콤한 고기 반찬 대표주자", "🌶️"),
            MenuItem("닭갈비", "한식", "춘천 대표 매콤달콤 요리", "🍗"),
            MenuItem("순두부찌개", "한식", "부드럽고 얼큰한 한입", "🍲"),
            MenuItem("갈비탕", "한식", "든든한 국물 한 그릇", "🍖"),
            MenuItem("보쌈", "한식", "쌈 싸먹는 재미까지!", "🥬"),
            MenuItem("냉면", "한식", "여름엔 시원한 냉면이지!", "🥢"),

            // 중식
            MenuItem("짜장면", "중식", "달콤한 춘장소스의 정석!", "🍜"),
            MenuItem("짬뽕", "중식", "해물 듬뿍 시원한 국물!", "🌶️"),
            MenuItem("탕수육", "중식", "찍먹? 부먹? 당신의 선택은?", "🥢"),
            MenuItem("마파두부", "중식", "얼얼한 두부의 매력", "🔥"),
            MenuItem("볶음밥", "중식", "간단하지만 든든하게!", "🍳"),
            MenuItem("깐풍기", "중식", "매콤달콤한 바삭한 닭요리", "🍗"),
            MenuItem("유산슬", "중식", "고급 중국식 반찬 세트", "🥘"),
            MenuItem("마라탕", "중식", "중독성 있는 매운 국물", "🌶️"),
            MenuItem("고추잡채", "중식", "얇은 고기와 야채 볶음!", "🥩"),
            MenuItem("딤섬", "중식", "한입 가득 따끈한 행복", "🥟"),

            // 일식
            MenuItem("돈까스", "일식", "겉바속촉 고전 인기메뉴", "🍖"),
            MenuItem("규동", "일식", "소고기 덮밥, 달콤짭짤 맛", "🍚"),
            MenuItem("라멘", "일식", "진한 국물과 차슈의 조화", "🍜"),
            MenuItem("초밥", "일식", "한입에 행복, 초밥 세트!", "🍣"),
            MenuItem("우동", "일식", "쫄깃한 면발과 담백한 국물", "🥢"),
            MenuItem("오야코동", "일식", "닭고기와 달걀의 따뜻한 조화", "🍗"),
            MenuItem("가츠동", "일식", "돈까스+달걀+밥의 삼합", "🍳"),
            MenuItem("덮밥", "일식", "간단하고 푸짐하게 한 그릇", "🍚"),
            MenuItem("회덮밥", "일식", "신선한 회와 고추장의 만남", "🐟"),
            MenuItem("규카츠", "일식", "레어하게 즐기는 고급 돈까스", "🥩"),

            // 분식/기타
            MenuItem("라면", "분식", "가장 간단하고 확실한 점심", "🍜"),
            MenuItem("떡볶이", "분식", "매콤달콤 대표 간식", "🌶️"),
            MenuItem("김밥", "분식", "언제 어디서든 한 줄로 든든", "🍙"),
            MenuItem("순대", "분식", "간단하게 한 접시!", "🥢"),
            MenuItem("핫도그", "분식", "케첩 + 머스타드 완벽조합", "🌭"),
            MenuItem("햄버거", "패스트푸드", "간단하게 즐기는 한 끼", "🍔"),
            MenuItem("피자", "패스트푸드", "나눠먹기 좋은 메뉴", "🍕"),
            MenuItem("샌드위치", "패스트푸드", "가볍고 간편한 점심", "🥪"),
            MenuItem("샐러드", "가벼운 식사", "채소 듬뿍 상큼하게!", "🥗"),
            MenuItem("볶음우동", "퓨전", "한국식 감칠맛 버전!", "🍜"),
            MenuItem("쌀국수", "아시아푸드", "담백한 베트남식 면요리", "🍲"),
            MenuItem("분짜", "아시아푸드", "고기 + 채소 + 소스의 하모니", "🥩"),
            MenuItem("타코", "멕시칸", "한입에 멕시코 여행!", "🌮"),
            MenuItem("부리또", "멕시칸", "든든하게 한 줄로!", "🌯"),
            MenuItem("스테이크", "양식", "오늘은 좀 거하게?", "🥩"),
            MenuItem("파스타", "양식", "크림, 토마토, 오일파스타 중 선택!", "🍝"),
            MenuItem("오믈렛", "양식", "부드러운 달걀의 향연", "🍳"),
            MenuItem("리조또", "양식", "꾸덕꾸덕한 밥의 유혹", "🍚"),
            MenuItem("도시락", "한식/일식", "밸런스 좋은 구성", "🍱"),
            MenuItem("카레", "일식/인도식", "향신료 가득한 점심 대표", "🍛")
        )
    }

    var current by remember { mutableStateOf(menuList.random()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🍴 오늘 점심 뭐 먹지?") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "다시 추천"
                    )
                },
                text = { Text("다른 메뉴 추천") },
                onClick = {
                    var newItem = current
                    while (newItem == current) {
                        newItem = menuList.random()
                    }
                    current = newItem
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MenuCard(current)
        }
    }
}

@Composable
fun MenuCard(menu: MenuItem) {
    Card(
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = menu.emoji,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = menu.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "🍽️ 카테고리: ${menu.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = menu.desc,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
