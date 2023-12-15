package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.db.TopicRequestEntity;
import ru.softlab.efr.services.insurance.model.rest.FilterRequestsRq;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class RequestsSearchRepository {

    private EntityManager em;

    public RequestsSearchRepository(EntityManager em) {
        this.em = em;
    }

    public Page<RequestEntity> getFilteredRequestsRq(Pageable pageable, FilterRequestsRq filterRequestsRq) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RequestEntity> cq = cb.createQuery(RequestEntity.class);

        Root<RequestEntity> report = cq.from(RequestEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(filterRequestsRq.getId())) {
            predicates.add(cb.equal(report.get("id"), filterRequestsRq.getId()));
        }
        if (Objects.nonNull(filterRequestsRq.getTopicId())) {
            Join<RequestEntity, TopicRequestEntity> topicReports = report.join("topic");
            predicates.add(cb.equal(topicReports.get("id"), filterRequestsRq.getTopicId()));
        }
        if (Objects.nonNull(filterRequestsRq.getRequestDateFrom())) {
            Date from = Date.from(filterRequestsRq.getRequestDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant());
            predicates.add(cb.greaterThanOrEqualTo(report.get("requestDate"), from));
        }
        if (Objects.nonNull(filterRequestsRq.getRequestDateTo())) {
            Date to = Date.from(filterRequestsRq.getRequestDateTo().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            predicates.add(cb.lessThanOrEqualTo(report.get("requestDate"), to));
        }
        if (Objects.nonNull(filterRequestsRq.getClientSurname()) && Objects.nonNull(filterRequestsRq.getClientName())) {
            Join<RequestEntity, ClientEntity> topicClients = report.join("client");
            predicates.add(cb.like(cb.upper(topicClients.get("surName")), filterRequestsRq.getClientSurname().toUpperCase().trim() + '%'));
            predicates.add(cb.like(cb.upper(topicClients.get("firstName")), filterRequestsRq.getClientName().toUpperCase().trim() + '%'));
        } else if (Objects.nonNull(filterRequestsRq.getClientSurname())) {
            Join<RequestEntity, ClientEntity> topicClients = report.join("client");
            predicates.add(cb.like(cb.upper(topicClients.get("surName")), filterRequestsRq.getClientSurname().toUpperCase().trim() + '%'));
        } else if (Objects.nonNull(filterRequestsRq.getClientName())) {
            Join<RequestEntity, ClientEntity> topicClients = report.join("client");
            predicates.add(cb.like(cb.upper(topicClients.get("firstName")), filterRequestsRq.getClientName().toUpperCase().trim() + '%'));
        }
        if (Objects.nonNull(filterRequestsRq.getStatus())) {
            predicates.add(cb.equal(report.get("status"), filterRequestsRq.getStatus()));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        List<RequestEntity> responseList = em.createQuery(cq).getResultList();
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > responseList.size() ? responseList.size() : (start + pageable.getPageSize());
        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

}
