# 💊 MediGuard (메디가드)

**"1인 가구를 위한 스마트 건강 방패: 아플 때 더 이상 혼자가 아닙니다"**

**MediGuard**는 갑작스러운 증상에도 혼자 대처해야 하는 **1인 가구와 자취생**들이 소외되지 않도록 돕는 **AI 기반 통합 건강 케어 플랫폼**입니다. 사용자의 증상을 분석하여 적절한 상비약을 추천하고, 실시간 위치 기반으로 야간·24시간 운영 약국 정보를 제공하여 정보 불균형을 해소합니다.

---

### 🎯 핵심 가치 (Core Values)

* **Safe Self-Care**: 혼자 건강 문제를 해결해야 하는 상황에서 AI를 통해 증상 범주를 빠르게 파악하고 적절한 조치 지원


* **AI-Powered Insight**: Google Gemini 기반의 자연어 처리를 통한 맞춤형 증상 분석 및 복약 상담


* **Seamless Integration**: “증상 입력 → AI 분석 → 약 추천 → 약국 탐색”의 유기적인 통합 프로세스 제공



---

### ✨ 주요 기능 (Key Features)

#### 1. AI 복약 상담 챗봇 (Gemini AI)

* **증상 분석**: 사용자가 입력한 증상 데이터를 바탕으로 가능한 원인과 증상 범주 분석


* **의약품 추천**: 분석된 증상에 적합한 일반 의약품을 추천하고 효능, 복용법, 주의사항 정보 제공


* **대화형 상담**: 병용 금기 확인, 복용 타이밍 안내 등 실생활에 밀접한 복약 정보 문답



#### 2. 실시간 근처 약국 탐색 (Kakao Map)

* **위치 기반 검색**: 전국 **22,274개**의 약국 데이터를 기반으로 현재 위치에서 가장 가까운 약국 거리순 검색


* **지능형 필터링**: 현재 영업 중인 약국, 24시간 운영 약국, 심야 약국 필터링 기능 제공


* **데이터 신뢰성**: 하버사인(Haversine) 공식을 적용한 정밀 거리 계산 및 장소명 검색 실패 시 주소 검색 전환(Fallback) 로직 적용



#### 3. 리워드 및 게이미피케이션

* **포인트 시스템**: 복약 교육 영상 시청, 퀴즈 풀이, 연속 복약 기록 달성 시 포인트 지급


* **성장 요소**: 사용자 활동량에 따른 경험치(EXP) 및 포인트 적립을 통해 자가 건강 관리 독려


* **기프트샵**: 적립된 포인트를 실제 가치 있는 보상으로 교환 가능한 시스템



---

### 🛠 기술 스택 (Technical Stack)

#### **Backend**

* **Language**: Java 21


* **Framework**: Spring Boot 3.4


* **Security**: Spring Security, JWT (Stateless 인증 및 권한 관리)


* **Data**: Spring Data JPA, MariaDB


* **API Communication**: Spring WebFlux (WebClient)



#### **Frontend**

* **Framework**: React


* **Language**: JavaScript



#### **AI & External API**

* **AI Model**: Google Gemini 1.5 Flash


* **Map Service**: Kakao Local API



---

### 🏗 시스템 아키텍처 및 설계 원칙

* **관심사의 분리 (SoC)**: Controller(접점), Service(비즈니스 로직), Client(외부 통신) 계층을 분리하여 외부 API 변경이 비즈니스 로직에 영향을 주지 않도록 설계


* **대량 데이터 처리 파이프라인**: 공공데이터 API의 페이지네이션을 활용하여 전국 약국 데이터를 효율적으로 수집 및 DB 적재하는 자동화 로직 구현


* **중앙 집중식 예외 처리**: `ApiResponse` 규격과 `GlobalExceptionHandler`를 통해 모든 API 응답 형식을 통일하여 안정성 확보



---

### 📚 API 명세 및 시작하기

**Swagger API Documentation** 서버 구동 후 아래 주소에서 인터랙티브한 API 명세를 확인할 수 있습니다:

`http://localhost:8080/swagger-ui/index.html`

**Backend Setup**

```bash
# 환경 변수 설정 (.env 파일 생성 필요)
# GEMINI_API_KEY, KAKAO_REST_API_KEY 등

./gradlew clean build
./gradlew bootRun

```