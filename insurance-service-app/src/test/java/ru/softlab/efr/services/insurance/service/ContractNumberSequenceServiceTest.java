package ru.softlab.efr.services.insurance.service;

import org.junit.Test;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.services.contructnumbers.ContractNumberSequenceImpl;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceFactory;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceFactoryApi;
import ru.softlab.efr.services.insurance.services.contructnumbers.services.ContractNumberSequenceLifeService;

import static org.junit.Assert.*;

/**
 * Тесты для проверки ContractNumberSequenceService.
 *
 * @author olshansky
 * @since 18.12.2018
 */
public class ContractNumberSequenceServiceTest {

    /**
     * Проверить, что сквозной номер у вновь сгенерированного номера договора подобрался правильно для указанного типа программы страхования
     */
    @Test
    public void checkSuccess() {
        checkGeneratedContractNumber(ProgramKind.ISJ, "23", "А02", "01", "1", null);
        checkGeneratedContractNumber(ProgramKind.RENT, "99", "Б02", "02", "2", "99Б020220000006");
        checkGeneratedContractNumber(ProgramKind.NSJ, "12", "В02", "03", "3", "12В020330001235");
        checkGeneratedContractNumber(ProgramKind.KSP, "11", "Ё02", "04", "4", null);
        checkGeneratedContractNumber(null, "43", "Я02", "01", "4", "43Я020140000001");
    }

    public static boolean checkShortGeneratedContractNumber(String contractNumber) {
        assertNotNull(contractNumber);
        assertEquals(15, contractNumber.trim().length());
        String contractNumberRegexMask = "\\d{2}[А-ЯЁ]\\d{2}\\d[1-9]\\d\\d{7}";
        assertTrue(contractNumber.matches(contractNumberRegexMask));
        return true;
    }

    private static boolean checkGeneratedContractNumber(ProgramKind programKind, String branchCode, String policyCode, String variant, String currencyCode, String oldContractNumber) {
        ContractNumberSequenceImpl contractNumberSequenceService = new ContractNumberSequenceImpl(new ContractNumberSequenceRepositoryStub(), new InsuranceRepositoryStub(), null);

        ContractNumberSequenceLifeService contractNumberSequenceLifeService = new ContractNumberSequenceLifeService();
        Long currentNumber = contractNumberSequenceService.getNextSequenceValueByProgramKind(programKind);
        String contractNumber = contractNumberSequenceLifeService.generateContractNumber(branchCode, policyCode, variant, currencyCode, oldContractNumber, currentNumber);
        //AABBBCCDEEEEEEE
        System.out.println(contractNumber);

        assertNotNull(contractNumber);
        assertEquals(15, contractNumber.trim().length());
        String contractNumberRegexMask = "\\d{2}[А-ЯЁ]\\d{2}\\d[1-9]\\d\\d{7}";
        assertTrue(contractNumber.matches(contractNumberRegexMask));

        assertEquals(contractNumber.substring(0, 2), branchCode);
        assertEquals(contractNumber.substring(2, 5), policyCode);
        assertEquals(contractNumber.substring(5, 7), variant);
        assertEquals(contractNumber.substring(7, 8), currencyCode);


        String numberMustBe = String.format("%07d", currentNumber);
        assertEquals(numberMustBe, contractNumber.substring(8));
        return true;
    }

}
