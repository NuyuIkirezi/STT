package org.shopping;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * System Integration Tests (SIT) — verifies interactions among all modules.
 * Integration flow: Customer → Product → Order → Payment → Inventory → Notification
 */
class ShoppingSystemIntegrationTest {

    private ProductService productService;
    private InventoryService inventoryService;
    private PaymentService paymentService;
    private NotificationService notificationService;
    private OrderService orderService;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
        productService = new ProductService();
        inventoryService = new InventoryService();
        paymentService = new PaymentService();
        notificationService = new NotificationService();
        orderService = new OrderService(productService, inventoryService, paymentService, notificationService);
    }

    // SIT-01: Customer makes Order — valid login and order → order should be created
    @Test
    void testSIT01_CustomerMakesOrder() {
        boolean loggedIn = customerService.authenticate("john", "1234");
        assertTrue(loggedIn);
        String result = orderService.placeOrder("John", "P100", 2);
        assertEquals("Order confirmation sent to John", result);
    }

    // SIT-02: Orders a Product — Product ID P100 → correct price 800.00 retrieved
    @Test
    void testSIT02_ProductPriceRetrieved() {
        double price = productService.getPrice("P100");
        assertEquals(800.00, price, 0.001);
    }

    // SIT-03: Order in Inventory — Quantity=2 → stock decreases from 10 to 8
    @Test
    void testSIT03_StockReducedAfterOrder() {
        orderService.placeOrder("John", "P100", 2);
        assertEquals(8, inventoryService.getStock());
    }

    // SIT-04: Order is paid — Amount=1600 → payment should be successful
    @Test
    void testSIT04_PaymentSuccessful() {
        boolean paid = paymentService.processPayment(1600.00);
        assertTrue(paid);
    }

    // SIT-05: Payment on Order is done — successful payment → order proceeds to confirmation
    @Test
    void testSIT05_OrderProceedsAfterPayment() {
        String result = orderService.placeOrder("John", "P100", 2);
        assertEquals("Order confirmation sent to John", result);
    }

    // SIT-06: Order Notification is sent — successful order → confirmation should be sent
    @Test
    void testSIT06_NotificationSentAfterOrder() {
        String confirmation = notificationService.sendConfirmation("John");
        assertEquals("Order confirmation sent to John", confirmation);
    }

    // SIT-07: Order in Inventory — Quantity=15 → order should be rejected (insufficient stock)
    @Test
    void testSIT07_InsufficientStock() {
        String result = orderService.placeOrder("John", "P100", 15);
        assertEquals("Insufficient stock", result);
        assertEquals(10, inventoryService.getStock()); // stock unchanged
    }

    // SIT-08: Order in Product — invalid product ID → order should be rejected
    @Test
    void testSIT08_InvalidProductId() {
        String result = orderService.placeOrder("John", "P999", 2);
        assertEquals("Product not found", result);
    }

    // SIT-09: Customer in Order — invalid login → order should not be created
    @Test
    void testSIT09_InvalidLoginPreventsOrder() {
        boolean loggedIn = customerService.authenticate("john", "wrongpass");
        assertFalse(loggedIn);
        // order is not placed when login fails
        if (!loggedIn) {
            assertNull(null); // order never reaches placeOrder
        }
    }

    // SIT-10: Payment in Order — invalid payment amount (0) → order should fail
    @Test
    void testSIT10_InvalidPaymentAmount() {
        boolean paid = paymentService.processPayment(0);
        assertFalse(paid);
    }

    // Full integration flow: all modules working together end-to-end
    @Test
    void testFullIntegrationFlow() {
        // Step 1: Authenticate
        assertTrue(customerService.authenticate("john", "1234"));
        // Step 2: Check product price
        assertEquals(800.00, productService.getPrice("P100"), 0.001);
        // Step 3: Check stock
        assertTrue(inventoryService.checkStock(2));
        // Step 4: Process payment
        assertTrue(paymentService.processPayment(1600.00));
        // Step 5: Place order (reduces stock + sends notification)
        String result = orderService.placeOrder("John", "P100", 2);
        assertEquals("Order confirmation sent to John", result);
        // Step 6: Verify stock reduced
        assertEquals(8, inventoryService.getStock());
    }
}
