package com.lion.demo.controller;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.Cart;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mall")
public class CartController {
    @Autowired private CartService cartService;
    @Autowired private BookService bookService;
    @Autowired private UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Book> bookList = bookService.getBooksByPage(3);
//        System.out.println(">>>>>>>>>>>>>" + bookList.size());
        model.addAttribute("bookList", bookList);
        return "mall/list";
    }

    @GetMapping("/detail/{bid}")
    public String detail(@PathVariable long bid, Model model) {
        Book book = bookService.findByBid(bid);
        model.addAttribute("book", book);
        return "mall/detail";
    }

    @PostMapping("/addItemToCart")
    public String addItemToCart(long bid, int quantity, HttpSession session) {
        String uid = (String) session.getAttribute("sessUid");
        cartService.addToCart(uid, bid, quantity);
        return "redirect:/mall/list";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        String uid = (String) session.getAttribute("sessUid");
        List<Cart> cartList = cartService.getCartItemsByUser(uid);
        int totalPrice = 0;
        for (Cart cart: cartList) {
            totalPrice += cart.getBook().getPrice() * cart.getQuantity();
        }
        model.addAttribute("cartList", cartList);
        model.addAttribute("totalPrice", totalPrice);
        return "mall/cart";
    }
}