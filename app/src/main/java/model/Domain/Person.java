package model.Domain;

import java.util.LinkedList;
import java.util.List;


public class Person {
    String email; // = ID
    String firstName;
    String lastName;
    String phone;
    String password;

    public Person(String email, String firstName, String lastName, String phone, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.password = password;
    }
}

