package pl.sda.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by pzawa on 02.02.2017.
 */
@Entity
@Table(name = "Dept")
public class Department {

    @Id
    @Column(name = "deptno")
    private int deptno;

    @Column(name = "dname")
    private String dname;

    @Column(name = "location")
    private String location;

    //@OneToMany(mappedBy = "dept")
    @Transient
    private List<Employee> employees;

    public Department() {
    }

    public Department(int deptno, String dname, String location, List<Employee> employees) {
        this.deptno = deptno;
        this.dname = dname;
        this.location = location;
        this.employees = employees;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return deptno == that.deptno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptno);
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
