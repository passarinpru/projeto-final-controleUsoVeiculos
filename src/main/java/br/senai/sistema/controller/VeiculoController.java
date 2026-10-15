package br.senai.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Veiculo;
import br.senai.sistema.service.VeiculoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("veiculos", veiculoService.listarTodos());
        return "veiculos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        return "veiculos/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("veiculo") Veiculo veiculo,
            BindingResult resultado,
            RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "veiculos/form";
        }

        veiculoService.salvar(veiculo);
        atributos.addFlashAttribute("mensagem",
                "Veículo salvo com sucesso.");

        return "redirect:/veiculos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        return "veiculos/form";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes atributos) {
        veiculoService.excluir(id);
        atributos.addFlashAttribute("mensagem",
                "Veículo excluído com sucesso.");

        return "redirect:/veiculos";
    }
}