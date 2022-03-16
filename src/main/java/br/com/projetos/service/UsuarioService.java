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
	
	public Usuario get(String resetToken) {
		return ur.findByResetToken(resetToken);
	}
	
	public void sendEmail(String email, String senhaLink) {
		String host = "smtp.mailtrap.io:2525";
		String para = email;
		String de = "from@example.com";
		String conteudo = "<p>Olá, </p>" +
				"<p>Você solicitou mudança em sua senha.</p>" +
				"<p>Clique no link abaixo para mudar sua senha:</p>" +
				"<a href=\"" + senhaLink + "\">Mudar a senha</a>" +
				"<p>Desconsidere este e-mail caso não tenha solicitado a mudança de senha.</p>";
		final String username = "b36ce146cd2db8:012c116b7963fc";
		final String password = "b36ce146cd2db8:012c116b7963fc";
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "2525");
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    	  protected PasswordAuthentication getPasswordAuthentication() {
    		  return new PasswordAuthentication(username, password);
		  }
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(de));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
			message.setSubject("Link para reiniciar sua senha");
			message.setText(conteudo);
			Transport.send(message);
			System.out.println("Mensagem enviada com sucesso....");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
}