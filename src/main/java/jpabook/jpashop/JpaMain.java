package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 고객의 요청이 들어오면 EntityManager 내에서 작업해야 한다
        EntityManager em = emf.createEntityManager();

        // 데이터베이스는 삽입 , 수정 등 동작이 이루어질때 꼭 트랜잭션이 발생해야 한다다
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // 자원이 끝나면 닫아줘야 한다다
        }
        // was 실행하면 EntityManagerFactory를 닫아줘야 풀링이 된다
        emf.close();

    }
}
