package com.dawes.controller;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.Service.EjemplarService;
import com.dawes.Service.ProductoService;
import com.dawes.modelo.EjemplarVO;
import com.dawes.modelo.ProductoVO;

@Controller
@RequestMapping("/ejemplares")
public class EjemplarController {
	
			
			@Autowired
			private EjemplarService es;
			
			@Autowired
			private ProductoService ps;
			
			
			//Insertar un ejemplar se realiza desde el controlador de productos
			
			//Persistencia de un ejempplar
			@ExceptionHandler
			@RequestMapping("/admin/persistir")
			public String persistir(@ModelAttribute EjemplarVO obj, Model modelo) {
				int idproducto = obj.getProducto().getIdproducto();
				String mensaje = "Ejemplar modificado.";
				try {
					if(!es.findById(obj.getIdejemplar()).isEmpty()) {
						es.save(obj);
						return "redirect:/ejemplares/admin/modificarForm?id="+obj.getIdejemplar()+"&mensaje="+mensaje;
					} else {
						es.save(obj);
						mensaje = "Ejemplar agregado.";
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+mensaje;
					}
				} catch(Exception e) {
					String error = "ERROR: Error en la persistencia.";
					if(e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Clave ya existente.";
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;
					}
					if(e.getCause() instanceof NoSuchElementException) {
						error = "ERROR: El ejemplar no existe.";
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;	
					}
					if(e.getCause() instanceof DataException) {
						error = "ERROR: Longitud de campo superada.";
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;	
					}
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;
				}
			}
			
			//Modificar un Ejemplar (Form que luego persiste) 
			@RequestMapping("/admin/modificarForm")
			public String modificar(@RequestParam int id, String mensaje, Model modelo) {
				try {
					modelo.addAttribute("ejemplar",es.findById(id).get());
					modelo.addAttribute("productos", ps.findAll());
			        modelo.addAttribute("byNombre", Comparator.comparing(ProductoVO::getNombre));
					modelo.addAttribute("mensaje", mensaje);
					return "admin/ejemplares/modificarForm";
				} catch (NoSuchElementException e) {
					String error = "ERROR: El ejemplar no existe.";
					return "redirect:/productos/admin?mensaje="+error;
				}	
			}
			
			//Borrar un ejemplar
			@RequestMapping("/admin/eliminar")
			public String delete(@RequestParam int id) {
				try {
					//referencia del producto para la siguiente vista
					int idproducto = es.findById(id).get().getProducto().getIdproducto();
					//Mensaje de reporte
					String mensaje = "Ejemplar "+es.findById(id).get().getClave()+" eliminado.";
					//Borrado
					es.deleteById(id);
					return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+mensaje;
				} catch(Exception e) {
					String error = "ERROR: Error en el borrado.";
					int idproducto;
					//Try/Catch necesario ya que se intenta recuperar un id para el return a raiz de un elemento que no existe
					//Necesario cuando la entidad dependa directamente de otra, como es en el caso del ejemplar para recuperar el producto
						try {
							idproducto = es.findById(id).get().getProducto().getIdproducto();
						} catch (Exception e2) {
							error = "ERROR: El ejemplar no existe.";
							return "redirect:/productos/admin?id="+id+"&mensaje="+error;
						} 
					if(e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Clave asignada a un pedido.";
						return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;
					}
					return "redirect:/productos/admin/ejemplares?id="+idproducto+"&mensaje="+error;
				}
			}
}
