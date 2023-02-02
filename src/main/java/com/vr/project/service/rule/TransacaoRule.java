package com.vr.project.service.rule;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.vr.project.exception.TransacaoException;

@Service
public class TransacaoRule {	

	public void validaSenha(String senhaDto, String senhaCartao) {
		if(!senhaDto.equals(senhaCartao)) throw new TransacaoException("SENHA_INVALIDA");		
	}

	public void verificaLimiteValor(BigDecimal valorDto, BigDecimal valorCartao) {
		if(valorDto.compareTo(valorCartao) > 0) throw new TransacaoException("SALDO_INSUFICIENTE");
	}
}
