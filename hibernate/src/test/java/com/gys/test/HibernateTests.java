package com.gys.test;

import com.gys.pojo.Admin;
import com.gys.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

public class HibernateTests {

    @Test
    public void getAndLoad() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = (Admin) session.get(Admin.class,8);
        System.out.println(admin == null);
        System.out.println(admin);


        /*Admin admin1 = (Admin) session.load(Admin.class,9);//ObjectNotFoundException
        System.out.println(admin1 == null);
        System.out.println(admin1);*/

        session.getTransaction().commit();
    }

    @Test
    public void saveAndPersist() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setUsername("可可");
        admin.setPassword("西里");

        /*session.save(admin);
        System.out.println(admin.getId());//直接获取
        Integer id = (Integer) session.save(admin);
        System.out.println(id);*/
        //以上两个方法同时执行的时候，只保存了一次（在同一个session中，同样的对象）

        session.persist(admin);

        session.getTransaction().commit();

    }

    @Test
    public void saveAndUpdate() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setUsername("列克星敦");
        admin.setPassword("萨拉托加");
        session.save(admin);

        session.getTransaction().commit();

        Session session1 = HibernateUtil.getSession();
        session1.getTransaction().begin();

        admin.setPassword("埃塞克斯");
        session1.save(admin);//重新保存一个对象
        session1.update(admin);//修改原对象相应的属性值

        session1.getTransaction().commit();

    }

    @Test
    public void saveOrUpdate() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setUsername("应瑞");
        admin.setPassword("肇和");
        session.saveOrUpdate(admin);//save

        session.getTransaction().commit();

        Session session1 = HibernateUtil.getSession();
        session1.getTransaction().begin();

        admin.setUsername("平海");
        admin.setPassword("宁海");
        session1.saveOrUpdate(admin);//update

        session1.getTransaction().commit();
    }

    @Test
    public void merge() {

        /*Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setUsername("逸仙");
        admin.setPassword("提督");

        session.save(admin);
        session.getTransaction().commit();

        Session session1 = HibernateUtil.getSession();
        session1.getTransaction().begin();

        admin.setPassword("首长");

        session1.merge(admin);//select  update

        session1.getTransaction().commit();*/






        /*Session session2 = HibernateUtil.getSession();
        session2.getTransaction().begin();

        Admin admin1 = new Admin();
        admin1.setUsername("胡德");
        admin1.setPassword("声望");

        session2.merge(admin1);//insert

        session2.getTransaction().commit();

        Session session3 = HibernateUtil.getSession();
        session3.getTransaction().begin();

        admin1.setUsername("威尔士亲王");
        admin1.setPassword("反击");

        session3.merge(admin1);//insert

        session3.getTransaction().commit();*/

    }

    @Test
    public void saveOrUpdateVSMerge() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = new Admin();
        admin.setId(22);
                admin.setUsername("关岛");
                admin.setPassword("阿拉斯加");

        /*session.saveOrUpdate(admin);//insert/update*/

                Admin admin1 = (Admin) session.get(Admin.class,22);

       /* session.saveOrUpdate(admin);//NonUniqueObjectException*/

        session.merge(admin);//啥都不干
        session.merge(admin1);//啥都不干
        session.getTransaction().commit();

    }

    @Test
    public void clearAndFlush() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Admin admin = (Admin) session.get(Admin.class,21);

        admin.setPassword("长官");

        /*session.clear();//未更新*/
        session.flush();//update
        session.clear();//已更新，不影响

        session.getTransaction().commit();

    }

}
