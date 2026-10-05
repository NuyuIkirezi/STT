package org.orgx;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Black-Box (Functional) Tests — tests based on inputs/outputs only, no internal logic knowledge.
 */
class BlackBoxTest {

    private EmployeeManager manager;
    private PaymentProcessor processor;
    private ReportGenerator reporter;

    @BeforeEach
    void setUp() {
        manager = new EmployeeManager();
        processor = new PaymentProcessor();
        reporter = new ReportGenerator();
    }

    // TC-BB-01: Add a valid employee
    @Test
    void testAddValidEmployee() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        manager.addEmployee(e);
        assertNotNull(manager.getEmployee("E001"));
    }

    // TC-BB-02: Add employee with duplicate ID
    @Test
    void testAddDuplicateEmployee() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        assertThrows(IllegalArgumentException.class,
                () -> manager.addEmployee(new Employee("E001", "Bob", "Manager", 6000.0)));
    }

    // TC-BB-03: Add employee with empty ID
    @Test
    void testAddEmployeeEmptyId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Alice", "Engineer", 5000.0));
    }

    // TC-BB-04: Add employee with negative salary
    @Test
    void testAddEmployeeNegativeSalary() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("E002", "Bob", "Manager", -100.0));
    }

    // TC-BB-05: Remove existing employee
    @Test
    void testRemoveExistingEmployee() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        assertTrue(manager.removeEmployee("E001"));
        assertNull(manager.getEmployee("E001"));
    }

    // TC-BB-06: Remove non-existing employee
    @Test
    void testRemoveNonExistingEmployee() {
        assertFalse(manager.removeEmployee("E999"));
    }

    // TC-BB-07: Update employee details
    @Test
    void testUpdateEmployee() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        manager.updateEmployee("E001", "Alice Updated", "Senior Engineer", 7000.0);
        Employee e = manager.getEmployee("E001");
        assertEquals("Alice Updated", e.getName());
        assertEquals(7000.0, e.getBaseSalary());
    }

    // TC-BB-08: Calculate net pay (salary - 15% tax)
    @Test
    void testCalculateNetPay() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        double net = processor.calculateNetPay(e);
        assertEquals(4250.0, net, 0.001);
    }

    // TC-BB-09: Calculate bonus (10% of salary)
    @Test
    void testCalculateBonus() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        assertEquals(500.0, processor.calculateBonus(e), 0.001);
    }

    // TC-BB-10: Generate payslip contains all fields
    @Test
    void testGeneratePayslip() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        Map<String, Double> payslip = processor.generatePayslip(e);
        assertTrue(payslip.containsKey("grossSalary"));
        assertTrue(payslip.containsKey("tax"));
        assertTrue(payslip.containsKey("bonus"));
        assertTrue(payslip.containsKey("netPay"));
    }

    // TC-BB-11: Employee report with employees
    @Test
    void testEmployeeReportNotEmpty() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        String report = reporter.generateEmployeeReport(manager.getAllEmployees());
        assertTrue(report.contains("Alice"));
        assertTrue(report.contains("Total Employees: 1"));
    }

    // TC-BB-12: Employee report with no employees
    @Test
    void testEmployeeReportEmpty() {
        String report = reporter.generateEmployeeReport(new ArrayList<>());
        assertEquals("No employees found.", report);
    }

    // TC-BB-13: Payroll report totals correctly
    @Test
    void testPayrollReportTotal() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 4000.0));
        manager.addEmployee(new Employee("E002", "Bob", "Manager", 6000.0));
        String report = reporter.generatePayrollReport(manager.getAllEmployees(), processor);
        assertTrue(report.contains("Total Payroll: 8500.00")); // (4000+6000)*0.85
    }

    // TC-BB-14: Net pay for zero salary
    @Test
    void testNetPayZeroSalary() {
        Employee e = new Employee("E003", "Charlie", "Intern", 0.0);
        assertEquals(0.0, processor.calculateNetPay(e), 0.001);
    }

    // TC-BB-15: Get all employees count
    @Test
    void testGetAllEmployeesCount() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        manager.addEmployee(new Employee("E002", "Bob", "Manager", 6000.0));
        assertEquals(2, manager.count());
    }
}
