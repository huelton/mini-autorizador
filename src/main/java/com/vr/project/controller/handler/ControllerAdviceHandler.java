package com.vr.project.controller.handler;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.vr.project.exception.CartaoException;
import com.vr.project.exception.ErrorConstraint;
import com.vr.project.exception.ErrorResponse;
import com.vr.project.exception.StandardError;
import com.vr.project.exception.TransacaoException;
import com.vr.project.exception.ValidationErrorResponse;
import com.vr.project.exception.Violation;

@ControllerAdvice
public class ControllerAdviceHandler {
	
	@ExceptionHandler(CartaoException.class)
	public ResponseEntity<StandardError> cartaoError(CartaoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(TransacaoException.class)
	public ResponseEntity<StandardError> transacaoError(TransacaoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> cartaoErrorDataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgument(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErrorResponse err = new ErrorResponse();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Validated exceptions");
		ValidationErrorResponse error = new ValidationErrorResponse();
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        	error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
		err.setViolations(error);
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorConstraint> contrainViolation(ConstraintViolationException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErrorConstraint err = new ErrorConstraint();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Constraint Error");
		err.setPath(request.getRequestURI());
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

		err.setDetails(constraintViolations.stream()
		        .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
		        .collect(Collectors.toList()));

		return ResponseEntity.status(status).body(err);
	    }
}
