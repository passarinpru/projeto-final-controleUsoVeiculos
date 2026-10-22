
package br.senai.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.UtilizacaoVeiculo;
import br.senai.sistema.service.UtilizacaoVeiculoService;
import br.senai.sistema.service.FuncionarioService;
import br.senai.sistema.service.VeiculoService;
import br.senai.sistema.service.RegraNegocioException;

@Controller
@RequestMapping("/utilizacoes")
public class UtilizacaoVeiculoController {

    private final UtilizacaoVeiculoService service;
    private final FuncionarioService funcionarioService;
    private final VeiculoService veiculoService;

    public UtilizacaoVeiculoController(
            UtilizacaoVeiculoService service,
            FuncionarioService funcionarioService,
            VeiculoService veiculoService) {

        this.service = service;
        this.funcionarioService = funcionarioService;
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "utilizacoes", service.listarTodos());

        return "utilizacoes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute(
                "utilizacao", new UtilizacaoVeiculo());

        model.addAttribute(
                "funcionarios", funcionarioService.listarTodos());

        model.addAttribute(
                "veiculos", veiculoService.listarTodos());

        return "utilizacoes/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute UtilizacaoVeiculo utilizacao,
            RedirectAttributes redirectAttributes) {

        if (utilizacao.getFuncionario() == null
                || utilizacao.getVeiculo() == null
                || utilizacao.getDataHoraSaida() == null) {

            redirectAttributes.addFlashAttribute(
                    "erro", "Preencha todos os campos obrigatórios.");

            return "redirect:/utilizacoes/novo";
        }

        try {

            service.salvar(utilizacao);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Saída do veículo registrada com sucesso!");

            return "redirect:/utilizacoes";

        } catch (RegraNegocioException e) {

            redirectAttributes.addFlashAttribute(
                    "erro", e.getMessage());

            return "redirect:/utilizacoes/novo";
        }
    }

    @PostMapping("/{id}/devolver")
    public String devolver(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            service.devolver(id);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Devolução registrada com sucesso!");

        } catch (RegraNegocioException e) {

            redirectAttributes.addFlashAttribute(
                    "erro", e.getMessage());
        }

        return "redirect:/utilizacoes";
    }
}
