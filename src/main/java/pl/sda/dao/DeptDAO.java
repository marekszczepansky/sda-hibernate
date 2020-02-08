package pl.sda.dao;

import pl.sda.domain.Department;

import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public interface DeptDAO extends EntityDAO<Department> {

    void updateName(int deptId, String dname) throws Exception;

    List<Department> findByName(String name);

    List<Department> findByLocation(String location);
}
