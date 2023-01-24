package com.vr.project.dto;

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
}
