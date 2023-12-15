package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Репозиторий для работы с сущностью результатов проверки клиента по справочникам (спискам)
 *
 * @author krivenko
 * @since 10.01.2019
 */
public interface ClientCheckRepository extends JpaRepository<ClientCheck, Long> {

    @Query("select new ru.softlab.efr.services.insurance.repositories.InsuranceSummary" +
            "(c.contractNumber, c.creationDate) " +
            "from Insurance as c where c.holder.id = :clientId or c.insured.id = :clientId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<InsuranceSummary> findAllContracts(@Param("clientId") Long clientId);

    @Query("select c from Insurance as c where c.holder.id = :clientId or c.insured.id = :clientId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Insurance> findAllContractsWithAllInfo(@Param("clientId") Long clientId);

    @Query("select c from ClientCheck as c where c.client.id = :clientId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ClientCheck> findAllChecksByClientId(@Param("clientId") Long clientId);

    @Query("select c.client from ClientCheck as c where c.checkResult = 'TRUE' and c.checkType = :typeEnum and " +
            "c.creationDate = (select max (cc.creationDate) from ClientCheck cc where cc.client = c.client and cc.checkType = :typeEnum)")
    List<ClientShortData> findAllSuspiciousClients(@Param("typeEnum") CheckUnitTypeEnum typeEnum);

    @Query("select new ru.softlab.efr.services.insurance.repositories.ClientCheckDTO" +
            "(cl.id,cl.surName,cl.firstName,cl.middleName,cl.birthDate, ad.addressType, ad.country, ad.countryCode, " +
            "ad.region, ad.okato, ad.area, ad.city, ad.locality, ad.street, ad.house, ad.construction, ad.housing, ad.apartment, ad.postIndex ) " +
            "from ClientCheck as c " +
            "inner join c.client as cl " +
            "left join cl.addresses as ad with (ad.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = cl and adr.addressType = 'RESIDENCE')) " +
            "where c.checkResult = 'TRUE' and c.checkType = :typeEnum and " +
            "c.updateId = (select max (cc.updateId) from ClientCheck cc " +
            "where cc.client = cl and cc.checkType = :typeEnum)")
    List<ClientCheckDTO> findAllTerroristsAndBlocked(@Param("typeEnum") CheckUnitTypeEnum typeEnum);

    @Query("select new ru.softlab.efr.services.insurance.repositories.ClientCheckDTO" +
            "(cl.id,cl.surName,cl.firstName,cl.middleName,cl.birthDate, ad.addressType, ad.country, ad.countryCode, " +
            "ad.region, ad.okato, ad.area, ad.city, ad.locality, ad.street, ad.house, ad.construction, ad.housing, ad.apartment, ad.postIndex ) " +
            "from ClientCheck as c " +
            "inner join c.client as cl " +
            "left join cl.addresses as ad with (ad.id in (select min(adr.id) from AddressForClientEntity adr where adr.client = cl and adr.addressType = 'RESIDENCE')) " +
            "where c.checkResult = 'TRUE' and c.checkType = :typeEnum " +
            "and cl.id in (:clientIds)" +
            "and c.updateId = (select max (cc.updateId) from ClientCheck cc " +
            "where cc.client = cl and cc.checkType = :typeEnum)")
    List<ClientCheckDTO> findAllTerroristsAndBlocked(@Param("typeEnum") CheckUnitTypeEnum typeEnum, @Param("clientIds") List<Long> clientIds);

    @Query("select c.client from ClientCheck as c where c.checkResult = 'TRUE' and c.checkType = :typeEnum and " +
            "c.client.id in (:clientIds) and " +
            "c.creationDate = (select max (cc.creationDate) from ClientCheck cc where cc.client = c.client and cc.checkType = :typeEnum)")
    List<ClientShortData> findAllSuspiciousClients(@Param("typeEnum") CheckUnitTypeEnum typeEnum, @Param("clientIds") List<Long> clientIds);

    @Query("select count(c) from ClientCheck as c where c.checkType = :typeEnum and c.creationDate = " +
            "(select max (cc.creationDate) from ClientCheck cc where cc.client = c.client and cc.checkType = :typeEnum)")
    Integer countByType(@Param("typeEnum") CheckUnitTypeEnum typeEnum);

    @Query("select count(c) from ClientCheck as c " +
            "where c.checkType = :typeEnum " +
            "and c.client.id in (:clientIds) " +
            "and c.creationDate = " +
            "(select max (cc.creationDate) from ClientCheck cc where cc.client = c.client and cc.checkType = :typeEnum)")
    Integer countByType(@Param("typeEnum") CheckUnitTypeEnum typeEnum, @Param("clientIds") List<Long> clientIds);
}