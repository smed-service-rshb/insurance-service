package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_status_history")
public class InsuranceStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Договор страхования
     */
    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

    /**
     * Статус
     */
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private InsuranceStatus status;

    /**
     * Дата и время изменения статуса
     */
    @Column(name = "change_date", nullable = false)
    private LocalDateTime changeDate = LocalDateTime.now();

    /**
     * ID сотрудника
     */
    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * ФИО сотрудника
     */
    @Column(name = "employee_name")
    private String employeeName;

    /**
     * ID подразделения в котором выполнено изменение статуса
     */
    @Column(name = "subdivision_id")
    private Long subdivisionId;

    /**
     * Наименование подразделения в котором выполнено изменеие статуса
     */
    @Column(name = "subdivision_name")
    private String subdivisionName;

    /**
     * Комментарий
     */
    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public InsuranceStatus getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatus status) {
        this.status = status;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getSubdivisionId() {
        return subdivisionId;
    }

    public void setSubdivisionId(Long subdivisionId) {
        this.subdivisionId = subdivisionId;
    }

    public String getSubdivisionName() {
        return subdivisionName;
    }

    public void setSubdivisionName(String subdivisionName) {
        this.subdivisionName = subdivisionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
