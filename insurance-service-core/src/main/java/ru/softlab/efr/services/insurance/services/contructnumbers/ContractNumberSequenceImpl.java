package ru.softlab.efr.services.insurance.services.contructnumbers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ContractNumberSequence;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.repositories.ContractNumberSequenceRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceFactoryApi;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceHomeService;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceLifeService;

import java.util.Objects;
import java.util.Optional;

@Service
public class ContractNumberSequenceImpl implements ContractNumberSequenceApi {

    private final ContractNumberSequenceRepository contractNumberSequenceRepository;
    private final InsuranceRepository insuranceRepository;

    private ContractNumberSequenceFactoryApi contractNumberSequenceFactoryApi;

    @Autowired
    public ContractNumberSequenceImpl(ContractNumberSequenceRepository contractNumberSequenceRepository,
                                      InsuranceRepository insuranceRepository,
                                      ContractNumberSequenceFactoryApi contractNumberSequenceFactoryApi) {
        this.contractNumberSequenceRepository = contractNumberSequenceRepository;
        this.insuranceRepository = insuranceRepository;
        this.contractNumberSequenceFactoryApi = contractNumberSequenceFactoryApi;
    }

    @Override
    public Insurance generateContractNumberAndSave(Insurance insuranceContract, String subdivisionName) {
        if (Objects.nonNull(subdivisionName)) {
            subdivisionName = subdivisionName.substring(0, 2);
        }
        return generateContractNumberAndSaveProcess(
                insuranceContract,
                subdivisionName,
                contractNumberSequenceFactoryApi.getContractNumber(
                        insuranceContract,
                        subdivisionName,
                        insuranceContract.getProgramSetting().getProgram().getProgramCharCode(),
                        insuranceContract.getContractNumber(),
                        getNextSequenceValueByProgramKind(insuranceContract.getProgramSetting().getProgram().getType())));
    }

    @Override
    @Transactional
    public String generateContractNumber(Insurance insuranceContract) {
        contractNumberSequenceRepository.incrementSequenceValueByProgramKind(insuranceContract.getProgramSetting().getProgram().getType());
        Long newValue = contractNumberSequenceRepository.getSequenceValueByProgramKind(insuranceContract.getProgramSetting().getProgram().getType());
        if (Objects.isNull(newValue)) {
            contractNumberSequenceRepository.saveAndFlush(new ContractNumberSequence(1L, insuranceContract.getProgramSetting().getProgram().getType()));
            newValue = 1L;
        }
        return contractNumberSequenceFactoryApi.getContractNumber(
                insuranceContract,
                null,
                insuranceContract.getProgramSetting().getProgram().getProgramCharCode(),
                insuranceContract.getContractNumber(),
                newValue);
    }

    private synchronized void incrementSequenceValueByProgramKind(ProgramKind programKind) {
        if (getCurrentSequenceValueByProgramKind(programKind) == null) {
            contractNumberSequenceRepository.saveAndFlush(new ContractNumberSequence(1L, programKind));
        } else {
            contractNumberSequenceRepository.incrementSequenceValueByProgramKind(programKind);
        }
    }

    private Long getCurrentSequenceValueByProgramKind(ProgramKind programKind) {
        return contractNumberSequenceRepository.getSequenceValueByProgramKind(programKind);
    }

    public Long getNextSequenceValueByProgramKind(ProgramKind programKind) {
        Long number = getCurrentSequenceValueByProgramKind(programKind);

        if (number == null) {
            number = 1L;
        } else {
            number++;
        }
        return number;
    }


    private synchronized Insurance generateContractNumberAndSaveProcess(Insurance insuranceContract, String subdivisionName, String generatedContractNumber) {
        Boolean isExistContract = insuranceRepository.existsByContractNumberAndIdNot(generatedContractNumber, insuranceContract.getId());

        if (isExistContract != null && isExistContract.equals(Boolean.TRUE)) {
            return generateContractNumberAndSaveProcess(insuranceContract, subdivisionName, incrementContractNumber(generatedContractNumber));
        }
        insuranceContract.setContractNumber(generatedContractNumber);
        insuranceContract = insuranceRepository.saveAndFlush(insuranceContract);
        incrementSequenceValueByProgramKind(insuranceContract.getProgramSetting().getProgram().getType());

        return insuranceContract;
    }

    private String incrementContractNumber(String contractNumber) {
        Integer contractNum = Integer.valueOf(contractNumber.substring(8));
        contractNum++;
        return contractNumber.substring(0,8).concat(String.format("%07d", contractNum));
    }
}
