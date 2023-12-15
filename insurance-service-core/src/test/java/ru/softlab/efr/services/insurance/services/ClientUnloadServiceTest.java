package ru.softlab.efr.services.insurance.services;

import org.junit.Test;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.services.impl.CountryServiceImpl;

import static org.junit.Assert.*;

/**
 *
 * @author olshansky
 * @since 18.01.2019
 */
public class ClientUnloadServiceTest {


    ClientUnloadService service = new ClientUnloadService(new CountryServiceImpl(), null, null);
    @Test
    public void replaceFirstWordOnBack() {
        assertEquals("Российская Федерация", service.replaceFirstWordOnBack("Российская Федерация"));
        assertEquals("Конго Республика", service.replaceFirstWordOnBack("Республика Конго"));
        assertEquals("Набережные Челны", service.replaceFirstWordOnBack("Набережные Челны"));
        assertEquals("Ханты-Мансийск", service.replaceFirstWordOnBack("Ханты-Мансийск"));
        assertEquals("Генерала Петрова", service.replaceFirstWordOnBack("Генерала Петрова"));
        assertEquals("Мира проспект", service.replaceFirstWordOnBack("проспект Мира"));
        assertEquals("Московская обл.", service.replaceFirstWordOnBack("обл. Московская"));
        assertEquals("Ногинский р-н", service.replaceFirstWordOnBack("р-н Ногинский"));
        assertEquals("Институтский пр-т", service.replaceFirstWordOnBack("пр-т Институтский"));
        assertEquals("Москва г.", service.replaceFirstWordOnBack("г. Москва"));
        assertEquals("Пришвина у", service.replaceFirstWordOnBack("у Пришвина"));
        assertEquals("Пришвина у.", service.replaceFirstWordOnBack("у. Пришвина"));
        assertEquals("8 дом", service.replaceFirstWordOnBack("дом 8"));
        assertEquals("2 корп.", service.replaceFirstWordOnBack("корп. 2"));
        assertEquals("8 д", service.replaceFirstWordOnBack("д 8"));
        assertEquals("205 офис", service.replaceFirstWordOnBack("офис 205"));
        assertEquals("Ленина ул", service.replaceFirstWordOnBack("ул Ленина"));
        assertEquals("Ленина ул.", service.replaceFirstWordOnBack("ул.Ленина"));
        assertEquals("12 кв.", service.replaceFirstWordOnBack("кв.12"));
        assertEquals("3 корп.", service.replaceFirstWordOnBack("корп.3"));
        assertEquals("Северной Двины набережная", service.replaceFirstWordOnBack("набережная Северной Двины"));
        assertEquals("Ленина улица", service.replaceFirstWordOnBack("улица Ленина"));
        assertEquals("Ленина ул.", service.replaceFirstWordOnBack("ул. Ленина"));
        assertEquals("Сахарная Головка пос.", service.replaceFirstWordOnBack("пос. Сахарная Головка"));
        assertEquals("Дмитровское шоссе", service.replaceFirstWordOnBack("шоссе Дмитровское"));
        assertEquals("Дмитровское шоссе", service.replaceFirstWordOnBack("Дмитровское шоссе"));
    }
    @Test
    public void presentSnils() {
        assertEquals( "123-456-789 12", service.presentSnils("12345678912"));
        assertEquals( "1234567891", service.presentSnils("1234567891"));
    }
    @Test
    public void getCitizenShipKind() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setResident("Российская Федерация");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("russian");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("БЕЛАРУСЬ");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("Республика Беларусь");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("КАЗАХСТАН");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("Республика Казахстан");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("АРМЕНИЯ");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("Республика Армения");
        assertEquals( "1", service.getCitizenShipKind(clientEntity));
        clientEntity.setResident("АЗЕРБАЙДЖАН");
        assertEquals( "2", service.getCitizenShipKind(clientEntity));
    }

    @Test
    public void isPointName() {
        assertFalse(service.isPointName("Российская"));
        assertFalse(service.isPointName("Набережные"));
        assertFalse(service.isPointName("Ханты-Мансийск"));
        assertFalse(service.isPointName("Генерала"));
        assertFalse(service.isPointName("Николая"));

        assertTrue(service.isPointName("страна"));
        assertTrue(service.isPointName("улица"));
        assertTrue(service.isPointName("дом"));
        assertTrue(service.isPointName("квартира"));
    }

    @Test
    public void addDescriptionIfNeed() {
        assertEquals("Ленина ул", service.addDescriptionIfNeed("Ленина", "ул"));
        assertEquals("улица Ленина", service.addDescriptionIfNeed("улица Ленина", "ул"));
        assertEquals("Москва г.", service.addDescriptionIfNeed("Москва г.", "г"));
        assertEquals("Арбат ул", service.addDescriptionIfNeed("Арбат ул", "ул"));
        assertEquals("ул.Арбат", service.addDescriptionIfNeed("ул.Арбат", "ул"));
        assertEquals("Арбат у", service.addDescriptionIfNeed("Арбат", "у"));
        assertEquals("", service.addDescriptionIfNeed("", "у"));
        assertNull(service.addDescriptionIfNeed(null, "у"));
        assertEquals("         ", service.addDescriptionIfNeed("         ", "у"));
    }
}