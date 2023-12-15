package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.envers.Audited;
import ru.softlab.efr.services.insurance.model.rest.Phone;
import ru.softlab.efr.services.insurance.model.rest.PhoneType;

import javax.persistence.*;

/**
 * Телефон клиента
 * @author basharin
 * @since 09.02.2018
 */
@Entity
@Table(name = "phones_for_client")
@Audited
public class PhoneForClaimEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String number;

    @Column(name = "phone_type")
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @Column
    private boolean main;

    @Column
    private boolean verified;

    @Column
    private boolean notification;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    protected ClientEntity client;

    public PhoneForClaimEntity() {}

    /**
     * Конструктор
     * @param phone - телефон
     * @param client заявка на клиентские данные
     */
    public PhoneForClaimEntity(Phone phone, ClientEntity client) {
        update(phone);
        this.client = client;
    }

    /**
     * Получить id номера телефона
     * @return id номера телефона
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить номер телефона
     * @return номер телефона
     */
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Получить тип номера телефона
     * @return тип номера телефона
     */
    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    /**
     * Получить признак основной ли номер телефона
     * @return признак основной ли номер телефона
     */
    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    /**
     * Получить признак верифицирован ли номер телефона
     * @return признак верифицирован ли номер телефона
     */
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Получить признак оповещения номера телефона
     * @return признак оповещения номера телефона
     */
    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    /**
     * Обновить информацию о телефоне
     * @param phone телефон
     */
    public final void update(Phone phone) {
        this.number = phone.getNumber();
        this.phoneType = phone.getType();
        this.main = BooleanUtils.isTrue(phone.isMain());
        this.verified = BooleanUtils.isTrue(phone.isVerified());
        this.notification = BooleanUtils.isTrue(phone.isNotification());
    }

    /**
     * Конвертация в объект класса {@link Phone}
     * @param entity телефон
     * @return телефон
     */
    public static Phone toPhone(PhoneForClaimEntity entity) {
        Phone phone = new Phone();
        phone.setNumber(entity.getNumber());
        phone.setType(entity.getPhoneType());
        phone.setMain(BooleanUtils.isTrue(entity.isMain()));
        phone.setVerified(BooleanUtils.isTrue(entity.isVerified()));
        phone.setNotification(BooleanUtils.isTrue(entity.isNotification()));
        return phone;
    }
}
