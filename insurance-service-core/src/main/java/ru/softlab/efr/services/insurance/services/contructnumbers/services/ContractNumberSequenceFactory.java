package ru.softlab.efr.services.insurance.services.contructnumbers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.services.promocode.PromocodeApiConnectorService;
import ru.softlab.efr.services.insurance.services.promocode.models.PromocodeRegisterModel;

import java.time.LocalDate;

@Service
public class ContractNumberSequenceFactory implements ContractNumberSequenceFactoryApi {

    private ContractNumberSequenceLifeService contractNumberSequenceLifeService;
    private ContractNumberSequenceHomeService contractNumberSequenceHomeService;
    private ContractNumberSequenceSmsService contractNumberSequenceSmsService;



    @Autowired
    public ContractNumberSequenceFactory(ContractNumberSequenceLifeService contractNumberSequenceLifeService,
                                         ContractNumberSequenceHomeService contractNumberSequenceHomeService,
                                         ContractNumberSequenceSmsService contractNumberSequenceSmsService) {
        this.contractNumberSequenceHomeService = contractNumberSequenceHomeService;
        this.contractNumberSequenceLifeService = contractNumberSequenceLifeService;
        this.contractNumberSequenceSmsService = contractNumberSequenceSmsService;
    }

    @Override
    public String getContractNumber(Insurance insuranceContract, String subdivisionName, String charCode, String contractNumber, Long nextValue) {
        switch (insuranceContract.getProgramSetting().getProgram().getType()) {
            case HOME:
                return contractNumberSequenceHomeService.generateContractNumber(
                        charCode,
                        insuranceContract.getProgramSetting().getProgram().getPolicyCode(),
                        insuranceContract.getProgramSetting().getProgram().getVariant(),
                        nextValue
                );
            case SMS:
                return contractNumberSequenceSmsService.generateContractNumber(
                        insuranceContract.getProgramSetting().getProgram().getProgramCode(),
                        insuranceContract.getProgramSetting().getProgram().getProgramTariff(),
                        LocalDate.now().getDayOfMonth(),
                        nextValue
                );
            default:
                return contractNumberSequenceLifeService.generateContractNumber(
                        subdivisionName,
                        insuranceContract.getProgramSetting().getProgram().getPolicyCode(),
                        insuranceContract.getProgramSetting().getProgram().getVariant(),
                        insuranceContract.getCurrency().toString(),
                        contractNumber,
                        nextValue
                );
        }
    }

}
