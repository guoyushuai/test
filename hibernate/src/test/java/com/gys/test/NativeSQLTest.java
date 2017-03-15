package com.gys.test;

import com.gys.pojo.Admin;
import com.gys.util.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

public class NativeSQLTest {

    @Test
    public void findAll() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        /*String sql = "select * from t_admin";*/
        String sql = "select id,username,password from t_admin";
        /*SQLQuery query = session.createSQLQuery(sql);

        List<Object[]> adminList = query.list();//数组

        for (Object[] obj : adminList) {
            System.out.println(obj[0] + ">" + obj[1] + ">" + obj[2] );
        }*/

        SQLQuery query = session.createSQLQuery(sql).addEntity(Admin.class);//addEntity

        List<Admin> adminList = query.list();//对象

        for (Admin admin : adminList) {
            System.out.println(admin);
        }

        session.getTransaction().commit();

    }

}
