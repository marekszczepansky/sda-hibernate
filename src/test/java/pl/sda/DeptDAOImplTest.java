package pl.sda;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import pl.sda.dao.DeptDAO;
import pl.sda.dao.DeptDAOImpl;
import pl.sda.domain.Department;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOImplTest {

    private DeptDAO deptDAO;

    @Before
    public void init() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//        TestUtil.cleanUpDatabase(factory);
        deptDAO = new DeptDAOImpl(factory);

    }

    @Test
    public void findById() throws Exception {
        Department department = deptDAO.findById(10);
        assertNotNull(department);
        assertEquals(10, department.getDeptno());
        assertEquals("Accounting", department.getDname());
        assertEquals("New York", department.getLocation());
    }

    @Test
    public void create() throws Exception {
        Department department = new Department(99, "HR", "Las Vegas", new ArrayList<>());
        deptDAO.create(department);

        Department departmentFromDb = deptDAO.findById(99);

        assertNotNull(departmentFromDb);
        assertEquals(department.getDeptno(), departmentFromDb.getDeptno());
        assertEquals(department.getDname(), departmentFromDb.getDname());
        assertEquals(department.getLocation(), departmentFromDb.getLocation());
    }

    @Test(expected=PersistenceException.class)
    public void createDuplicatedDepartment() throws Exception {
        Department department = new Department(99, "HR", "Las Vegas", new ArrayList<>());
        deptDAO.create(department);
        deptDAO.create(department);
    }

    @Test
    public void update() throws Exception {
        Department department = deptDAO.findById(10);
        assertNotNull(department);

        department.setDname("NEW_DEPT");
        deptDAO.update(department);

        department = deptDAO.findById(10);

        assertNotNull(department);
        assertEquals(10, department.getDeptno());
        assertEquals("NEW_DEPT", department.getDname());
        assertEquals("New York", department.getLocation());
    }

    @Test
    public void updateName() throws Exception {
        deptDAO.updateName(10, "SUPER_DEPT");
        Department department = deptDAO.findById(10);

        assertNotNull(department);
        assertEquals(10, department.getDeptno());
        assertEquals("SUPER_DEPT", department.getDname());
        assertEquals("New York", department.getLocation());
    }

    @Test
    public void delete() throws Exception {
        Department department = deptDAO.findById(40);
        assertNotNull(department);

        deptDAO.delete(40);

        department = deptDAO.findById(40);
        assertNull(department);
    }

    @Test
    public void findByName() throws Exception {
        List<Department> departmentList = deptDAO.findByName("Sales");
        assertNotNull(departmentList);
        assertTrue(departmentList.size() == 1);
    }

    @Test
    public void findByLocation() throws Exception {
        List<Department> departmentList = deptDAO.findByLocation("Chicago");
        assertNotNull(departmentList);
        assertTrue(departmentList.size() == 1);
    }
}