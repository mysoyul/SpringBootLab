package com.rookies3.myspringbootlab.runner;

import com.rookies3.myspringbootlab.entity.Department;
import com.rookies3.myspringbootlab.entity.Student;
import com.rookies3.myspringbootlab.entity.StudentDetail;
import com.rookies3.myspringbootlab.repository.DepartmentRepository;
import com.rookies3.myspringbootlab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class StudentDepartmentsInitRunner implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        // Check if data already exists
        if (departmentRepository.count() > 0) {
            log.info("Data already exists, skipping initialization");
            return;
        }

        // Create departments
        List<Department> departments = createDepartments();

        // Create students
        createStudents(departments);

        log.info("Data initialization completed successfully");
    }

    private List<Department> createDepartments() {
        log.info("Creating departments...");

        List<Department> departmentList = new ArrayList<>();

        departmentList.add(Department.builder()
                .name("Computer Science")
                .code("CS")
                .build());

        departmentList.add(Department.builder()
                .name("Electrical Engineering")
                .code("EE")
                .build());

        departmentList.add(Department.builder()
                .name("Mechanical Engineering")
                .code("ME")
                .build());

        departmentList.add(Department.builder()
                .name("Business Administration")
                .code("BA")
                .build());

        departmentList.add(Department.builder()
                .name("Mathematics")
                .code("MATH")
                .build());

        departmentList.add(Department.builder()
                .name("Physics")
                .code("PHYS")
                .build());

        departmentList.add(Department.builder()
                .name("Chemistry")
                .code("CHEM")
                .build());

        departmentList.add(Department.builder()
                .name("Biology")
                .code("BIO")
                .build());

        departmentList.add(Department.builder()
                .name("English Literature")
                .code("ENG")
                .build());

        departmentList.add(Department.builder()
                .name("Psychology")
                .code("PSY")
                .build());

        List<Department> departments = departmentRepository.saveAll(departmentList);
        log.info("Created {} departments", departments.size());
        return departments;
    }

    private void createStudents(List<Department> departments) {
        log.info("Creating students...");

        List<Student> allStudents = new ArrayList<>();

        // 각 학과별로 학생 생성
        for (int deptIndex = 0; deptIndex < departments.size(); deptIndex++) {
            Department department = departments.get(deptIndex);
            String deptCode = department.getCode();

            // 각 학과별로 15-25명의 학생 생성
            int studentCount = 15 + random.nextInt(11); // 15~25명

            for (int i = 1; i <= studentCount; i++) {
                String studentNumber = String.format("%s%03d", deptCode, i);
                String studentName = generateStudentName(deptIndex, i);

                Student student;

                // 80% 확률로 StudentDetail 생성
                if (random.nextDouble() < 0.8) {
                    student = createStudentWithDetail(
                            studentName,
                            studentNumber,
                            department,
                            generateAddress(i),
                            generatePhoneNumber(deptIndex, i),
                            generateEmail(studentName, deptCode),
                            generateBirthDate()
                    );
                } else {
                    // 20% 확률로 Detail 없는 학생
                    student = Student.builder()
                            .name(studentName)
                            .studentNumber(studentNumber)
                            .department(department)
                            .build();
                }

                allStudents.add(student);
            }
        }

        // 배치로 저장
        List<Student> savedStudents = studentRepository.saveAll(allStudents);
        log.info("Created {} students across {} departments", savedStudents.size(), departments.size());
    }

    private String generateStudentName(int deptIndex, int studentIndex) {
        String[] firstNames = {
                "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
                "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
                "Thomas", "Sarah", "Christopher", "Karen", "Charles", "Nancy", "Daniel", "Lisa",
                "Matthew", "Betty", "Anthony", "Helen", "Mark", "Sandra", "Donald", "Donna",
                "Steven", "Carol", "Paul", "Ruth", "Andrew", "Sharon", "Kenneth", "Michelle",
                "Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Grace",
                "Isaac", "Julia", "Kevin", "Laura", "Mason", "Nina", "Oliver", "Paula",
                "Quinn", "Rachel", "Samuel", "Tina", "Victor", "Wendy", "Xavier", "Yolanda",
                "Aaron", "Amanda", "Benjamin", "Catherine", "Christopher", "Deborah", "Eric", "Emily",
                "Frank", "Frances", "Gary", "Gloria", "Harold", "Hannah", "Ian", "Isabella",
                "Jack", "Janet", "Kyle", "Kimberly", "Louis", "Linda", "Martin", "Margaret",
                "Nathan", "Nicole", "Oscar", "Olivia", "Peter", "Pamela", "Quincy", "Rebecca",
                "Ryan", "Stephanie", "Timothy", "Teresa", "Ulysses", "Ursula", "Vincent", "Victoria",
                "Walter", "Wanda", "Zachary", "Zoe", "Adam", "Andrea", "Brian", "Brenda",
                "Carl", "Christina", "Douglas", "Diane", "Ethan", "Emma", "Felix", "Faith"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
                "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
                "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
                "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young",
                "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
                "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell",
                "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker",
                "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy",
                "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey",
                "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
                "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza",
                "Ruiz", "Hughes", "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers"
        };

        int firstNameIndex = (deptIndex * 10 + studentIndex) % firstNames.length;
        int lastNameIndex = (deptIndex * 7 + studentIndex * 3) % lastNames.length;

        return firstNames[firstNameIndex] + " " + lastNames[lastNameIndex];
    }

    private String generateAddress(int index) {
        String[] streets = {
                "Main Street", "Oak Avenue", "Pine Road", "Maple Lane", "Cedar Drive",
                "Elm Street", "Park Avenue", "First Street", "Second Street", "Third Street",
                "Broadway", "Washington Street", "Lincoln Avenue", "Jefferson Road", "Madison Lane",
                "Monroe Drive", "Adams Street", "Jackson Avenue", "Van Buren Road", "Harrison Lane",
                "Tyler Street", "Polk Avenue", "Taylor Road", "Fillmore Lane", "Pierce Drive",
                "Buchanan Street", "Johnson Avenue", "Grant Road", "Hayes Lane", "Garfield Drive",
                "Arthur Street", "Cleveland Avenue", "Harrison Road", "McKinley Lane", "Roosevelt Drive"
        };

        int streetNumber = 100 + (index * 17) % 900;
        String street = streets[index % streets.length];

        return streetNumber + " " + street;
    }

    private String generatePhoneNumber(int deptIndex, int studentIndex) {
        // 010으로 시작하는 한국 휴대폰 번호 형식
        int middle = 1000 + (deptIndex * 100 + studentIndex) % 9000;
        int last = 1000 + (studentIndex * 123) % 9000;

        return String.format("010-%04d-%04d", middle, last);
    }

    private String generateEmail(String name, String deptCode) {
        // 이름에서 공백 제거하고 소문자로 변환
        String emailName = name.toLowerCase().replace(" ", ".");
        return emailName + "@" + deptCode.toLowerCase() + ".university.edu";
    }

    private LocalDate generateBirthDate() {
        // 1995년부터 2003년 사이의 생년월일 생성
        int year = 1995 + random.nextInt(9); // 1995~2003
        int month = 1 + random.nextInt(12);  // 1~12
        int day = 1 + random.nextInt(28);    // 1~28 (간단하게 처리)

        return LocalDate.of(year, month, day);
    }

    private Student createStudentWithDetail(String name, String studentNumber, Department department,
                                            String address, String phoneNumber, String email, LocalDate dateOfBirth) {
        StudentDetail detail = StudentDetail.builder()
                .address(address)
                .phoneNumber(phoneNumber)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .build();

        Student student = Student.builder()
                .name(name)
                .studentNumber(studentNumber)
                .department(department)
                .studentDetail(detail)
                .build();

        detail.setStudent(student);
        return student;
    }
}