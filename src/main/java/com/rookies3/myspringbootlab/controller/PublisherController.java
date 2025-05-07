package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.entity.Publisher;
import com.rookies3.myspringbootlab.entity.viewmodel.book.PublisherVM;
import com.rookies3.myspringbootlab.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    @Autowired
    private PublisherRepository publisherRepository;

    @GetMapping
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        return publisherRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Publisher createPublisher(@RequestBody Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id,
                                                     @RequestBody Publisher publisherDetails) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    publisher.setName(publisherDetails.getName());
                    publisher.setEstablishedDate(publisherDetails.getEstablishedDate());
                    publisher.setAddress(publisherDetails.getAddress());
                    Publisher updatedPublisher = publisherRepository.save(publisher);
                    return ResponseEntity.ok(updatedPublisher);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    publisherRepository.delete(publisher);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<Map<String, Long>> getBookCount(@PathVariable Long id) {
        if (!publisherRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Long count = publisherRepository.countBooksByPublisherId(id);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherVM> getPublisherById(@PathVariable String name) {
        return ResponseEntity.ok(publisherRepository.findByName(name));
    }
}