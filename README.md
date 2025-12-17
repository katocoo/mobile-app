
## 주차별 학습

| 주차 | 주제 | 결과물 스크린샷 | 핵심 포인트 |
|:---:|:---|:---:|:---|
| **3주차** | **Compose Coffee** | <img src="https://github.com/user-attachments/assets/b1525c28-39cb-4a70-bfc9-e64c9e05b72f" width="100"> | 컴포즈 레이아웃 구조 설계 및 기본 UI 구성 |
| **4주차** | **프로필 카드** | <img src="https://github.com/user-attachments/assets/e6fb2607-ea70-4412-999b-ad639e37d483" width="120"> | Card 및 CircleShape 등 레이아웃 시스템 활용 |
| **5주차** | **스톱워치** | <img src="https://github.com/user-attachments/assets/d3bb9587-95ff-4d64-ac7c-bb642bed6073" width="120"> | LaunchedEffect를 활용한 상태 관리와 시간 로직 |
| **6주차** | **버블버블 게임** | <img src="https://github.com/user-attachments/assets/81e2c69c-faf2-47df-9787-a09040204003" width="100"> | Canvas 그래픽 처리 및 터치 인터랙션 실습 |
| **7주차** | **클릭 게임(초판)** | <img src="https://github.com/user-attachments/assets/60ced477-4834-4df3-83ba-f226ae5a9ef0" width="100"> | `remember` + `mutableStateOf` 실시간 점수 관리 |
| **8주차** | **음식 추천 앱** | <img src="https://github.com/user-attachments/assets/ad763a57-77c8-4a1a-a228-13188e9ed82e" width="80"> <img src="https://github.com/user-attachments/assets/d611042a-4ef7-4d0a-896f-bdfe12f31e9f" width="80"> | 랜덤 리스트 선택 및 State → UI 자동 업데이트 |
| **9주차** | **비만도 계산기** | <img src="https://github.com/user-attachments/assets/cd75f03e-5fe3-45ba-9b23-9ee3b75d72d7" width="80"> <img src="https://github.com/user-attachments/assets/a4a0ca32-35fa-4e63-ba9b-de858cba6000" width="80"> | TextField 입력 처리 및 BMI 유틸리티 로직 구현 |
| **10주차** | **페이크 스토어** | <img src="https://github.com/user-attachments/assets/0eb636dc-e06b-4ca0-9474-58ec6de7dbed" width="100"> | Retrofit + Coroutine 기반 외부 REST API 통신 |
| **11주차** | **풍선 게임(최종)** | <img src="https://github.com/user-attachments/assets/c53a3236-dc6d-43a7-aaf3-6fce7e7f2ead" width="100"> | 더블탭 판정, 콤보 시스템, 애니메이션, 랭킹 시스템 |

---

## 주요 프로젝트 상세 소개

### 10주차: 페이크 스토어(Fake Store API) 앱
외부 REST API를 사용하여 실제 데이터를 불러오고 화면에 출력하는 과정을 학습했습니다.
* **Retrofit + Coroutine:** 비동기 네트워크 통신을 통한 데이터 로드
* **LazyColumn:** 대량의 상품 데이터를 효율적인 리스트로 구성
* **Material3 Card:** 상품 이미지와 상세 정보를 깔끔한 카드 UI로 렌더링

### 11주차: 풍선 더블클릭 게임 (최종 프로젝트)
기존의 단순 클릭 게임을 완전히 업그레이드하여 실제 게임다운 기능을 대폭 추가했습니다.

**1. 더블클릭 판정 시스템 (Double-Tap Detection)**
* 200ms 이내 두 번 클릭 시 콤보 가산 로직을 통해 스킬 기반 게임성 확보

**2. 시각적 피드백과 애니메이션**
* `animateFloatAsState`를 활용하여 클릭 시 풍선이 '톡' 튀어 오르는(Scale) 효과 구현

**3. 점수 & 랭킹 기능 (Top 5)**
* 플레이 결과를 점수순으로 정렬하여 상위 5개의 기록을 관리하는 시스템 구축

---

## 기술적으로 배운 점
이 과정을 통해 Compose 앱 개발의 핵심 패턴들을 심도 있게 경험했습니다.
* **정밀 입력 이벤트 처리:** Double-Tap 판정 등 사용자 인터랙션 설계
* **네트워크 데이터 활용:** API 통신 및 데이터 파싱, 리스트 렌더링
* **상태 기반 UI 업데이트:** State 변화에 따른 실시간 UI 최적화
* **애니메이션 활용:** 애니메이션 API를 통한 사용자 경험(UX) 개선

---

## Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material3)
- **Networking:** Retrofit2, OkHttp
- **Async:** Kotlin Coroutines
- **IDE:** Android Studio
