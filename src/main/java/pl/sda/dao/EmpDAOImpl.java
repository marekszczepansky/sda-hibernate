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
    // TODO: implement method
        return null;
    }

    @Override
    public void create(Employee employee) throws Exception {
        // TODO: implement method

    }

    @Override
    public void update(Employee employee) throws Exception {
        // TODO: implement method

    }

    @Override
    public void delete(int id) throws Exception {
        // TODO: implement method

    }

    @Override
    public void create(List<Employee> employees) throws Exception {
        // TODO: implement method - create all entities in ine transaction (all on nothing)

    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        // TODO: implement method
        return null;
    }

    @Override
    public List<Employee> getEmployeesByDept(int deptNo) {
        // TODO: implement method
        return null;
    }

    @Override
    public List<Employee> getEmployeeByName(String ename) {
        // TODO: implement method
        return null;
    }
}
