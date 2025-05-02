package com.rookies3.myspringbootlab.repository;

import com.rookies3.myspringbootlab.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    Publisher findByName(String name);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.publisher.id = :publisherId")
    Long countBooksByPublisherId(@Param("publisherId") Long publisherId);
}