package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {
    @Mock private BookRepository bookRepository;
    @InjectMocks private BookServiceImpl bookService;   // BookServiceImpl 객체 생성하고, bookRepository 주입

    @BeforeEach     // 매 테스트 메소드 실행할 때 마다 수행
    void setup() {
        MockitoAnnotations.openMocks(this);     // Mockito 초기화
    }

    @Test
    void testFindByBid() {
        // Given
        Book book = Book.builder()
                .bid(999L).title("title").price(23000)
                .build();
        when(bookRepository.findById(999L)).thenReturn(Optional.of(book));

        // When
        Book foundBook = bookService.findByBid(999L);

        // Then
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("title");
        assertThat(foundBook.getPrice()).isEqualTo(23000);
    }

    @Test
    void testGetBooksByPage() {
        Book book1 = Book.builder().bid(999L).title("title1").price(23000).build();
        Book book2 = Book.builder().bid(1000L).title("title2").price(24000).build();
        List<Book> bookList = Arrays.asList(book1, book2);
        Pageable pageable = PageRequest.of(0, BookService.PAGE_SIZE);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<Book> foundBookList = bookService.getBooksByPage(1);

        assertThat(foundBookList).hasSize(2);
        assertThat(foundBookList.get(0).getTitle()).isEqualTo("title1");
        assertThat(foundBookList.get(1).getPrice()).isEqualTo(24000);
    }

    @Test
    void testGetBookList() {
        Book book1 = Book.builder().bid(999L).title("title1").author("author1").company("company1").summary("summary1").build();
        Book book2 = Book.builder().bid(1000L).title("title2").author("author2").company("company2").summary("summary2").build();
        List<Book> bookList = Arrays.asList(book1, book2);
        Pageable pageable = PageRequest.of(0, BookService.PAGE_SIZE);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        when(bookRepository.findByTitleContaining("title", pageable)).thenReturn(bookPage);
        when(bookRepository.findByAuthorContaining("author", pageable)).thenReturn(bookPage);
        when(bookRepository.findByCompanyContaining("company", pageable)).thenReturn(bookPage);
        when(bookRepository.findBySummaryContaining("summary", pageable)).thenReturn(bookPage);

        List<Book> foundBooksByTitle = bookService.getBookList(1, "title", "title");
        List<Book> foundBooksByAuthor = bookService.getBookList(1, "author", "author");
        List<Book> foundBooksByCompany = bookService.getBookList(1, "company", "company");
        List<Book> foundBooksBySummary = bookService.getBookList(1, "summary", "summary");

        assertThat(foundBooksByTitle).hasSize(2);
        assertThat(foundBooksByTitle.get(0).getTitle()).contains("title");
        assertThat(foundBooksByAuthor).hasSize(2);
        assertThat(foundBooksByAuthor.get(1).getAuthor()).contains("author");
        assertThat(foundBooksByCompany).hasSize(2);
        assertThat(foundBooksByCompany.get(0).getCompany()).contains("company");
        assertThat(foundBooksBySummary).hasSize(2);
        assertThat(foundBooksBySummary.get(1).getSummary()).contains("summary");
    }

    @Test
    void testInsertBook() {
        Book book = Book.builder()
                .bid(999L).title("title").price(23000)
                .build();
        bookService.insertBook(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        Book book = Book.builder()
                .bid(999L).title("title").price(23000)
                .build();
        bookService.updateBook(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
