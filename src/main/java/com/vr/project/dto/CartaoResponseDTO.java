package com.vr.project.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponseDTO {

	@JsonIgnore
	private Integer id;
	private String numeroCartao;
	private String senha;
	private BigDecimal valor;
	
}
