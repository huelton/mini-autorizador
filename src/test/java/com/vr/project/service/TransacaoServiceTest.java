package com.vr.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.exception.TransacaoException;
import com.vr.project.model.Cartao;
import com.vr.project.model.Transacao;
import com.vr.project.repository.CartaoRepository;
import com.vr.project.repository.TransacaoRepository;

public class TransacaoServiceTest {

	@InjectMocks
	private TransacaoServiceImpl transacaoService;
	
	@InjectMocks
	private CartaoServiceImpl cartaoService;

	@Mock
	private TransacaoRepository transacaoRepository;
	
	@Mock
	private CartaoRepository cartaoRepository;

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "1234";
	private static final String SENHA = "4321";
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal("500.00");
	private static final Instant DATA_TRANSACAO = Instant.now();
	private static final BigDecimal VALOR = new BigDecimal("500.00");
	private static final BigDecimal SEM_SALDO = new BigDecimal(-1.00);

	private static final Integer ID_INVALIDO = 9999;
	private static final String TRANSACAO_EXCEPTION_ID_INVALIDO = "CARTAO_INEXISTENTE";
	private static final String TRANSACAO_EXCEPTION_SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
	private static final String TRANSACAO_EXCEPTION_SENHA_INVALIDA = "SENHA_INVALIDA";
	
    private Cartao cartao;
	private Transacao transacaoInvalida;
	private TransacaoResponseDTO transacaoResponseDTO;
	private TransacaoRequestDTO transacaoRequestDTO;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		instanciaTransacao();
	}

	@Test
	public void quandoSalvarDeveReturnarDadosDaTransacao() {
		when(cartaoRepository.findByNumeroCartao(transacaoRequestDTO.getNumeroCartao())).thenReturn(Optional.of(cartao));

		transacaoResponseDTO = transacaoService.salvarTransacao(transacaoRequestDTO);

		assertNotNull(transacaoResponseDTO);

		assertEquals(NUMERO_CARTAO, transacaoResponseDTO.getNumeroCartao());
		assertEquals(SENHA, transacaoResponseDTO.getSenha());
		assertEquals(VALOR_DEFAULT, transacaoResponseDTO.getValor());
	}	

	@Test 
	public void quandoTentarSalvarTransacaoDeveLancarExececaoSeValorNegativo() {
		when(cartaoRepository.findByNumeroCartao(transacaoRequestDTO.getNumeroCartao())).thenReturn(Optional.of(cartao));
		when(transacaoService.salvarTransacao(transacaoRequestDTO)).thenThrow(new TransacaoException("SALDO_INSUFICIENTE"));

		try {
			transacaoService.salvarTransacao(transacaoRequestDTO);
		} catch (TransacaoException e) {
			assertEquals(TransacaoException.class, e.getClass());
			assertEquals(TRANSACAO_EXCEPTION_SALDO_INSUFICIENTE, e.getMessage());
		}

		assertThrows(TransacaoException.class, () -> {
			transacaoService.salvarTransacao(transacaoRequestDTO);
		});

	}
	
	@Test
	public void quandoTentarSalvarUmTransacaoQueOCartaoNaoExisteDeveLancarExcecao() {

		when(transacaoRepository.save(transacaoInvalida)).thenThrow(new TransacaoException("CARTAO_INEXISTENTE"));

		try {
			transacaoRepository.findById(ID_INVALIDO);
		} catch (TransacaoException e) {
			assertEquals(TransacaoException.class, e.getClass());
			assertEquals(TRANSACAO_EXCEPTION_ID_INVALIDO, e.getMessage());
		}

		assertThrows(TransacaoException.class, () -> {
			transacaoRepository.save(transacaoInvalida);
		});

	}
	@Test
	public void quandoTentarSalvarUmCartaoComSenhaInvalidaLancarExcecao() {

		when(transacaoRepository.save(transacaoInvalida)).thenThrow(new TransacaoException("SENHA_INVALIDA"));

		try {
			transacaoRepository.findById(ID);
		} catch (TransacaoException e) {
			assertEquals(TransacaoException.class, e.getClass());
			assertEquals(TRANSACAO_EXCEPTION_SENHA_INVALIDA, e.getMessage());
		}

		assertThrows(TransacaoException.class, () -> {
			transacaoRepository.save(transacaoInvalida);
		});

	}

	private void instanciaTransacao() {
		transacaoRequestDTO = new TransacaoRequestDTO(NUMERO_CARTAO, SENHA, VALOR, DATA_TRANSACAO );
		transacaoResponseDTO = new TransacaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR);
		transacaoInvalida = new Transacao(ID, NUMERO_CARTAO, SENHA, SEM_SALDO, DATA_TRANSACAO);
		cartao = new Cartao(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT);

	}

}
