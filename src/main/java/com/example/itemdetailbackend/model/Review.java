package com.example.itemdetailbackend.model;

import lombok.Data;

@Data
public class Review {
    private String id;
    private String user;
    private int rating;
    private String comment;
    private String date;
} 