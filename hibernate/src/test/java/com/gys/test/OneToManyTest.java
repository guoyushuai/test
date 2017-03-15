package com.gys.test;

import com.gys.pojo.Dept;
import com.gys.pojo.Employee;
import com.gys.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OneToManyTest {

    @Test
    public void save() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Dept dept = new Dept();
        dept.setDeptname("财务部");

        Employee employee = new Employee();
        employee.setEmpname("王五");
        employee.setDept(dept);//面向对象的方式给t_employee表中的deptid字段复制

        Employee employee1 = new Employee();
        employee1.setEmpname("赵六");
        employee1.setDept(dept);

        /*Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(employee);
        employeeSet.add(employee1);

        dept.setEmployeeSet(employeeSet);*/

        //先存一，再存多
        //让一的一端放弃关系维护<set name="employeeSet" inverse="true"/>(让多的一端维护因为多的一端存放有一的外键)

        session.save(dept);
        session.save(employee);
        session.save(employee1);

        session.getTransaction().commit();
    }

    @Test
    public void findOneToMany() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Dept dept = (Dept) session.get(Dept.class,1);
        System.out.println(dept.getDeptname());

        //默认懒加载
        Set<Employee> employeeSet = dept.getEmployeeSet();
        for (Employee employee : employeeSet) {
            System.out.println(employee.getEmpname());
        }

        session.getTransaction().commit();
    }

    @Test
    public void findManyToOne() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        /*<!--
            lazy:false 关闭懒加载
            fetch:join 获取相应属性时采用链接查询，避免N+1
            (每次获得多的时候总是需要获得一的时候才配fetch)
        -->*/
        /*<many-to-one name="dept" class="Dept" column="deptid" fetch="join"/>*/

        Criteria criteria = session.createCriteria(Employee.class);
        List<Employee> employeeList = criteria.list();

        for (Employee employee : employeeList) {
            System.out.println(employee.getEmpname() + "->" + employee.getDept().getDeptname());
        }

        //N+1
        /*String hql = "from Employee";//严格按照HQL的方式查询，配置里配了join也没用
        Query query = session.createQuery(hql);
        List<Employee> employeeList = query.list();

        for (Employee employee : employeeList) {
            System.out.println(employee.getEmpname() + "->" + employee.getDept().getDeptname());
        }*/

        /*select * from t_employee left join t_dept on t_employee.deptid = t_dept.id*/


        /*Employee employee = (Employee) session.get(Employee.class,8);
        System.out.println(employee.getEmpname());
        //懒加载
        System.out.println(employee.getDept());*/

        session.getTransaction().commit();
    }

    /**
     * 级联删除(外键约束)
     * 1、触发器
     * 2、一的一端配置cascade="delete"（删部门的同时裁掉部门的员工）
     */
    @Test
    public void delete() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Dept dept = (Dept) session.get(Dept.class,4);
        session.delete(dept);

        session.getTransaction().commit();

    }
}


