package org.orgx;

import java.util.*;

public class EmployeeManager {
    private final Map<String, Employee> employees = new HashMap<>();

    public void addEmployee(Employee e) {
        if (employees.containsKey(e.getId()))
            throw new IllegalArgumentException("Employee with ID " + e.getId() + " already exists");
        employees.put(e.getId(), e);
    }

    public Employee getEmployee(String id) {
        return employees.get(id);
    }

    public boolean removeEmployee(String id) {
        return employees.remove(id) != null;
    }

    public void updateEmployee(String id, String name, String role, double salary) {
        Employee e = employees.get(id);
        if (e == null) throw new NoSuchElementException("Employee not found: " + id);
        e.setName(name);
        e.setRole(role);
        e.setBaseSalary(salary);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public int count() {
        return employees.size();
    }
}
