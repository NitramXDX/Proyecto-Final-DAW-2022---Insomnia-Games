package com.dawes.controller;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dawes.Service.FileService;
import com.dawes.Service.PlataformaService;
import com.dawes.Service.ProductoService;
import com.dawes.modelo.PlataformaVO;

@Controller
@RequestMapping("/plataformas")
public class PlataformaController {
	
		@Autowired
		private PlataformaService ps;
		
		@Autowired
		private ProductoService pSer;
		
		@Autowired
		private FileService fs;
		
		
		//Persistencia de de la clase
		@RequestMapping("/admin/persistir")
		public String persistir(@ModelAttribute PlataformaVO obj, @RequestParam MultipartFile file, Model modelo) {
			try {
				//Carga de la imagen
				if(!file.isEmpty()) {
					if (ps.existsById(obj.getIdplataforma())) fs.fileDelete("src/main/resources/static/images/platforms", ps.findById(obj.getIdplataforma()).get().getImagen());
					
					fs.fileSave("src/main/resources/static/images/platforms", file, "imagen"+obj.getNombre().replaceAll(" ","").replaceAll("[^a-zA-Z0-9]", ""));
					obj.setImagen("imagen"+obj.getNombre().replaceAll(" ","").replaceAll("[^a-zA-Z0-9]", "")+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
				} else {
					//Control de campo vacio
					if(ps.existsById(obj.getIdplataforma()))
						obj.setImagen(ps.findById(obj.getIdplataforma()).get().getImagen());
					else
						obj.setImagen("default.jpg");
				}
				
				//Persistencia
				if (!ps.findById(obj.getIdplataforma()).isEmpty()) {
					ps.save(obj);
					String mensaje = "Plataforma modificada.";
					return "redirect:/plataformas/admin/modificarForm?mensaje="+mensaje+"&id="+obj.getIdplataforma();
				} else {
					ps.save(obj);
					String mensaje = "Plataforma '"+obj.getNombre()+"' guardada.";
					return "redirect:/plataformas/admin/insertarForm?mensaje="+mensaje;
				}
			} catch (Exception e) {
				String error = "ERROR: Error en la persistencia.";
				if(e.getCause() instanceof ConstraintViolationException) {
					error = "ERROR: Plataforma ya existente.";
					if (!ps.findById(obj.getIdplataforma()).isEmpty()) {
						return "redirect:/plataformas/admin/modificarForm?mensaje="+error+"&id="+obj.getIdplataforma();
					}
				return "redirect:/plataformas/admin/insertarForm?mensaje="+error;
				}
				if(e.getCause() instanceof DataException) {
					error = "ERROR: Longitud de campo superada.";
					if (!ps.findById(obj.getIdplataforma()).isEmpty()) {
						return "redirect:/plataformas/admin/modificarForm?mensaje="+error+"&id="+obj.getIdplataforma();
					}
					return "redirect:/plataformas/admin/insertarForm?mensaje="+error;	
				}
				return "redirect:/plataformas/admin/insertarForm?mensaje="+error;
			}
		}
		
		//Lista de todos los objetos de esta clase
		@RequestMapping("/admin")
		public String mostrar(@RequestParam Map<String, Object> params, Model modelo, String mensaje) {	
			
			Map<String, Object> paginas = ps.pagination(params, 6);
			modelo.addAttribute("plataformas",paginas.get("list"));
			modelo.addAttribute("pages", paginas.get("pages"));
			modelo.addAttribute("current", paginas.get("current"));
			modelo.addAttribute("next", paginas.get("next"));
			modelo.addAttribute("prev", paginas.get("prev"));
			modelo.addAttribute("last", paginas.get("last"));
			
	        modelo.addAttribute("byNombre", Comparator.comparing(PlataformaVO::getNombre));
	        modelo.addAttribute("mensaje", mensaje);
	        
			return "admin/plataformas/mostrar";
		}
		
		//Insertar un objeto (Form que luego persiste)
		@RequestMapping("/admin/insertarForm")
		public String insertar(Model modelo, String mensaje) {
			modelo.addAttribute("plataforma", new PlataformaVO());
			modelo.addAttribute("porductos", pSer.findAll());
			modelo.addAttribute("mensaje", mensaje);
			return "admin/plataformas/insertarForm";
		}
		
		//Modificar un objeto (Form que luego persiste) 
		@RequestMapping("/admin/modificarForm")
		public String modificar(@RequestParam int id, Model modelo, String mensaje) {
			try {
				modelo.addAttribute("plataforma",ps.findById(id).get());
				modelo.addAttribute("productos", pSer.findAll());
				modelo.addAttribute("mensaje", mensaje);
				return "admin/plataformas/modificarForm";
			} catch (NoSuchElementException e) {
				String error = "ERROR: La plataforma no existe.";
				return "redirect:/plataformas/admin?mensaje="+error;
			}	
		}
		
		//Delete
		@RequestMapping("/admin/eliminar")
		public String delete(@RequestParam int id) {
			try {
				String nombreImg = ps.findById(id).get().getImagen();
				String mensaje = "Plataforma '"+ps.findById(id).get().getNombre()+"' eliminada.";
				ps.deleteById(id);
				fs.fileDelete("src/main/resources/static/images/platforms", nombreImg);
				return "redirect:/plataformas/admin?mensaje="+mensaje;
			} catch (Exception e) {
				String error = "ERROR: La plataforma no existe.";
				if (e.getCause() instanceof ConstraintViolationException) {
					error = "ERROR: Esta plataforma cuenta con productos asignados.";
					return "redirect:/plataformas/admin?mensaje="+error;
				}
				return "redirect:/plataformas/admin?mensaje="+error;
			}		
		}
	
}
