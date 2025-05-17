package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.DepartmentDTO;
import com.rookies3.myspringbootlab.controller.dto.StudentDTO;
import com.rookies3.myspringbootlab.entity.Department;
import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.exception.BusinessException;
import com.rookies3.myspringbootlab.exception.ErrorCode;
import com.rookies3.myspringbootlab.repository.DepartmentRepository;
import com.rookies3.myspringbootlab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

//        public List<DepartmentDTO.SimpleResponse> getAllDepartments() {
//        return departmentRepository.findAll()
//                .stream()
//                .map(DepartmentDTO.SimpleResponse::fromEntity)
//                .toList();
//    }

    // 모든 학과 조회 - 학생 정보 제외, 별도 카운트 조회
    public List<DepartmentDTO.SimpleResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream()
                .map(department -> {
                    // 학생 수만 별도로 조회하여 students 컬렉션에 접근하지 않음
                    Long studentCount = studentRepository.countByDepartmentId(department.getId());
                    return DepartmentDTO.SimpleResponse.builder()
                            .id(department.getId())
                            .name(department.getName())
                            .code(department.getCode())
                            .studentCount(studentCount)
                            .build();
                })
                .toList();
    }

    public DepartmentDTO.Response getDepartmentById(Long id) {
        Department department = departmentRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Department", "id", id));
        return DepartmentDTO.Response.fromEntity(department);
    }

    // 방법 2: Department만 조회하고 Student를 별도 조회
//    public DepartmentDTO.Response getDepartmentById(Long id) {
//        Department department = departmentRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
//                        "Department", "id", id));
//
//        // Department의 Students 컬렉션에 접근하지 않고 별도로 조회
//        List<Student> students = studentRepository.findByDepartmentId(id);
//
//        // StudentDetail 없이 SimpleResponse만 생성
//        List<StudentDTO.SimpleResponse> studentResponses = students.stream()
//                .map(StudentDTO.SimpleResponse::fromEntity)
//                .toList();
//
//        return DepartmentDTO.Response.builder()
//                .id(department.getId())
//                .name(department.getName())
//                .code(department.getCode())
//                .studentCount((long) students.size())
//                .students(studentResponses)
//                .build();
//    }


    public DepartmentDTO.Response getDepartmentByCode(String code) {
        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Department", "code", code));
        return DepartmentDTO.Response.fromEntity(department);
    }

    @Transactional
    public DepartmentDTO.Response createDepartment(DepartmentDTO.Request request) {
        // Validate department code is not already in use
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_DUPLICATE,
                    request.getCode());
        }

        // Validate department name is not already in use
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NAME_DUPLICATE,
                    request.getName());
        }

        // Create department entity
        Department department = Department.builder()
                .name(request.getName())
                .code(request.getCode())
                .build();

        // Save and return the department
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentDTO.Response.fromEntity(savedDepartment);
    }

    @Transactional
    public DepartmentDTO.Response updateDepartment(Long id, DepartmentDTO.Request request) {
        // Find the department
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Department", "id", id));

        // Check if another department already has the code
        if (!department.getCode().equals(request.getCode()) &&
                departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_DUPLICATE,
                    request.getCode());
        }

        // Check if another department already has the name
        if (!department.getName().equals(request.getName()) &&
                departmentRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NAME_DUPLICATE,
                    request.getName());
        }

        // Update department info
        department.setName(request.getName());
        department.setCode(request.getCode());

        // Save and return updated department
        Department updatedDepartment = departmentRepository.save(department);
        return DepartmentDTO.Response.fromEntity(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Department", "id", id);
        }

        // Check if department has students
        Long studentCount = studentRepository.countByDepartmentId(id);
        if (studentCount > 0) {
            throw new BusinessException(ErrorCode.DEPARTMENT_HAS_STUDENTS,
                    id, studentCount);
        }

        departmentRepository.deleteById(id);
    }
}