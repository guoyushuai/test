package com.gys.dao.impl;

import com.gys.dao.AdminDao;
import com.gys.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminDaoImpl implements AdminDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;//(applicationContext.xml中配置好的Bean)

    public void save(Admin admin) {
        String sql = "insert into t_admin(username,password) values(?,?)";
        jdbcTemplate.update(sql,admin.getUsername(),admin.getPassword());
    }

    public void update(Admin admin) {
        String sql = "update t_admin set username=?,password=? where id = ?";
        jdbcTemplate.update(sql,admin.getUsername(),admin.getPassword(),admin.getId());
    }

    public void delete(Integer id) {
        String sql = "delete from t_admin where id = ?";
        jdbcTemplate.update(sql,id);
    }

    public Admin findById(Integer id) {
        String sql = "select * from t_admin where id = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<Admin>() {
            @Override
            public Admin mapRow(ResultSet resultSet, int i) throws SQLException {
                Admin admin = new Admin();
                admin.setId(resultSet.getInt("id"));
                admin.setUsername(resultSet.getString("username"));
                admin.setPassword(resultSet.getString("password"));
                return admin;
            }
        },id);
    }

    public List<Admin> findAll() {
        String sql = "select * from t_admin";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Admin>(Admin.class));
        /*return jdbcTemplate.query(sql, new RowMapper<Admin>() {
            @Override
            public Admin mapRow(ResultSet resultSet, int i) throws SQLException {
                Admin admin = new Admin();
                admin.setId(resultSet.getInt("id"));
                admin.setUsername(resultSet.getString("username"));
                admin.setPassword(resultSet.getString("password"));
                return admin;
            }
        });*/
    }

    public Admin findByUsername(String username) {
        String sql = "select * from t_admin where username = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Admin.class),username);
    }

    public Long count() {
        String sql = "select count(*) from t_Admin";
        return jdbcTemplate.queryForObject(sql,new SingleColumnRowMapper<Long>());
    }
}
