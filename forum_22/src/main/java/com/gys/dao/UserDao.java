package com.gys.dao;

import com.gys.entity.User;
import com.gys.util.DbHelp;
import com.gys.util.Page;
import com.gys.vo.AdminUserVo;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

/*数据访问对象*/
public class UserDao {

    public User findByUsername(String username) {
        String sql = "select * from t_user where username = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),username);
    }

    public User findByEmail(String email) {
        String sql = "select * from t_user where email = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),email);
    }

    public void save(User user) {
        String sql = "insert into t_user(username,password,email,phone,state,avatar) values(?,?,?,?,?,?)";
        DbHelp.update(sql,user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar());
    }

    public void update(User user) {
        String sql = "update t_user set password=?,email=?,phone=?,state=?,avatar=? where id=?";
        DbHelp.update(sql,user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar(),user.getId());
    }

    public User findById(Integer id) {
        String sql = "select * from t_user where id = ?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),id);
    }

    public int countUsersByState() {
        String sql = "SELECT COUNT(*) FROM t_user WHERE state > 0";
        return DbHelp.query(sql,new ScalarHandler<Long>()).intValue();
    }

    /*public List<AdminUserVo> countUsersAndLoginByState(Page<AdminUserVo> userVoPage)  {
        String sql = ""
    }*/

    public List<User> finAllUsers() {
        String sql = "select * from t_user";
        return DbHelp.query(sql,new BeanListHandler<>(User.class));
    }

    public List<User> findAllUsersByPage(Page<AdminUserVo> userVoPage) {
        String sql = "SELECT * FROM t_user ORDER BY id LIMIT ?,?";
        return DbHelp.query(sql,new BeanListHandler<User>(User.class),userVoPage.getStart(),userVoPage.getPageSize());
    }

    public AdminUserVo findAdminUserVoByUserid(Integer userid) {
        String sql = "SELECT tu.id AS userid,tu.username,tu.createtime,tll.logintime AS lastlogintime,tll.ip AS lastloginip,tu.state FROM t_login_log tll,t_user tu WHERE tll.userid = tu.id AND tu.id = ? ORDER BY logintime DESC LIMIT 0,1";
        return DbHelp.query(sql,new BeanHandler<AdminUserVo>(AdminUserVo.class),userid);
    }
}
