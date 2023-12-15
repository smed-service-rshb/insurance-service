package ru.softlab.efr.services.insurance.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.softlab.efr.services.auth.exceptions.ConnectActiveDirectoryException;
import ru.softlab.efr.services.auth.exceptions.TimeoutActiveDirectoryException;
import ru.softlab.efr.services.insurance.exception.AcquiringException;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.rest.ErrorModel;

import javax.transaction.NotSupportedException;
import java.util.Arrays;
import java.util.List;

/**
 * Обработчик не контролируемых контроллером исключений.
 *
 * @author prihodskiy
 * @since 03.09.2018
 */
@RestControllerAdvice
@Service(value = "InsGlobalExceptionHandler")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    ResponseEntity handleException(final Throwable exception) {
        logger.error("Ошибка обработки запроса", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final TimeoutActiveDirectoryException exception) {
        logger.error("Таймаут подключения к AD.", exception);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final ConnectActiveDirectoryException exception) {
        logger.error("Ошибка соединения с AD.", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final ValidationException exception) {
        logger.error("Ошибка при валидации данных запроса", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(exception.getErrorMessages()));
    }


    @ExceptionHandler
    ResponseEntity handleException(final NotSupportedException exception) {
        logger.error("Ошибка при валидации данных запроса", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(Arrays.asList(exception.getMessage())));
    }
    @ExceptionHandler
    ResponseEntity handleException(final AcquiringException exception) {
        logger.error("Ошибка при оформлении договора", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(Arrays.asList(exception.getMessage())));
    }
    @ExceptionHandler
    ResponseEntity handleException(final IllegalStateException exception) {
        logger.error("Ошибка при получении договора", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.warn("Несоответствие запроса допустимому формату.", ex);
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.error("Ошибка валидации.", ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorModel errorData = processFieldErrors(fieldErrors);
        return handleExceptionInternal(ex, errorData, headers, HttpStatus.BAD_REQUEST, request);
    }

    private ErrorModel processFieldErrors(List<FieldError> fieldErrors) {
        ErrorModel dto = new ErrorModel();
        for (FieldError fieldError: fieldErrors) {
            String error = fieldError.getDefaultMessage();
            dto.addErrorsItem(error);
        }
        return dto;
    }
}
