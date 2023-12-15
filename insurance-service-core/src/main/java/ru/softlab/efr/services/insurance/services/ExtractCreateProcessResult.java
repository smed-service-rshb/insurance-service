package ru.softlab.efr.services.insurance.services;

import ru.softlab.efr.services.insurance.model.db.Extract;

public class ExtractCreateProcessResult {

    private Extract extract;
    private boolean alreadyExists;

    ExtractCreateProcessResult(Extract extract, boolean alreadyExists) {
        this.extract = extract;
        this.alreadyExists = alreadyExists;
    }

    public Extract getExtract() {
        return extract;
    }

    public boolean isAlreadyExists() {
        return alreadyExists;
    }
}
