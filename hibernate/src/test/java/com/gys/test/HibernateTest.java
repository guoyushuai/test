package com.gys.test;


import com.gys.pojo.Admin;
import com.gys.util.HibernateUtil;
import com.sun.org.glassfish.gmbal.AMXMBeanInterface;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SourceType;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class HibernateTest {


    /**
     * 保存对象
     */
    @Test
    public void save() {

        //创建sessionFactory
        Configuration configuration = new Configuration().configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        //创建session
        Session session = sessionFactory.getCurrentSession();//openSession();耗资源，不推荐

        //开启事务
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("123123");

        session.save(admin);

        //提交事务
        session.getTransaction().commit();//session.close();自动关闭
    }

    /**
     * 根据id查询对象
     */
    @Test
    public void findById() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.getCurrentSession();

        session.getTransaction().begin();

        Admin admin = (Admin) session.get(Admin.class,1);
        System.out.println(admin);

        session.getTransaction().commit();
    }

    /**
     *修改对象
     */
    @Test
    public void update() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.getCurrentSession();

        session.getTransaction().begin();

        Admin admin = (Admin) session.get(Admin.class,1);
        admin.setPassword("233666");

        session.getTransaction().commit();
    }

    /**
     * 删除对象
     */
    @Test
    public void delete() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionfactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionfactory.getCurrentSession();
        session.getTransaction().begin();

        Admin admin = (Admin) session.get(Admin.class,1);
        session.delete(admin);

        session.getTransaction().commit();
    }

    /**
     * 查询全部
     */
    @Test
    public void findAll() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();

        String hql = "from Admin";
        Query query = session.createQuery(hql);

        List<Admin> adminList = query.list();
        for (Admin admin : adminList) {
            System.out.println(admin);
        }

        session.getTransaction().commit();
    }

    /**
     * 根据条件查找（费主键）
     */
    @Test
    public void findByUsername() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();

        String hql = "from Admin where username = ?";
        Query query = session.createQuery(hql);
        query.setParameter(0,"admin");

        /*String ad = query.getQueryString();*/
        /*String ad = query.getComment();*/
        /*Integer ad = query.getFirstResult();*/
        /*Integer ad = query.getMaxResults();*/
        /*String[] ad = query.getReturnAliases();
        System.out.println(ad);*/


        List<Admin> adminList = query.list();
        for (Admin admin : adminList) {
            System.out.println(admin);
        }

        session.getTransaction().commit();
    }

}
