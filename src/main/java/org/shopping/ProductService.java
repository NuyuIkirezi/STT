package org.shopping;

public class ProductService {
    public double getPrice(String productId) {
        if (productId.equals("P100")) return 800.00;
        return 0.0;
    }
}
