package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)    //Imports the data configuration (needed to enable auditing)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    //Disables the default behavior of relying on an embedded test database since we want to use Testcontainers
@ActiveProfiles("integration")   //Enables the “integration” profile to load configuration from application-integration.yml
class BookRepositoryJdbcTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;    // A lower object to interact with the database

    @Test
    void findBookByIsbnWhenExisting(){
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
        jdbcAggregateTemplate.save(book);   // JdbcAggregateTemplate is used to prepare the data targeted by the test.
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }
}
