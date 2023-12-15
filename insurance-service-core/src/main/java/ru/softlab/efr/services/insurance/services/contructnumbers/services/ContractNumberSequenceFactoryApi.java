package ru.softlab.efr.services.insurance.services.contructnumbers.services;

import ru.softlab.efr.services.insurance.model.db.Insurance;

public interface ContractNumberSequenceFactoryApi {

    String getContractNumber(Insurance insuranceContract, String subdivisionName, String charCode, String contractNumber, Long nextValue);

}
