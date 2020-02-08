package pl.sda.dao;

import pl.sda.domain.Department;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOJpaImpl implements DeptDAO {
    private final EntityManagerFactory emf;

    private void doInTransaction(Consumer<EntityManager> job) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            job.accept(em);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    private <R> R getWithoutTransaction(Function<EntityManager, R> job) {
        EntityManager em = emf.createEntityManager();
        try {
            return job.apply(em);
        } finally {
            em.close();
        }
    }

    public DeptDAOJpaImpl(EntityManagerFactory em) {
        this.emf = em;
    }

    @Override
    public Department findById(int id) {
        return getWithoutTransaction(em ->em.find(Department.class, id));
    }

    @Override
    public void create(Department department) {
        doInTransaction(em -> em.persist(department));
    }

    @Override
    public void update(Department department) {
        doInTransaction(em -> em.merge(department));
    }

    @Override
    public void updateName(int id, String dname) {
        doInTransaction(em -> {
            Department dept = em.find(Department.class, id);
            dept.setDname(dname);
        });
    }

    @Override
    public void delete(int id) {
        doInTransaction(em -> {
            Department dept = em.find(Department.class, id);
            em.remove(dept);
        });
    }

    @Override
    public List<Department> findByName(String dname) {
        return getWithoutTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Department> q = cb.createQuery(Department.class);
            Root<Department> c = q.from(Department.class);
            ParameterExpression<String> p = cb.parameter(String.class);
            q.select(c).where(cb.equal(c.get("dname"), p));

            TypedQuery<Department> query = em.createQuery(q);
            query.setParameter(p, dname);
            List<Department> departments = query.getResultList();
            return departments;
        });
    }

    @Override
    public List<Department> findByLocation(String location) {
        return getWithoutTransaction(em -> {
            TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d WHERE d.location = :location", Department.class);
            query.setParameter("location", location);
            List<Department> departments = query.getResultList();
            return departments;
        });
    }

//    Constuctor Query - does not return jpa entity
//    @Override
//    public List<Department> findByLocation(String location) {
//        EntityManager em = sessionFactory.createEntityManager();
//        try{
//            TypedQuery<Department> query = em.createQuery("SELECT new pl.sda.domain.Department(d.deptno, d.dname, d.location) FROM Department d WHERE d.location = :location", Department.class);
//            query.setParameter("location", location);
//            List<Department> departments = query.getResultList();
//            return departments;
//        }finally {
//            em.close();
//        }
//    }

//    Native query example
//    @Override
//    public List<Department> findByLocation(String location) {
//        EntityManager em = sessionFactory.createEntityManager();
//        try{
//            javax.persistence.Query query = em.createNativeQuery("SELECT d.* FROM Dept d WHERE d.location = :location", Department.class);
//            query.setParameter("location", location);
//            List<Department> departments = query.getResultList();
//            return departments;
//        }finally {
//            em.close();
//        }
//    }
}
