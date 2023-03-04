package com.polarbookshop.catalogservice.domain;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("delete from Book where isbn = :isbn")
    @Transactional
    @Modifying
    void deleteByIsbn(String isbn);


    //From InMemoryBookRepository
//    Iterable<Book> findAll();
//
//    Optional<Book> findByIsbn(String isbn);
//
//    boolean existsByIsbn(String isbn);
//
//    Book save(Book book);
//
//    void deleteByIsbn(String isbn);
}
