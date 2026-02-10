package com.example.demo.Solid.O;

public class O {
    public static void main(String[] args) {

    }
}

interface Payment {
    void process(double amount);
}

class CreditCard implements Payment {

    @Override
    public void process(double amount) {
        System.out.println("Payment is going through Credit Card");
    }
}