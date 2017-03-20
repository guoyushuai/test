package com.gys.service;

import com.gys.dao.StudentDao;
import com.gys.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    public void save(Student student) {
        studentDao.save(student);
    }

    @Transactional(readOnly = true)
    public Student findById(Integer id) {
        return studentDao.findById(id);
    }

    public void delete(Student student) {
        studentDao.delete(student);
    }

    public void delete(Integer id) {
        studentDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentDao.findAll();
    }



}
