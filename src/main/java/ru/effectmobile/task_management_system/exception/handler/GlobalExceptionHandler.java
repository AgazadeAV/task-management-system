package ru.effectmobile.task_management_system.exception.handler;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.DecodingException;
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
import ru.effectmobile.task_management_system.exception.custom.BusinessException;
import ru.effectmobile.task_management_system.exception.util.ExceptionResponseUtil.ErrorResponseFormat;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ACCESS_DENIED_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.INVALID_CREDENTIALS_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.JWT_DECODING_ERROR_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.JWT_ERROR_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.MALFORMED_JSON_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getConstraintViolationMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getDataIntegrityViolationMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getExceptionMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getHandlerMethodValidationMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getMethodArgumentNotValidMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.getMissingRequestHeaderMessage;
import static ru.effectmobile.task_management_system.exception.util.ExceptionResponseUtil.buildErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseFormat> handleBusinessException(BusinessException ex) {
        return buildErrorResponse(ex.getHttpStatus(), ex.getMessage());
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
        return buildErrorResponse(HttpStatus.BAD_REQUEST, getHandlerMethodValidationMessage(ex));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseFormat> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, getMissingRequestHeaderMessage(ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseFormat> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, getDataIntegrityViolationMessage(ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseFormat> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, getExceptionMessage(ex, ACCESS_DENIED_MESSAGE));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseFormat> handleBadCredentialsException(BadCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, getExceptionMessage(ex, INVALID_CREDENTIALS_MESSAGE));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseFormat> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, getExceptionMessage(ex, USER_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseFormat> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, getExceptionMessage(ex, MALFORMED_JSON_MESSAGE));
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ErrorResponseFormat> handleJwtDecodingException(DecodingException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, getExceptionMessage(ex, JWT_DECODING_ERROR_MESSAGE));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseFormat> handleJwtException(JwtException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, getExceptionMessage(ex, JWT_ERROR_MESSAGE));
    }
}
