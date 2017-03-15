package com.gys.test;

import com.gys.pojo.Topic;
import com.gys.pojo.TopicContent;
import com.gys.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

public class OneToOne1 {

    @Test
    public void save() {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Topic topic = new Topic();
        topic.setTitle("核心技术");

        TopicContent topicContent = new TopicContent();
        topicContent.setContent("hijklmn");

        //产生关联(单方面维护就行，这样总会多一个update)
        topic.setTopicContent(topicContent);
        topicContent.setTopic(topic);

        session.save(topic);
        session.save(topicContent);

        session.getTransaction().commit();

    }

    @Test
    public void find() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Topic topic = (Topic) session.get(Topic.class,5);
        System.out.println(topic.getTitle());

        //懒加载
        System.out.println(topic.getTopicContent().getContent());

        session.getTransaction().commit();
    }

}
