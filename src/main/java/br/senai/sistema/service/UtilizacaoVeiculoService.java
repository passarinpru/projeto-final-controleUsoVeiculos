
package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.UtilizacaoVeiculo;
import br.senai.sistema.repository.UtilizacaoVeiculoRepository;

@Service
public class UtilizacaoVeiculoService {

    private final UtilizacaoVeiculoRepository repository;

    public UtilizacaoVeiculoService(
            UtilizacaoVeiculoRepository repository) {
        this.repository = repository;
    }

    public List<UtilizacaoVeiculo> listarTodos() {
        return repository.findAll(
                Sort.by("dataHoraSaida").descending());
    }

    public UtilizacaoVeiculo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Utilização não encontrada: " + id));
    }

    public UtilizacaoVeiculo salvar(
            UtilizacaoVeiculo utilizacao) {
        return repository.save(utilizacao);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
