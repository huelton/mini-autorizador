package com.vr.project.controller;

import java.net.URI;

import javax.validation.Valid;

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
import com.vr.project.exception.StandardError;
import com.vr.project.service.CartaoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Api(value = "CartaoController", description = "Endpoint Responsavel por criar o Cartão e lista o cartão pela numero do Cartão", tags = {"CartaoController"})
@SwaggerDefinition(tags = {
        @Tag(name = "CartaoController", description = "Endpoint Responsavel por criar o Cartão e lista o cartão pela numero do Cartão")
})
@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	private final CartaoServiceImpl cartaoService;	
	
	public CartaoController(CartaoServiceImpl cartaoService) {
		this.cartaoService = cartaoService;
	}
	
	@ApiOperation(value = "Cria Cartão", notes = "Este Endpoint é responsavel por criar um Cartão", response = CartaoResponseDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Success", response = CartaoResponseDTO.class),
	        @ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
	        @ApiResponse(code = 422, message = "Unprocessable Entity"),
	        @ApiResponse(code = 500, message = "Failure")
	})
	@PostMapping
	public ResponseEntity<CartaoResponseDTO> salvarCartao(@Valid @RequestBody CartaoRequestDTO dto) {
		CartaoResponseDTO response = cartaoService.salvarCartao(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(location).body(response);
	}
	
	@ApiOperation(value = "Retorna dados do Cartão", notes = "Este Endpoint retorna dados de cartão especifico pelo numero do Cartão", response = CartaoResponseDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Success", response = CartaoResponseDTO.class),
	        @ApiResponse(code = 422, message = "Unprocessable Entity"),
	        @ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
	        @ApiResponse(code = 500, message = "Failure")
	})
	@GetMapping("/{numeroCartao}")
	public ResponseEntity<CartaoResponseDTO> retornaDadosCartaoPeloNumeroCartao(@PathVariable String numeroCartao) {
		CartaoResponseDTO dto = cartaoService.retornarDadosPeloCartao(numeroCartao);
		return ResponseEntity.ok().body(dto);
	}
}