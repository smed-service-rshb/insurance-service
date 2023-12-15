package ru.softlab.efr.services.insurance.services.contructnumbers;

import ru.softlab.efr.services.insurance.model.db.Insurance;

public interface ContractNumberSequenceApi {

    Insurance generateContractNumberAndSave(Insurance insuranceContract, String subdivisionName);

    String generateContractNumber(Insurance insuranceContract);

}
