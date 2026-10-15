package br.senai.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Funcionario;
import br.senai.sistema.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        return "funcionarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("funcionario") Funcionario funcionario,
            BindingResult resultado,
            RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "funcionarios/form";
        }

        funcionarioService.salvar(funcionario);
        atributos.addFlashAttribute("mensagem",
                "Funcionário salvo com sucesso.");

        return "redirect:/funcionarios";
    }
    @GetMapping("/{id}/editar")
public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("funcionario", funcionarioService.buscarPorId(id));
    return "funcionarios/form";
}
@PostMapping("/{id}/excluir")
public String excluir(@PathVariable Long id,
                      RedirectAttributes atributos) {
    funcionarioService.excluir(id);
    atributos.addFlashAttribute("mensagem",
            "Funcionário excluído com sucesso.");
    return "redirect:/funcionarios";
}
}