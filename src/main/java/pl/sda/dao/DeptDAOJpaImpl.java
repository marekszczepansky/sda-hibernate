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

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOJpaImpl implements DeptDAO {
    private final EntityManagerFactory emf;

    public DeptDAOJpaImpl(EntityManagerFactory em) {
        this.emf = em;
    }

    @Override
    public Department findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Department department) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(department);
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

    @Override
    public void update(Department department) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(department);
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

    @Override
    public void updateName(int id, String dname) {
        doInTransaction(em -> {
            Department dept = em.find(Department.class, id);
            dept.setDname(dname);
        });

    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Department dept = em.find(Department.class, id);
            em.remove(dept);
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

    @Override
    public List<Department> findByName(String dname) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Department> q = cb.createQuery(Department.class);
            Root<Department> c = q.from(Department.class);
            ParameterExpression<String> p = cb.parameter(String.class);
            q.select(c).where(cb.equal(c.get("dname"), p));

            TypedQuery<Department> query = em.createQuery(q);
            query.setParameter(p, dname);
            List<Department> departments = query.getResultList();
            return departments;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Department> findByLocation(String location) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d WHERE d.location = :location", Department.class);
            query.setParameter("location", location);
            List<Department> departments = query.getResultList();
            return departments;
        } finally {
            em.close();
        }
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
