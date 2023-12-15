package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.softlab.efr.services.insurance.model.rest.RequestStatus;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Запрос на обработку обращения клиента
 */
@ApiModel(description = "Запрос на обработку обращения клиента")
@Validated
public class ProcessingClientRequestRq   {
    @JsonProperty("status")
    private RequestStatus status = null;

    @JsonProperty("info")
    private String info = null;

    @JsonProperty("clientComment")
    private String clientComment = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ProcessingClientRequestRq() {}

    /**
     * Создает экземпляр класса
     * @param status Новый статус обращения
     * @param info Внутренний комментарий к обращению клиента
     * @param clientComment Комментарий, отображаемый клиенту
     */
    public ProcessingClientRequestRq(RequestStatus status, String info, String clientComment) {
        this.status = status;
        this.info = info;
        this.clientComment = clientComment;
    }

    /**
     * Новый статус обращения
    * @return Новый статус обращения
    **/
    @ApiModelProperty(required = true, value = "Новый статус обращения")
      @NotNull

  @Valid


    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }


    /**
     * Внутренний комментарий к обращению клиента
    * @return Внутренний комментарий к обращению клиента
    **/
    @ApiModelProperty(value = "Внутренний комментарий к обращению клиента")
    


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    /**
     * Комментарий, отображаемый клиенту
    * @return Комментарий, отображаемый клиенту
    **/
    @ApiModelProperty(value = "Комментарий, отображаемый клиенту")
    


    public String getClientComment() {
        return clientComment;
    }

    public void setClientComment(String clientComment) {
        this.clientComment = clientComment;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessingClientRequestRq processingClientRequestRq = (ProcessingClientRequestRq) o;
        return Objects.equals(this.status, processingClientRequestRq.status) &&
            Objects.equals(this.info, processingClientRequestRq.info) &&
            Objects.equals(this.clientComment, processingClientRequestRq.clientComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, info, clientComment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProcessingClientRequestRq {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    info: ").append(toIndentedString(info)).append("\n");
        sb.append("    clientComment: ").append(toIndentedString(clientComment)).append("\n");
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

