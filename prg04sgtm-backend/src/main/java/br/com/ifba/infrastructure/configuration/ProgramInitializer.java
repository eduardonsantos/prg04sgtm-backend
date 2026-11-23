package br.com.ifba.infrastructure.configuration;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioIService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cria o usuário administrador inicial quando ainda não há usuários ativos.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProgramInitializer implements CommandLineRunner {
    private final UsuarioIService usuarioService;

    @Override
    public void run(String... args) {
        log.info("Iniciando verificação do sistema...");

        List<Usuario> usuariosAtivos = usuarioService.listarAtivos();
        if (!usuariosAtivos.isEmpty()) {
            log.info("O banco de dados já possui usuários. Nenhuma ação necessária.");
            return;
        }

        log.info("Banco vazio detectado. Criando administrador...");
        Usuario administrador = new Usuario();
        administrador.setNome("Administrador");
        administrador.setLogin("admin");
        administrador.setSenha("admin123");
        administrador.setNivelAcesso("ADMIN");

        usuarioService.salvar(administrador);
        log.info("Administrador criado com sucesso!");
    }
}
