package com.rookies3.myspringbootlab.entity.viewmodel;

import java.util.List;

public interface DepartmentVM {
    String getName();
    String getCode();
    List<StudentSummaryVM> getStudents();
}
