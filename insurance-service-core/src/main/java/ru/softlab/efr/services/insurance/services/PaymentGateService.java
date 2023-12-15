package ru.softlab.efr.services.insurance.services;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.softlab.efr.services.insurance.model.db.OrderEntity;

import java.util.Objects;


/**
 * Сервис осуществляющий функции интеграции с платёжным шлюзом.
 *
 * @author olshansky
 * @since 20.02.2019
 */
@Service
@PropertySource(value = {"classpath:acquiring.properties"}, encoding = "UTF-8")
public class PaymentGateService {

    private static final Logger LOGGER = Logger.getLogger(PaymentGateService.class);

    private static final String CREATE_ORDER_METHOD_NAME = "register.do";
    private static final String GET_ORDER_STATUS_METHOD_NAME = "getOrderStatus.do";

    @Value("${api.address}")
    private String ADDRESS;

    @Value("${api.token}")
    private String TOKEN;

    @Value("${api.login}")
    private String LOGIN;
    @Value("${api.pswd}")
    private String PASSWORD;

    @Value("${language.code}")
    private String LANGUAGE;

    private final RestTemplate restTemplate;

    @Autowired
    public PaymentGateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OrderEntity createOrder(OrderEntity orderEntity, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String redirect = url.concat("/").concat(orderEntity.getId().toString());
        //map.add("token", TOKEN);
        map.add("userName", LOGIN);
        map.add("password", PASSWORD);
        map.add("orderNumber", orderEntity.getId().toString());
        map.add("amount", orderEntity.getAmount().toString());
        map.add("currency", orderEntity.getCurrency().toString());
        map.add("returnUrl", redirect);
        map.add("failUrl", redirect);
        map.add("description", "Платёж из Фронтальной Офисной Системы РСХБ-Страхование");
        map.add("language", LANGUAGE);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            LOGGER.info(String.format("Выполнение запроса к платёжному шлюзу: %s", request.getBody()));
            ResponseEntity<String> response = restTemplate.postForEntity(ADDRESS.concat(CREATE_ORDER_METHOD_NAME), request, String.class);
            LOGGER.info(String.format("Получен ответ от платёжного шлюза: http-код: %s, тело: %s", response.getStatusCodeValue(), response.getBody()));

            orderEntity.setExtId(getValueFromJson(response.getBody(), "$.orderId"));
            orderEntity.setRedirectUrl(getValueFromJson(response.getBody(), "$.formUrl"));
            Integer errorCode = getValueFromJson(response.getBody(), "$.errorCode");

            if (!Objects.isNull(errorCode)) {
                orderEntity.setErrorCode(errorCode);
            }
            orderEntity.setErrorMessage(getValueFromJson(response.getBody(), "$.errorMessage"));

        } catch (RestClientException ex) {
            LOGGER.error("Произошла ошибка во время выполнения запроса создания заказа к платёжному шлюзу, причина: ", ex);
        }
        return orderEntity;
    }

    public OrderEntity getOrderStatus(OrderEntity orderEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //map.add("token", TOKEN);
        map.add("userName", LOGIN);
        map.add("password", PASSWORD);
        map.add("orderId", orderEntity.getExtId());
        map.add("language", LANGUAGE);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(ADDRESS.concat(GET_ORDER_STATUS_METHOD_NAME), request, String.class);
            LOGGER.info(String.format("Получен ответ от платёжного шлюза: http-код: %s, тело: %s", response.getStatusCodeValue(), response.getBody()));

            orderEntity.setOrderCode(getValueFromJson(response.getBody(), "$.OrderStatus"));
            String errorCode = getValueFromJson(response.getBody(), "$.ErrorCode");
            if (StringUtils.isNotBlank(errorCode)) {
                orderEntity.setErrorCode(Integer.parseInt(errorCode));
            }
            orderEntity.setErrorMessage(getValueFromJson(response.getBody(), "$.ErrorMessage"));
            orderEntity.setPan(getValueFromJson(response.getBody(), "$.Pan"));
            orderEntity.setExpiration(getValueFromJson(response.getBody(), "$.expiration"));
            orderEntity.setCardHolderName(getValueFromJson(response.getBody(), "$.cardholderName"));
            Integer amount = getValueFromJson(response.getBody(), "$.Amount");
            if (amount != null) {
                orderEntity.setAmount(Long.valueOf(amount));
            }
            String currency = getValueFromJson(response.getBody(), "$.currency");
            if (StringUtils.isNotBlank(currency)) {
                orderEntity.setCurrency(Integer.parseInt(currency));
            }
            orderEntity.setIp(getValueFromJson(response.getBody(), "$.Ip"));
            return orderEntity;

        } catch (RestClientException ex) {
            LOGGER.error("Произошла ошибка во время выполнения запроса получения информации о статусе заказа к платёжному шлюзу, причина: ", ex);
        }
        return null;
    }

    private <T> T getValueFromJson(String json, String jsonPathExpression) {
        JsonPath jsonPath = JsonPath.compile(jsonPathExpression);
        try {
            return jsonPath.read(json);
        } catch (PathNotFoundException ex) {
            return null;
        }
    }
}
