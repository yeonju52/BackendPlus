package com.lion.demo.controller;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.entity.OrderItem;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.OrderService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired private BookService bookService;
    @Autowired private CartService cartService;
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    @GetMapping("/createOrder")
    public String createOrder(HttpSession session) {
        String uid = (String) session.getAttribute("sessUid");
        List<Cart> cartList = cartService.getCartItemsByUser(uid);
        Order order = orderService.createOrder(uid, cartList);
        //        System.out.println(order); // Order - OrderItem 양방향 참조 (연관관계로 설정했기 때문에 호출 시 무한루프에 빠진다.)
        //        # ISSUE: java.lang.StackOverflowError: null
        //        # 무한루프 발생 예쌍. 왜 문제가 생기나?
        //                양방향 참조 자체가 문제는 아닙니다. 그러나 toString() 메서드에서 양방향 참조 필드를 포함할 경우, 무한 루프가 발생할 수 있습니다.
        //        무한 루프의 원인
        //                * Order.toString() 호출 → orderItems.toString() 호출
        //                * orderItems의 toString() 호출 → 각 OrderItem의 toString() 호출
        //                * OrderItem.toString() 호출 → 다시 Order 참조를 출력
        //                * Order.toString() 호출 → 무한 반복

        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        String uid = (String) session.getAttribute("sessUid");
        List<Order> orderList = orderService.getOrdersByUser(uid);
        List<String> orderTitleList = new ArrayList<>();
        for (Order order: orderList) {
            List<OrderItem> orderItems = order.getOrderItems();
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1)
                title += " 외 " + (size - 1) + " 건";
            orderTitleList.add(title);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        return "order/list";
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        // 2024년 12월
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999);
        List<Order> orderList = orderService.getOrdersByDateRange(startTime, endTime);
        int totalRevenue = 0, totalBooks = 0;
        List<String> orderTitleList = new ArrayList<>();
        for (Order order: orderList) {
            totalRevenue += order.getTotalAmount();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem: orderItems)
                totalBooks += orderItem.getQuantity();
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1)
                title += " 외 " + (size - 1) + " 건";
            orderTitleList.add(title);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalBooks", totalBooks);
        return "order/listAll";
    }
}