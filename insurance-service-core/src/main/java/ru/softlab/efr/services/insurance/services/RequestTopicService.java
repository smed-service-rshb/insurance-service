package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.model.db.TopicRequestEntity;
import ru.softlab.efr.services.insurance.model.rest.BaseTopicsModel;
import ru.softlab.efr.services.insurance.repositories.TopicRequestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RequestTopicService {

    private TopicRequestRepository topicRequestRepository;
    private PrincipalDataSource principalDataSource;

    @Autowired
    public RequestTopicService(TopicRequestRepository topicRequestRepository, PrincipalDataSource principalDataSource) {
        this.topicRequestRepository = topicRequestRepository;
        this.principalDataSource = principalDataSource;
    }

    public List<TopicRequestEntity> getTopics() {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        List<TopicRequestEntity> availableTopicFormats;
        if (principalData.getRights().contains(Right.EDIT_CLIENT_REPORT_TOPICS)){
            availableTopicFormats = topicRequestRepository.findAll();

        } else {
            availableTopicFormats = topicRequestRepository.getByIsActive(true);
        }
        return availableTopicFormats;
    }

    public ResponseEntity<String> updateTopic(BaseTopicsModel updateTopicRequest) {
        TopicRequestEntity topic = Optional.ofNullable(topicRequestRepository.getById(updateTopicRequest.getId()))
                .orElse(new TopicRequestEntity());
        topic.setName(updateTopicRequest.getName());
        topic.setDescription(updateTopicRequest.getTopicDescription());
        topic.setEmail(updateTopicRequest.getEmail());
        topic.setActive(updateTopicRequest.isIsActive());
        topicRequestRepository.save(topic);
        return ResponseEntity.ok().body(String.format("Сохранена тема %s", topic.getName()));
    }

    public Page<TopicRequestEntity> getTopicsPagenated(Pageable pageable) {
        return topicRequestRepository.findAll(pageable);
    }

    public TopicRequestEntity gettopicById(long id) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        if (principalData.getRights().contains(Right.EDIT_CLIENT_REPORT_TOPICS)) {
            return topicRequestRepository.getById(id);
        } else {
            return topicRequestRepository.getByIdAndIsActive(id, true);
        }
    }
}
