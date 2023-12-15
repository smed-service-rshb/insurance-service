package ru.softlab.efr.services.insurance.model.db;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Заказ на оплату договоров страхования
 *
 * @author olshansky
 * @since 19.02.2019
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания заказа.
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * Номер (идентификатор) заказа в системе магазина, уникален для каждого магазина в пределах
     * системы. Если номер заказа генерируется на стороне платёжного шлюза, этот параметр передавать
     * необязательно.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "contract_id")
    private Insurance contract;

    /**
     * Номер (идентификатор) клиента в системе магазина. Используется для реализации функционала
     * связок. Может присутствовать, если магазину разрешено создание связок.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    /**
     * Сумма платежа в копейках (или центах)
     */
    @Column
    private Long amount;

    /**
     * Код валюты платежа ISO 4217. Если не указан, считается равным коду валюты по умолчанию.
     */
    @Column(name = "currency_iso_code")
    private Integer currency;

    /**
     * Номер заказа в платежной системе. Уникален в пределах системы. Отсутствует если регистрация заказа на
     * удалась по причине ошибки, детализированной в ErrorCode.
     */
    @Column(name = "ext_id")
    private String extId;

    /**
     * URL платежной формы, на который надо перенаправить броузер клиента. Не возвращается если
     * регистрация заказа не удалась по причине ошибки, детализированной в ErrorCode
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

    /**
     * Код состояния заказа.
     * 0 Заказ зарегистрирован, но не оплачен
     * 1 Предавторизованная сумма захолдирована (для двухстадийных платежей)
     * 2 Проведена полная авторизация суммы заказа
     * 3 Авторизация отменена
     * 4 По транзакции была проведена операция возврата
     * 5 Инициирована авторизация через ACS банка-эмитента
     * 6 Авторизация отклонена
     */
    @Column(name = "order_code")
    private Integer orderCode;

    /**
     * Код ошибки.
     * 0 Обработка запроса прошла без системных ошибок
     * 1 Заказ с таким номером уже зарегистрирован в системе
     * 3 Неизвестная (запрещенная) валюта
     * 4 Отсутствует обязательный параметр запроса
     * 5 Ошибка значение параметра запроса
     * 7 Системная ошибка
     */
    @Column(name = "error_code")
    private Integer errorCode;

    /**
     * Описание ошибки на языке, переданном в параметре Language в запросе.
     */
    @Column(name = "error_message")
    private String errorMessage;

    /**
     * Маскированный номер карты, которая использовалась для оплаты. Указан только после оплаты
     * заказа
     */
    @Column
    private String pan;

    /**
     * Срок истечения действия карты в формате YYYYMM. Указан только после оплаты заказа.
     */
    @Column
    private String expiration;

    /**
     * Имя держателя карты. Указан только после оплаты заказа.
     */
    @Column(name = "card_holder_name")
    private String cardHolderName;

    /**
     * IP адрес пользователя, который оплачивал заказ
     */
    @Column
    private String ip;

    /**
     * Дата и время обновления заказа.
     */
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public OrderEntity() {
    }

    public OrderEntity(Insurance contract, ClientEntity client, Long amount, Integer currency) {
        this.contract = contract;
        this.client = client;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Insurance getContract() {
        return contract;
    }

    public void setContract(Insurance contract) {
        this.contract = contract;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
