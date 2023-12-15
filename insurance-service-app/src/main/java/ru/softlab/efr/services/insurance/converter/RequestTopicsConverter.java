package ru.softlab.efr.services.insurance.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.TopicRequestEntity;
import ru.softlab.efr.services.insurance.model.rest.AvailableTopicFormat;
import ru.softlab.efr.services.insurance.model.rest.BaseTopicsModel;

@Service
public class RequestTopicsConverter {

    public AvailableTopicFormat toAvailableTopicFormat(TopicRequestEntity topicRequestEntity) {
        AvailableTopicFormat topicFormat = new AvailableTopicFormat();
        topicFormat.setId(topicRequestEntity.getId());
        topicFormat.setName(topicRequestEntity.getName());
        topicFormat.setAbout(topicRequestEntity.getDescription());
        topicFormat.setActive(topicRequestEntity.isActive());
        return topicFormat;
    }

    public BaseTopicsModel toBaseTopicsModel(TopicRequestEntity topicRequestEntity) {
        BaseTopicsModel baseTopicsModel = new BaseTopicsModel();
        baseTopicsModel.setId(topicRequestEntity.getId());
        baseTopicsModel.setName(topicRequestEntity.getName());
        baseTopicsModel.setTopicDescription(topicRequestEntity.getDescription());
        baseTopicsModel.setEmail(topicRequestEntity.getEmail());
        baseTopicsModel.setIsActive(topicRequestEntity.isActive());
        return baseTopicsModel;
    }
}
