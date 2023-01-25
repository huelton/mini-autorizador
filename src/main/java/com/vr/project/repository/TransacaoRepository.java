package com.vr.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vr.project.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Integer>{
}
