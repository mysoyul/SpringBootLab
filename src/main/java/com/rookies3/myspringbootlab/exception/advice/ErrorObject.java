package com.rookies3.myspringbootlab.exception.advice;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//Exception 이 발생했을때 에러정보를 저장하는 역할
@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private String timestamp;

    public String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        return DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss E a", 
                Locale.KOREA).format(ldt);
    }
}