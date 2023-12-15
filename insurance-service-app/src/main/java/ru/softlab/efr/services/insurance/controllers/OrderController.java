package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.insurance.model.db.OrderEntity;
import ru.softlab.efr.services.insurance.model.rest.CreateOrderResponse;
import ru.softlab.efr.services.insurance.model.rest.OrderData;
import ru.softlab.efr.services.insurance.services.OrderService;
import ru.softlab.efr.services.insurance.services.PaymentGateService;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import javax.validation.Valid;

/**
 * Контроллер для работы с заказами на оплату договоров страхования.
 *
 * @author olshansky
 * @since 20.02.2019
 */
@RestController
public class OrderController implements OrderApi {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class);

    private final OrderService orderService;
    private final PaymentGateService paymentGateService;

    @Autowired
    public OrderController(OrderService orderService, PaymentGateService paymentGateService) {
        this.orderService = orderService;
        this.paymentGateService = paymentGateService;
    }

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody OrderData createOrder) throws Exception {
        OrderEntity orderEntity = paymentGateService.createOrder(orderService.create(createOrder), createOrder.getUrl());
        orderService.update(orderEntity.getId(), orderEntity);
        CreateOrderResponse response = new CreateOrderResponse();
        if (orderEntity.getErrorCode() != null && !orderEntity.getErrorCode().equals(0)) {
            response.setErrorMessage(orderEntity.getErrorMessage());
        } else {
            response.setRedirectUrl(orderEntity.getRedirectUrl());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OrderData> getOrder(@PathVariable("id") Long id) throws Exception {
        OrderEntity orderBack = orderService.getById(id);
        if (orderBack == null) {
            LOGGER.error(String.format("Заказ с ID=%s не найден!", id));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(mapBack2FrontOrder(orderBack));
    }

    @Override
    public ResponseEntity<Page<OrderData>> getOrderList(Pageable pageable) throws Exception {
        return ResponseEntity.ok(orderService.getAll(pageable).map(this::mapBack2FrontOrder));
    }

    private OrderData mapBack2FrontOrder(OrderEntity orderEntity) {
        OrderData orderData = new OrderData();
        if (orderEntity != null) {
            if (StringUtils.isNotBlank(orderEntity.getExtId())) {
                orderEntity = paymentGateService.getOrderStatus(orderEntity);
                orderService.update(orderEntity.getId(), orderEntity);
            }
            AppUtils.mapSimilarObjects(orderEntity, orderData);
        }
        return orderData;
    }

    @Override
    public ResponseEntity<Void> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderData orderData) throws Exception {
        OrderEntity orderBack = orderService.getById(id);
        AppUtils.mapSimilarObjects(orderData, orderBack);
        orderService.update(id, orderBack);
        return ResponseEntity.ok().build();
    }
}
