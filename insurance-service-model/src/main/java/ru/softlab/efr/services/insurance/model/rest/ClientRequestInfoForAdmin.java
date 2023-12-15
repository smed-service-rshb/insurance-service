package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.AvailableTopicFormat;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об обращении клиента для администратора
 */
@ApiModel(description = "Информация об обращении клиента для администратора")
@Validated
public class ClientRequestInfoForAdmin   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("requestDate")
    private LocalDate requestDate = null;

    @JsonProperty("requestsTopic")
    private AvailableTopicFormat requestsTopic = null;

    @JsonProperty("programId")
    private Long programId = null;

    @JsonProperty("programName")
    private String programName = null;

    @JsonProperty("insuranceId")
    private Long insuranceId = null;

    @JsonProperty("insuranceNumber")
    private String insuranceNumber = null;

    @JsonProperty("status")
    private RequestStatus status = null;

    @JsonProperty("clientId")
    private Long clientId = null;

    @JsonProperty("clientFirstname")
    private String clientFirstname = null;

    @JsonProperty("clientSurname")
    private String clientSurname = null;

    @JsonProperty("clientMiddleName")
    private String clientMiddleName = null;

    @JsonProperty("clientBirthDate")
    private LocalDate clientBirthDate = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("requestText")
    private String requestText = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("clientComment")
    private String clientComment = null;

    @JsonProperty("documentId")
    private Long documentId = null;

    @JsonProperty("documentName")
    private String documentName = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ClientRequestInfoForAdmin() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор обращения
     * @param requestDate Дата создания обращения
     * @param requestsTopic Тема для обращения
     * @param programId Идентификатор страхового продукта
     * @param programName Наименование страхового продукта
     * @param insuranceId Идентификатор договора страхования
     * @param insuranceNumber Номер договора страхования
     * @param status 
     * @param clientId Идентификатор клиента
     * @param clientFirstname Имя клиента
     * @param clientSurname Фамилия клиента
     * @param clientMiddleName Отчество клиента
     * @param clientBirthDate Дата рождения клиента
     * @param phone Номер телефона
     * @param email Адрес электронной почты
     * @param requestText текст сообщения
     * @param additionalInfo скрытый комментарий к обращению
     * @param isActive статус активности сообщения
     * @param clientComment комментарий для клиента
     * @param documentId Идентификатор приложенного документа
     * @param documentName Наименование приложенного документа
     */
    public ClientRequestInfoForAdmin(Long id, LocalDate requestDate, AvailableTopicFormat requestsTopic, Long programId, String programName, Long insuranceId, String insuranceNumber, RequestStatus status, Long clientId, String clientFirstname, String clientSurname, String clientMiddleName, LocalDate clientBirthDate, String phone, String email, String requestText, String additionalInfo, Boolean isActive, String clientComment, Long documentId, String documentName) {
        this.id = id;
        this.requestDate = requestDate;
        this.requestsTopic = requestsTopic;
        this.programId = programId;
        this.programName = programName;
        this.insuranceId = insuranceId;
        this.insuranceNumber = insuranceNumber;
        this.status = status;
        this.clientId = clientId;
        this.clientFirstname = clientFirstname;
        this.clientSurname = clientSurname;
        this.clientMiddleName = clientMiddleName;
        this.clientBirthDate = clientBirthDate;
        this.phone = phone;
        this.email = email;
        this.requestText = requestText;
        this.additionalInfo = additionalInfo;
        this.isActive = isActive;
        this.clientComment = clientComment;
        this.documentId = documentId;
        this.documentName = documentName;
    }

    /**
     * Идентификатор обращения
    * @return Идентификатор обращения
    **/
    @ApiModelProperty(value = "Идентификатор обращения")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Дата создания обращения
    * @return Дата создания обращения
    **/
    @ApiModelProperty(value = "Дата создания обращения")
    
  @Valid


    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }


    /**
     * Тема для обращения
    * @return Тема для обращения
    **/
    @ApiModelProperty(value = "Тема для обращения")
    
  @Valid


    public AvailableTopicFormat getRequestsTopic() {
        return requestsTopic;
    }

    public void setRequestsTopic(AvailableTopicFormat requestsTopic) {
        this.requestsTopic = requestsTopic;
    }


    /**
     * Идентификатор страхового продукта
    * @return Идентификатор страхового продукта
    **/
    @ApiModelProperty(value = "Идентификатор страхового продукта")
    


    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }


    /**
     * Наименование страхового продукта
    * @return Наименование страхового продукта
    **/
    @ApiModelProperty(value = "Наименование страхового продукта")
    


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }


    /**
     * Идентификатор договора страхования
    * @return Идентификатор договора страхования
    **/
    @ApiModelProperty(value = "Идентификатор договора страхования")
    


    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }


    /**
     * Номер договора страхования
    * @return Номер договора страхования
    **/
    @ApiModelProperty(value = "Номер договора страхования")
    


    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }


    /**
    * Get status
    * @return 
    **/
    @ApiModelProperty(value = "")
    
  @Valid


    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }


    /**
     * Идентификатор клиента
    * @return Идентификатор клиента
    **/
    @ApiModelProperty(value = "Идентификатор клиента")
    


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    /**
     * Имя клиента
    * @return Имя клиента
    **/
    @ApiModelProperty(value = "Имя клиента")
    


    public String getClientFirstname() {
        return clientFirstname;
    }

    public void setClientFirstname(String clientFirstname) {
        this.clientFirstname = clientFirstname;
    }


    /**
     * Фамилия клиента
    * @return Фамилия клиента
    **/
    @ApiModelProperty(value = "Фамилия клиента")
    


    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }


    /**
     * Отчество клиента
    * @return Отчество клиента
    **/
    @ApiModelProperty(value = "Отчество клиента")
    


    public String getClientMiddleName() {
        return clientMiddleName;
    }

    public void setClientMiddleName(String clientMiddleName) {
        this.clientMiddleName = clientMiddleName;
    }


    /**
     * Дата рождения клиента
    * @return Дата рождения клиента
    **/
    @ApiModelProperty(value = "Дата рождения клиента")
    
  @Valid


    public LocalDate getClientBirthDate() {
        return clientBirthDate;
    }

    public void setClientBirthDate(LocalDate clientBirthDate) {
        this.clientBirthDate = clientBirthDate;
    }


    /**
     * Номер телефона
    * @return Номер телефона
    **/
    @ApiModelProperty(value = "Номер телефона")
    


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * Адрес электронной почты
    * @return Адрес электронной почты
    **/
    @ApiModelProperty(value = "Адрес электронной почты")
    
 @Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(((\\w+)|(\\w+-\\w+))\\.)+[a-zA-Z]{2,6})$)")

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * текст сообщения
    * @return текст сообщения
    **/
    @ApiModelProperty(value = "текст сообщения")
    


    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }


    /**
     * скрытый комментарий к обращению
    * @return скрытый комментарий к обращению
    **/
    @ApiModelProperty(value = "скрытый комментарий к обращению")
    


    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    /**
     * статус активности сообщения
    * @return статус активности сообщения
    **/
    @ApiModelProperty(value = "статус активности сообщения")
    


    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    /**
     * комментарий для клиента
    * @return комментарий для клиента
    **/
    @ApiModelProperty(value = "комментарий для клиента")
    


    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }


    /**
     * Идентификатор приложенного документа
    * @return Идентификатор приложенного документа
    **/
    @ApiModelProperty(value = "Идентификатор приложенного документа")
    


    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }


    /**
     * Наименование приложенного документа
    * @return Наименование приложенного документа
    **/
    @ApiModelProperty(value = "Наименование приложенного документа")
    


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientRequestInfoForAdmin clientRequestInfoForAdmin = (ClientRequestInfoForAdmin) o;
        return Objects.equals(this.id, clientRequestInfoForAdmin.id) &&
            Objects.equals(this.requestDate, clientRequestInfoForAdmin.requestDate) &&
            Objects.equals(this.requestsTopic, clientRequestInfoForAdmin.requestsTopic) &&
            Objects.equals(this.programId, clientRequestInfoForAdmin.programId) &&
            Objects.equals(this.programName, clientRequestInfoForAdmin.programName) &&
            Objects.equals(this.insuranceId, clientRequestInfoForAdmin.insuranceId) &&
            Objects.equals(this.insuranceNumber, clientRequestInfoForAdmin.insuranceNumber) &&
            Objects.equals(this.status, clientRequestInfoForAdmin.status) &&
            Objects.equals(this.clientId, clientRequestInfoForAdmin.clientId) &&
            Objects.equals(this.clientFirstname, clientRequestInfoForAdmin.clientFirstname) &&
            Objects.equals(this.clientSurname, clientRequestInfoForAdmin.clientSurname) &&
            Objects.equals(this.clientMiddleName, clientRequestInfoForAdmin.clientMiddleName) &&
            Objects.equals(this.clientBirthDate, clientRequestInfoForAdmin.clientBirthDate) &&
            Objects.equals(this.phone, clientRequestInfoForAdmin.phone) &&
            Objects.equals(this.email, clientRequestInfoForAdmin.email) &&
            Objects.equals(this.requestText, clientRequestInfoForAdmin.requestText) &&
            Objects.equals(this.additionalInfo, clientRequestInfoForAdmin.additionalInfo) &&
            Objects.equals(this.isActive, clientRequestInfoForAdmin.isActive) &&
            Objects.equals(this.clientComment, clientRequestInfoForAdmin.clientComment) &&
            Objects.equals(this.documentId, clientRequestInfoForAdmin.documentId) &&
            Objects.equals(this.documentName, clientRequestInfoForAdmin.documentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestDate, requestsTopic, programId, programName, insuranceId, insuranceNumber, status, clientId, clientFirstname, clientSurname, clientMiddleName, clientBirthDate, phone, email, requestText, additionalInfo, isActive, clientComment, documentId, documentName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientRequestInfoForAdmin {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    requestDate: ").append(toIndentedString(requestDate)).append("\n");
        sb.append("    requestsTopic: ").append(toIndentedString(requestsTopic)).append("\n");
        sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
        sb.append("    programName: ").append(toIndentedString(programName)).append("\n");
        sb.append("    insuranceId: ").append(toIndentedString(insuranceId)).append("\n");
        sb.append("    insuranceNumber: ").append(toIndentedString(insuranceNumber)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    clientFirstname: ").append(toIndentedString(clientFirstname)).append("\n");
        sb.append("    clientSurname: ").append(toIndentedString(clientSurname)).append("\n");
        sb.append("    clientMiddleName: ").append(toIndentedString(clientMiddleName)).append("\n");
        sb.append("    clientBirthDate: ").append(toIndentedString(clientBirthDate)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    requestText: ").append(toIndentedString(requestText)).append("\n");
        sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
        sb.append("    clientComment: ").append(toIndentedString(clientComment)).append("\n");
        sb.append("    documentId: ").append(toIndentedString(documentId)).append("\n");
        sb.append("    documentName: ").append(toIndentedString(documentName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

