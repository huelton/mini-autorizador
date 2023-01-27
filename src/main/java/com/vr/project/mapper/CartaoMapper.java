package com.vr.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.vr.project.dto.CartaoRequestDTO;
import com.vr.project.dto.CartaoResponseDTO;
import com.vr.project.model.Cartao;

@Mapper
@MapperConfig(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL, 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartaoMapper {

	CartaoMapper INTANCE = Mappers.getMapper(CartaoMapper.class);

	Cartao requestDTOToEntity(CartaoRequestDTO dto);

	CartaoResponseDTO entityToResponseDTO(Cartao transacao);

}

