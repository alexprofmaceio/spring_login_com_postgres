package br.com.projetos.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import net.bytebuddy.utility.RandomString;

import br.com.projetos.models.Usuario;
import br.com.projetos.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository ur;

	@GetMapping("/")
	public String home() {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String paginaLogin() {

		return "login";
	}
		
	@GetMapping("/registrar")
	public String registrar(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}
	
	@PostMapping("/registrar")
	public String registrar(Usuario usuario, Model model) {
		Usuario cadastrado = ur.findByLogin(usuario.getLogin());
		if(cadastrado == null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCripto = encoder.encode(usuario.getSenha());
			usuario.setSenha(senhaCripto);
			ur.save(usuario);			
			return "index";
		} else {
			model.addAttribute("aviso", "Usuário já cadastrado!");
			return "registro";
		}
	}
	
	@GetMapping("/recuperaSenha")
	public String recuperaSenha(Model model) {
		model.addAttribute("pageTitle", "Forgot Password");
		return "esqueceuSenha";
	}
	
	@PostMapping("/recuperaSenha")
	public String recuperaSenha(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(45);
		System.out.println("E-mail: " + email);
		System.out.println("Token: " + token);

		return "esqueceuSenha";
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
