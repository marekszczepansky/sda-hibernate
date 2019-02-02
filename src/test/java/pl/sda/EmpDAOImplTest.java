package pl.sda;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import pl.sda.dao.DeptDAO;
import pl.sda.dao.DeptDAOImpl;
import pl.sda.dao.EmpDAO;
import pl.sda.dao.EmpDAOImpl;
import pl.sda.domain.Department;
import pl.sda.domain.Employee;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOImplTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private EmpDAO empDAO;
    private DeptDAO deptDAO;

    @Before
    public void init() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//        TestUtil.cleanUpDatabase(factory);
        empDAO = new EmpDAOImpl(factory);
        deptDAO = new DeptDAOImpl(factory);
    }

    @Test
    public void findById() throws Exception {
        Employee employee = empDAO.findById(7369);

        assertNotNull(employee);
        assertEquals(20, employee.getDept().getDeptno());
        assertEquals("SMITH", employee.getEname());
        assertEquals("CLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertEquals(BigDecimal.valueOf(800), employee.getSalary());
        assertTrue(BigDecimal.valueOf(0.0).compareTo(employee.getCommision()) == 0);

    }

    @Test
    public void create() throws Exception {
        Department department = deptDAO.findById(20);

        Employee newEmployee = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), department);

        empDAO.create(newEmployee);

        Employee employeeFromDB = empDAO.findById(9000);

        assertNotNull(employeeFromDB);
        assertEquals(employeeFromDB.getEmpno(), newEmployee.getEmpno());
        assertEquals(employeeFromDB.getEname(), newEmployee.getEname());
        assertEquals(employeeFromDB.getJob(), newEmployee.getJob());
        assertEquals(employeeFromDB.getHiredate().getTime(),newEmployee.getHiredate().getTime());
        assertTrue(employeeFromDB.getSalary().compareTo(newEmployee.getSalary()) == 0);
        assertTrue(employeeFromDB.getCommision().compareTo(newEmployee.getCommision()) == 0);
    }

    @Test
    public void update() throws Exception {
        Employee employee = empDAO.findById(7369);
        assertNotNull(employee);

        employee.setJob("SUPERCLERK");
        empDAO.update(employee);
        employee = empDAO.findById(7369);

        assertNotNull(employee);
        assertEquals("SMITH", employee.getEname());
        assertEquals("SUPERCLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertTrue(BigDecimal.valueOf(800).compareTo(employee.getSalary()) == 0);
        assertTrue(BigDecimal.valueOf(0.0).compareTo(employee.getCommision()) == 0);

    }

    @Test
    public void delete() throws Exception {
        Employee employee = empDAO.findById(7369);
        assertNotNull(employee);

        empDAO.delete(7369);

        employee = empDAO.findById(7369);
        assertNull(employee);
    }

    @Test
    public void createMultipleEmployeesAllOk() throws Exception {
        Department department20 = deptDAO.findById(20);
        Department department30 = deptDAO.findById(30);

        Employee newEmployee1 = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), department20);
        Employee newEmployee2 = new Employee(9001, "JNOWAK", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10001), BigDecimal.valueOf(9.0), department30);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(newEmployee1);
        employeeList.add(newEmployee2);

        empDAO.create(employeeList);

        Employee employeeFromDB1 = empDAO.findById(9000);
        Employee employeeFromDB2 = empDAO.findById(9001);

        assertNotNull(employeeFromDB1);
        assertEquals(employeeFromDB1.getEname(), newEmployee1.getEname());
        assertEquals(employeeFromDB1.getJob(), newEmployee1.getJob());
        assertEquals(employeeFromDB1.getHiredate().getTime(), newEmployee1.getHiredate().getTime());
        assertTrue(employeeFromDB1.getSalary().compareTo(newEmployee1.getSalary()) == 0);
        assertTrue(employeeFromDB1.getCommision().compareTo(newEmployee1.getCommision()) == 0);

        assertNotNull(employeeFromDB2);
        assertEquals(employeeFromDB2.getEname(), newEmployee2.getEname());
        assertEquals(employeeFromDB2.getJob(), newEmployee2.getJob());
        assertEquals(employeeFromDB2.getHiredate().getTime(), newEmployee2.getHiredate().getTime());
        assertTrue(employeeFromDB2.getSalary().compareTo(newEmployee2.getSalary()) == 0);
        assertTrue(employeeFromDB2.getCommision().compareTo(newEmployee2.getCommision()) == 0);
    }

    @Test(expected=PersistenceException.class)
    public void createMultipleEmployeesSecondRowFail() throws Exception {
        Department department20 = deptDAO.findById(20);
        Department department30 = deptDAO.findById(30);

        Employee newEmployee1 = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), department20);
        Employee newEmployee2 = new Employee(9000, "JNOWAK", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10001), BigDecimal.valueOf(9.0), department30);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(newEmployee1);
        employeeList.add(newEmployee2);

        try {
            empDAO.create(employeeList);
        }catch(Exception ex){
            Employee employeeFromDB = empDAO.findById(9000);
            assertNull(employeeFromDB);
            throw ex;
        }
    }

    @Test
    public void getTotalSalaryByDept() throws Exception {
        BigDecimal salaryFor10Dept = empDAO.getTotalSalaryByDept(10);

        assertTrue(BigDecimal.valueOf(8750).compareTo(salaryFor10Dept) == 0);
    }

    @Test
    public void getEmployeesByDept() throws Exception {
        List<Employee> employeeList = empDAO.getEmployeesByDept(10);

        assertNotNull(employeeList);
        assertTrue(employeeList.size() == 3);
    }

    @Test
    public void getEmployeeByName() throws Exception {
        List<Employee> employeeList = empDAO.getEmployeeByName("SMITH");

        assertNotNull(employeeList);
        assertTrue(employeeList.size() == 1);
    }

}
