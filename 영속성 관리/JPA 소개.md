### JPA

JPA(Java Persistence API)는 자바 진영의 ORM 기술 표준이다.

여기서 ORM(Object-Relational Mapping)은 이름 그대로 객체와 관계형 데이터베이스를 매핑한다는 뜻이다. 

ORM 프레임워크는 객체와 테이블을 매핑해서 패러다임의 불일치 문제를 개발자 대신 해결해준다. 

즉 **객체는 객체대로 개발하고, RDB는 RDM대로 개발하여 그 차이들을 ORM이 중간에서 매핑**해준다.

### 왜 JPA를 사용해야 하는가?

* 생산성
* 유지보수
* 패러다임의 불일치 해결
* 성능

#### 생산성

JPA를 사용하면 다음 코드처럼 자바 컬렉션에 객체를 저장하듯이 JPA에게 저장할 객체를 전달하면 된다.

```java

jpa.persist(member); //저장
Member member = jpa.find(memberId); //조회

```
 
PA는 알아서 insert, select sql을 DB에 보내주기 때문에 개발자 입장에서 지루하고 반복적인 SQL을 작성하지 않아도 된다. <br/>
JPA는 CRUD가 이미 정의되어 있고, 심지어 setter로 DB값까지 변경이 가능하다. 

#### 유지보수

앞서 얘기했듯이 SQL에 의존적인 개발에서 엔티티에 필드를 하나만 추가해도 관련된 등록, 수정, 조회 SQL 결과를 매핑하기 위한 JDBC API 코드를 모두 변경해야 했다. 

반면에 JPA를 사용하면 이런 과정을 JPA가 알아서 처리해주므로 필드를 추가하거나 삭제할 때 수정해야 할 코드가 줄어든다.


#### 패러다임의 불일치 해결

앞서 얘기한 패러다임의 불일치로 인한 문제점들을 JPA가 해결해준다. 

상속을 예로 들면 persist만 해줘도 부모와 자식의 쿼리를 나눠서 insert 해주고 조회의 경우도 JPA가 알아서 부모와 자식을 조인해서 가져온다.


#### 성능

* 캐시

JPA는 자체적으로 1차 캐시를 가지고 있고 이는 한 트랜잭션 단위로 움직인다. 

```java

String memberId = "100";
Member m1 = jpa.find(Member.class, memberId); //SQL
Member m2 = jpa.find(Member.class, memberId); //캐시
println(m1 == m2) //true

```


위와 같이 같은 PK값의 객체를 조회할 때, 한 번 조회한 객체는 1차 캐시에 저장해두고 이 후 조회할 때 sql을 DB에 보내지 않고 캐시에서 해당 객체를 조회한다.

캐시의 경우 일부 성능 향상이 있긴 하지만 사용 목적이 성능 향상을 위한 것은 아니고 JPA 내부 메커니즘을 유지하기 위해서 사용된다.(m1==m2 -> true)

* 쓰기지연

트랜잭션을 커밋할 때까지 insert sql을 모아서 한번에 전송한다.

```java

transaction.begin(); // [트랜잭션] 시작
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);
//여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
//커밋하는 순간 데이터베이스에 INSERT SQL을 모아서 보낸다.
transaction.commit(); // [트랜잭션] 커밋

```

* 지연로딩

지연 로딩 : 객체가 실제 사용될 때 로딩 

```java

Member member = memberDAO.find(memberId); //select * from member
 Team team = member.getTeam();
 String teamName = team.getName(); //select * from team

```

즉시 로딩 : JOIN SQL로 한번에 연관된 객체까지 미리 조회

```java

Member member = memberDAO.find(memberId); //SELECT M.*, T.* FROM MEMBER JOIN TEAM …
 Team team = member.getTeam();
 String teamName = team.getName();

```

지연로딩은 네트워크를 두번 타지만, 객체가 실제로 사용될 때 sql을 보내기 때문에 굳이 사용하지 않는 객체까지 조회할 필요가 없어 성능 향상을 기대할 수 있다. 

그러나 멤버를 조회할 때 무조건 팀 객체를 같이 쓴다면 즉시 로딩으로 한 번에 가져오면 DB에 접근하는 횟수가 줄어들기 때문에 좋다.






