package br.com.projetos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.projetos.models.Usuario;
import br.com.projetos.repository.UsuarioRepository;

public class LoginUsuarioService implements UserDetailsService{

	@Autowired
	private UsuarioRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = ur.findByLogin(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não localizado.");
		}
		return new NovoUsuario(usuario);
	}
}
