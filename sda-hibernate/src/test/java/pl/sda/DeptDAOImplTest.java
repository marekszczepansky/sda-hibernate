package pl.sda;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.Test;
import pl.sda.dao.DeptDAO;
import pl.sda.dao.DeptDAOImpl;
import pl.sda.domain.Department;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOImplTest {

    private DeptDAO deptDAO;

    @Before
    public void init() throws IOException, ClassNotFoundException, SQLException {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        try(Session session = factory.openSession()){
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {
                        TestUtil.cleanUpDatabase(connection);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

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
        Department department = new Department(99, "HR", "Las Vegas");
        deptDAO.create(department);

        Department departmentFromDb = deptDAO.findById(99);

        assertNotNull(departmentFromDb);
        assertEquals(department.getDeptno(), departmentFromDb.getDeptno());
        assertEquals(department.getDname(), departmentFromDb.getDname());
        assertEquals(department.getLocation(), departmentFromDb.getLocation());

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