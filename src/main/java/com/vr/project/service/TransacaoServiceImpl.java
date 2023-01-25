package com.vr.project.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.exception.TransacaoException;
import com.vr.project.mapper.TransacaoMapper;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;
import com.vr.project.repository.TransacaoRepository;

@Service
public class TransacaoServiceImpl implements TransacaoService {

	private final TransacaoRepository transacaoRepository;
	private final CartaoRepository cartaoRepository;

	public TransacaoServiceImpl(TransacaoRepository transacaoRepository, CartaoRepository cartaoRepository) {
		this.transacaoRepository = transacaoRepository;
		this.cartaoRepository = cartaoRepository;
	}

	@Override
	public TransacaoResponseDTO salvarTransacao(TransacaoRequestDTO dto) {
		
		Optional<Cartao> cartao = cartaoRepository.findByNumeroCartao(dto.getNumeroCartao());
		cartao.orElseThrow(() -> new TransacaoException("CARTAO_INEXISTENTE"));
		
		validaSenha(dto.getSenha(), cartao.get().getSenha());
		verificaLimiteValor(dto.getValor(), cartao.get().getValor());
		
		BigDecimal saldo =  cartao.get().getValor().subtract(dto.getValor());
		cartao.get().setValor(saldo);
		cartaoRepository.save(cartao.get());
		
		var entity = TransacaoMapper.INTANCE.requestDTOToEntity(dto);
		
		transacaoRepository.save(entity);
		var response = TransacaoMapper.INTANCE.entityToResponseDTO(entity);
		return response;
	}

	private void validaSenha(String senhaDto, String senhaCartao) {
		if(!senhaDto.equals(senhaCartao)) throw new TransacaoException("SENHA_INVALIDA");		
	}

	private void verificaLimiteValor(BigDecimal valorDto, BigDecimal valorCartao) {
		if(valorDto.compareTo(valorCartao) > 0) throw new TransacaoException("SALDO_INSUFICIENTE");
	}

}
