package com.vr.project.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.exception.StandardError;
import com.vr.project.service.TransacaoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Api(value = "TransacaoController", description = "Endpoint Responsavel por criar as transações do cartão", tags = {"TransacaoController"})
@SwaggerDefinition(tags = {
        @Tag(name = "TransacaoController", description = "Endpoint Responsavel por criar as transações do cartão")
})
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	private final TransacaoServiceImpl transacaoService;	
	
	public TransacaoController(TransacaoServiceImpl transacaoService) {
		this.transacaoService = transacaoService;
	}
	
	@ApiOperation(value = "Cria Transacao", notes = "Este Endpoint é responsavel por criar uma Transacao", response = Empty.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Success", response = Empty.class),
	        @ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
	        @ApiResponse(code = 422, message = "Unprocessable Entity", response = StandardError.class),
	        @ApiResponse(code = 500, message = "Failure")
	})
	@PostMapping
	public ResponseEntity<TransacaoResponseDTO> salvarTransacao(@RequestBody TransacaoRequestDTO dto) {
		TransacaoResponseDTO response = transacaoService.salvarTransacao(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

}