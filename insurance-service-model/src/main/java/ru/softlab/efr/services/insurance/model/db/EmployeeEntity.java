package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import ru.softlab.efr.clients.model.Employee;
//import ru.softlab.efr.common.bpm.support.model.PrincipalInfo;

import javax.persistence.*;

/**
 * Данные сотрудника, инициализирующего заявку на создание/редактирование клиента
 */
@Entity
@Table(name = "client_employees")
@Audited
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "surname")
    private String surName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "personnel_number")
    private String personnelNumber;

    @Column(name = "office")
    private String office;

    @Column(name = "position")
    private String position;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "client")
    @NotAudited
    protected ClientEntity client;

    /**
     *  конструктор
     */
    public EmployeeEntity() {
    }

    /**
     * конструктор
     * @param surName фамилия
     * @param firstName имя
     * @param middleName отчество
     * @param personnelNumber персональный номер
     * @param office офис
     * @param position должность
     * @param client заявка
     */
    public EmployeeEntity(String surName, String firstName, String middleName, String personnelNumber, String office, String position, ClientEntity client) {
        this.surName = surName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.personnelNumber = personnelNumber;
        this.office = office;
        this.client = client;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    /**
     * Конветрация в объекта класса {@link Employee}
     * @param employeeEntity данные сотрудника
     * @return данные сотрудника
     */
    public static Employee toEmployee(EmployeeEntity employeeEntity){
        return new Employee(
                employeeEntity.getSurName(),
                employeeEntity.getFirstName(),
                employeeEntity.getMiddleName(),
                employeeEntity.getPersonnelNumber(),
                employeeEntity.getOffice(),
                employeeEntity.getPosition()
        );
    }

    /**
     * Конветрация в объекта класса {@link Employee}
     * @param principalInfo данные сотрудника
     * @return данные сотрудника
     */
/*
    public static Employee toEmployee(PrincipalInfo principalInfo){
        return new Employee(
                principalInfo.getSecondName(),
                principalInfo.getFirstName(),
                principalInfo.getMiddleName(),
                principalInfo.getPersonnelNumber(),
                principalInfo.getOffice(),
                null
        );
    }
*/
}


