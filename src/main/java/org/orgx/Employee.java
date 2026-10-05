package org.orgx;

public class Employee {
    private String id;
    private String name;
    private String role;
    private double baseSalary;

    public Employee(String id, String name, String role, double baseSalary) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (baseSalary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.id = id;
        this.name = name;
        this.role = role;
        this.baseSalary = baseSalary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public double getBaseSalary() { return baseSalary; }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        this.name = name;
    }
    public void setRole(String role) { this.role = role; }
    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s', role='%s', salary=%.2f}", id, name, role, baseSalary);
    }
}
