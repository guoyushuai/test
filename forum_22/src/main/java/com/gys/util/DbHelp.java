package com.gys.util;

import com.gys.exception.DataAccessException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DbHelp {

    //private static Logger logger = Logger.getLogger(DbHelp.class);//log4j
    private static Logger logger = LoggerFactory.getLogger(DbHelp.class);//slf4j

    //一般情况下通过数据库连接池获取连接，不再直接获取数据库连接，连接池达到连接上限且超过等待最长时间后直接连接数据库
    private static Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    public static void update(String sql,Object... params) throws DataAccessException {

        try {
            QueryRunner queryRunner = new QueryRunner(ConnectionManager.getDataSource());
            queryRunner.update(sql, params);

            //System.out.println("SQL: " + sql);
            //logger.debug("SQL:" + sql);//log4j
            logger.debug("SQL:{}",sql);//slf4j
        } catch (SQLException ex) {
            logger.error("执行{}异常",sql);
            throw new DataAccessException("执行"+ sql + "异常",ex);
        }
    }

    public static <T> T query(String sql, ResultSetHandler<T> handler, Object... params) throws DataAccessException {

        QueryRunner queryRunner = new QueryRunner(ConnectionManager.getDataSource());
        try {
            T t = queryRunner.query(sql,handler,params);

            //System.out.println("SQL: " + sql);
            //logger.debug("SQL:" + sql);//log4j
            logger.debug("SQL:{}",sql);//slf4j
            return t;
        } catch (SQLException e) {
            logger.error("执行{}异常",sql);
            throw new DataAccessException("执行"+ sql + "异常",e);
        }
    }

    private static void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("关闭connection异常");
                throw new DataAccessException("关闭Connection异常",e);
            }
        }
    }

}
