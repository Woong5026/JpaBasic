JPA의 구동방식은 다음과 같다.

![image](https://user-images.githubusercontent.com/78454649/226581858-a61dcdda-dbc1-444e-8778-50fcf19e3fbb.png)

이에 맞춰 다음 코드를 작성한다.(JpaMain.java)

```java

public class JpaMain {
 
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
 
        EntityManager em = emf.createEntityManager();
 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
 
        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("Han");
            em.persist(member);
            
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
 
        emf.close();
    }
}

```

코드를 크게 3부분으로 나누어 보겠다

<br/>

#### 엔티티 매니저 설정

JPA를 시작하려면 우선 persistence.xml의 설정 정보를 사용해서 엔티티 매니저 팩토리를 생성해야 한다.

```java

EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

```

엔티티 매니저 팩토리는 애플리케이션 전체에서 딱 한 번만 생성하고 공유해서 사용해야 한다. <br/>
엔티티 매니저 팩토리에서 엔티티 매니저를 생성한다.

<br/>

#### 트랜잭션 관리

엔티티 매니저를 사용해서 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있다. <br/>
엔티티 매니저는 트랜잭션(고객 요청)당 생성, 사용하고 버린다. (JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다.)

```java

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        // 등록/수정/삭제/조회
        ...
        
        tx.commit()
        
        ...
        
        
        // 사용이 끝난 엔티티 매니저는 반드시 종료해야 한다. 애플리케이션을 종료할 때 엔티티 매니저 팩토리도 종료해야 한다.
        
        em.close();
        ...
        emf.close();


```

<br/>

#### 비즈니스 로직

비즈니스 로직은 크게 등록 ,수정, 삭제, 조회 4가지가 있다.

<br/>

* 등록

```java

em.persist(member)

```

단순히 객체 필드에 원하는 값을 넣고 persist하면 된다. 그럼 JPA는 객체 엔티티에 매핑 정보(어노테이션)를 분석해서 커밋시 insert 쿼리를 DB에 전달한다.

<br/>

* 수정

수정은 신기하게도 setter를 사용하면 DB에까지 영향을 줄 수 있다. <br/>
이 역시 커밋 시 기존 객체 정보에 대해 수정된 사항이 있으면 update 쿼리를 DB에 전달하기 때문이다.

<br/>

* 삭제

동일한 로직으로 진행된다.(em.remove(member))

<br/>

* 조회

한 건을 조회할 때는 간단하다.

```java

Member findMember = em.find(Member.class, id);

```

find() 메서드는 조회할 엔티티 타입과 @Id(PK)로 엔티티 하나를 조회할 수 있다. 이 때, select 쿼리를 DB에 전달한다.

그러나 하나 이상의 회원 목록을 일정 조건으로 조회하는 경우가 있다. <br/>
JPA는 객체를 중심으로 개발하지만 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능하다. <br/>
즉 sql을 사용해야 하는데, JPA는 sql을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공한다. 

데이터베이스 테이블을 대상으로 쿼리하는 SQL과 달리 JPQL은 객체를 대상으로 쿼리하는 객체 지향 SQL이기 때문에 <br/>
객체 지향 프로그래밍과 잘 맞고 DB dialect를 변경해도 그에 맞게 잘 번역해준다. 

```java

List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

```




