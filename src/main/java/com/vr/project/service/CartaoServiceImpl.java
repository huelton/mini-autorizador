package com.vr.project.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.mapper.CartaoMapper;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;

@Service
public class CartaoServiceImpl implements CartaoService {

	private final CartaoRepository cartaoRepository;
	
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal("500.00");

	public CartaoServiceImpl(CartaoRepository cartaoRepository) {
		this.cartaoRepository = cartaoRepository;
	}

	@Override
	public CartaoResponseDTO salvarCartao(CartaoRequestDTO dto) {
		var request = CartaoMapper.INTANCE.requestDTOToEntity(dto);
		request.setValor(VALOR_DEFAULT);
		cartaoRepository.save(request);
		var response = CartaoMapper.INTANCE.entityToResponseDTO(request);
		return response;
	}

	@Override
	public CartaoResponseDTO retornarDadosPeloCartao(String numeroCartao) {
		Optional<Cartao> tr = cartaoRepository.findByNumeroCartao(numeroCartao);
		tr.orElseThrow(() -> new RuntimeException("Numero de cartão não encontrado"));
		var response = CartaoMapper.INTANCE.entityToResponseDTO(tr.get());

		return response;
	}

}
