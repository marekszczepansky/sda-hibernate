package pl.sda.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOImpl implements EmpDAO {
    private final SessionFactory sessionFactory;

    public EmpDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee findById(int id) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Employee.class, id);
        }
    }

    @Override
    public void create(Employee employee) throws Exception {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

    }

    @Override
    public void update(Employee employee) throws Exception {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

    }

    @Override
    public void delete(int id) throws Exception {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Employee employee = session.find(Employee.class, id);
            session.delete(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

    }

    @Override
    public void create(List<Employee> employees) throws Exception {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            employees.forEach(session::persist);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            Query<BigDecimal> query = session.createQuery("select sum(salary) from Employee where dept.deptno = :dept", BigDecimal.class);
            query.setParameter("dept", dept);
            List<BigDecimal> departments = query.list();
            return departments.get(0);
        }
    }

    @Override
    public List<Employee> getEmployeesByDept(int deptNo) {
        try(Session session = sessionFactory.openSession()) {
            Criteria cr = session.createCriteria(Employee.class);
            cr.add(Restrictions.eq("dept.deptno", deptNo));
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return cr.list();
        }
    }

    @Override
    public List<Employee> getEmployeeByName(String ename) {
        try(Session session = sessionFactory.openSession()) {
            Query<Employee> query = session.createQuery("from Employee where ename = :ename", Employee.class);
            query.setParameter("ename", ename);
            return query.list();
        }
    }
}
