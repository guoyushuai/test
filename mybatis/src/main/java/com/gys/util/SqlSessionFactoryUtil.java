package com.gys.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class SqlSessionFactoryUtil {

    //单例模式（与静态代码块相比更流行）
    private static SqlSessionFactory sessionFactory = buildSqlSessionFactory();

    private static SqlSessionFactory buildSqlSessionFactory() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis.xml");
            /*SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);*/
            return new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new RuntimeException("读取MyBatis.xml文件异常",e);
        }
    }

    public static SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static SqlSession getSqlSession() {
        return sessionFactory.openSession();
    }

    //获取一个自动提交事务的SqlSession对象
    public static SqlSession getSqlSession(boolean isAutoCommit) {
        return sessionFactory.openSession(isAutoCommit);
    }

}
