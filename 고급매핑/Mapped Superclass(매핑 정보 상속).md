공통 매핑 정보가 필요할 때 사용(id, name)

이전의 엔티티 상속과는 다르게 공통매핑 정보만 통일하고 싶을 때 사용

![image](https://user-images.githubusercontent.com/78454649/153156439-cbc7b4b6-b22f-41ad-b092-ee1bb5361932.png)

### 테이블 설계를 다 마치거나 테이블을 설계할 때 모든 테이블에 공통적으로 수정일과 등록일 등이 있어야 한다면?

다 복붙하는 방법도 있겠지만 속성만 상속받아 사용하고 싶어..! 그것이 **Mapped Superclass**

BaseEntity.java(매핑정보만 받는 슈퍼클래스)

```java

**@MappedSuperclass**
@Getter @Setter
public class BaseEntity {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModiBy;
    private LocalDateTime lastModiDate;
}

```

다음 상속이 필요한 엔티티에 extend해주기만 하면 된다

```java
public class Team **extends BaseEntity**{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> memberList = new ArrayList<>();

```

이렇게 상속받고 실행한다면 콘솔창에 

```java

create table Member (
       MEMBER_ID bigint not null,
        createdBy varchar(255),
        createdDate timestamp,
        lastModiBy varchar(255),
        lastModiDate timestamp,
        city varchar(255),
        name varchar(255),

```
상속받은 필드 추가 완료!

![image](https://user-images.githubusercontent.com/78454649/153158430-66f60620-304c-4346-8132-06e8a7ff03dc.png)

---

- 정리

상속관계 매핑X 

엔티티X, 테이블과 매핑X 

부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공

조회, 검색 불가(em.find(BaseEntity) 불가) 

직접 생성해서 사용할 일이 없으므로 추상 클래스 권장

테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할

주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
