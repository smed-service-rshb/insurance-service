package ru.softlab.efr.services.insurance.utils;

import java.util.*;

public class EmployeeFilter {

    private static final Long UNEXIST_EMPLOYEE_ID = -1L;

    private boolean canViewAllContract;
    private boolean isAdmin;
    private Long employeeIdFilter = UNEXIST_EMPLOYEE_ID;
    private Set<Long> employeeOfficesFilter = new HashSet<>();
    private List<String> employeeGroupFilter = new ArrayList<>();

    public EmployeeFilter(boolean canViewAllContract, boolean isAdmin, Long employeeIdFilter, Set<Long> employeeOfficesFilter,
                          List<String> employeeGroupFilter) {
        this.isAdmin = isAdmin;
        this.canViewAllContract = canViewAllContract;
        this.employeeIdFilter = employeeIdFilter;
        this.employeeOfficesFilter = employeeOfficesFilter;
        this.employeeGroupFilter = employeeGroupFilter;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean canViewAllContract() {
        return canViewAllContract;
    }

    public Long getEmployeeIdFilter() {
        return employeeIdFilter;
    }

    public Set<Long> getEmployeeOfficesFilter() {
        return employeeOfficesFilter;
    }

    public List<String> getEmployeeGroupFilter() {
        return employeeGroupFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeFilter that = (EmployeeFilter) o;
        return canViewAllContract == that.canViewAllContract &&
                Objects.equals(employeeIdFilter, that.employeeIdFilter) &&
                Objects.equals(isAdmin, that.isAdmin) &&
                Objects.equals(employeeOfficesFilter, that.employeeOfficesFilter) &&
                Objects.equals(employeeGroupFilter, that.employeeGroupFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canViewAllContract, isAdmin, employeeIdFilter, employeeOfficesFilter, employeeGroupFilter);
    }
}