
package br.senai.sistema.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.senai.sistema.model.Funcionario;
import br.senai.sistema.service.FuncionarioService;

@Component
public class FuncionarioConverter
        implements Converter<String, Funcionario> {

    private final FuncionarioService service;

    public FuncionarioConverter(FuncionarioService service) {
        this.service = service;
    }

    @Override
    public Funcionario convert(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        return service.buscarPorId(Long.valueOf(id));
    }
}
