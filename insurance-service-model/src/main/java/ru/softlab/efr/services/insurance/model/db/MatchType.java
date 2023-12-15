package ru.softlab.efr.services.insurance.model.db;

/**
 * Найденное совпадение
 * @author bazanova
 * @since 04.07.2018
 */
public class MatchType {

    /**
     * Ссылка на источник
     */
    private String link;
    /**
     * Данные из источника
     */
    private String linkDecs;

    public MatchType() {}

    /**
     * Создать MatchType
     * @param link ссылка на источник
     * @param linkDecs данные из источника
     */
    public MatchType(String link, String linkDecs) {
        this.link = link;
        this.linkDecs = linkDecs;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkDecs() {
        return linkDecs;
    }

    public void setLinkDecs(String linkDecs) {
        this.linkDecs = linkDecs;
    }
}
