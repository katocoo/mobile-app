# 🚀 Jetpack Compose 과제 아카이브 (W03-W11)
**개발자: 박은혁** 모바일 앱 프로그래밍 수업을 통해 학습한 안드로이드 컴포즈 실습 기록입니다.

---

## 📂 주차별 프로젝트 히스토리

### 🟢 Phase 1: 컴포즈 기초 및 레이아웃 (W03-W04)
* **W03 | Compose Coffee**
  * 커피 주문 화면을 통해 기본적인 UI 컴포넌트(`Column`, `Row`, `Box`) 배치 학습
  * <img src="https://github.com/user-attachments/assets/b1525c28-39cb-4a70-bfc9-e64c9e05b72f" width="120">
* **W04 | Profile Card Project**
  * `Material3` 디자인 시스템과 `Card` 레이아웃 적용 실습
  * <img src="https://github.com/user-attachments/assets/e6fb2607-ea70-4412-999b-ad639e37d483" width="160">

### 🔵 Phase 2: 상태 관리와 애니메이션 (W05-W07)
* **W05 | 실시간 스톱워치**
  * `LaunchedEffect`와 고수준 상태 관리를 통한 정밀한 시간 측정 로직 구현
  * <img src="https://github.com/user-attachments/assets/d3bb9587-95ff-4d64-ac7c-bb642bed6073" width="160">
* **W06-W07 | 버블 게임 & 클릭 게임**
  * `Canvas` 그래픽 렌더링 및 `remember` 기반의 점수 상태 업데이트 실습
  * <img src="https://github.com/user-attachments/assets/81e2c69c-faf2-47df-9787-a09040204003" width="120"> <img src="https://github.com/user-attachments/assets/60ced477-4834-4df3-83ba-f226ae5a9ef0" width="120">

### 🟠 Phase 3: 데이터 활용 및 최종 프로젝트 (W08-W11)
* **W08-W09 | 음식 추천 & BMI 계산기**
  * 랜덤 로직 및 `TextField` 입력값에 따른 조건부 UI 변경 처리
  * <img src="https://github.com/user-attachments/assets/ad763a57-77c8-4a1a-a228-13188e9ed82e" width="110"> <img src="https://github.com/user-attachments/assets/cd75f03e-5fe3-45ba-9b23-9ee3b75d72d7" width="110">
* **W10 | Fake Store API 연동**
  * `Retrofit` + `Coroutine`을 활용한 실제 공공 데이터를 리스트로 렌더링
  * <img src="https://github.com/user-attachments/assets/0eb636dc-e06b-4ca0-9474-58ec6de7dbed" width="140">
* **W11 | 풍선 더블클릭 게임 (최종)**
  * **핵심 기능:** 200ms 더블탭 판정, 콤보 시스템, `animateFloatAsState` 스케일 애니메이션
  * <img src="https://github.com/user-attachments/assets/c53a3236-dc6d-43a7-aaf3-6fce7e7f2ead" width="140">

---

## ✍️ 학습 회고 및 기술적 성취
이번 9주간의 실습을 통해 단순히 화면을 그리는 것을 넘어 **안드로이드 앱의 생명주기와 상태 흐름**을 깊이 이해하게 되었습니다.

1. **상태 관리의 중요성:** `mutableStateOf`가 UI 리컴포지션에 미치는 영향을 직접 코드로 확인하며 데이터 흐름을 최적화하는 법을 배웠습니다.
2. **사용자 경험(UX) 개선:** 풍선 게임에서 애니메이션 효과 하나가 앱의 몰입감을 얼마나 높이는지 실감했습니다.
3. **네트워크 통신:** 비동기 프로그래밍인 코루틴을 사용하여 데이터 로딩 중 앱이 멈추지 않게 처리하는 기술을 습득했습니다.

---

## 🛠 환경 설정
- **Kotlin** & **Jetpack Compose (Material3)**
- **Retrofit2**, **OkHttp**, **Coroutines**
