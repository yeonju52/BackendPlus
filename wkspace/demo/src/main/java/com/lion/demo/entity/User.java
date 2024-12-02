package com.lion.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
@Builder
public class User {
    @Id                 // primary key
    private String uid;

    private String pwd;
    private String uname;
    private String email;
    private LocalDate regDate;
    private String role;
}
