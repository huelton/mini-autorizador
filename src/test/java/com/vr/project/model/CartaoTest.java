package com.vr.project.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartaoTest {

	private static final Integer ID = 1;
	private static final String NUMERO_CARTAO = "12345";
	private static final String SENHA_CARTAO = "4321";
	private static final BigDecimal VALOR_DEFAULT = new BigDecimal(500.00);
	
	private Cartao cartao;
	
	@BeforeEach
	public void config() {
		intanciaCartao();
		
	}	

	@Test        
	public void deveCompararOsDadosDoObjetoNoCartao() {
		Assertions.assertAll("Todos os Dados da Entidade",
		() -> Assertions.assertEquals(ID, cartao.getId()),
		() -> Assertions.assertEquals(NUMERO_CARTAO, cartao.getNumeroCartao()),
		() ->  Assertions.assertEquals(SENHA_CARTAO, cartao.getSenha()),
		() ->  Assertions.assertEquals(VALOR_DEFAULT, cartao.getValor()));
	}
	
	@Test   
	public void deveConterOMesmoTamanhoNasListas() {
		List<Cartao> listaCartaUm = new ArrayList<>();
		listaCartaUm.add(cartao);
		listaCartaUm.add(cartao);	
		
		List<Cartao> listaCartaDois = new ArrayList<>();
		listaCartaDois.add(cartao);
		listaCartaDois.add(cartao);
		
		Assertions.assertIterableEquals(listaCartaUm, listaCartaDois);
		
	}

	private void intanciaCartao() {
		cartao = new Cartao(ID, NUMERO_CARTAO, SENHA_CARTAO,VALOR_DEFAULT);
		
	}	

}