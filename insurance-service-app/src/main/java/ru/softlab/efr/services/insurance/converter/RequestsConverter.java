package ru.softlab.efr.services.insurance.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.RequestEntity;
import ru.softlab.efr.services.insurance.model.rest.ClientRequestInfoForAdmin;
import ru.softlab.efr.services.insurance.model.rest.ClientRequestInfo;
import ru.softlab.efr.services.insurance.model.rest.UserRequestResponse;

import java.util.Objects;

@Service
public class RequestsConverter {

    private RequestTopicsConverter requestTopicsConverter;

    @Autowired
    RequestsConverter(RequestTopicsConverter requestTopicsConverter) {
        this.requestTopicsConverter = requestTopicsConverter;
    }

    public ClientRequestInfoForAdmin toClientRequestInfoForAdmin(RequestEntity requestEntity) {
        ClientRequestInfoForAdmin clientRequestInfoForAdmin = new ClientRequestInfoForAdmin();
        clientRequestInfoForAdmin.setId(requestEntity.getId());
        clientRequestInfoForAdmin.setRequestDate(requestEntity.getRequestDate().toLocalDateTime().toLocalDate());
        clientRequestInfoForAdmin.setRequestsTopic(requestTopicsConverter.toAvailableTopicFormat(requestEntity.getTopic()));
        if (Objects.nonNull(requestEntity.getProduct())) {
            clientRequestInfoForAdmin.setProgramId(requestEntity.getProduct().getId());
            clientRequestInfoForAdmin.setProgramName(requestEntity.getProduct().getName());
        }
        if (Objects.nonNull(requestEntity.getInsurance())) {
            clientRequestInfoForAdmin.setInsuranceId(requestEntity.getInsurance().getId());
            clientRequestInfoForAdmin.setInsuranceNumber(requestEntity.getInsurance().getContractNumber());
        }
        clientRequestInfoForAdmin.setStatus(requestEntity.getStatus());
        clientRequestInfoForAdmin.setClientId(requestEntity.getClient().getId());
        clientRequestInfoForAdmin.setClientFirstname(requestEntity.getClient().getFirstName());
        clientRequestInfoForAdmin.setClientSurname(requestEntity.getClient().getSurName());
        clientRequestInfoForAdmin.setClientMiddleName(requestEntity.getClient().getMiddleName());
        clientRequestInfoForAdmin.setClientBirthDate(requestEntity.getClient().getBirthDate());
        clientRequestInfoForAdmin.setPhone(requestEntity.getPhone());
        clientRequestInfoForAdmin.setEmail(requestEntity.getEmail());
        clientRequestInfoForAdmin.setRequestText(requestEntity.getRequestText());
        clientRequestInfoForAdmin.setAdditionalInfo(requestEntity.getAdditionalInfo());
        clientRequestInfoForAdmin.setIsActive(requestEntity.getIsActive());
        clientRequestInfoForAdmin.setClientComment(requestEntity.getComment());
        if (requestEntity.getDocument() != null) {
            clientRequestInfoForAdmin.setDocumentId(requestEntity.getDocument().getId());
            clientRequestInfoForAdmin.setDocumentName(requestEntity.getDocument().getAttachment().getFileName());
        }
        return clientRequestInfoForAdmin;
    }

    public ClientRequestInfo toClientRequestInfo(RequestEntity requestEntity) {
        ClientRequestInfo clientRequestInfo = new ClientRequestInfo();
        clientRequestInfo.setId(requestEntity.getId());
        clientRequestInfo.setRequestDate(requestEntity.getRequestDate().toLocalDateTime().toLocalDate());
        clientRequestInfo.setRequestsTopic(requestTopicsConverter.toAvailableTopicFormat(requestEntity.getTopic()));
        if (Objects.nonNull(requestEntity.getProduct())) {
            clientRequestInfo.setProgramId(requestEntity.getProduct().getId());
            clientRequestInfo.setProgramName(requestEntity.getProduct().getName());
        }
        if (Objects.nonNull(requestEntity.getInsurance())) {
            clientRequestInfo.setInsuranceId(requestEntity.getInsurance().getId());
            clientRequestInfo.setInsuranceNumber(requestEntity.getInsurance().getContractNumber());
        }
        clientRequestInfo.setStatus(requestEntity.getStatus());
        clientRequestInfo.setPhone(requestEntity.getPhone());
        clientRequestInfo.setEmail(requestEntity.getEmail());
        clientRequestInfo.setRequestText(requestEntity.getRequestText());
        clientRequestInfo.setIsActive(requestEntity.getIsActive());
        clientRequestInfo.setClientComment(requestEntity.getComment());
        if (requestEntity.getDocument() != null) {
            clientRequestInfo.setDocumentId(requestEntity.getDocument().getId());
            clientRequestInfo.setDocumentName(requestEntity.getDocument().getAttachment().getFileName());
        }
        return clientRequestInfo;
    }

    public UserRequestResponse toUserRequestResponse(RequestEntity requestEntity) {
        UserRequestResponse userRequestResponse = new UserRequestResponse();
        userRequestResponse.setId(requestEntity.getId());
        userRequestResponse.setRequestDate(requestEntity.getRequestDate().toLocalDateTime().toLocalDate());
        userRequestResponse.setRequestsTopic(requestTopicsConverter.toAvailableTopicFormat(requestEntity.getTopic()));
        userRequestResponse.setStatus(requestEntity.getStatus());
        userRequestResponse.setPhone(requestEntity.getPhone());
        if (Objects.nonNull(requestEntity.getInsurance())) {
            userRequestResponse.setInsuranceId(requestEntity.getInsurance().getId());
            userRequestResponse.setInsuranceNumber(requestEntity.getInsurance().getContractNumber());
        }
        return userRequestResponse;
    }
}
