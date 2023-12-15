package ru.softlab.efr.services.insurance.pojo;

import ru.softlab.efr.services.insurance.model.db.Quote;
import ru.softlab.efr.services.insurance.model.db.ShareEntity;

import java.util.ArrayList;
import java.util.List;

public class ParseShareResult {

    private List<ShareEntity> shares;
    private List<Quote> quotes;

    public ParseShareResult(List<ShareEntity> shares, List<Quote> quotes) {
        this.shares = shares;
        this.quotes = quotes;
    }

    public ParseShareResult() {
        this.shares = new ArrayList<>();
        this.quotes = new ArrayList<>();
    }

    public List<ShareEntity> getShares() {
        return shares;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
