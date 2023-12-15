package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.converter.RequestTopicsConverter;
import ru.softlab.efr.services.insurance.model.db.TopicRequestEntity;
import ru.softlab.efr.services.insurance.model.rest.AvailableTopicFormat;
import ru.softlab.efr.services.insurance.model.rest.AvailableTopics;
import ru.softlab.efr.services.insurance.model.rest.BaseTopicsModel;
import ru.softlab.efr.services.insurance.services.RequestTopicService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RequestsTopicsController implements RequestsTopicsApi {

    private RequestTopicService requestTopicService;
    private RequestTopicsConverter requestTopicsConverter;

    @Autowired
    public RequestsTopicsController(RequestTopicService requestTopicService,
                                    RequestTopicsConverter requestTopicsConverter) {
        this.requestTopicService = requestTopicService;
        this.requestTopicsConverter = requestTopicsConverter;
    }

    @Override
    public ResponseEntity<AvailableTopics> available() throws Exception {
        List<TopicRequestEntity> reports = requestTopicService.getTopics();
        AvailableTopics availableTopics = new AvailableTopics();
        availableTopics.setCount((long) reports.size());
        availableTopics.setTopics(reports
                .stream()
                .map(requestTopicsConverter::toAvailableTopicFormat)
                .collect(Collectors.toList()));
        return ResponseEntity.ok().body(availableTopics);
    }

    @Override
    public ResponseEntity<BaseTopicsModel> availableById(@PathVariable("topicId") Long topicId) throws Exception {
        return ResponseEntity.ok().body(requestTopicsConverter.toBaseTopicsModel(requestTopicService.gettopicById(topicId)));
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_REPORT_TOPICS)
    public ResponseEntity<Page<AvailableTopicFormat>> availablePaginated(@PageableDefault(value = 50) Pageable pageable) throws Exception {
        return ResponseEntity.ok(requestTopicService.getTopicsPagenated(pageable).map(requestTopicsConverter::toAvailableTopicFormat));
    }

    @Override
    @HasRight(Right.EDIT_CLIENT_REPORT_TOPICS)
    public ResponseEntity<String> availableSet(@Valid @RequestBody BaseTopicsModel updateTopicRequest) throws Exception {
        return requestTopicService.updateTopic(updateTopicRequest);
    }

}
