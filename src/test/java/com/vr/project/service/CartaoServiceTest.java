package com.vr.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.exception.CartaoException;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;

public class CartaoServiceTest {

	@InjectMocks
	private CartaoServiceImpl service;

	@Mock
	private CartaoRepository repository;

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "1234";
	private static final String SENHA = "4321";
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal("500.00");
	private static final BigDecimal VALOR = new BigDecimal(500.00);
	private static final BigDecimal SEM_SALDO = new BigDecimal(-1.00);
	private static final Integer VERSAO = 1;

	private static final String CARTAO_EXCEPTION_DUPLICADO = "CARTAO_DUPLICADO";
	private static final String CARTAO_EXCEPTION_SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
	private static final String CARTAO_EXCEPTION_SENHA_INVALIDA = "SENHA_INVALIDA";

	private Cartao cartao, cartaoInvalido;
	private CartaoResponseDTO cartaoResponseDTO;
	private CartaoRequestDTO cartaoRequestDTO;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		instanciaCartao();
	}

	@Test
	public void quandoSalvarDeveReturnarDadosDoCartao() {
		when(repository.save(cartao)).thenReturn(cartao);

		cartaoResponseDTO = service.salvarCartao(cartaoRequestDTO);

		assertNotNull(cartaoResponseDTO);

		assertEquals(NUMERO_CARTAO, cartaoResponseDTO.getNumeroCartao());
		assertEquals(SENHA, cartaoResponseDTO.getSenha());
		assertEquals(VALOR_DEFAULT, cartaoResponseDTO.getValor());

	}

	@Test
	public void quandoRealizarABuscaPeloNumeroDoCartaoDeveRetornarOsDados() {

		when(repository.findById(ID)).thenReturn(Optional.of(cartao));

		assertNotNull(cartao);
		assertEquals(ID, cartao.getId());
		assertEquals(NUMERO_CARTAO, cartao.getNumeroCartao());
		assertEquals(SENHA, cartao.getSenha());
		assertEquals(VALOR_DEFAULT, cartao.getValor());
	}

	@Test
	public void quandoTentarSalvarCartaoDeveLancarExececaoSeValorNegativo() {

		when(repository.save(cartaoInvalido)).thenThrow(new CartaoException("SALDO_INSUFICIENTE"));

		try {
			repository.save(cartaoInvalido);
		} catch (CartaoException e) {
			assertEquals(CartaoException.class, e.getClass());
			assertEquals(CARTAO_EXCEPTION_SALDO_INSUFICIENTE, e.getMessage());
		}

		assertThrows(CartaoException.class, () -> {
			repository.save(cartaoInvalido);
		});

	}

	@Test
	public void quandoTentarSalvarUmCartaoQueJaFoiSalvoAnteriormenteLancarExcecao() {
		when(repository.save(cartao)).thenReturn(cartao);
		when(service.salvarCartao(cartaoRequestDTO)).thenThrow(new DataIntegrityViolationException("CARTAO_DUPLICADO"));

		try {
			service.salvarCartao(cartaoRequestDTO);
		} catch (DataIntegrityViolationException e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
			assertEquals(CARTAO_EXCEPTION_DUPLICADO, e.getMessage());
		}

		assertThrows(DataIntegrityViolationException.class, () -> {
			service.salvarCartao(cartaoRequestDTO);
		});

	}

	@Test
	public void quandoTentarSalvarUmCartaoComSenhaInvalidaLancarExcecao() {

		when(repository.save(cartaoInvalido)).thenThrow(new CartaoException("SENHA_INVALIDA"));

		try {
			repository.findById(ID);
		} catch (CartaoException e) {
			assertEquals(CartaoException.class, e.getClass());
			assertEquals(CARTAO_EXCEPTION_SENHA_INVALIDA, e.getMessage());
		}

		assertThrows(CartaoException.class, () -> {
			repository.save(cartaoInvalido);
		});

	}

	private void instanciaCartao() {
		cartaoRequestDTO = new CartaoRequestDTO(NUMERO_CARTAO, SENHA);
		cartao = new Cartao(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT, VERSAO);
		cartaoResponseDTO = new CartaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR);
		cartaoInvalido = new Cartao(ID, NUMERO_CARTAO, SENHA, SEM_SALDO, VERSAO);
	}

}
