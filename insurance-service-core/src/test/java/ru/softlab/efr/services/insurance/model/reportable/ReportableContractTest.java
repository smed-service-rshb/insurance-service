package ru.softlab.efr.services.insurance.model.reportable;

import org.junit.Test;
import ru.softlab.efr.services.insurance.model.db.AddressForClientEntity;
import ru.softlab.efr.services.insurance.model.enums.AddressTypeEnum;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author olshansky
 * @since 11.12.2018
 */
public class ReportableContractTest {

    @Test
    public void getAddress() {
        AddressForClientEntity address = new AddressForClientEntity();
        address.setAddressType(AddressTypeEnum.RESIDENCE);
        address.setArea("AREA");
        address.setCity("CITY");
        address.setHouse("29");
        address.setHousing("housing");

        String addressString = address.getAddress();
        assertNotNull(addressString);
        assertFalse(addressString.startsWith(","));
        assertFalse(addressString.endsWith(", "));
        assertFalse(addressString.matches("(,\\s*){2,9}"));
    }

    @Test
    public void getRurStringAmount() {
        BigDecimal amount = BigDecimal.valueOf(4123412.56123);
        String amountString = ReportableContract.getRurStringAmount(amount);
        assertNotNull(amountString);
        assertEquals(amountString,"четыре миллиона сто двадцать три тысячи четыреста двенадцать рублей пятьдесят шесть копеек");
    }

}
