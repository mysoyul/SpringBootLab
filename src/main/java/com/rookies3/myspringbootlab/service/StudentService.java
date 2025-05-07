package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.entity.StudentDetail;
import com.rookies3.myspringbootlab.entity.dto.StudentDTO;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.repository.StudentDetailRepository;
import com.rookies3.myspringbootlab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentDetailRepository studentDetailRepository;

    public List<StudentDTO.Response> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public StudentDTO.Response getStudentById(Long id) {
        Student student = studentRepository.findByIdWithStudentDetail(id)
                .orElseThrow(() -> new BusinessException("Student not found with id: " + id));
        return StudentDTO.Response.fromEntity(student);
    }

    public StudentDTO.Response getStudentByStudentNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new BusinessException("Student not found with student number: " + studentNumber));
        return StudentDTO.Response.fromEntity(student);
    }

    @Transactional
    public StudentDTO.Response createStudent(StudentDTO.Request request) {
        if (studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BusinessException("Student already exists with student number: " + request.getStudentNumber());
        }
        
        if (request.getDetailRequest() != null && 
            request.getDetailRequest().getEmail() != null && 
            !request.getDetailRequest().getEmail().isEmpty() && 
            studentDetailRepository.existsByEmail(request.getDetailRequest().getEmail())) {
            throw new BusinessException("Student detail already exists with email: " + request.getDetailRequest().getEmail());
        }

        if (request.getDetailRequest() != null && 
            studentDetailRepository.existsByPhoneNumber(request.getDetailRequest().getPhoneNumber())) {
            throw new BusinessException("Student detail already exists with phone number: " + request.getDetailRequest().getPhoneNumber());
        }

        Student student = Student.builder()
                .name(request.getName())
                .studentNumber(request.getStudentNumber())
                .build();
        
        if (request.getDetailRequest() != null) {
            StudentDetail studentDetail = StudentDetail.builder()
                    .address(request.getDetailRequest().getAddress())
                    .phoneNumber(request.getDetailRequest().getPhoneNumber())
                    .email(request.getDetailRequest().getEmail())
                    .dateOfBirth(request.getDetailRequest().getDateOfBirth())
                    .student(student)
                    .build();
            
            student.setStudentDetail(studentDetail);
        }

        Student savedStudent = studentRepository.save(student);
        return StudentDTO.Response.fromEntity(savedStudent);
    }

    @Transactional
    public StudentDTO.Response updateStudent(Long id, StudentDTO.Request request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Student not found with id: " + id));

        // Check if another student already has the student number
        if (!student.getStudentNumber().equals(request.getStudentNumber()) && 
                studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BusinessException("Student already exists with student number: " + request.getStudentNumber());
        }

        student.setName(request.getName());
        student.setStudentNumber(request.getStudentNumber());
        
        if (request.getDetailRequest() != null) {
            StudentDetail studentDetail = student.getStudentDetail();
            
            if (studentDetail == null) {
                studentDetail = new StudentDetail();
                studentDetail.setStudent(student);
                student.setStudentDetail(studentDetail);
            }
            
            // Check for duplicate email and phone number
            if (request.getDetailRequest().getEmail() != null && 
                !request.getDetailRequest().getEmail().isEmpty() &&
                (studentDetail.getEmail() == null || !studentDetail.getEmail().equals(request.getDetailRequest().getEmail())) &&
                studentDetailRepository.existsByEmail(request.getDetailRequest().getEmail())) {
                throw new BusinessException("Student detail already exists with email: " + request.getDetailRequest().getEmail());
            }
            
            if ((studentDetail.getPhoneNumber() == null || !studentDetail.getPhoneNumber().equals(request.getDetailRequest().getPhoneNumber())) &&
                studentDetailRepository.existsByPhoneNumber(request.getDetailRequest().getPhoneNumber())) {
                throw new BusinessException("Student detail already exists with phone number: " + request.getDetailRequest().getPhoneNumber());
            }
            
            studentDetail.setAddress(request.getDetailRequest().getAddress());
            studentDetail.setPhoneNumber(request.getDetailRequest().getPhoneNumber());
            studentDetail.setEmail(request.getDetailRequest().getEmail());
            studentDetail.setDateOfBirth(request.getDetailRequest().getDateOfBirth());
        }

        Student updatedStudent = studentRepository.save(student);
        return StudentDTO.Response.fromEntity(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new BusinessException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}