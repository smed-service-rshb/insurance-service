package ru.softlab.efr.services.insurance.model.enums;

/**
 * Типы родственных отношений
 * spouse - Супруг/супруга
 * parent - Отец/мать
 * child - Сын/дочь
 * grandparent - Дедушка/бабушка
 * grandchild - Внук/внучка
 * sibling - Брат/сестра (в том числе неполнородные)
 * stepparent - Отчим/мачеха
 *
 * @author olshansky
 * @since 07.11.2018
 */
public enum RelationTypeEnum {

    SPOUSE("spouse"),
    PARENT("parent"),
    CHILD("child"),
    GRANDPARENT("grandparent"),
    GRANDCHILD("grandchild"),
    SIBLING("sibling"),
    STEPPARENT("stepparent");

    private String value;

    RelationTypeEnum(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static RelationTypeEnum fromValue(String text) {
        for (RelationTypeEnum b : RelationTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
