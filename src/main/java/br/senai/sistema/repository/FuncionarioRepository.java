package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Funcionario;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Long> {
}
