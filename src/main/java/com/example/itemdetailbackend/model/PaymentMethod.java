package com.example.itemdetailbackend.model;

import lombok.Data;
import java.util.List;

@Data
public class PaymentMethod {
    private String type;
    private String name;
    private List<Installment> installments;

    @Data
    public static class Installment {
        private int quantity;
        private double amount;
    }
} 