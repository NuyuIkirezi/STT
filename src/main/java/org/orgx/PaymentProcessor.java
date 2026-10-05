package org.orgx;

import java.util.*;

public class PaymentProcessor {

    public static final double TAX_RATE = 0.15;
    public static final double BONUS_RATE = 0.10;

    public double calculateNetPay(Employee e) {
        if (e == null) throw new IllegalArgumentException("Employee cannot be null");
        double gross = e.getBaseSalary();
        double tax = gross * TAX_RATE;
        return gross - tax;
    }

    public double calculateBonus(Employee e) {
        if (e == null) throw new IllegalArgumentException("Employee cannot be null");
        return e.getBaseSalary() * BONUS_RATE;
    }

    public Map<String, Double> generatePayslip(Employee e) {
        Map<String, Double> payslip = new LinkedHashMap<>();
        payslip.put("grossSalary", e.getBaseSalary());
        payslip.put("tax", e.getBaseSalary() * TAX_RATE);
        payslip.put("bonus", calculateBonus(e));
        payslip.put("netPay", calculateNetPay(e));
        return payslip;
    }
}
