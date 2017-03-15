package com.gys.test;

import com.gys.pojo.Admin;
import com.gys.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.List;

public class CriteriaTest {

    /**
     * 查询全部
     */
    @Test
    public void findAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Criteria criteria = session.createCriteria(Admin.class);

        List<Admin> adminList = criteria.list();
        for (Admin admin : adminList) {
            System.out.println(admin);
        }

        session.getTransaction().commit();

    }

    @Test
    public void where() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Criteria criteria = session.createCriteria(Admin.class);

        criteria.addOrder(Order.desc("id"));

        /*criteria.add(Restrictions.eq("username","列克星敦"));*/
        //criteria.add(Restrictions.or(Restrictions.eq("username","列克星敦"),Restrictions.eq("username","可可")));
        //criteria.add(Restrictions.and(Restrictions.eq("username","列克星敦"),Restrictions.eq("password","萨拉托加")));

        /*List<Admin> adminList = criteria.list();
        for (Admin admin : adminList) {
            System.out.println(admin);
        }*/

        criteria.add(Restrictions.eq("id",22));//id属性value参数值设置时为Integer
        Admin admin = (Admin) criteria.uniqueResult();//唯一值
        System.out.println(admin);

        session.getTransaction().commit();

    }

    @Test
    public void count() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Criteria criteria = session.createCriteria(Admin.class);

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.rowCount());
        projectionList.add(Projections.max("id"));

        criteria.setProjection(projectionList);

        Object[] result = (Object[]) criteria.uniqueResult();//唯一值
        System.out.println(result[0] + ">" + result[1]);

        session.getTransaction().commit();

    }

}
