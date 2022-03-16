package br.com.projetos.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import net.bytebuddy.utility.RandomString;

import br.com.projetos.models.Usuario;
import br.com.projetos.repository.UsuarioRepository;
import br.com.projetos.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository ur;

	@Autowired
	private UsuarioService us;
	
	@GetMapping("/login")
	public String paginaLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}
	
	@PostMapping("/login")
	public String paginaLogin(@ModelAttribute Usuario usuario, Model model) {
		Usuario dados = ur.findByLogin(usuario.getLogin());
		if(dados != null) {
			model.addAttribute("usuario", dados.getNome());
			return "index";
		} else {
			return "errorPage";
		}
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
		try {
			
			us.atualizaSenha(null, token);
		}
		return "esqueceuSenha";
	}
	
	
}
