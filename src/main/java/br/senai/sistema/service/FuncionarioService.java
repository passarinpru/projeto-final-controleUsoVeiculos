
package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sistema.model.Funcionario;
import br.senai.sistema.repository.FuncionarioRepository;
import br.senai.sistema.repository.UtilizacaoVeiculoRepository;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UtilizacaoVeiculoRepository utilizacaoRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            UtilizacaoVeiculoRepository utilizacaoRepository) {

        this.funcionarioRepository = funcionarioRepository;
        this.utilizacaoRepository = utilizacaoRepository;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll(Sort.by("nome"));
    }

    public void salvar(Funcionario funcionario) {
        funcionarioRepository.save(funcionario);
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Funcionário não encontrado."));
    }

    // Impede a exclusão de funcionários com histórico.
    @Transactional
    public void excluir(Long id) {

        Funcionario funcionario = buscarPorId(id);

        // Verifica se existem utilizações vinculadas.
        if (utilizacaoRepository.existsByFuncionarioId(id)) {
            throw new RegraNegocioException(
                "Não é possível excluir este funcionário, pois ele possui registros no histórico de utilização de veículos.");
        }

        funcionarioRepository.delete(funcionario);
    }
}
