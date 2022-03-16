package br.com.projetos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.projetos.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByLogin(String login);

	Usuario findByResetToken(String token);
}
