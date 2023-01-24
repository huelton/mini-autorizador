package com.vr.project.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.service.TransacaoServiceImpl;

@RestController
@RequestMapping("/cartoes")
public class TransacaoController {
	
	private final TransacaoServiceImpl transacaoService;	
	
	public TransacaoController(TransacaoServiceImpl transacaoService) {
		this.transacaoService = transacaoService;
	}
	
	@PostMapping
	public ResponseEntity<Void> salvarTransacao(@RequestBody TransacaoRequestDTO dto) {
		TransacaoResponseDTO transacao = transacaoService.salvarTransacao(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(transacao.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{numeroCartao}")
	public ResponseEntity<TransacaoResponseDTO> retornaTransacaoPeloCartao(@PathVariable String numeroCartao) {
		TransacaoResponseDTO dto = transacaoService.retornarDadosPeloCartao(numeroCartao);
		return ResponseEntity.ok().body(dto);
	}
}