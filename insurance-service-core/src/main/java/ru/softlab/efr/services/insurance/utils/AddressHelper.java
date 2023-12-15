package ru.softlab.efr.services.insurance.utils;

import ru.softlab.efr.clients.model.Address;

import java.util.ArrayList;

public class AddressHelper {

    private static final String ADDRESS_DELIMITER = ", ";

    public static String getAddressString(Address address) {
        ArrayList<String> addressAttributes = new ArrayList<>();
        addNotNullStrToList(address.getIndex(), addressAttributes);
        addNotNullStrToList(address.getCountry(), addressAttributes);
        addNotNullStrToList(address.getRegion(), addressAttributes);
        addNotNullStrToList(address.getArea(), addressAttributes);
        addNotNullStrToList(address.getCity(), addressAttributes);
        addNotNullStrToList(address.getLocality(), addressAttributes);
        addNotNullStrToList(address.getStreet(), addressAttributes);
        addNotNullStrToList(address.getHouse(), addressAttributes);
        addNotNullStrToList(address.getConstruction(), addressAttributes);
        addNotNullStrToList(address.getHousing(), addressAttributes);
        addNotNullStrToList(address.getApartment(), addressAttributes);
        return String.join(ADDRESS_DELIMITER, addressAttributes);
    }

    private static void addNotNullStrToList(String str, ArrayList<String> strs) {
        if (str != null) {
            strs.add(str);
        }
    }
}
