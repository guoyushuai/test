package com.gys.test;

import com.gys.pojo.Card;
import com.gys.pojo.Person;
import com.gys.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Test;

public class OneToOne {

    @Test
    public void save() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();;

        Person person = new Person();
        person.setName("胡德");

        Card card = new Card();
        card.setCardname("001");
        card.setPerson(person);//card与person进行关联

        //hibernate会自动先存person再存card(主键、外键约束)
        session.save(person);
        session.save(card);

        session.getTransaction().commit();
    }

    @Test
    public void find() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        /*Person person = (Person) session.get(Person.class,1);
        System.out.println(person.getName());*/

        //自动关联，join查询,无法修改，遵循数据库设计的范式

        Card card = (Card) session.get(Card.class,1);
        System.out.println(card.getCardname());

        session.getTransaction().commit();

    }


    /**
     *
     */
    @Test
    public void delete() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Person person = (Person) session.get(Person.class,1);
        session.delete(person);

        session.getTransaction().commit();
    }

}
