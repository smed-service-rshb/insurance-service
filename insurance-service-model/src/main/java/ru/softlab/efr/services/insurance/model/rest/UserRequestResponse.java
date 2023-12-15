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
 * Модель обращения, возвращаемого пользователю
 */
@ApiModel(description = "Модель обращения, возвращаемого пользователю")
@Validated
public class UserRequestResponse   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("requestDate")
    private LocalDate requestDate = null;

    @JsonProperty("requestsTopic")
    private AvailableTopicFormat requestsTopic = null;

    @JsonProperty("status")
    private RequestStatus status = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("insuranceId")
    private Long insuranceId = null;

    @JsonProperty("insuranceNumber")
    private String insuranceNumber = null;


    /**
     * Создает пустой экземпляр класса
     */
    public UserRequestResponse() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор темы обращения
     * @param requestDate Дата создания обращения
     * @param requestsTopic Тема для обращения
     * @param status 
     * @param phone Номер телефона
     * @param insuranceId Идентификатор договора страхования
     * @param insuranceNumber Номер договора страхования
     */
    public UserRequestResponse(Long id, LocalDate requestDate, AvailableTopicFormat requestsTopic, RequestStatus status, String phone, Long insuranceId, String insuranceNumber) {
        this.id = id;
        this.requestDate = requestDate;
        this.requestsTopic = requestsTopic;
        this.status = status;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.insuranceNumber = insuranceNumber;
    }

    /**
     * Идентификатор темы обращения
    * @return Идентификатор темы обращения
    **/
    @ApiModelProperty(value = "Идентификатор темы обращения")
    


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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRequestResponse userRequestResponse = (UserRequestResponse) o;
        return Objects.equals(this.id, userRequestResponse.id) &&
            Objects.equals(this.requestDate, userRequestResponse.requestDate) &&
            Objects.equals(this.requestsTopic, userRequestResponse.requestsTopic) &&
            Objects.equals(this.status, userRequestResponse.status) &&
            Objects.equals(this.phone, userRequestResponse.phone) &&
            Objects.equals(this.insuranceId, userRequestResponse.insuranceId) &&
            Objects.equals(this.insuranceNumber, userRequestResponse.insuranceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestDate, requestsTopic, status, phone, insuranceId, insuranceNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserRequestResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    requestDate: ").append(toIndentedString(requestDate)).append("\n");
        sb.append("    requestsTopic: ").append(toIndentedString(requestsTopic)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    insuranceId: ").append(toIndentedString(insuranceId)).append("\n");
        sb.append("    insuranceNumber: ").append(toIndentedString(insuranceNumber)).append("\n");
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

