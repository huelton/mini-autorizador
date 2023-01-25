package com.vr.project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Violation {

	private final String fieldName;
	private final String message;
	
	
}