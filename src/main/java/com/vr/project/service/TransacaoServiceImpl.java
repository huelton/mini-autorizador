package com.vr.project.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.exception.TransacaoException;
import com.vr.project.mapper.TransacaoMapper;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;
import com.vr.project.repository.TransacaoRepository;
import com.vr.project.service.rule.TransacaoRule;

@Service
public class TransacaoServiceImpl implements TransacaoService {

	private final TransacaoRepository transacaoRepository;
	private final CartaoRepository cartaoRepository;
	private final TransacaoRule rule;

	public TransacaoServiceImpl(TransacaoRepository transacaoRepository, CartaoRepository cartaoRepository,
			TransacaoRule rule) {
		this.transacaoRepository = transacaoRepository;
		this.cartaoRepository = cartaoRepository;
		this.rule = rule;
	}

	@Override
	@Transactional
	public TransacaoResponseDTO salvarTransacao(TransacaoRequestDTO dto) {

		Optional<Cartao> cartaoOptional = cartaoRepository.findByNumeroCartao(dto.getNumeroCartao());
		cartaoOptional.orElseThrow(() -> new TransacaoException("CARTAO_INEXISTENTE"));
		var cartao = cartaoOptional.get();

		rule.validaSenha(dto.getSenha(), cartao.getSenha());
		rule.verificaLimiteValor(dto.getValor(), cartao.getValor());

		BigDecimal saldo = cartao.getValor().subtract(dto.getValor());
		cartao.setValor(saldo);
		cartaoRepository.save(cartao);

		var transacao = TransacaoMapper.INTANCE.requestDTOToEntity(dto);

		transacaoRepository.save(transacao);
		var response = TransacaoMapper.INTANCE.entityToResponseDTO(transacao);
		return response;
	}

}
