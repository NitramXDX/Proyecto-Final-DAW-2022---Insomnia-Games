package com.dawes.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dawes.ServiceImpl.UsuarioServiceImpl;

@Configuration //Indica que se altera la configuración
@EnableWebSecurity //Indica que configuración es la que se va a alterar
public class miseguridad extends WebSecurityConfigurerAdapter {

	@Autowired
	UsuarioServiceImpl us;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Autenticación 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(us);
	}
	
	
	//Este método permite encriptar la contraseña, dado que la seguridad no nos permite guardar contraseñas sin encriptar
	public String encripta(String password) {
		//Creamos un objeto BCryptPasswordEncoder a partir del encode de nuestra contraseña
		return passwordEncoder().encode(password);
	}
	
	
	//Método para comparar un string con una contraseña codificada (No se está usando)
	public boolean passCompara(String password, String encodedPass) {
		return passwordEncoder().matches(password, encodedPass);
	}
	
	
	//Autorización
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/*/admin/**").hasAuthority("ADMIN");
		http.authorizeRequests().antMatchers("/*/user/**").hasAnyAuthority("USER", "ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("ADMIN", "USER");
		http.formLogin().loginPage("/login");
		http.formLogin().defaultSuccessUrl("/afterLogin", true); //El controlador identifica el role del usuario y lo redirecciona
		http.logout().logoutSuccessUrl("/");
		http.csrf().disable();  //Activa y desactiva el paso intermedio del logout, para evitar ataques, también capa el método post
	}
	
}
