import com.gys.mapper.AdminMapper;
import com.gys.pojo.Admin;
import com.gys.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyBatisInterfaceTestCase {

    @Test
    public void findById() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        //自动产生一个AdminMapper接口的实现类
        AdminMapper adminMapper = session.getMapper(AdminMapper.class);

        Admin admin = adminMapper.findById(1);
        System.out.println(admin);

        session.close();
    }

    @Test
    public void findAll() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        AdminMapper adminMapper = session.getMapper(AdminMapper.class);

        List<Admin> adminList = adminMapper.findAll();
        for (Admin admin:adminList) {
            System.out.println(admin);
        }

        session.close();
    }

    @Test
    public void save() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession(true);

        AdminMapper adminMapper = session.getMapper(AdminMapper.class);

        Admin admin = new Admin();
        admin.setUsername("haha");
        admin.setPassword("233");

        adminMapper.save(admin);

        /*session.commit();*/
        session.close();
    }

    @Test
    public void update() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        AdminMapper adminMapper = session.getMapper(AdminMapper.class);

        Admin admin = adminMapper.findById(3);
        admin.setUsername("xixi");

        adminMapper.update(admin);

        session.commit();
        session.close();
    }

    @Test
    public void delete() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        AdminMapper adminMapper = session.getMapper(AdminMapper.class);
        adminMapper.delete(3);

        session.close();
    }
}
