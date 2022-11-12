package com.example.springsecuritychapter11.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private String username;
    private String password;
}
