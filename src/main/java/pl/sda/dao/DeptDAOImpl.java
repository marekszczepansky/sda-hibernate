package pl.sda.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import pl.sda.domain.Department;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOImpl implements DeptDAO {
    private final SessionFactory sessionFactory;

    public DeptDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Department findById(int id) throws Exception {
        try(Session session = sessionFactory.openSession()) {
            return session.find(Department.class, id);
        }

    }

    @Override
    public void create(Department department) throws Exception {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(department);
            tx.commit();
        }catch(Exception ex){
            if(tx != null && !tx.getRollbackOnly()){
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void update(Department department) throws Exception {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(department);
            tx.commit();
        }catch(Exception ex){
            if(tx != null && !tx.getRollbackOnly()){
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void updateName(int id, String dname) throws Exception {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Department dept = session.find(Department.class, id);
            dept.setDname(dname);
            tx.commit();
        }catch(Exception ex){
            if(tx != null && !tx.getRollbackOnly()){
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void delete(int id) throws Exception {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Department dept = session.find(Department.class, id);
            session.delete(dept);
            tx.commit();
        }catch(Exception ex){
            if(tx != null && !tx.getRollbackOnly()){
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public List<Department> findByName(String dname) {
        try(Session session = sessionFactory.openSession()) {
            Criteria cr = session.createCriteria(Department.class);
            cr.add(Restrictions.eq("dname", dname));
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List departments = cr.list();
            return departments;
        }

    }

    @Override
    public List<Department> findByLocation(String location) {
        try(Session session = sessionFactory.openSession()) {
            Query<Department> query = session.createQuery("from Department where location = :location", Department.class);
            query.setParameter("location", location);
            List<Department> departments = query.list();
            return  departments;
        }
    }
}
