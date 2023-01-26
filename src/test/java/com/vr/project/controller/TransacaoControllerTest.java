package com.vr.project.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.exception.TransacaoException;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;
import com.vr.project.repository.TransacaoRepository;
import com.vr.project.service.CartaoServiceImpl;
import com.vr.project.service.TransacaoServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoControllerTest {

	@MockBean
	private TransacaoServiceImpl transacaoService;
	
	@MockBean
	private CartaoServiceImpl cartaoService;
	
	@MockBean
	private TransacaoRepository transacaoRepository;
	
	@MockBean
	private CartaoRepository cartaoRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "1234";
	private static final String SENHA = "4321";

	private static final BigDecimal VALOR_DEFAULT = new BigDecimal("500.00");
	private static final BigDecimal VALOR_TRANSACAO = new BigDecimal("100.00");
	private static final BigDecimal VALOR_DEBITADO = new BigDecimal("400.00");
	private static final String LOCATION_URI = "http://localhost/transacoes/1";
	private static final Instant DATA_TRANSACAO = Instant.now();
	private static final Integer VERSAO = 1;

	private TransacaoRequestDTO transacaoRequestDTO;
	private TransacaoResponseDTO transacaoResponseDTO;
	private Cartao cartao, cartaoDebitado;
	private CartaoResponseDTO cartaoResponseDTO, cartaoResponseDebitado;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		instanciaTransacao();
	}

	@Test
	public void deveCriaUmaTransacaoEInformarQueFoiCriadoComUri() throws Exception {
		doReturn(transacaoResponseDTO).when(transacaoService).salvarTransacao(any());
		mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, LOCATION_URI));
	}

	@Test
	void deveBuscarCartaoRealizarUmaTransacaoEConsultarCartaoComDebito() throws Exception {
		doReturn(cartao).when(cartaoRepository).save(cartao);
		doReturn(cartaoResponseDTO).when(cartaoService).retornarDadosPeloCartao(NUMERO_CARTAO);

		mockMvc.perform(get("/cartoes/1234")).andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.numeroCartao", is("1234")))
				.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(500.00)));
		
		doReturn(transacaoResponseDTO).when(transacaoService).salvarTransacao(any());
		mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, LOCATION_URI));
		
		doReturn(cartao).when(cartaoRepository).save(cartaoDebitado);
		doReturn(cartaoResponseDebitado).when(cartaoService).retornarDadosPeloCartao(NUMERO_CARTAO);
		
		mockMvc.perform(get("/cartoes/1234")).andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

		.andExpect(jsonPath("$.numeroCartao", is("1234")))
		.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(400.00)));
	}
	
	@Test
	void develancarExcecaoQuandoOSaldoMenorQueZeroNoCartao() throws Exception {
		doReturn(cartao).when(cartaoRepository).save(cartao);
		doReturn(cartaoResponseDTO).when(cartaoService).retornarDadosPeloCartao(NUMERO_CARTAO);

		mockMvc.perform(get("/cartoes/1234")).andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.numeroCartao", is("1234")))
				.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(500.00)));
		
		doReturn(transacaoResponseDTO).when(transacaoService).salvarTransacao(any());
		mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoRequestDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, LOCATION_URI));
		
		doReturn(cartao).when(cartaoRepository).save(cartaoDebitado);
		doReturn(cartaoResponseDebitado).when(cartaoService).retornarDadosPeloCartao(NUMERO_CARTAO);
		
		mockMvc.perform(get("/cartoes/1234")).andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

		.andExpect(jsonPath("$.numeroCartao", is("1234")))
		.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(400.00)));
		
		
		Mockito.doThrow(new TransacaoException("SALDO_INSUFICIENTE")).when(transacaoService).salvarTransacao(any());

		mockMvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoRequestDTO)))
		.andExpect(status().is4xxClientError())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.status", is(422)))
				.andExpect(jsonPath("$.message", is("SALDO_INSUFICIENTE")));

	}
	
	private void instanciaTransacao() {
		transacaoRequestDTO = new TransacaoRequestDTO(NUMERO_CARTAO, SENHA, VALOR_TRANSACAO, DATA_TRANSACAO);
		transacaoResponseDTO = new TransacaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR_TRANSACAO);

		cartao = new Cartao(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT, VERSAO);
		cartaoDebitado = new Cartao(ID, NUMERO_CARTAO, SENHA, VALOR_DEBITADO, VERSAO);
		cartaoResponseDebitado = new CartaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR_DEBITADO);
		cartaoResponseDTO = new CartaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT);
	}

	
}
