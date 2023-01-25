package com.vr.project.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequestDTO {

	@NotNull
	@NotBlank
	private String numeroCartao;
	@NotNull
	@NotBlank
	private String senha;
}
