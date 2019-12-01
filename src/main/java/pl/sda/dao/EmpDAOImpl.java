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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOImpl implements EmpDAO {
    private final SessionFactory sessionFactory;

    public EmpDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void doInTransaction(Consumer<Session> work) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            work.accept(session);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public <T> T getInTransaction(Function<Session, T> work) {
        T result = null;
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            result = work.apply(session);
        }
        return result;
    }

    @Override
    public Employee findById(int id) throws Exception {
        return getInTransaction(session -> session.find(Employee.class, id));
    }

    @Override
    public void create(Employee employee) throws Exception {
        doInTransaction(session -> session.persist(employee));
    }

    @Override
    public void update(Employee employee) throws Exception {
        doInTransaction(session -> session.update(employee));
    }

    @Override
    public void delete(int id) throws Exception {
        doInTransaction(session -> {
            Employee employee = session.find(Employee.class, id);
            session.delete(employee);
        });
    }

    @Override
    public void create(List<Employee> employees) throws Exception {
        doInTransaction(session -> employees.forEach(session::persist));
    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) {
        return getInTransaction(session -> {
            Query<BigDecimal> query = session.createQuery("select sum(salary) from Employee where dept.deptno = :dept", BigDecimal.class);
            query.setParameter("dept", dept);
            List<BigDecimal> departments = query.list();
            return departments.get(0);
        });
    }

    @Override
    public List<Employee> getEmployeesByDept(int deptNo) {
        return getInTransaction(session -> {
            Criteria cr = session.createCriteria(Employee.class);
            cr.add(Restrictions.eq("dept.deptno", deptNo));
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return cr.list();
        });
    }

    @Override
    public List<Employee> getEmployeeByName(String ename) {
        return getInTransaction(session -> {
            Query<Employee> query = session.createQuery("from Employee where ename = :ename", Employee.class);
            query.setParameter("ename", ename);
            return query.list();
        });
    }
}
