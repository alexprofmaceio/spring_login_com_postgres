package br.com.projetos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new LoginUsuarioService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider autenticador = new DaoAuthenticationProvider();
		autenticador.setUserDetailsService(userDetailsService());
		autenticador.setPasswordEncoder(passwordEncoder());
		return autenticador;
	}

	// Configurações de Autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(authenticationProvider());
	}
		
	// Configurações de Autorizações
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(HttpMethod.GET,"/registrar").permitAll()
			.antMatchers(HttpMethod.POST,"/registrar").permitAll()
			.antMatchers(HttpMethod.GET,"/recuperaSenha").permitAll()
			.antMatchers(HttpMethod.POST,"/recuperaSenha").permitAll()
			.antMatchers(HttpMethod.GET,"/novaSenha/{token}").permitAll()
			.antMatchers(HttpMethod.POST,"/novaSenha/{token}").permitAll()
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/js/**", "/css/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login").permitAll()
					.defaultSuccessUrl("/")
			.and()
			.logout()
        		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll() 
        		.logoutSuccessUrl("/login")
        .and()
        	.csrf().disable();
	}

	// Configurações de recursos estáticos(js, css, imagens, etc. )
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring().antMatchers("/static/**");
	}
}
