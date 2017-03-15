package com.gys.test;

import com.gys.pojo.Student;
import com.gys.pojo.Teacher;
import com.gys.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ManyToMany {

    @Test
    public void save() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Student s1 = new Student();
        s1.setStuname("S1");

        Student s2 = new Student();
        s2.setStuname("S2");

        Teacher t1 = new Teacher();
        t1.setTeaname("T1");

        Teacher t2 = new Teacher();
        t2.setTeaname("T2");

        //维护两表之间的关系

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(t1);
        teacherSet.add(t2);

        s1.setTeacherSet(teacherSet);
        s2.setTeacherSet(teacherSet);

        //若没有联合主键约束下边会再次维护数据库，有会报错
        //inverse="true"其中一方放弃关系维护，下边的代码无效

        Set<Student> studentSet = new HashSet<>();
        studentSet.add(s1);
        studentSet.add(s2);

        t1.setStudentSet(studentSet);
        t2.setStudentSet(studentSet);

        //谁后保存无所谓，关系维护在第三张表中保存

        session.save(s1);
        session.save(s2);
        session.save(t1);
        session.save(t2);

        session.getTransaction().commit();
    }

    @Test
    public void find() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Student student = (Student) session.get(Student.class,30);
        System.out.println(student.getStuname());

        //1.在映射文件中奖lazy属性配置为false
        //2.Hibernate.initialize(student.getTeacherSet());
        //3.student.getTeacherSet().size();
        //4.OpenSessionInView Filter(最好的做法)

        //延迟加载
        Set<Teacher> teacherSet = student.getTeacherSet();
        for(Teacher teacher : teacherSet) {
            System.out.println(teacher.getTeaname());
        }

        session.getTransaction().commit();

    }

}
