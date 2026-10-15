package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.Veiculo;
import br.senai.sistema.repository.VeiculoRepository;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll(Sort.by("modelo"));
    }

    public void salvar(Veiculo veiculo) {
        veiculoRepository.save(veiculo);
    }

    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Veículo não encontrado."));
    }

    public void excluir(Long id) {
        veiculoRepository.deleteById(id);
    }
}