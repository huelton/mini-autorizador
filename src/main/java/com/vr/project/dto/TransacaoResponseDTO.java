package com.vr.project.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoResponseDTO {

	private Integer id;
	private String numeroCartao;
	private String senha;
	private BigDecimal valor;
	
}
