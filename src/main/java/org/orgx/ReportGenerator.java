package org.orgx;

import java.util.*;

public class ReportGenerator {

    public String generateEmployeeReport(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) return "No employees found.";
        StringBuilder sb = new StringBuilder("=== Employee Report ===\n");
        for (Employee e : employees) {
            sb.append(e.toString()).append("\n");
        }
        sb.append("Total Employees: ").append(employees.size());
        return sb.toString();
    }

    public String generatePayrollReport(List<Employee> employees, PaymentProcessor processor) {
        if (employees == null || employees.isEmpty()) return "No payroll data.";
        StringBuilder sb = new StringBuilder("=== Payroll Report ===\n");
        double totalNet = 0;
        for (Employee e : employees) {
            double net = processor.calculateNetPay(e);
            totalNet += net;
            sb.append(String.format("%s | Net Pay: %.2f%n", e.getName(), net));
        }
        sb.append(String.format("Total Payroll: %.2f", totalNet));
        return sb.toString();
    }
}
