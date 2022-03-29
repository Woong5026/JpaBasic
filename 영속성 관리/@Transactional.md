Spring 에서 JPA 기술을 쓸 때 빼놓을 수 없는 기능중 하나는 @Transactional 입니다.

아래와 같이 많은 편리성을 우리에게 제공해주기 때문이지요.

transaction begin, commit을 자동 수행해준다. <br/>
예외를 발생시키면, rollback 처리를 자동 수행해준다.

### readOnly

기본값: false <br/>
사용법: @Transactional(readOnly = true)

기본값은 false 이며 true 로 세팅하는 경우 트랜잭션을 읽기 전용으로 변경합니다.

만약 읽기 전용 트랜잭션 내에서 INSERT, UPDATE, DELETE 작업을 해도 반영이 되지 않거나 DB 종류에 따라서 아예 예외가 발생하는 경우도 있습니다.

성능 향상을 위해 사용하거나 읽기 외의 다른 동작을 방지하기 위해 사용하기도 합니다.

### JPA 에서 Dirty Checking 무시

JPA 에는 Dirty Checking 이라는 기능이 있습니다.

개발자가 임의로 UPDATE 쿼리를 사용하지 않아도 트랜잭션 커밋 시에 1차 캐시에 저장되어 있는 <br/>
Entity 와 스냅샷을 비교해서 변경된 부분이 있으면 UPDATE 쿼리를 날려주는 기능입니다.

하지만 readOnly = true 옵션을 주면 스프링 프레임워크가 하이버네이트의 FlushMode 를 MANUAL <br/>
로 설정해서 Dirty Checking 에 필요한 스냅샷 비교 등을 생략하기 때문에 성능이 향상됩니다.

참고 : https://bcp0109.tistory.com/322
