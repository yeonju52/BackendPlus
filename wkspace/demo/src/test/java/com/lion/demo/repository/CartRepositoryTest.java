package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.Cart;
import com.lion.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CartRepositoryTest {
    @Autowired private CartRepository cartRepository;

    @Test
    void testSaveCart() {
        // Given
        User user = User.builder().uid("test").uname("테스트").build();
        Book book = Book.builder().title("title").price(15000).build();
        Cart cart = Cart.builder()
                .user(user).book(book).quantity(3)
                .build();

        // When
        Cart savedCart = cartRepository.save(cart);

        // Then
        assertThat(savedCart.getCid()).isNotNull();
        assertThat(savedCart.getUser().getUid()).isEqualTo("test");
        assertThat(savedCart.getBook().getPrice()).isEqualTo(15000);
        assertThat(savedCart.getQuantity()).isEqualTo(3);
    }

    @Test
    void testFindCart() {
        // Given
        User user = User.builder().uid("test").uname("테스트").build();
        Book book = Book.builder().title("title").price(15000).build();
        Cart cart = Cart.builder()
                .user(user).book(book).quantity(3)
                .build();
        Cart savedCart = cartRepository.save(cart);

        // When
        Cart foundCart = cartRepository.findById(savedCart.getCid()).orElse(null);

        // Then
        assertThat(foundCart.getCid()).isNotNull();
        assertThat(foundCart.getUser().getUid()).isEqualTo("test");
        assertThat(foundCart.getBook().getPrice()).isEqualTo(15000);
        assertThat(foundCart.getQuantity()).isEqualTo(3);
    }
}
