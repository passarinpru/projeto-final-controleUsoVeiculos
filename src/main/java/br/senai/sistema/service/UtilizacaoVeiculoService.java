
package br.senai.sistema.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sistema.model.StatusUtilizacao;
import br.senai.sistema.model.UtilizacaoVeiculo;
import br.senai.sistema.repository.UtilizacaoVeiculoRepository;

@Service
public class UtilizacaoVeiculoService {

    private final UtilizacaoVeiculoRepository repository;

    public UtilizacaoVeiculoService(
            UtilizacaoVeiculoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<UtilizacaoVeiculo> listarTodos() {

        List<UtilizacaoVeiculo> utilizacoes = repository.findAll(
                Sort.by("dataHoraSaida").descending());

        // Atualiza a situação dos registros antigos.
        for (UtilizacaoVeiculo uso : utilizacoes) {

            if (uso.getStatus() == null) {

                if (uso.getDataHoraRetorno() == null) {
                    uso.setStatus(StatusUtilizacao.EM_UTILIZACAO);
                } else {
                    uso.setStatus(StatusUtilizacao.DEVOLVIDO);
                }

                repository.save(uso);
            }
        }

        return utilizacoes;
    }

    public UtilizacaoVeiculo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Utilização não encontrada: " + id));
    }

    @Transactional
    public UtilizacaoVeiculo salvar(
            UtilizacaoVeiculo utilizacao) {

        // RN02: retorno não pode ser anterior à saída.
        if (utilizacao.getDataHoraRetorno() != null
                && utilizacao.getDataHoraRetorno()
                    .isBefore(utilizacao.getDataHoraSaida())) {

            throw new RegraNegocioException(
                    "O retorno não pode ser anterior à saída.");
        }

        // Define a situação automaticamente.
        if (utilizacao.getDataHoraRetorno() == null) {
            utilizacao.setStatus(StatusUtilizacao.EM_UTILIZACAO);
        } else {
            utilizacao.setStatus(StatusUtilizacao.DEVOLVIDO);
        }

        // RN03: impede duas utilizações em aberto.
        if (utilizacao.getStatus() == StatusUtilizacao.EM_UTILIZACAO) {

            boolean veiculoEmUso = repository.findAll()
                    .stream()
                    .anyMatch(uso ->
                        Objects.equals(
                            uso.getVeiculo().getId(),
                            utilizacao.getVeiculo().getId()
                        )
                        && uso.getDataHoraRetorno() == null
                        && !Objects.equals(
                            uso.getId(),
                            utilizacao.getId()
                        )
                    );

            if (veiculoEmUso) {
                throw new RegraNegocioException(
                    "Este veículo já está em utilização e ainda não foi devolvido."
                );
            }
        }

        return repository.save(utilizacao);
    }

    @Transactional
    public void devolver(Long id) {

        UtilizacaoVeiculo utilizacao = buscarPorId(id);

        if (utilizacao.getDataHoraRetorno() != null) {
            throw new RegraNegocioException(
                    "Este veículo já foi devolvido.");
        }

        LocalDateTime agora = LocalDateTime.now();

        // RN02: retorno não pode ser anterior à saída.
        if (agora.isBefore(utilizacao.getDataHoraSaida())) {
            throw new RegraNegocioException(
                    "O retorno não pode ser anterior à saída.");
        }

        utilizacao.setDataHoraRetorno(agora);
        salvar(utilizacao);
    }

    // Exclui somente utilizações que ainda não foram devolvidas.
    @Transactional
    public void excluir(Long id) {

        UtilizacaoVeiculo utilizacao = buscarPorId(id);

        // Preserva o histórico de utilizações finalizadas.
        if (utilizacao.getDataHoraRetorno() != null) {
            throw new RegraNegocioException(
                "Não é permitido excluir uma utilização já finalizada.");
        }

        repository.delete(utilizacao);
    }
}
