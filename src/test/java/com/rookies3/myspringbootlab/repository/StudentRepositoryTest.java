package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.entity.StudentDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentDetailRepository studentDetailRepository;

    @Test
    public void createStudentWithStudentDetail() {
        // Given
        Student student = Student.builder()
                .name("John Doe")
                .studentNumber("S12345")
                .build();
        
        StudentDetail studentDetail = StudentDetail.builder()
                .address("123 Main St")
                .phoneNumber("555-1234")
                .email("john.doe@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .student(student)
                .build();
        
        student.setStudentDetail(studentDetail);

        // When
        Student savedStudent = studentRepository.save(student);

        // Then
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("John Doe");
        assertThat(savedStudent.getStudentNumber()).isEqualTo("S12345");
        assertThat(savedStudent.getStudentDetail()).isNotNull();
        assertThat(savedStudent.getStudentDetail().getAddress()).isEqualTo("123 Main St");
    }

    @Test
    public void findStudentByStudentNumber() {
        // Given
        Student student = Student.builder()
                .name("John Doe")
                .studentNumber("S12345")
                .build();
        
        StudentDetail studentDetail = StudentDetail.builder()
                .address("123 Main St")
                .phoneNumber("555-1234")
                .email("john.doe@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .student(student)
                .build();
        
        student.setStudentDetail(studentDetail);
        studentRepository.save(student);

        // When
        Optional<Student> foundStudent = studentRepository.findByStudentNumber("S12345");

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getName()).isEqualTo("John Doe");
    }

    @Test
    public void findByIdWithStudentDetail() {
        // Given
        Student student = Student.builder()
                .name("John Doe")
                .studentNumber("S12345")
                .build();
        
        StudentDetail studentDetail = StudentDetail.builder()
                .address("123 Main St")
                .phoneNumber("555-1234")
                .email("john.doe@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .student(student)
                .build();
        
        student.setStudentDetail(studentDetail);
        Student savedStudent = studentRepository.save(student);

        // When
        Optional<Student> foundStudent = studentRepository.findByIdWithStudentDetail(savedStudent.getId());

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getStudentDetail()).isNotNull();
        assertThat(foundStudent.get().getStudentDetail().getPhoneNumber()).isEqualTo("555-1234");
    }
    
    @Test
    public void findStudentDetailByStudentId() {
        // Given
        Student student = Student.builder()
                .name("John Doe")
                .studentNumber("S12345")
                .build();
        
        StudentDetail studentDetail = StudentDetail.builder()
                .address("123 Main St")
                .phoneNumber("555-1234")
                .email("john.doe@example.com")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .student(student)
                .build();
        
        student.setStudentDetail(studentDetail);
        Student savedStudent = studentRepository.save(student);

        // When
        Optional<StudentDetail> foundStudentDetail = studentDetailRepository.findByStudentId(savedStudent.getId());

        // Then
        assertThat(foundStudentDetail).isPresent();
        assertThat(foundStudentDetail.get().getEmail()).isEqualTo("john.doe@example.com");
    }
}