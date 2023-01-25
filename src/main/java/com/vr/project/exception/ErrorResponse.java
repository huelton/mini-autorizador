package com.vr.project.exception;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private Instant timeStamp;
	private Integer status;
	private String error;
	private String path;
	private ValidationErrorResponse violations;
	
}