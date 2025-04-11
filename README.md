# 네이트 뉴스 디스커버 (Nate News Discover)

## 소개
네이트 뉴스 디스커버는 네이트 뉴스의 기사들을 효율적으로 탐색하고 읽을 수 있는 웹 애플리케이션입니다. 최신 뉴스와 인기 뉴스를 카테고리별로 제공하며, 사용자 친화적인 인터페이스를 통해 뉴스 소비 경험을 향상시킵니다.

## 주요 기능
- 최신 뉴스 및 인기 뉴스 조회
- 카테고리별 뉴스 필터링
- 무한 스크롤을 통한 뉴스 더보기
- 다크 모드 지원
- 반응형 웹 디자인
- 뉴스 기사 좋아요 기능
- 중복 기사 필터링

## 기술 스택
### Backend
- Java 17
- Spring Boot 3.4.0
- Gradle
- JUnit 5
- Mockito

### Frontend
- Thymeleaf
- HTML5
- CSS3
- JavaScript (ES6+)

## 프로젝트 구조
src/  
├── main/  
│ ├── java/  
│ │ └── com/nate/news/app/  
│ │ ├── common/  
│ │ │ ├── constants/  
│ │ │ ├── exception/  
│ │ │ └── util/  
│ │ ├── controller/  
│ │ ├── domain/  
│ │ │ ├── adaptor/  
│ │ │ ├── dto/  
│ │ │ └── service/  
│ │ └── NewsAppApplication.java
│ └── resources/  
│ ├── static/  
│ │ ├── css/  
│ │ └── js/  
│ └── templates/  
└── test/  
└── java/  
└── com/nate/news/app/  
├── controller/  
├── domain/  
└── common/  


## 설치 및 실행 방법

### 요구사항
- JDK 17 이상
- Gradle 7.x 이상

### 설치
```bash
# 프로젝트 클론
git clone https://github.com/username/news-app.git
cd news-app

# 의존성 설치 및 빌드
./gradlew build
```

### 실행
```bash
./gradlew bootRun
```
애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

## API 엔드포인트

### 뉴스 조회 API
GET /api/v1/news

#### 요청 파라미터
| 파라미터 | 타입 | 필수 | 기본값 | 설명                     |
|---------|------|------|--------|------------------------|
| function | String | 선택 | ranking | 조회 유형 (ranking/recent) |
| category | String | 선택 | all | 뉴스 카테고리                |
| page | Integer | 선택 | 1 | 페이지 번호                 |

#### 지원하는 카테고리
- all: 전체
- pol: 정치
- eco: 경제
- soc: 사회
- int: 국제
- its: IT/과학
- spo: 스포츠
- ent: 연예
- col: 컬럼

## 테스트
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "com.nate.news.app.controller.NewsApiControllerTest"
```

## 브라우저 지원
- Chrome (최신 버전)
- Firefox (최신 버전)
- Safari (최신 버전)
- Edge (최신 버전)

## 다크 모드
시스템의 다크 모드 설정에 따라 자동으로 테마가 변경됩니다. 별도의 설정이 필요하지 않습니다.

## 성능 최적화
- 이미지 레이지 로딩
- 무한 스크롤 구현
- 응답 캐싱
- 중복 기사 필터링

## 라이선스
이 프로젝트는 MIT 라이선스 하에 있습니다.