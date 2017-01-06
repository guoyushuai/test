import com.gys.mapper.GradeMapper;
import com.gys.pojo.Grade;
import com.gys.pojo.Student;
import com.gys.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GradeMapperTestCase {

    private SqlSession session;
    private GradeMapper gradeMapper;

    @Before
    public void setup() {
         session = SqlSessionFactoryUtil.getSqlSession();
         gradeMapper = session.getMapper(GradeMapper.class);
    }

    @After
    public void close() {
        session.close();
    }

    @Test
    public void findById() {
        Grade grade = gradeMapper.findById(2);
        System.out.println(grade);
        List<Student> studentList = grade.getStudentList();

        /*List<Student> studentList = studentMapper.findByGradeId(2);*/
        for (Student student:studentList) {
            System.out.println(student);
        }
        session.close();
    }





    @Test
    public void findAll() {
        List<Grade> gradeList = gradeMapper.findAll();
        for (Grade grade : gradeList) {
            System.out.println(grade);
            List<Student> studentList = grade.getStudentList();
            for (Student student : studentList) {
                System.out.println(student);
            }
        }
        session.close();
    }

}
