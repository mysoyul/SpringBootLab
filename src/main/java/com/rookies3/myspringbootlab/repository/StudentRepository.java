package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNumber(String studentNumber);
    
    @Query("SELECT s FROM Student s JOIN FETCH s.studentDetail WHERE s.id = :id")
    Optional<Student> findByIdWithStudentDetail(@Param("id") Long id);
    
    boolean existsByStudentNumber(String studentNumber);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentDetail LEFT JOIN FETCH s.department WHERE s.id = :id")
    Optional<Student> findByIdWithAllDetails(@Param("id") Long id);

    // 부서별 학생 조회 (리스트)
    List<Student> findByDepartmentId(Long departmentId);

    // 부서별 학생 수 조회
    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    // 부서별 학생 조회 (페이징)
    Page<Student> findByDepartmentId(Long departmentId,
                                     Pageable pageable);

    // 학생 이름으로 검색 (페이징)
    Page<Student> findByNameContainingIgnoreCase(String name,
                                                 Pageable pageable);

    // 학생 번호로 검색 (페이징)
    Page<Student> findByStudentNumberContainingIgnoreCase(String studentNumber,
                                                          Pageable pageable);

    // 부서별 + 이름 검색 (페이징)
    Page<Student> findByDepartmentIdAndNameContainingIgnoreCase(Long departmentId,
                                                                String name,
                                                                Pageable pageable);
}