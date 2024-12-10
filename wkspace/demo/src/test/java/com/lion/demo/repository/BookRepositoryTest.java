package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

// 테스트마다 트랜잭션이 생성되고, 테스트가 끝나면 자동으로 롤백
@DataJpaTest
public class BookRepositoryTest {
    @Autowired private BookRepository bookRepository;

    @Test
    void testSaveAndFindBook() {
        // Given
        Book book = new Book(0L, "title", "author", "company", 20000, "Image URL", "summary");

        // When
        bookRepository.save(book);

        // Then
        List<Book> bookList = bookRepository.findAll();
        int size = bookList.size();
        System.out.println("size = " + size);

        assertThat(bookList).hasSize(1);
        assertThat(bookList.get(0).getTitle()).isEqualTo("title");
        assertThat(bookList.get(0).getPrice()).isEqualTo(20000);
    }

    @Test
    void testSaveAndFindBookByTitle() {
        // Given
        Book book1 = new Book(0L, "title1", "author", "company", 20000, "Image URL", "summary");
        Book book2 = new Book(0L, "title2", "author", "company", 20000, "Image URL", "summary");

        // When
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Then
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = bookRepository.findByTitleContaining("title", pageable).getContent();
        int size = bookList.size();
        System.out.println("size = " + size);

        assertThat(bookList).hasSize(2);
        assertThat(bookList.get(0).getAuthor()).isEqualTo("author");
        assertThat(bookList.get(0).getPrice()).isEqualTo(20000);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 75f1fdd (6일차: test 앱 잘못된 경로 수정)
