import com.gys.mapper.StudentMapper;
import com.gys.pojo.Grade;
import com.gys.pojo.Student;
import com.gys.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StudentMapperTestCase {

    private SqlSession session;
    private StudentMapper studentMapper;

    @Before
    public void setup() {
        session = SqlSessionFactoryUtil.getSqlSession();
        studentMapper = session.getMapper(StudentMapper.class);
    }

    @After
    public void close() {
        session.close();
    }

    @Test
    public void findById() {
        Student student = studentMapper.findById(2);
        System.out.println(student);
        Grade grade = student.getGrade();
        System.out.println(grade);
        session.close();
    }





    @Test
    public void findAll() {
        List<Student> studentList = studentMapper.findAll();
        for (Student student:studentList) {
            System.out.println(student);
            Grade grade = student.getGrade();
            System.out.println(grade);
        }
        session.close();
    }

}
