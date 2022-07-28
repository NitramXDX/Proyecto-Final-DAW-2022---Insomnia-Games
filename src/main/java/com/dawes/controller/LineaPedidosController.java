package com.dawes.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.Service.LineaPedidoService;
import com.dawes.Service.PedidoService;
import com.dawes.Service.ProductoService;
import com.dawes.modelo.LineaPedidoVO;
import com.dawes.modelo.PedidoVO;
import com.dawes.modelo.ProductoVO;

@Controller
@RequestMapping("/lineaPedidos")
public class LineaPedidosController {
	
	@Autowired
	private PedidoService ps;
	
	@Autowired
	private LineaPedidoService lps;
	
	@Autowired
	private ProductoService prods;
	
	//Mostrar las lineas de un pedido
	@RequestMapping("/admin")
	public String mostrar(@RequestParam Map<String, Object> params,@RequestParam int idpedido,Model modelo, String mensaje) {
		try {
			Map<String, Object> paginas = prods.pagination(params, 6);
			modelo.addAttribute("productos",paginas.get("list"));
			modelo.addAttribute("pages", paginas.get("pages"));
			modelo.addAttribute("current", paginas.get("current"));
			modelo.addAttribute("next", paginas.get("next"));
			modelo.addAttribute("prev", paginas.get("prev"));
			modelo.addAttribute("last", paginas.get("last"));
			
			modelo.addAttribute("pedido",ps.findById(idpedido).get());
			modelo.addAttribute("lineasPedido", ps.findById(idpedido).get().getLineapedido()); //Lineas actuales del pedido
			modelo.addAttribute("mensaje", mensaje);
			return "admin/lineaPedidos/mostrar";
		} catch (NoSuchElementException e) {
			String error = "ERROR: El pedido no existe.";
			return "redirect:/pedidos/admin?mensaje="+error;
		}
	}
	
	//Formulario para insertar un pedido con sus lineas de pedido
	@RequestMapping("/admin/asignar")
	public String insertarForm(Model modelo, @RequestParam int idpedido, @RequestParam int idproducto) {
		try {
			LineaPedidoVO lineapedido = new LineaPedidoVO(); //Linea Pedido que estamos creando
			ProductoVO producto = prods.findById(idproducto).get(); //Producto seleccionado
			PedidoVO pedido = ps.findById(idpedido).get(); //Pedido al que corresponde
			
			if(pedido.getEstado().equals("Finalizado") || pedido.getEstado().equals("Cancelado")) {
				String error = "ERROR: Pedido Finalizado o cancelado.";
				return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+error;
			}
			
			//Calculo del descuento
			Double precioFinal;
			if (producto.getDescuento() >0) precioFinal = producto.getPrecio() - (producto.getPrecio()*producto.getDescuento()/100);
			else precioFinal = producto.getPrecio();
			
			precioFinal = (double)Math.round(precioFinal * 100d) / 100d;
			
			//Parametros de la linea pedido (Son parametro que no están referidos pero han de coincidir con los productos de la base de datos)
			lineapedido.setNombreprod(producto.getNombre());
			lineapedido.setPlataforma(producto.getPlataforma().getNombre());
			lineapedido.setImporte(precioFinal);
			lineapedido.setPedido(pedido);
			lineapedido.setImagen(producto.getImagen());
			
			//Persistencia de la lineapedido
			lps.save(lineapedido);
			
			//Actualización del importe actual del Pedido
			if (pedido.getImporte()==null) {
				pedido.setImporte(precioFinal);
				ps.save(pedido);
			} else {
				pedido.setImporte(pedido.getImporte() + precioFinal);
				ps.save(pedido);
			}
			
			String mensaje = "Producto '"+prods.findById(idproducto).get().getNombre()+" - "+prods.findById(idproducto).get().getPlataforma().getNombre()+"' asignado al carrito.";
			
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+mensaje;
		} catch(Exception e) {
			String error = "ERROR: Error durante la asignación.";
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+error;
		}
	}
	
	//Quitar asignacion de la linea pedido
	@RequestMapping("/admin/eliminarLP")
	public String eliminar(@RequestParam int idlineaPedido, @RequestParam int idpedido) {
		try {
			PedidoVO pedido = ps.findById(idpedido).get(); //Pedido al que corresponde
			LineaPedidoVO lineapedido = lps.findById(idlineaPedido).get(); //Producto seleccionado
	
			if(pedido.getEstado().equals("Finalizado")) {
				String error = "ERROR: Pedido Finalizado o cancelado.";
				return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+error;
			}
			
			//Control de decimales
			Double precioFinal = pedido.getImporte() - lineapedido.getImporte();
			precioFinal = (double)Math.round(precioFinal * 100d) / 100d;
			pedido.setImporte(precioFinal);
			
			//Para evitar que queden decimales muy pequeños que no podemos manejar
			if (pedido.getImporte() <= 00.01D) pedido.setImporte(0D);
			ps.save(pedido);
			
			String mensaje = "Producto '"+lps.findById(idlineaPedido).get().getNombreprod()+" - "+lps.findById(idlineaPedido).get().getPlataforma()+"' eliminado del carrito.";
			
			
			lps.deleteById(idlineaPedido);
			
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+mensaje;
			
		} catch(NoSuchElementException e) {
			String error = "ERROR: La linea no existe.";
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+error;		
		}
	}
	
	
	//Asignar Ejemplares a las lineas de producto
	@RequestMapping("/admin/asignarClaves")
	public String asignarClaves(@RequestParam int idpedido) {
	
		try {
			PedidoVO pedido = ps.findById(idpedido).get();
			String mensaje = "";
			//Si el pedido no está pendiente se evita realizar la asignación
			if(!pedido.getEstado().equals("Pendiente")) {
				mensaje ="ERROR: Pedido finalizado o cancelado.";
				return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+mensaje;
			}
			if(lps.asignarClavesLP(pedido)) {
				pedido.setEstado("Finalizado");
				ps.save(pedido);
				mensaje = "Claves asignadas correctamente.";
			} else {
				mensaje = "ERROR: No hay suficientes existencias.";
			}
			
			if(pedido.getLineapedido().size()==0) {
				mensaje = "ERROR: El pedido no contiene productos.";
			}
			
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+mensaje;
		} catch (Exception e) {
			String error = "ERROR: Error en la asignación de claves.";
			return "redirect:/lineaPedidos/admin?idpedido="+idpedido+"&mensaje="+error;
		}
	}
	
	//USER PERMISSION
	@RequestMapping("/user/detallePedido")
	public String detallePedido(@RequestParam int idpedido,Model modelo, String mensaje, Principal principal) {
		try {
			//Medida de seguridad para evitar que otros usuarios vean los pedidos de otros
			if(!principal.getName().equals(ps.findById(idpedido).get().getUsuario().getNick())) return "404";
			modelo.addAttribute("pedido",ps.findById(idpedido).get());
			modelo.addAttribute("lineasPedido", ps.findById(idpedido).get().getLineapedido()); //Lineas actuales del pedido
			List<ProductoVO> productos = (List<ProductoVO>)prods.findAll();
			Collections.reverse(productos);
			modelo.addAttribute("productos", productos); //Productos disponibles para asignar al carrito
			modelo.addAttribute("mensaje", mensaje);
			return "user/detallePedido";
		} catch (NoSuchElementException e) {
			String error = "ERROR: El pedido no existe.";
			return "redirect:/usuarios/user/usuarioPedidos?mensaje="+error;
		}
	}
}
