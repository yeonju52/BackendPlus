package com.lion.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bid;

    private String title;
    private String author;
    private String company;
    private int price;
    private String imageUrl;

    @Column(length = 8191) // Column's length expands 255 -> 8k
    private String summary;
}
