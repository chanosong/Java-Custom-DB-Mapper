# JAVA CUSTOM DB MAPPER

----

### 해당 리포지토리는 제한된 상황에서 DB를 사용하기 위한 커스텀 매퍼를 구현하는 것이 목적이다.

### 1차 목표
- simpleDb.setDevMode(true); 를 수행 시 실제로 실행되는 SQL 쿼리가 콘솔 출력
- build.gradle 에 기술된 라이브러리 외에 다른 라이브러리 사용 불가
- simpleDb는 MySQL / MariaDB를 지원
- SimpleDbTest.java 의 모든 테스트케이스 통과를 목표

### 추가 목표
- 트랜잭션 기능 구현
  - simpleDb.startTransaction();
  - simpleDb.rollback();
  - simpleDb.commit();
- simpleDb 객체를 다중스레드 환경에서 동시에 사용가능하게, 스레드 당 1개 씩 존재하게 변경
- 리플렉션을 사용한 ddl-auto 기능 구현
- connection pool 기능 구현