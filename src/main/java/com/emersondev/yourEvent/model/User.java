package com.emersondev.yourEvent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    
    @Column(name = "user_name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "user_email", length = 255, nullable = false, unique = true)
    private String email;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
