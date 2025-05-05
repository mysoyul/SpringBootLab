package com.rookies3.myspringbootlab.entity.dto;

import java.util.List;

public interface DepartmentVM {
    String getName();
    String getCode();
    List<StudentSummaryVM> getStudents();
}
