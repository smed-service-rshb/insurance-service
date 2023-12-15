package ru.softlab.efr.services.insurance.model.enums;

/**
 *  Enum перечисляющий названия определенных рисков, требуемых для специфичного отображения в определенных печатных формах.
 */
public enum NecessaryRisksEnum {

    DEATH_BY_SHIPWRECK("Смерть в результате кораблекрушения"),
    DEATH_INSURED_BY_SHIPWRECK("Смерть Застрахованного в результате кораблекрушения"),
    DEATH_FOR_ANY_REASON("Смерть по любой причине"),
    DEATH_INSURED_FOR_ANY_REASON("Смерть Застрахованного по любой причине"),
    DEATH_BY_ACCIDENT("Смерть от несчастного случая"),
    DEATH_BY_ACCIDENT_TYPE2("Смерть в результате несчастного случая"),
    DEATH_BY_ACCIDENT_TYPE3("Смерть Застрахованного от несчастного случая"),
    DEATH_BY_ACCIDENT_SHORT("несчастного"),
    DISABILITY("Инвалидность I, II группы в результате несчастного случая"),
    DISABILITY_SHORT("Инвалидность I, II группы"),
    SURVIVAL("Дожитие"),
    SURVIVAL_TILL("Дожитие до"),
    SURVIVAL_INSURED_TILL("Дожитие Застрахованного");

    private String deathName;

    NecessaryRisksEnum(String deathName) {
        this.deathName = deathName;
    }

    public String getDeathName() {
        return deathName;
    }
}
