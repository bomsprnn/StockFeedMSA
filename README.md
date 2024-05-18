### 프로젝트 소개

***

- MSA로 구현한 소셜 미디어 플랫폼입니다.
- 네이버 주가 데이터를 활용, 주식 정보와 관련 지표를 제공합니다.

<br/>

### 기술스택

***

- SpringBoot 3.2.x / JAVA 21
- Mysql 8.0
- Redis
- Kafka
- Docker

<br/>

### 주요기능

***

**USER**

- Gmail SMTP 활용 회원가입
- JWT 로그인-로그아웃, 모든 기기에서 로그아웃

**SNS**

- 포스트, 댓글, 좋아요, 팔로우
- 팔로우한 사용자의 포스트, 댓글, 상호작용, 팔로우 활동이 뉴스피드에 생성

**STOCKS**

- 평일 새벽 5시에 주식 일봉 데이터 업데이트
- 종목에 대한 이동평균선, 볼린저밴드, MACD 정보 제공
- 종목에 대한 시가, 고가, 저가, 종가, 거래량 제공
- 종목 이름, 종목 코드, 어제의 거래금액, 어제의 등락률 기준으로 KOSPI, KOSDAQ 데이터 정렬

<br/>

### 설계

***
**아키텍쳐**

![mas_diagram.drawio.png](..%2F..%2FDownloads%2Fmas_diagram.drawio.png)
<br/>

**ERD**

![ERD](https://velog.velcdn.com/images/oat/post/5c9d8118-2b53-49e3-ad53-d7e7c4a3cbb3/image.png)

<br/>

### 트러블 슈팅

***

- [batchUpdate](https://oatt.notion.site/batchUpdate-348b56077e5f4b57b22008f30200b93a?pvs=4)를 사용하여 대용량 데이터 저장 성능 향상
- 데이터 중복 저장 문제 해결을 위한 [Redis 분산 Lock](https://oatt.notion.site/Redis-Lock-32bca39d0ee647338b5bf3905a9155c5?pvs=4) 도입
- 주식 데이터 fetch 시 [flatMap](https://oatt.notion.site/fetch-flatMap-9214cc1ec87b4b79a9d7e594045276e6?pvs=4)을 사용하여 백프레셔 관리

<br/>

### 개발기간

***

- 2024.04 ~

<br/>


