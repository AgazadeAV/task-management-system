package ru.effectmobile.task_management_system.controller.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.effectmobile.task_management_system.exception.NotFoundException;
import ru.effectmobile.task_management_system.util.ExceptionResponseUtil.ErrorResponseFormat;

import static ru.effectmobile.task_management_system.util.ExceptionMessageUtil.getConstraintViolationMessage;
import static ru.effectmobile.task_management_system.util.ExceptionMessageUtil.getMethodArgumentNotValidMessage;
import static ru.effectmobile.task_management_system.util.ExceptionResponseUtil.buildErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseFormat> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseFormat> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, getMethodArgumentNotValidMessage(ex));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseFormat> handleConstraintViolationException(ConstraintViolationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, getConstraintViolationMessage(ex));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseFormat> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseFormat> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
