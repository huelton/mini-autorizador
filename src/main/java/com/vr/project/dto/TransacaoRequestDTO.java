package com.vr.project.dto;

import java.math.BigDecimal;

import com.vr.project.model.Transacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequestDTO {

	private Integer id;
	private String numeroCartao;
	private String senha;
	private BigDecimal valor =  new BigDecimal("500.00");
}
