package com.rookies3.myspringbootlab.entity.viewmodel;

public interface StudentVM {
    String getName();
    String getStudentNumber();

    DepartmentSummaryVM getDepartment();
}
