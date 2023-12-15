package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterRequestsRq
 */
@Validated
public class FilterRequestsRq   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("topicId")
    private Long topicId = null;

    @JsonProperty("requestDateFrom")
    private LocalDate requestDateFrom = null;

    @JsonProperty("requestDateTo")
    private LocalDate requestDateTo = null;

    @JsonProperty("clientSurname")
    private String clientSurname = null;

    @JsonProperty("clientName")
    private String clientName = null;

    @JsonProperty("clientId")
    private Long clientId = null;

    @JsonProperty("status")
    private RequestStatus status = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterRequestsRq() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор обращения
     * @param topicId Идентификатор обращения
     * @param requestDateFrom Дата создания обращения (начальная дата выборки)
     * @param requestDateTo Дата создания обращения (конечная дата выборки)
     * @param clientSurname Фамилия клиента или его часть
     * @param clientName Имя клиента или его часть
     * @param clientId Идентификатор клиента, создавшего договор
     * @param status 
     */
    public FilterRequestsRq(Long id, Long topicId, LocalDate requestDateFrom, LocalDate requestDateTo, String clientSurname, String clientName, Long clientId, RequestStatus status) {
        this.id = id;
        this.topicId = topicId;
        this.requestDateFrom = requestDateFrom;
        this.requestDateTo = requestDateTo;
        this.clientSurname = clientSurname;
        this.clientName = clientName;
        this.clientId = clientId;
        this.status = status;
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
     * Идентификатор обращения
    * @return Идентификатор обращения
    **/
    @ApiModelProperty(value = "Идентификатор обращения")
    


    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }


    /**
     * Дата создания обращения (начальная дата выборки)
    * @return Дата создания обращения (начальная дата выборки)
    **/
    @ApiModelProperty(value = "Дата создания обращения (начальная дата выборки)")
    
  @Valid


    public LocalDate getRequestDateFrom() {
        return requestDateFrom;
    }

    public void setRequestDateFrom(LocalDate requestDateFrom) {
        this.requestDateFrom = requestDateFrom;
    }


    /**
     * Дата создания обращения (конечная дата выборки)
    * @return Дата создания обращения (конечная дата выборки)
    **/
    @ApiModelProperty(value = "Дата создания обращения (конечная дата выборки)")
    
  @Valid


    public LocalDate getRequestDateTo() {
        return requestDateTo;
    }

    public void setRequestDateTo(LocalDate requestDateTo) {
        this.requestDateTo = requestDateTo;
    }


    /**
     * Фамилия клиента или его часть
    * @return Фамилия клиента или его часть
    **/
    @ApiModelProperty(value = "Фамилия клиента или его часть")
    


    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }


    /**
     * Имя клиента или его часть
    * @return Имя клиента или его часть
    **/
    @ApiModelProperty(value = "Имя клиента или его часть")
    


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    /**
     * Идентификатор клиента, создавшего договор
    * @return Идентификатор клиента, создавшего договор
    **/
    @ApiModelProperty(value = "Идентификатор клиента, создавшего договор")
    


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterRequestsRq filterRequestsRq = (FilterRequestsRq) o;
        return Objects.equals(this.id, filterRequestsRq.id) &&
            Objects.equals(this.topicId, filterRequestsRq.topicId) &&
            Objects.equals(this.requestDateFrom, filterRequestsRq.requestDateFrom) &&
            Objects.equals(this.requestDateTo, filterRequestsRq.requestDateTo) &&
            Objects.equals(this.clientSurname, filterRequestsRq.clientSurname) &&
            Objects.equals(this.clientName, filterRequestsRq.clientName) &&
            Objects.equals(this.clientId, filterRequestsRq.clientId) &&
            Objects.equals(this.status, filterRequestsRq.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicId, requestDateFrom, requestDateTo, clientSurname, clientName, clientId, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterRequestsRq {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
        sb.append("    requestDateFrom: ").append(toIndentedString(requestDateFrom)).append("\n");
        sb.append("    requestDateTo: ").append(toIndentedString(requestDateTo)).append("\n");
        sb.append("    clientSurname: ").append(toIndentedString(clientSurname)).append("\n");
        sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

