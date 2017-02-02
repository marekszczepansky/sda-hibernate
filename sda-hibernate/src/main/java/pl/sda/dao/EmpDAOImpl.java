package pl.sda.dao;

import org.hibernate.SessionFactory;
import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOImpl implements EmpDAO {
    private final SessionFactory sessionFactory;

    public EmpDAOImpl(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee findById(int id) throws Exception {
        return null;
    }

    @Override
    public void create(Employee employee) throws Exception {

    }

    @Override
    public void update(Employee employee) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception {

    }

    @Override
    public void create(List<Employee> employees) throws Exception {

    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        return null;
    }

    @Override
    public List<Employee> getEmployeesByDept(int deptNo) {
        return null;
    }

    @Override
    public List<Employee> getEmployeeByName(String ename) {
        return null;
    }
}
