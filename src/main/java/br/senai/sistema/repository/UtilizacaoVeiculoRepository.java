
package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.senai.sistema.model.UtilizacaoVeiculo;

public interface UtilizacaoVeiculoRepository
        extends JpaRepository<UtilizacaoVeiculo, Long> {

}
