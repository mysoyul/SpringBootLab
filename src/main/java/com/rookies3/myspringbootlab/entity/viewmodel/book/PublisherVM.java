package com.rookies3.myspringbootlab.entity.viewmodel.book;

import java.util.List;

public interface PublisherVM {
    String getName();
    String getAddress();

    List<BookSummaryVM> getBooks(); // 책 요약만 참조
}