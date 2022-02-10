특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들도 싶을 때

예): 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장

연관관계와는 관계가 

parent.java

```java

public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent")
    private List<Child> childList = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }

```


child.java

```java

public class Child {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_Id")
    private Parent parent;

```

main.java

```java

Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
            em.persist(child1);
            em.persist(child2);

```

코드를 짤 때 Parent 중심으로 짜고 싶고 persist를 세 번 하기도 번거롭다. parent가 persist될 떄 child도 자동으로 persist되었으면 한다

해결법은 cascade!

--- 

parent.java

```java

@OneToMany(mappedBy = "parent", **cascade = CascadeType.ALL**)
    private List<Child> childList = new ArrayList<>();

```

main.java

```java

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent); // parent만 persist 했으니 parent만 저장되는게 맞는데, child도 같이 저장된다 

```

### 주의사항

영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음

엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐

하나의 부모가 하나의 자식만 관리할 때는 써도 되지만 (연관관계가 서로 밖에 없을 때) , 그 외에 다른 엔티티랑 연관관계가 있다면 쓰지 말 것

### 종류

• ALL: 모두 적용<br/>
• PERSIST: 영속<br/>
• REMOVE: 삭제<br/>
• MERGE: 병합<br/>
• REFRESH: REFRESH <br/>
• DETACH: DETACH<br/>

---

### 고아객체

고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제

* orphanRemoval = true 

* Parent parent1 = em.find(Parent.class, id); 
parent1.getChildren().remove(0); // 첫번 째 자식 엔티티를 컬렉션에서 제거

* DELETE FROM CHILD WHERE ID=?

### 주의점

참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로보고 삭제하는 기능

참조하는 곳이 하나일 때 사용해야함! 

**특정 엔티티가 개인 소유할 때 사용**

@OneToOne, @OneToMany만 가능

* 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께
제거된다. 이것은 CascadeType.REMOVE처럼 동작한다


