### Named 쿼리

정적 쿼리는 미리 정의한 쿼리에 이름을 부여해서 필요할 때 사용할 수 있는데 이것을 Named 쿼리라고 한다. <br/>
Named 쿼리는 한 번 정의하면 변경할 수 없는 정적인 쿼리다.

 
Named 쿼리는 애플리케이션 로딩 시점에 JPQL 문법을 체크하고 미리 파싱해둔다. 따라서 오류를 빨리 확인할 수 있고, <br/>
사용하는 시점에는 파싱된 결과를 재사용하므로 성능상 이점도 있다.

 ### 어노테이션
 
 먼저 @NamedQuery 어노테이션을 사용해 정적 쿼리를 사용해 보자.
 
 ```java
 
 @Entity
@NamedQuery(
        name = "Member.findByUsername",
        query="select m from Member m where m.username = :username")
public class Member {
	...
}
 
 ```
 
 name 옵션에 쿼리 이름을 부여하고 query 옵션에 사용할 쿼리를 입력한다.
 
 ```java

List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
	.setParameter("username", "회원1")
	.getResultList();

```
 
 Named 쿼리를 사용할 때는 위 예시와 같이 em.createNamedQuery() 메소드에 Named 쿼리 이름을 입력하면 된다.

+) 일반적으로 충돌을 방지하기 위해 Named 쿼리는 name 앞에 [클래스명]. 을 붙여준다.
