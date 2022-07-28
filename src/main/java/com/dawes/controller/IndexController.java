package com.dawes.controller;

import java.security.Principal;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawes.Service.PlataformaService;
import com.dawes.Service.UsuarioService;
import com.dawes.modelo.PlataformaVO;

@Controller
@RequestMapping("")
public class IndexController {
	
	@Autowired
	private UsuarioService us;
	
	@Autowired
	private PlataformaService ps;
	
	
	@RequestMapping("/index")
	public String index() {
		//Implementar le nombre del usuario logueado una vez tengamos hecha toda la parte de seguridad
		return "index";
	}
	
	//Redirección en función del role del usuario
	@RequestMapping("/afterLogin")
	public String afterLogin(Principal principal) {
		if(us.findUsuarioVOByNick(principal.getName()).get().getRole().getNombre().equals("ADMIN")) {
			return "redirect:/productos/admin";
		} else {
			return "index.html";
		}
	}	
	
	@RequestMapping("/login")
	public String login(Principal principal, String mensaje, Model modelo) {
		if(principal != null) {
			return "redirect:/usuarios/user/perfil";
		}
		modelo.addAttribute("mensaje", mensaje);
		return "login";
	}
	
	@RequestMapping("/logout") 
	public String logout() {
		return "logout";
	}
	
	@RequestMapping("/tienda")
	public String shop(Model modelo, String filtro) {

        modelo.addAttribute("byNombre", Comparator.comparing(PlataformaVO::getNombre));
		modelo.addAttribute("plataformas", ps.findAll());
		modelo.addAttribute("platSelected", filtro);
		
		return "tienda";
	}
	
	@RequestMapping("/contacto") 
	public String contact(Model modelo, String mensajeEstado) {
		modelo.addAttribute("mensaje", mensajeEstado);
		return "contacto";
	}
	
	@RequestMapping("/FAQ") 
	public String faq() {
		return "faq";
	}
	
}
