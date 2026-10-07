package org.shopping;

public class CustomerService {
    public boolean authenticate(String username, String password) {
        return username.equals("john") && password.equals("1234");
    }
}
