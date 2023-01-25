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

import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.service.CartaoServiceImpl;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	private final CartaoServiceImpl cartaoService;	
	
	public CartaoController(CartaoServiceImpl cartaoService) {
		this.cartaoService = cartaoService;
	}
	
	@PostMapping
	public ResponseEntity<CartaoResponseDTO> salvarCartao(@RequestBody CartaoRequestDTO dto) {
		CartaoResponseDTO response = cartaoService.salvarCartao(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(location).body(response);
	}
	
	@GetMapping("/{numeroCartao}")
	public ResponseEntity<CartaoResponseDTO> retornaDadosCartaoPeloNumeroCartao(@PathVariable String numeroCartao) {
		CartaoResponseDTO dto = cartaoService.retornarDadosPeloCartao(numeroCartao);
		return ResponseEntity.ok().body(dto);
	}
}