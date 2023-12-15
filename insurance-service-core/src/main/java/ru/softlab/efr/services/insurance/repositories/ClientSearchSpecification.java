package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.softlab.efr.clients.model.PhoneType;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientSearchSpecification {

    public static Specification<ClientEntity> clientEntitySpecification(final String surName, final String firstName, final String middleName,
                                                                        final LocalDate birthDate, String mobilePhone, IdentityDocTypeEnum docType,
                                                                        String docSeries, String docNumber, String email,
                                                                        LocalDateTime checkStartDate, LocalDateTime checkEndDate,
                                                                        LocalDate startConclusionDate, LocalDate endConclusionDateTime,
                                                                        CheckUnitTypeEnum checkType, boolean likeSearch, Long excludeClientId) {
        return new Specification<ClientEntity>() {
            @Override
            public Predicate toPredicate(Root<ClientEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                criteriaQuery.distinct(true);
                if (!StringUtils.isEmpty(surName)) {
                    if (surName.length() >= 3 && likeSearch) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("surName")), "%" + surName.toUpperCase().trim() + "%"));
                    } else {
                        predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("surName")), surName.toUpperCase().trim()));
                    }
                }
                if (!StringUtils.isEmpty(firstName))
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("firstName")), firstName.toUpperCase().trim()));
                if (!StringUtils.isEmpty(middleName))
                    if (likeSearch) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("middleName")), middleName.toUpperCase().trim() + '%'));
                    } else {
                        predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("middleName")), middleName.toUpperCase().trim()));
                    }
                if (birthDate != null) predicates.add(criteriaBuilder.equal(root.get("birthDate"), birthDate));
                if (!StringUtils.isEmpty(mobilePhone)) {
                    Join<ClientEntity, PhoneForClaimEntity> phones = root.join("phones");
                    predicates.add(criteriaBuilder.equal(phones.get("phoneType"), PhoneType.MOBILE));
                    predicates.add(criteriaBuilder.equal(phones.get("number"), mobilePhone));
                }
                if (!StringUtils.isEmpty(email))
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")), email.toLowerCase().trim()));
                if (!StringUtils.isEmpty(docType) && !StringUtils.isEmpty(docNumber)) {
                    Join<ClientEntity, DocumentForClientEntity> documents = root.join("documents");
                    predicates.add(criteriaBuilder.equal(documents.get("docType"), docType));
                    predicates.add(criteriaBuilder.equal(documents.get("docNumber"), docNumber.trim()));
                    if (!StringUtils.isEmpty(docSeries)) {
                        predicates.add(criteriaBuilder.equal(documents.get("docSeries"), docSeries.trim()));
                    }
                }
                if (checkStartDate != null && checkEndDate != null) {
                    Join<ClientEntity, ClientCheck> checks = root.join("clientChecks");
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(checks.get("creationDate"), checkEndDate));
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(checks.get("creationDate"), checkStartDate));
                    if (checkType != null) {
                        predicates.add(criteriaBuilder.equal(checks.get("checkType"), checkType));
                    }
                }
                if (startConclusionDate != null && endConclusionDateTime != null) {
                    Join<ClientEntity, Insurance> holder = root.join("insurancesHolder", JoinType.LEFT);
                    Join<ClientEntity, Insurance> insured = root.join("insurancesInsured", JoinType.LEFT);
                    predicates.add(criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo(holder.get("conclusionDate"), endConclusionDateTime),
                            criteriaBuilder.lessThanOrEqualTo(insured.get("conclusionDate"), endConclusionDateTime)));
                    predicates.add(criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(holder.get("conclusionDate"), startConclusionDate),
                            criteriaBuilder.greaterThanOrEqualTo(insured.get("conclusionDate"), startConclusionDate)));
                }
                if (excludeClientId != null){
                    predicates.add(criteriaBuilder.notEqual(root.get("id"), excludeClientId));
                }
                return predicates.size() > 1
                        ? criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
                        : predicates.get(0);
            }
        };
    }
}
