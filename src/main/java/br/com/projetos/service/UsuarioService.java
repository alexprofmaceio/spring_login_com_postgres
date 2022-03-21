package br.com.projetos.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.projetos.models.Usuario;
import br.com.projetos.repository.UsuarioRepository;

public class UsuarioService {

	@Autowired
	UsuarioRepository ur;
	
	public void recuperaSenha(String token, String email) throws UsuarioNotFoundException {
		Usuario usuario = ur.findByLogin(email);
		if(usuario != null) {
			usuario.setResetToken(token);
			ur.save(usuario);
		} else {
			throw new UsuarioNotFoundException("Não existe usuário com o email " + email);
		}
	}
	
	public void atualizaSenha(Usuario usuario, String novaSenha) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodePassword = passwordEncoder.encode(novaSenha);
		usuario.setSenha(encodePassword);
		usuario.setResetToken(null);
		ur.save(usuario);
	}
}