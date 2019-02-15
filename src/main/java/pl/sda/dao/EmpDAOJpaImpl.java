package pl.sda.dao;

import pl.sda.domain.Employee;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by marek
 */
public class EmpDAOJpaImpl implements EmpDAO {
    private final EntityManagerFactory emf;

    public EmpDAOJpaImpl(EntityManagerFactory em) {
        this.emf = em;
    }

    @Override
    public Employee findById(int id) {
        // TODO: implement method
        return null;
    }

    @Override
    public void create(Employee employee) {
        // TODO: implement method

    }

    @Override
    public void update(Employee employee) {
        // TODO: implement method

    }

    @Override
    public void delete(int id) {
        // TODO: implement method

    }

    @Override
    public void create(List<Employee> employees) {
        // TODO: implement method - create all entities in one transaction (all on nothing)

    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) {
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
