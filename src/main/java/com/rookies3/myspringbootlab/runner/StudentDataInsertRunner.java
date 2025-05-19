package com.rookies3.myspringbootlab.runner;

import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.entity.StudentDetail;
import com.rookies3.myspringbootlab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 어플리케이션 시작 시 Student와 StudentDetail 샘플 데이터를 자동으로 생성하는 러너 클래스
 * Department는 포함하지 않고 Student와 StudentDetail만 생성합니다.
 * 
 * @Order(4) - 다른 러너들 다음에 실행되도록 설정
 */
@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class StudentDataInsertRunner implements CommandLineRunner {

    private final StudentRepository studentRepository;

    /**
     * 어플리케이션 시작 시 실행되는 메서드
     * Student와 StudentDetail 샘플 데이터를 생성합니다.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting Student data initialization...");

        // 기존 데이터가 있는지 확인 (Department 없는 Student가 있는지)
        long existingStudentCount = studentRepository.count();
        
        // 이미 많은 데이터가 있다면 skip
        if (existingStudentCount >= 20) {
            log.info("Sufficient student data already exists ({} students), skipping Student initialization", existingStudentCount);
            return;
        }

        // Student와 StudentDetail 생성
        createStudentsWithDetails();

        log.info("Student data initialization completed successfully");
    }

    /**
     * Student와 StudentDetail 샘플 데이터를 생성합니다.
     * Department 정보는 null로 설정됩니다.
     */
    private void createStudentsWithDetails() {
        log.info("Creating students with details (no department)...");

        // 1. 컴퓨터과학 전공 학생들
        Student student1 = createStudentWithDetail(
                "김소프트", "CS2024001",
                "서울시 강남구 테헤란로 123", "010-1111-2222", "kim.soft@email.com",
                LocalDate.of(2000, 3, 15)
        );

        Student student2 = createStudentWithDetail(
                "이프로그램", "CS2024002",
                "서울시 서초구 서초대로 456", "010-3333-4444", "lee.program@email.com",
                LocalDate.of(2001, 7, 22)
        );

        Student student3 = createStudentWithDetail(
                "박개발", "CS2024003",
                "서울시 송파구 올림픽대로 789", "010-5555-6666", "park.dev@email.com",
                LocalDate.of(1999, 11, 8)
        );

        // 2. 전자공학 전공 학생들
        Student student4 = createStudentWithDetail(
                "최전자", "EE2024001",
                "서울시 관악구 관악로 111", "010-7777-8888", "choi.elec@email.com",
                LocalDate.of(2000, 5, 30)
        );

        Student student5 = createStudentWithDetail(
                "정회로", "EE2024002",
                "서울시 동작구 흑석로 222", "010-9999-0000", "jung.circuit@email.com",
                LocalDate.of(2001, 12, 12)
        );

        // 3. 기계공학 전공 학생들
        Student student6 = createStudentWithDetail(
                "한기계", "ME2024001",
                "서울시 양천구 목동로 333", "010-1234-5678", "han.machine@email.com",
                LocalDate.of(1999, 2, 28)
        );

        Student student7 = createStudentWithDetail(
                "오설계", "ME2024002",
                "서울시 마포구 홍대입구 444", "010-2345-6789", "oh.design@email.com",
                LocalDate.of(2000, 9, 10)
        );

        // 4. 경영학 전공 학생들
        Student student8 = createStudentWithDetail(
                "윤비즈니스", "BA2024001",
                "서울시 종로구 종로 555", "010-3456-7890", "yoon.biz@email.com",
                LocalDate.of(2001, 4, 18)
        );

        Student student9 = createStudentWithDetail(
                "임경영", "BA2024002",
                "서울시 중구 명동 666", "010-4567-8901", "lim.manage@email.com",
                LocalDate.of(2000, 8, 25)
        );

        // 5. 수학과 학생들
        Student student10 = createStudentWithDetail(
                "강수학", "MATH2024001",
                "서울시 은평구 연신내 777", "010-5678-9012", "kang.math@email.com",
                LocalDate.of(1999, 6, 3)
        );

        Student student11 = createStudentWithDetail(
                "조통계", "MATH2024002",
                "서울시 노원구 상계동 888", "010-6789-0123", "cho.stats@email.com",
                LocalDate.of(2001, 1, 14)
        );

        // 6. 화학과 학생들
        Student student12 = createStudentWithDetail(
                "서화학", "CHEM2024001",
                "서울시 구로구 구로동 999", "010-7890-1234", "seo.chem@email.com",
                LocalDate.of(2000, 10, 7)
        );

        Student student13 = createStudentWithDetail(
                "신실험", "CHEM2024002",
                "서울시 금천구 가산동 101", "010-8901-2345", "shin.lab@email.com",
                LocalDate.of(1999, 5, 29)
        );

        // 상세 정보가 없는 학생들 (StudentDetail이 null)
        Student simpleStudent1 = Student.builder()
                .name("홍길동")
                .studentNumber("GEN2024001")
                .build();

        Student simpleStudent2 = Student.builder()
                .name("김영희")
                .studentNumber("GEN2024002")
                .build();

        Student simpleStudent3 = Student.builder()
                .name("박철수")
                .studentNumber("GEN2024003")
                .build();

        Student simpleStudent4 = Student.builder()
                .name("이민수")
                .studentNumber("GEN2024004")
                .build();

        Student simpleStudent5 = Student.builder()
                .name("정수진")
                .studentNumber("GEN2024005")
                .build();

        // 모든 학생 저장
        List<Student> students = studentRepository.saveAll(
                List.of(student1, student2, student3, student4, student5, student6, student7,
                        student8, student9, student10, student11, student12, student13,
                        simpleStudent1, simpleStudent2, simpleStudent3, simpleStudent4, simpleStudent5)
        );

        log.info("Created {} students with/without details", students.size());
    }

    /**
     * Student와 StudentDetail을 함께 생성하는 헬퍼 메서드
     * 양방향 연관관계를 올바르게 설정합니다.
     * 
     * @param name 학생 이름
     * @param studentNumber 학번
     * @param address 주소
     * @param phoneNumber 전화번호
     * @param email 이메일
     * @param dateOfBirth 생년월일
     * @return 생성된 Student 엔티티 (StudentDetail과 연관관계 설정됨)
     */
    private Student createStudentWithDetail(String name, String studentNumber, String address,
                                          String phoneNumber, String email, LocalDate dateOfBirth) {
        
        // StudentDetail 생성
        StudentDetail detail = StudentDetail.builder()
                .address(address)
                .phoneNumber(phoneNumber)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .build();

        // Student 생성 (Department는 null)
        Student student = Student.builder()
                .name(name)
                .studentNumber(studentNumber)
                .studentDetail(detail)
                .build();

        // 양방향 연관관계 설정
        detail.setStudent(student);

        return student;
    }
}