package com.vr.project.service;

import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;

public interface CartaoService {
	
	public CartaoResponseDTO salvarCartao(CartaoRequestDTO dto);
	
	public CartaoResponseDTO retornarDadosPeloCartao(String cartao);

}
