package br.senai.sistema.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.senai.sistema.model.Funcionario;
import br.senai.sistema.model.Perfil;
import br.senai.sistema.model.Usuario;
import br.senai.sistema.repository.FuncionarioRepository;
import br.senai.sistema.repository.UsuarioRepository;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final FuncionarioRepository funcionarioRepository;

    public DadosIniciais(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        FuncionarioRepository funcionarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("Administrador", "admin",
                    passwordEncoder.encode("admin123"), Perfil.ADMIN));

            usuarioRepository.save(new Usuario("Operador", "operador",
                    passwordEncoder.encode("operador123"), Perfil.OPERADOR));
        }

        if (funcionarioRepository.count() == 0) {
            Funcionario funcionario1 = new Funcionario();
            funcionario1.setNome("Ana");
            funcionario1.setMatricula("001");
            funcionarioRepository.save(funcionario1);

            Funcionario funcionario2 = new Funcionario();
            funcionario2.setNome("Bruno");
            funcionario2.setMatricula("002");
            funcionarioRepository.save(funcionario2);

            Funcionario funcionario3 = new Funcionario();
            funcionario3.setNome("Carlos");
            funcionario3.setMatricula("003");
            funcionarioRepository.save(funcionario3);
        }
    }
}
