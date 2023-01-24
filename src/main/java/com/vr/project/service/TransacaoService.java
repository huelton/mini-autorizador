package com.vr.project.service;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;

public interface TransacaoService {
	
	public void salvarTransacao(TransacaoRequestDTO dto);
	
	public TransacaoResponseDTO retornarDadosTransacao(TransacaoRequestDTO dto);

}
