package com.vr.project.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.mapper.TransacaoMapper;
import com.vr.project.model.Transacao;
import com.vr.project.repository.TransacaoRepository;

@Service
public class TransacaoServiceImpl implements TransacaoService {

	private final TransacaoRepository transacaoRepository;

	public TransacaoServiceImpl(TransacaoRepository transacaoRepository) {
		this.transacaoRepository = transacaoRepository;
	}

	@Override
	public TransacaoResponseDTO salvarTransacao(TransacaoRequestDTO dto) {
		var request = TransacaoMapper.INTANCE.requestDTOToEntity(dto);
		request.setValor(new BigDecimal("500.00"));
		transacaoRepository.save(request);
		var response = TransacaoMapper.INTANCE.entityToResponseDTO(request);

		return response;
	}

	@Override
	public TransacaoResponseDTO retornarDadosPeloCartao(String numeroCartao) {
		Optional<Transacao> tr = transacaoRepository.findByNumeroCartao(numeroCartao);
		tr.orElseThrow(() -> new RuntimeException("Numero de cartão não encontrado"));
		var response = TransacaoMapper.INTANCE.entityToResponseDTO(tr.get());

		return response;
	}

	@Override
	public TransacaoResponseDTO debitaCreditoTransacao(TransacaoRequestDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
