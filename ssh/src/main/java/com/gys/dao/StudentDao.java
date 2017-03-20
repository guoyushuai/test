package com.gys.dao;

import com.gys.pojo.Student;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Student student) {
        getSession().save(student);
    }

    public Student findById(Integer id) {
        return (Student) getSession().get(Student.class,id);
    }

    public void delete(Student student) {
        getSession().delete(student);
    }

    public void delete(Integer id) {
        getSession().delete(findById(id));
    }

    public List<Student> findAll() {
        Criteria criteria = getSession().createCriteria(Student.class);
        return criteria.list();
    }

    /*public Student findByStuname(String stuname) {

        String hql = "from Student where stuname = ?";
        Query query = getSession().createQuery(hql);
        query.setParameter(0,"tom");
        return (Student) query.uniqueResult();
    }*/
}
