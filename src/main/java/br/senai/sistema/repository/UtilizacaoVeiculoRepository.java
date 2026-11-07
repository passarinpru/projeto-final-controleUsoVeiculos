
package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.UtilizacaoVeiculo;

public interface UtilizacaoVeiculoRepository
        extends JpaRepository<UtilizacaoVeiculo, Long> {

    // Verifica se existem utilizações vinculadas a um funcionário.
    boolean existsByFuncionarioId(Long funcionarioId);

    // Verifica se existem utilizações vinculadas a um veículo.
    boolean existsByVeiculoId(Long veiculoId);
}
