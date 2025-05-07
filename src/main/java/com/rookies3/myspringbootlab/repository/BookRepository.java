package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.entity.Publisher;
import com.rookies3.myspringbootlab.entity.viewmodel.book.BookVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<BookVM> findByIsbn(String isbn);
    
    List<BookVM> findByAuthor(String author);

    List<BookVM> findByPublisher(Publisher publisher);

    List<BookVM> findByTitleContaining(String title);
}