package ru.softlab.efr.services.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.converter.RequestsConverter;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.services.RequestService;

import javax.validation.Valid;
import java.util.Objects;

@RestController
public class RequestController implements RequestApi {

    private RequestService requestService;
    private RequestsConverter requestsConverter;

    @Autowired
    public RequestController(RequestService requestService,
                             RequestsConverter requestsConverter) {
        this.requestService = requestService;
        this.requestsConverter = requestsConverter;
    }

    @Override
    public ResponseEntity<Void> processingClientRequest(@PathVariable("requestId") Long requestId,
                                                          @Valid @RequestBody ProcessingClientRequestRq body) throws Exception {
        requestService.processing(requestId, RequestStatus.valueOf(body.getStatus().name()), body.getInfo(), body.getClientComment());
        return ResponseEntity.ok().build();
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_VIEW)
    public ResponseEntity<Page<UserRequestResponse>> getClientRequestList(@PageableDefault(value = 50) Pageable pageable) {
        return ResponseEntity.ok().body(requestService.getUserReportsDataPaginated(pageable).map(requestsConverter::toUserRequestResponse));
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_VIEW)
    public ResponseEntity<ClientRequestInfo> getRequestById(@PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok().body(requestsConverter.toClientRequestInfo(requestService.getRequestById(requestId)));
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_PROCESSING)
    public ResponseEntity<String> closeRequest(@PathVariable("requestId") Long requestId) {
        return requestService.closeReport(requestId);
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_VIEW)
    public ResponseEntity<ClientRequestInfoForAdmin> getRequestByIdForAdmin(@PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok().body(requestsConverter.toClientRequestInfoForAdmin(requestService.getRequestById(requestId)));
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_CREATE)
    public ResponseEntity<ClientRequestInfo> addRequest(@Valid @RequestBody ClientRequestRq clientRequestRq) {
        RequestEntity request = requestService.addRequest(clientRequestRq);
        if (Objects.nonNull(request)) {
            return ResponseEntity.ok().body(requestsConverter.toClientRequestInfo(request));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Override
    @HasRight(Right.CLIENT_REQUEST_PROCESSING)
    public ResponseEntity<Page<ClientRequestInfoForAdmin>> getRequestPaginatedList(@PageableDefault(value = 50) Pageable pageable,
                                                                                   @Valid @RequestBody FilterRequestsRq filterData,
                                                                                   @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) {
        return ResponseEntity.ok().body(requestService.getRequestPaginatedList(pageable, filterData).map(requestsConverter::toClientRequestInfoForAdmin));
    }

}
