package com.vr.project.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransacaoTest {

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "12345";
	private static final String SENHA_CARTAO = "4321";
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal(500.00);
	private static final Instant DATA_TRANSACAO = Instant.now();
	
	private Transacao transacao;
	
	@BeforeEach
	public void config() {
		intanciaTransacao();
		
	}	

	@Test        
	public void deveCompararOsDadosDoObjetoNaTransacao() {
		Assertions.assertAll("Todos os Dados da Entidade",
		() -> Assertions.assertEquals(ID, transacao.getId()),
		() -> Assertions.assertEquals(NUMERO_CARTAO, transacao.getNumeroCartao()),
		() ->  Assertions.assertEquals(SENHA_CARTAO, transacao.getSenha()),
		() ->  Assertions.assertEquals(VALOR_DEFAULT, transacao.getValor()),
		() ->  Assertions.assertEquals(DATA_TRANSACAO, transacao.getDataTransacao()));
	}
	
	@Test   
	public void deveConterOMesmoTamanhoNasListas() {
		List<Transacao> listaTransacaoUm = new ArrayList<>();
		listaTransacaoUm.add(transacao);
		listaTransacaoUm.add(transacao);	
		
		List<Transacao> listaTransacaoDois = new ArrayList<>();
		listaTransacaoDois.add(transacao);
		listaTransacaoDois.add(transacao);
		
		Assertions.assertIterableEquals(listaTransacaoUm, listaTransacaoDois);
		
	}

	private void intanciaTransacao() {
		transacao = new Transacao(ID, NUMERO_CARTAO, SENHA_CARTAO,VALOR_DEFAULT,DATA_TRANSACAO);
		
	}	

}