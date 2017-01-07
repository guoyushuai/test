import com.gys.mapper.AdminMapper;
        import com.gys.pojo.Admin;
        import com.gys.util.SqlSessionFactoryUtil;
        import org.apache.ibatis.session.SqlSession;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMapperTestCase {

    private SqlSession sqlSession;
    private AdminMapper adminMapper;

    @Before
    public void setup() {
        sqlSession = SqlSessionFactoryUtil.getSqlSession();
        adminMapper = sqlSession.getMapper(AdminMapper.class);
    }

    @After
    public void close() {
        sqlSession.close();
    }

    @Test
    public void findByUsernameAndPassword1() {
        Admin admin = adminMapper.findByUsernameAndPassword1("hehe","233");
        System.out.println(admin);
    }

    @Test
    public void findByUsernameAndPassword2() {
        Admin admin = adminMapper.findByUsernameAndPassword2("haha","233");
        System.out.println(admin);
    }

    @Test
    public void findByUsernameAndPassword3() {
        Admin admin1 = new Admin();
        admin1.setUsername("haha");
        admin1.setPassword("233");
        Admin admin2 = adminMapper.findByUsernameAndPassword3(admin1);
        System.out.println(admin1);
        System.out.println(admin2);
    }

    @Test
    public void findByUsernameAndPassword4() {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("name","hehe");
        param.put("pwd","233");
        Admin admin = adminMapper.findByUsernameAndPassword4(param);
        System.out.println(admin);
    }




    @Test
    public void findByParam1() {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("username","hehe");
        param.put("password","233");
        Admin admin = adminMapper.findByParam1(param);
        System.out.println(admin);
    }

    @Test
    public void findByParam2() {
        Map<String,Object> param = new HashMap<>();
        //param.put("username","haha");
        param.put("password","233");
        Admin admin = adminMapper.findByParam2(param);
        System.out.println(admin);
    }

    @Test
    public void findByParam3() {
        Map<String,Object> param = new HashMap<>();
        param.put("username","hehe");
        param.put("password","666");
        Admin admin = adminMapper.findByParam3(param);
        System.out.println(admin);
    }

    @Test
    public void findByIds() {
        List<Admin> adminList = adminMapper.findByIds(Arrays.asList(3,4,5));
        for (Admin admin:adminList) {
            System.out.println(admin);
        }
    }

}
