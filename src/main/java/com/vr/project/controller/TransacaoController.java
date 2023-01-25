package com.vr.project.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.service.TransacaoServiceImpl;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	private final TransacaoServiceImpl transacaoService;	
	
	public TransacaoController(TransacaoServiceImpl transacaoService) {
		this.transacaoService = transacaoService;
	}
	
	@PostMapping
	public ResponseEntity<Void> salvarCartao(@RequestBody TransacaoRequestDTO dto) {
		TransacaoResponseDTO response = transacaoService.salvarTransacao(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

}