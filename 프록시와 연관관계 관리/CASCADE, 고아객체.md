특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶으면 영속성 전이 기능을 사용하면 된다. <br/>
JPA는 CASCADE 옵션으로 영속성 전이를 제공한다. <br/>
영속성 전이를 사용하면 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장할 수 있다.

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

위처럼 부모-자식 클래스가 1:N으로 매핑되어 있을 때, 부모를 영속성 컨텍스트에 저장한다면 자식들 역시 같이 저장할 수 있다. 

<br/>

### 주의사항

영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음 <br/>
엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐 <br/>
하나의 부모가 하나의 자식만 관리할 때는 써도 되지만 (연관관계가 서로 밖에 없을 때) , 그 외에 다른 엔티티랑 연관관계가 있다면 쓰지 말 것

+) 연관관계 편의메소드에서 주인(parent)값의 set정보를 지우고 반대쪽의 정보만 저장해보겠다(CascadeType.ALL은 그대로)

<Parent.java>

```java

@Entity
@Getter
@Setter
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) //영속성 전이
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
//        child.setParent(this); // 주석처리
    }

```

<Main.java>

```java

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            tx.commit();

```

아래 결과값은 CascadeType은 유지하고 persist는 parent만 한 상태에서 Child를 따로 persist 처리하지 않아도 엔티티가 잘 저장된 것을 볼 수 있고 <br/>
연관관계를 주인값에 저장하지 않았으니 child의 fk값은 null이 들어간 것을 볼 수 있다

이를 통해 CASCADE는 자식의 엔티티를 저장하는 기능만 할 뿐인지 연관관계 값 저장에는 영향을 미치지 않는 것을 볼 수 있다

![image](https://user-images.githubusercontent.com/78454649/204081261-e6eeca39-66ce-4794-955e-495a8a608335.png)

<br/>

### 종류

• ALL: 모두 적용<br/>
• PERSIST: 영속<br/>
• REMOVE: 삭제<br/>
• MERGE: 병합<br/>
• REFRESH: REFRESH <br/>
• DETACH: DETACH<br/>

---

<br/>

### 고아객체

고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제

* orphanRemoval = true 

* Parent parent1 = em.find(Parent.class, id); 
parent1.getChildren().remove(0); // 첫번 째 자식 엔티티를 컬렉션에서 제거

* DELETE FROM CHILD WHERE ID=?

<br/>

#### 주의점

참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로보고 삭제하는 기능<br/>
참조하는 곳이 하나일 때 사용해야함! 

**특정 엔티티가 개인 소유할 때 사용**

@OneToOne, @OneToMany만 가능

* 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께
제거된다. 이것은 CascadeType.REMOVE처럼 동작한다


