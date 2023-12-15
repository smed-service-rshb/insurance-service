package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.insurance.model.rest.QuoteData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Данные акции
 */
@ApiModel(description = "Данные акции")
@Validated
public class SharesData   {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("quotes")
    @Valid
    private List<QuoteData> quotes = new ArrayList<QuoteData>();


    /**
     * Создает пустой экземпляр класса
     */
    public SharesData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param name Наименование акции
     * @param description Описание акции
     * @param quotes 
     */
    public SharesData(Long id, String name, String description, List<QuoteData> quotes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quotes = quotes;
    }

    /**
     * Идентификатор записи
    * @return Идентификатор записи
    **/
    @ApiModelProperty(required = true, value = "Идентификатор записи")
      @NotNull



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование акции
    * @return Наименование акции
    **/
    @ApiModelProperty(required = true, value = "Наименование акции")
      @NotNull



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Описание акции
    * @return Описание акции
    **/
    @ApiModelProperty(value = "Описание акции")
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public SharesData addQuotesItem(QuoteData quotesItem) {
        this.quotes.add(quotesItem);
        return this;
    }

    /**
    * Get quotes
    * @return 
    **/
    @ApiModelProperty(required = true, value = "")
      @NotNull

  @Valid


    public List<QuoteData> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuoteData> quotes) {
        this.quotes = quotes;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SharesData sharesData = (SharesData) o;
        return Objects.equals(this.id, sharesData.id) &&
            Objects.equals(this.name, sharesData.name) &&
            Objects.equals(this.description, sharesData.description) &&
            Objects.equals(this.quotes, sharesData.quotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, quotes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SharesData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    quotes: ").append(toIndentedString(quotes)).append("\n");
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

