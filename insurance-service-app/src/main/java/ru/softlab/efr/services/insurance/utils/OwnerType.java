package ru.softlab.efr.services.insurance.utils;

public enum OwnerType {
    //страхователь
    HOLDER {
        @Override
        public String getText() {
            return "страхователю";
        }
    },
    //застрахованный
    INSURED {
        @Override
        public String getText() {
            return "застрахованному лицу";
        }
    }
    ;

    public String getText() {
        return this.toString();
    }

    public static String getTitle(OwnerType ownerType) {
        return String.format("Информация по %s. ", ownerType != null ? ownerType.getText() : "клиенту");
    }
}
