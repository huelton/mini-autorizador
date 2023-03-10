package com.vr.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.vr.project.dto.TransacaoRequestDTO;
import com.vr.project.dto.TransacaoResponseDTO;
import com.vr.project.model.Transacao;

@Mapper
@MapperConfig(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL, 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransacaoMapper {

	TransacaoMapper INTANCE = Mappers.getMapper(TransacaoMapper.class);

	Transacao requestDTOToEntity(TransacaoRequestDTO dto);
	
	TransacaoResponseDTO entityToResponseDTO(Transacao entity);
	
}

