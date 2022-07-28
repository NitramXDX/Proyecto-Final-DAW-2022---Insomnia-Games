package com.dawes.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import com.dawes.modelo.EjemplarVO;
import com.dawes.modelo.PlataformaVO;
import com.dawes.modelo.ProductoVO;


@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	
	//Variables únicas de la clase
	@Autowired
	private ProductoService ps;
	
	@Autowired
	private PlataformaService pServ;

	@Autowired
	private FileService fs;
	

	//Persistencia de de la clase
	@RequestMapping("/admin/persistir")
	public String persistir(@ModelAttribute ProductoVO obj, Model modelo, MultipartFile file) {
		try {
			//Subida de imagen
			if (!file.isEmpty()) {
				//Borrado de la imagen anterior: Url + Nombre del archivo
				if (ps.existsById(obj.getIdproducto())) fs.fileDelete("src/main/resources/static/images/products", ps.findById(obj.getIdproducto()).get().getImagen());
				fs.fileSave("src/main/resources/static/images/products", file, (obj.getNombre()).replaceAll(" ", "").replaceAll("[^a-zA-Z0-9]", ""));
				obj.setImagen((obj.getNombre()).replaceAll(" ", "").replaceAll("[^a-zA-Z0-9]", "")+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
				
			} else {
				if (ps.existsById(obj.getIdproducto()))
					obj.setImagen(ps.findById(obj.getIdproducto()).get().getImagen());
				 else 
					obj.setImagen("default.jpg");
			}
			
			//Diferenciar entre un modificar y una persistencia, para mostrar el mensaje apropiado
			if (!ps.findById(obj.getIdproducto()).isEmpty()) {
				ps.save(obj);
				String mensaje = obj.getNombre()+" modificado.";
				return "redirect:/productos/admin/modificarForm?mensaje="+mensaje+"&id="+obj.getIdproducto();
			} else {
				ps.save(obj);
				String mensaje = obj.getNombre()+" guardado.";
				return "redirect:/productos/admin/insertarForm?mensaje="+mensaje;
			}
		} catch (Exception e) {
			String error = "ERROR: Error en la persistencia.";
			if(e.getCause() instanceof ConstraintViolationException) {
				error = "ERROR: Producto ya existente.";
				if (!ps.findById(obj.getIdproducto()).isEmpty()) {
					return "redirect:/productos/admin/modificarForm?mensaje="+error+"&id="+obj.getIdproducto();
				}
				return "redirect:/productos/admin/insertarForm?mensaje="+error;
			}
			if(e.getCause() instanceof DataException) {
				error = "ERROR: Longitud de campo superada.";
				if (!ps.findById(obj.getIdproducto()).isEmpty()) {
					return "redirect:/productos/admin/modificarForm?mensaje="+error+"&id="+obj.getIdproducto();
				}
				return "redirect:/productos/admin/insertarForm?mensaje="+error;
			}
			return "redirect:/productos/admin/insertarForm?mensaje="+error;
		}
	}
	
	//Lista de todos los objetos de esta clase
	@RequestMapping("/admin")
	public String mostrar(@RequestParam Map<String, Object> params,Model modelo, String mensaje) { 
		
		Map<String, Object> paginas = ps.pagination(params, 6);
		modelo.addAttribute("productos",paginas.get("list"));
		modelo.addAttribute("pages", paginas.get("pages"));
		modelo.addAttribute("current", paginas.get("current"));
		modelo.addAttribute("next", paginas.get("next"));
		modelo.addAttribute("prev", paginas.get("prev"));
		modelo.addAttribute("last", paginas.get("last"));
		
		modelo.addAttribute("plataformas", pServ.findAll());
        modelo.addAttribute("byNombre", Comparator.comparing(ProductoVO::getNombre));
        modelo.addAttribute("mensaje", mensaje);
		return "admin/productos/mostrar";
		
	}
	
	//Insertar un objeto (Form que luego persiste)
	@RequestMapping("/admin/insertarForm")
	public String insertar(Model modelo, String mensaje) {
		modelo.addAttribute("producto", new ProductoVO());
		modelo.addAttribute("plataformas", pServ.findAll());
        modelo.addAttribute("byNombre", Comparator.comparing(PlataformaVO::getNombre));
		modelo.addAttribute("mensaje", mensaje);
		return "admin/productos/insertarForm";
	}
	
	//Modificar un objeto (Form que luego persiste) 
	@RequestMapping("/admin/modificarForm")
	public String modificar(@RequestParam int id, String mensaje, Model modelo) {
		try {
			modelo.addAttribute("producto",ps.findById(id).get());
			modelo.addAttribute("plataformas", pServ.findAll());
	        modelo.addAttribute("byNombre", Comparator.comparing(PlataformaVO::getNombre));
			modelo.addAttribute("mensaje", mensaje);
			return "admin/productos/modificarForm";
		} catch (Exception e) {
			String error = "ERROR: El producto no existe.";
			return "redirect:/productos/admin?mensaje="+error;
		}
	}
	
	//Delete
	@RequestMapping("/admin/eliminar")
	public String delete(@RequestParam int id) {
		try {
			boolean deletePermision = false;
			String nombreImg = ps.findById(id).get().getImagen();
			
			if(ps.findByNombre( ps.findById(id).get().getNombre()).size()==1 ) 
				deletePermision = true;
			
			String mensaje = "Producto '" +ps.findById(id).get().getNombre()+" - "+ps.findById(id).get().getPlataforma().getNombre()+"' eliminado.";
			ps.deleteById(id);
			
			if(deletePermision) fs.fileDelete("src/main/resources/static/images/products", nombreImg);
			
			return "redirect:/productos/admin?mensaje="+mensaje;
		} catch (Exception e) {
			String error = "ERROR: El producto no existe.";
			if (e.getCause() instanceof ConstraintViolationException) {
				error = "ERROR: Este producto cuenta con ejemplares asignados.";
				return "redirect:/productos/admin?mensaje="+error;
			}
			return "redirect:/productos/admin?mensaje="+error;
		}
	}

	//Ejemplares de un producto en especifico
	@RequestMapping("/admin/ejemplares")
	public String ejemplares(@RequestParam int id, String mensaje, Model modelo) {
		try {
			modelo.addAttribute("producto", ps.findById(id).get());
			
			//Preparando el objeto para la insercción
			EjemplarVO eje = new EjemplarVO();
			eje.setProducto((ProductoVO)modelo.getAttribute("producto"));
			modelo.addAttribute("ejemplar", eje);
			
			//Lista (Ordenado inversamente)
			List<EjemplarVO> ejemplares = ps.findById(id).get().getEjemplar();
			Collections.reverse(ejemplares);
			modelo.addAttribute("ejemplares", ejemplares);
			
			//Mensaje si existe
			modelo.addAttribute("mensaje", mensaje);
			return "admin/productos/ejemplaresProducto";
		} catch (NoSuchElementException e) {
			String error = "ERROR: El producto no existe.";
			return "redirect:/productos/admin?mensaje="+error;
		}
	}
	
	//AnonimousUSER PERMISSION
	//Detalle de producto
	@RequestMapping("detalle")
	public String detalleProducto(@RequestParam String nombre, @RequestParam String plataforma, Model modelo) {
		try {
			ProductoVO producto = ps.findByNombreAndPlataforma(nombre, plataforma).get();
			
			Double discountPrice = producto.getPrecio() - (producto.getDescuento()*producto.getPrecio())/100;
			discountPrice = Math.floor(discountPrice * 100) / 100;
			
			boolean existencias = ps.findByNombreAndPlataforma(nombre, plataforma).get().getEjemplar().size()>0;
			
			modelo.addAttribute("producto", producto);
			modelo.addAttribute("discountPrice", discountPrice);
			modelo.addAttribute("existencias", existencias);
			return "producto";
		} catch (Exception e) {
			return "redirect:/404";
		}
	}
	
	
}
