package com.AppRH.AppRH.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.Vaga;

public interface CandidatoRepository extends CrudRepository<Candidato, String> {

    Iterable<Candidato> findByVaga(Vaga vaga);
    
    Candidato findByCpf(String cpf);

    Candidato findById(long id);

    List<Candidato> findByNome(String nome);
}
