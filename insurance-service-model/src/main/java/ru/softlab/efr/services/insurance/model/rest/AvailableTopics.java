package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.AvailableTopicFormat;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Доступные темы для обращений
 */
@ApiModel(description = "Доступные темы для обращений")
@Validated
public class AvailableTopics   {
    @JsonProperty("count")
    private Long count = null;

    @JsonProperty("topics")
    @Valid
    private List<AvailableTopicFormat> topics = null;


    /**
     * Создает пустой экземпляр класса
     */
    public AvailableTopics() {}

    /**
     * Создает экземпляр класса
     * @param count Колличество тем для обращений
     * @param topics Список доступных тем для обращений
     */
    public AvailableTopics(Long count, List<AvailableTopicFormat> topics) {
        this.count = count;
        this.topics = topics;
    }

    /**
     * Колличество тем для обращений
    * @return Колличество тем для обращений
    **/
    @ApiModelProperty(value = "Колличество тем для обращений")
    


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


    public AvailableTopics addTopicsItem(AvailableTopicFormat topicsItem) {
        if (this.topics == null) {
            this.topics = new ArrayList<AvailableTopicFormat>();
        }
        this.topics.add(topicsItem);
        return this;
    }

    /**
     * Список доступных тем для обращений
    * @return Список доступных тем для обращений
    **/
    @ApiModelProperty(value = "Список доступных тем для обращений")
    
  @Valid


    public List<AvailableTopicFormat> getTopics() {
        return topics;
    }

    public void setTopics(List<AvailableTopicFormat> topics) {
        this.topics = topics;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableTopics availableTopics = (AvailableTopics) o;
        return Objects.equals(this.count, availableTopics.count) &&
            Objects.equals(this.topics, availableTopics.topics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, topics);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AvailableTopics {\n");
        
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    topics: ").append(toIndentedString(topics)).append("\n");
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

