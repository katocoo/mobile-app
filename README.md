## 주차별 학습 요약

| 주차 | 주제 | 결과물 스크린샷 | 핵심 포인트 |
|:---:|:---|:---:|:---|
| **3주차** | **Compose Coffee** | <img src="https://github.com/user-attachments/assets/b1525c28-39cb-4a70-bfc9-e64c9e05b72f" width="100"> | Column, Row, Box 등 Compose 레이아웃을 활용한 UI 구조 설계 |
| **4주차** | **프로필 카드** | <img src="https://github.com/user-attachments/assets/e6fb2607-ea70-4412-999b-ad639e37d483)"> | Card, Row, Column 조합 및 Image CircleShape 처리 |
| **5주차** | **스톱워치** | <img src="https://github.com/user-attachments/assets/d3bb9587-95ff-4d64-ac7c-bb642bed6073" width="150"> | `remember`와 `LaunchedEffect`를 활용한 상태 관리 및 시간 로직 |
| **6주차** | **버블버블 게임** | <img src="https://github.com/user-attachments/assets/81e2c69c-faf2-47df-9787-a09040204003" width="100"> | Canvas 그래픽 처리 및 터치 이벤트 인터랙션 |
| **9주차** | **비만도 계산기** | - | TextField 입력 처리 및 BMI 공식 로직 구현 |
| **11주차** | **풍선 게임(최종)** | - | 더블탭 판정, 콤보 시스템, 애니메이션, 랭킹 시스템 |

---

##  주요 프로젝트 상세: 풍선 더블클릭 게임 (W11)
기존의 단순 클릭 게임을 완전히 업그레이드하여 실제 게임다운 기능을 대폭 추가한 최종 실습입니다.

### 1️ 더블클릭 판정 시스템 (Double-Tap Detection)
* **로직:** 200ms 이내에 두 번 클릭 시 콤보 가산, 단일 클릭 시 콤보 초기화.
* 단순 클릭 게임을 사용자 반응 속도 중심의 **스킬 기반 게임**으로 발전시켰습니다.

### 2️ 제한시간 30초 타이머
* `LaunchedEffect`를 활용해 실시간 타이머를 구현하여 플레이의 긴장감을 강화했습니다.

### 3️⃣ 점수 & 콤보 시스템
* **단일 클릭:** +5점 / 콤보 초기화
* **더블클릭 성공:** 10 + (콤보 × 5) 점수 부여
* 콤보가 올라갈수록 점수가 기하급수적으로 상승하는 고득점 전략 구조를 설계했습니다.

### 4️ 풍선 클릭 애니메이션
* `animateFloatAsState`를 활용하여 클릭 시 풍선이 튀어 오르는(scale) 효과를 추가해 시각적 피드백과 타격감을 강화했습니다.

### 점수 저장 및 랭킹 기능 (Top 5)
* 플레이 결과를 리스트에 저장하고 최고 점수순으로 정렬하여 상위 5개의 랭킹을 유지하는 데이터 관리 로직을 구현했습니다.

---

## 기술적으로 배운 점
이 과정을 통해 Compose 앱 개발의 핵심 패턴들을 심도 있게 경험했습니다.
* **정밀 입력 이벤트 처리:** Double-Tap 판정 등 사용자 인터랙션 설계
* **상태 기반 UI 업데이트:** State 변화에 따른 실시간 UI 렌더링 최적화
* **비동기 처리:** Coroutine을 이용한 타이머 및 API 네트워크 통신
* **애니메이션 활용:** 애니메이션 API를 통한 사용자 경험(UX) 개선

---

## Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material3)
- **IDE:** Android Studio
