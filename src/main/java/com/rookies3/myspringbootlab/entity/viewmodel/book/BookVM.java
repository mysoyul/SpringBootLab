package com.rookies3.myspringbootlab.entity.viewmodel.book;

public interface BookVM {
    String getTitle();
    String getAuthor();
    String getIsbn();
    Integer getPrice();

    PublisherSummaryVM getPublisher(); // 요약만 참조
}