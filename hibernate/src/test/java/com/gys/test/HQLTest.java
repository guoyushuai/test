package com.gys.test;


import com.gys.pojo.Admin;
import com.gys.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

public class HQLTest {

    /**
     * 查找全部
     */
    @Test
    public void findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        String hql = "from Admin";
        Query query = session.createQuery(hql);

        List<Admin> adminList = query.list();

        for (Admin admin:adminList) {
            System.out.println(admin);
        }

        session.getTransaction().commit();
    }

    /**
     * where条件查询
     */
    @Test
    public void where() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();


        /*String hql = "from Admin where username = ? and password = ?";
        Query query = session.createQuery(hql);

        query.setParameter(0,"列克星敦");
        query.setParameter(1,"萨拉托加");


        List<Admin> adminList = query.list();
        for (Admin admin : adminList) {
            System.out.println(admin);
        }*/


        String hql1 = "from Admin where username = :name and password = :pwd";
        Query query1 = session.createQuery(hql1);

        query1.setParameter("name","列克星敦");
        query1.setParameter("pwd","萨拉托加");

        query1.setFirstResult(0);
        query1.setMaxResults(3);

        List<Admin> adminList1 = query1.list();
        for (Admin admin : adminList1) {
            System.out.println(admin);
        }

        session.getTransaction().commit();
    }


    /**
     * 查询不完整对象
     */
    @Test
    public void queryProperties() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        /*String hql = "select id,username,password from Admin";
        Query query = session.createQuery(hql);

        List<Object[]> adminList = query.list();//全查也得用数组接收

        for (Object[] obj:adminList) {
            System.out.println(obj[0] + "->" + obj[1] + "->" + obj[2] );

        }*/

        /*String hql1 = "select username from Admin";
        Query query1 = session.createQuery(hql1);
        List nameList = query1.list();
        for(Object obj:nameList){//Object类型！！
            System.out.println(obj);
        }*/

        String hql2 = "select username,password from Admin";
        Query query2 = session.createQuery(hql2);

        List<Object[]> nameAndPwdList = query2.list();
        for (Object[] obj : nameAndPwdList) {
            System.out.println(obj[0] + ">" + obj[1]);
        }

        session.getTransaction().commit();

    }

    /**
     * 统计查询
     */
    @Test
    public void count() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        String hql = "select count(*) from Admin";//count(xx)统计某一列时自动忽略null值
        Query query = session.createQuery(hql);

        Long count = (Long) query.uniqueResult();//唯一值
        System.out.println(count);

        session.getTransaction().commit();

    }

}
