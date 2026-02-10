package com.example.demo.Solid.S;

public class S {
    public static void main(String[] args) {
        System.out.println("A class should have only one reason to change.");

        User user = new User("Amit", "bengaluru");
        UserRepository userRepository = new  UserRepository();
        EmailService emailService = new EmailService();
        ReportGenerator reportGenerator = new ReportGenerator() ;

        userRepository.save(user);
        emailService.sendEmail(user);
        reportGenerator.generate(user);

    }

}

class User {
    private String name;
    private String city;

    public User(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

class UserRepository {
    public void save(User user) {
        System.out.println(user.getName() + " " + user.getCity());

    }
}

class EmailService {
    public void sendEmail(User user) {
        // email logic only

        System.out.println(user.getName() + " bro ko mail gya");

    }
}

class ReportGenerator {
    public String generate(User user) {
        // report logic only
        System.out.println(user.getName()+ " mail generated");

        return null;
    }
}