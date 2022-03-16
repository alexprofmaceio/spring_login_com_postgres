package br.com.projetos.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.projetos.models.Usuario;

public class NovoUsuario implements UserDetails{

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	
	public NovoUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {		
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
