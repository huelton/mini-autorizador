package com.vr.project.controller.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.vr.project.exception.CartaoException;
import com.vr.project.exception.StandardError;
import com.vr.project.exception.TransacaoException;

@SpringBootTest
public class ControllerAdviceHandlerTest {

	private static final String CARTAO_NOT_FOUND = "CARTAO_INEXISTENTE";
	private static final String CARTAO_INTEGRIDADE = "CARTAO_DUPLICADO";
	private static final String TRANSACAO_SEM_SALDO = "SALDO_INSUFICIENTE";

	@InjectMocks
	private ControllerAdviceHandler controllerAdvice;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void deveLancarExcecaoDeCartaoInexistenteRetornandoUmResponseEntity() {
		ResponseEntity<StandardError> resultado = controllerAdvice.cartaoError(new CartaoException(CARTAO_NOT_FOUND),
				new MockHttpServletRequest());

		assertNotNull(resultado);
		assertNotNull(resultado.getBody());
		assertTrue(resultado.hasBody());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resultado.getStatusCode());
		assertEquals(CARTAO_NOT_FOUND, resultado.getBody().getMessage());
		assertEquals(422, resultado.getBody().getStatus());

	}

	@Test
	public void deveLancarExcecaoCartaoSeCartaoJaPersistidoERetornarUmResponseEntity() {
		ResponseEntity<StandardError> resultado = controllerAdvice.cartaoErrorDataIntegrity(
				new DataIntegrityViolationException(CARTAO_INTEGRIDADE), new MockHttpServletRequest());
		assertNotNull(resultado);
		assertNotNull(resultado.getBody());
		assertTrue(resultado.hasBody());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resultado.getStatusCode());
		assertEquals(CARTAO_INTEGRIDADE, resultado.getBody().getMessage());
		assertEquals(422, resultado.getBody().getStatus());

	}

	@Test
	public void deveLancarExcecaoDaTransacaoSeSaldoInsuficienteERetornarUmResponseEntity() {
		ResponseEntity<StandardError> resultado = controllerAdvice
				.transacaoError(new TransacaoException(TRANSACAO_SEM_SALDO), new MockHttpServletRequest());

		assertNotNull(resultado);
		assertNotNull(resultado.getBody());
		assertTrue(resultado.hasBody());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resultado.getStatusCode());
		assertEquals(TRANSACAO_SEM_SALDO, resultado.getBody().getMessage());
		assertEquals(422, resultado.getBody().getStatus());

	}
}
