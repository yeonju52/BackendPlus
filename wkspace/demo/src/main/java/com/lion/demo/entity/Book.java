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
    private long bid;       // primary key, auto_increment

    private String title;
    private String author;
    private String company;
    private int price;
    private String imageUrl;

    @Column(length = 8191)
    private String summary;     // column 길이: 255 -> 8191
}