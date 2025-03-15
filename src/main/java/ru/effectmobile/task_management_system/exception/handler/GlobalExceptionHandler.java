package ru.effectmobile.task_management_system.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.effectmobile.task_management_system.exception.AlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.InvalidEnumValueException;
import ru.effectmobile.task_management_system.exception.NotFoundException;
import ru.effectmobile.task_management_system.exception.PasswordDoesNotMatchException;
import ru.effectmobile.task_management_system.exception.util.ExceptionResponseUtil.ErrorResponseFormat;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ACCESS_DENIED_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.DB_CONSTRAINT_VIOLATION_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.INVALID_CREDENTIALS_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.MALFORMED_JSON_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.MISSING_HEADER_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getConstraintViolationMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getMethodArgumentNotValidMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionResponseUtil.buildErrorResponse;

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

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponseFormat> handleInvalidEnumValueException(InvalidEnumValueException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseFormat> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ACCESS_DENIED_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseFormat> handleBadCredentialsException(BadCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseFormat> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, USER_NOT_FOUND_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public ResponseEntity<ErrorResponseFormat> handlePasswordDoesNotMatchException(PasswordDoesNotMatchException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseFormat> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, DB_CONSTRAINT_VIOLATION_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseFormat> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, MALFORMED_JSON_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseFormat> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_HEADER_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(AlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseFormat> handleAlreadyRegisteredException(AlreadyRegisteredException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseFormat> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
