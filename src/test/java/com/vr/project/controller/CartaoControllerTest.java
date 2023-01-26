package com.vr.project.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

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
import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.exception.CartaoException;
import com.vr.project.model.Cartao;
import com.vr.project.repository.CartaoRepository;
import com.vr.project.service.CartaoServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class CartaoControllerTest {

	@MockBean
	private CartaoServiceImpl service;
	
	@MockBean
	private CartaoRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "1234";
	private static final String SENHA = "4321";
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal("500.00");
	private static final Integer VERSAO = 1;

	private Cartao cartao;
	private CartaoResponseDTO cartaoResponseDTO;
	private CartaoRequestDTO cartaoRequestDTO;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		instanciaCartao();
	}

	@Test
	public void deveCriarUmCartaoERetornarDadosComURI() throws Exception {
		doReturn(cartaoResponseDTO).when(service).salvarCartao(any());
		mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartaoRequestDTO)))

				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/cartoes/1"))

				.andExpect(jsonPath("$.numeroCartao", is("1234")))
				.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(500.00)));
	}

	@Test
	void deveRetornarDadosDoCartaoPesquisadoPeloNumeroCartao() throws Exception {
		doReturn(cartao).when(repository).save(cartao);
		doReturn(cartaoResponseDTO).when(service).retornarDadosPeloCartao(NUMERO_CARTAO);

		mockMvc.perform(get("/cartoes/1234")).andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.numeroCartao", is("1234")))
				.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(500.00)));
	}
	
	@Test
	void deveRetornarExcecaoQuandoBuscarUmCartaoNaoCadastrado() throws Exception {
		Mockito.doThrow(new CartaoException("CARTAO_INEXISTENTE")).when(service).retornarDadosPeloCartao(any());

		mockMvc.perform(get("/cartoes/xxxx")).andExpect(status().is4xxClientError())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.status", is(422)))
				.andExpect(jsonPath("$.message", is("CARTAO_INEXISTENTE")));
	}
	
	@Test
	void deveRetornarExcecaoQuandoCartaoComMesmoNumeroJaCadastrado() throws Exception {
		doReturn(cartaoResponseDTO).when(service).salvarCartao(any());
		mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartaoRequestDTO)))

				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/cartoes/1"))

				.andExpect(jsonPath("$.numeroCartao", is("1234")))
				.andExpect(jsonPath("$.senha", is("4321"))).andExpect(jsonPath("$.valor", is(500.00)));
		
		Mockito.doThrow(new CartaoException("CARTAO_DUPLICADO")).when(service).salvarCartao(any());

		mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartaoRequestDTO)))
		.andExpect(status().is4xxClientError())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))

				.andExpect(jsonPath("$.status", is(422)))
				.andExpect(jsonPath("$.message", is("CARTAO_DUPLICADO")));
	}
	
	private void instanciaCartao() {

		cartaoRequestDTO = new CartaoRequestDTO(NUMERO_CARTAO, SENHA);
		cartao = new Cartao(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT, VERSAO);
		cartaoResponseDTO = new CartaoResponseDTO(ID, NUMERO_CARTAO, SENHA, VALOR_DEFAULT);
		
	}

	
}
