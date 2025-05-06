package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Department;
import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.entity.viewmodel.StudentVM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    List<StudentVM> findByDepartment(Department department);
    
    StudentVM findByStudentNumber(String studentNumber);
}