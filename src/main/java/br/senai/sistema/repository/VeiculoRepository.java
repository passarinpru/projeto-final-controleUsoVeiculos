package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Veiculo;

public interface VeiculoRepository
        extends JpaRepository<Veiculo, Long> {
}