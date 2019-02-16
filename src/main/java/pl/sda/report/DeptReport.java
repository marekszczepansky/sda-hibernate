package pl.sda.report;

import pl.sda.dao.DeptDAOJpaImpl;
import pl.sda.domain.Department;
import pl.sda.domain.Employee;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by pzawa on 21.02.2017.
 */
public class DeptReport {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory("PU");
        Department department = getDepartmentFromDB(10);
        System.out.println(department.getDeptno() + ":" + department.getDname() + ":" + department.getLocation());
        for (Employee employee : department.getEmployees()) {
            System.out.println("       " + employee.getEmpno() + ":" + employee.getEname() + ":" + employee.getJob() + ":" + employee.getSalary());
        }
    }


    private static Department getDepartmentFromMock(int deptId) {
        try {
            List<Employee> employeeList = new ArrayList<>();
            Department dept = new Department(deptId, "TEST", "TEST", employeeList);
            Employee newEmployee1 = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), dept);
            employeeList.add(newEmployee1);
            Employee newEmployee2 = new Employee(9001, "JNOWAK", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10001), BigDecimal.valueOf(9.0), dept);
            employeeList.add(newEmployee2);
            return dept;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Department getDepartmentFromDB(int deptId) {
        DeptDAOJpaImpl deptDAO = new DeptDAOJpaImpl(factory);

        return deptDAO.findById(deptId);
    }
}
