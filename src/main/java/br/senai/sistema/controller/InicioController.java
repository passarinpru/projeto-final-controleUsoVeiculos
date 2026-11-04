
package br.senai.sistema.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.sistema.model.UtilizacaoVeiculo;
import br.senai.sistema.service.DiagnosticoService;
import br.senai.sistema.service.VeiculoService;
import br.senai.sistema.service.UtilizacaoVeiculoService;

@Controller
public class InicioController {

    private final DiagnosticoService diagnosticoService;
    private final VeiculoService veiculoService;
    private final UtilizacaoVeiculoService utilizacaoService;

    public InicioController(
            DiagnosticoService diagnosticoService,
            VeiculoService veiculoService,
            UtilizacaoVeiculoService utilizacaoService) {

        this.diagnosticoService = diagnosticoService;
        this.veiculoService = veiculoService;
        this.utilizacaoService = utilizacaoService;
    }

    // Página inicial com indicadores
    @GetMapping("/")
    public String inicio(Model model) {

        int totalVeiculos = veiculoService.listarTodos().size();

        List<UtilizacaoVeiculo> utilizacoes =
                utilizacaoService.listarTodos();

        Set<Long> veiculosEmUso = utilizacoes.stream()
                .filter(uso -> uso.getDataHoraRetorno() == null)
                .map(uso -> uso.getVeiculo().getId())
                .collect(Collectors.toSet());

        int emUtilizacao = veiculosEmUso.size();
        int disponiveis = totalVeiculos - emUtilizacao;
        int totalUtilizacoes = utilizacoes.size();

        model.addAttribute("totalVeiculos", totalVeiculos);
        model.addAttribute("emUtilizacao", emUtilizacao);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("totalUtilizacoes", totalUtilizacoes);

        return "index";
    }

    // Diagnóstico do ambiente
    @GetMapping("/diagnostico")
    public String diagnostico(Model model) {

        Map<String, DiagnosticoService.Item> itens =
                diagnosticoService.verificar();

        model.addAttribute("itens", itens.values());
        model.addAttribute("tudoOk",
                diagnosticoService.tudoOk(itens));

        return "diagnostico";
    }

    // Tela de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
