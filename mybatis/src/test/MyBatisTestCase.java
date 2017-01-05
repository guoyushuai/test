import com.gys.pojo.Admin;
import com.gys.util.SqlSessionFactoryUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class MyBatisTestCase {

    @Test
    public void readXml() {
        try {
            //1、读取classpath(java,resources)中的配置文件
            Reader reader = Resources.getResourceAsReader("mybatis.xml");

            //2、构建SqlSessionFactory对象
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

            //3、创建SqlSession对象
            SqlSession sqlSession = sessionFactory.openSession();


            //执行单个对象语句
            Admin admin = sqlSession.selectOne("com.gys.mapper.AdminMapper.findById",1);
            System.out.println(admin);


            //4、释放资源
            sqlSession.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findById() {
        SqlSession sqlSession = SqlSessionFactoryUtil.getSqlSession();

        Admin admin = sqlSession.selectOne("com.gys.mapper.AdminMapper.findById",1);
        System.out.println(admin);

        sqlSession.close();
    }

    @Test
    public void findAll() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        List<Admin> adminList = session.selectList("com.gys.mapper.AdminMapper.findAll");
        for (Admin admin:adminList) {
            System.out.println(admin);
        }

        session.close();
    }

    @Test
    public void save() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        Admin admin = new Admin();
        admin.setUsername("呵呵");
        admin.setPassword("233666");

        session.insert("com.gys.mapper.AdminMapper.save",admin);

        //提交事务
        session.commit();
        session.close();

    }

    @Test
    public void update() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        Admin admin = session.selectOne("com.gys.mapper.AdminMapper.findById",3);
        admin.setUsername("hehe");

        session.update("com.gys.mapper.AdminMapper.update",admin);

        session.commit();
        session.close();

    }

    @Test
    public void delete() {
        SqlSession session = SqlSessionFactoryUtil.getSqlSession();

        session.delete("com.gys.mapper.AdminMapper.delete",2);

        session.commit();
        session.close();
    }

}
