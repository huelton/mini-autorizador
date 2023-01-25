package com.vr.project.dto;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequestDTO {

	@NotNull
	@NotBlank
	private String numeroCartao;
	@NotNull
	@NotBlank
	private String senha;
	@NotNull
	@NotBlank
	private BigDecimal valor;
	@JsonIgnore
	private Instant dataTransacao = Instant.now();
}
