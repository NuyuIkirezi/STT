package org.orgx;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * White-Box Tests — tests based on internal logic, branches, and paths.
 */
class WhiteBoxTest {

    private EmployeeManager manager;
    private PaymentProcessor processor;
    private ReportGenerator reporter;

    @BeforeEach
    void setUp() {
        manager = new EmployeeManager();
        processor = new PaymentProcessor();
        reporter = new ReportGenerator();
    }

    // TC-WB-01: Branch — addEmployee: new ID goes into map
    @Test
    void testAddEmployeeInsertsIntoMap() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        manager.addEmployee(e);
        assertEquals(1, manager.count());
        assertSame(e, manager.getEmployee("E001"));
    }

    // TC-WB-02: Branch — addEmployee: duplicate ID throws before inserting
    @Test
    void testAddDuplicateDoesNotIncreaseCount() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        try {
            manager.addEmployee(new Employee("E001", "Bob", "Manager", 6000.0));
        } catch (IllegalArgumentException ignored) {}
        assertEquals(1, manager.count());
    }

    // TC-WB-03: Branch — removeEmployee: existing key returns true
    @Test
    void testRemoveReturnsTrueForExisting() {
        manager.addEmployee(new Employee("E001", "Alice", "Engineer", 5000.0));
        assertTrue(manager.removeEmployee("E001"));
        assertEquals(0, manager.count());
    }

    // TC-WB-04: Branch — removeEmployee: missing key returns false
    @Test
    void testRemoveReturnsFalseForMissing() {
        assertFalse(manager.removeEmployee("NONE"));
    }

    // TC-WB-05: Branch — updateEmployee: throws NoSuchElementException for unknown ID
    @Test
    void testUpdateThrowsForUnknownId() {
        assertThrows(NoSuchElementException.class,
                () -> manager.updateEmployee("NONE", "X", "Y", 100));
    }

    // TC-WB-06: Path — calculateNetPay: gross - (gross * TAX_RATE)
    @Test
    void testNetPayFormula() {
        Employee e = new Employee("E001", "Alice", "Engineer", 8000.0);
        double expected = 8000.0 - (8000.0 * PaymentProcessor.TAX_RATE);
        assertEquals(expected, processor.calculateNetPay(e), 0.001);
    }

    // TC-WB-07: Path — calculateBonus: gross * BONUS_RATE
    @Test
    void testBonusFormula() {
        Employee e = new Employee("E001", "Alice", "Engineer", 3000.0);
        assertEquals(3000.0 * PaymentProcessor.BONUS_RATE, processor.calculateBonus(e), 0.001);
    }

    // TC-WB-08: Branch — calculateNetPay: null employee throws
    @Test
    void testNetPayNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> processor.calculateNetPay(null));
    }

    // TC-WB-09: Branch — generatePayslip: all four keys computed correctly
    @Test
    void testPayslipValuesCorrect() {
        Employee e = new Employee("E001", "Alice", "Engineer", 2000.0);
        Map<String, Double> p = processor.generatePayslip(e);
        assertEquals(2000.0, p.get("grossSalary"), 0.001);
        assertEquals(300.0, p.get("tax"), 0.001);       // 2000 * 0.15
        assertEquals(200.0, p.get("bonus"), 0.001);     // 2000 * 0.10
        assertEquals(1700.0, p.get("netPay"), 0.001);   // 2000 - 300
    }

    // TC-WB-10: Branch — generateEmployeeReport: null list returns "No employees found."
    @Test
    void testReportNullList() {
        assertEquals("No employees found.", reporter.generateEmployeeReport(null));
    }

    // TC-WB-11: Branch — generateEmployeeReport: iterates all employees
    @Test
    void testReportIteratesAll() {
        List<Employee> list = List.of(
                new Employee("E001", "Alice", "Engineer", 5000.0),
                new Employee("E002", "Bob", "Manager", 6000.0)
        );
        String report = reporter.generateEmployeeReport(list);
        assertTrue(report.contains("Alice"));
        assertTrue(report.contains("Bob"));
        assertTrue(report.contains("Total Employees: 2"));
    }

    // TC-WB-12: Branch — generatePayrollReport: empty list returns "No payroll data."
    @Test
    void testPayrollReportEmptyList() {
        assertEquals("No payroll data.", reporter.generatePayrollReport(new ArrayList<>(), processor));
    }

    // TC-WB-13: Path — generatePayrollReport: accumulates totalNet correctly
    @Test
    void testPayrollAccumulation() {
        List<Employee> list = List.of(
                new Employee("E001", "Alice", "Engineer", 1000.0),
                new Employee("E002", "Bob", "Manager", 1000.0)
        );
        // each net = 850, total = 1700
        String report = reporter.generatePayrollReport(list, processor);
        assertTrue(report.contains("1700.00"));
    }

    // TC-WB-14: Branch — Employee constructor: blank name throws
    @Test
    void testEmployeeBlankNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("E001", "   ", "Engineer", 5000.0));
    }

    // TC-WB-15: Branch — setBaseSalary: negative value throws
    @Test
    void testSetNegativeSalaryThrows() {
        Employee e = new Employee("E001", "Alice", "Engineer", 5000.0);
        assertThrows(IllegalArgumentException.class, () -> e.setBaseSalary(-1));
    }
}
