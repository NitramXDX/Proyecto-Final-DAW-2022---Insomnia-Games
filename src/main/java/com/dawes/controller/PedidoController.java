package com.dawes.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.Service.PedidoService;
import com.dawes.modelo.PedidoVO;


@Controller
@RequestMapping("/pedidos")
public class PedidoController {
	
		
		@Autowired
		private PedidoService ps;
		
		
		//Persistencia de de la clase
		@RequestMapping("/admin/persistir")
		public String persistir(@ModelAttribute PedidoVO pedido, Model modelo) {
			try {
			ps.save(pedido);
			String mensaje = "Pedido 'ID:"+pedido.getIdpedido()+"' agregado.";
			return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+pedido.getUsuario().getIdusuario()+"&mensaje="+mensaje;
			} catch (Exception e) {
				String error = "ERROR: Error al generar el pedido.";
				if(e.getCause() instanceof NoSuchElementException) {
					error = "ERROR: El pedido no existe.";
					return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+pedido.getUsuario().getIdusuario()+"&mensaje="+error;	
				}
				return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+pedido.getUsuario().getIdusuario()+"&mensaje="+error;
			}

		}
		
		//Lista de todos los objetos de esta clase
		@RequestMapping("/admin")
		public String mostrar(@RequestParam Map<String, Object> params, Model modelo, String mensaje) {
			List<PedidoVO> pedidos = (List<PedidoVO>)ps.findAll();
			Collections.reverse(pedidos);
			modelo.addAttribute("pedidos", pedidos);
	        modelo.addAttribute("byFecha", Comparator.comparing(PedidoVO::getFechacompra));
			modelo.addAttribute("mensaje", mensaje);
			return "admin/pedidos/mostrar";
		}
		
		//Delete
		@RequestMapping("/admin/eliminar")
       public String delete(@RequestParam int id) {
	       try {
	               int idusuario = ps.findById(id).get().getUsuario().getIdusuario();
	               String mensaje = "Pedido 'ID:"+ps.findById(id).get().getIdpedido()+"' eliminado.";
	               ps.deleteById(id);
	               return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+idusuario+"&mensaje="+mensaje;
	       } catch(Exception e) {
	
	               String error = "ERROR: Error en el borrado.";
	               int idusuario;
	
	               //Try/Catch necesario ya que se intenta recuperar un id para el return a raiz de un elemento que no existe
	               //Necesario cuando la entidad dependa directamente de otra, como es en el caso del ejemplar para recuperar el producto
	               try {
	                       idusuario = ps.findById(id).get().getUsuario().getIdusuario();
	               } catch (Exception e2) {
	                       error = "ERROR: Pedido no existente.";
	                       return "redirect:/pedidos/admin?mensaje="+error;
	               }
	               if(e.getCause() instanceof ConstraintViolationException) {
	                       idusuario = ps.findById(id).get().getUsuario().getIdusuario();
	                       error = "ERROR: El pedido contiene productos.";
	                       return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+idusuario+"&mensaje="+error;
	               }
	               return "redirect:/usuarios/admin/usuarioPedidos?idusuario="+idusuario+"&mensaje="+error;
	       }
      }

		
		//Cancelar Pedido
		@RequestMapping("/admin/cancelar")
		public String cancelarPedido(@RequestParam int id) {
			try {
				PedidoVO pedido = ps.findById(id).get();
				String mensaje="Pedido Cancelado o Finalizado."; //Mensaje por defecto en caso de que no llegar a realizar el borado
				//Medida de seguridad, en principio por sistema no se puede solicitar la cancelación de un pedido finalizado
				if (!pedido.getEstado().equals("Finalizado"))  {
					pedido.setEstado("Cancelado");
					mensaje = "Pedido 'ID:"+id+"' cancelado.";
				}
				ps.save(pedido);
				return "redirect:/pedidos/admin?mensaje="+mensaje;
			} catch (NoSuchElementException e) {
				String error = "ERROR: El pedido no existe.";
				return "redirect:/pedidos/admin?mensaje="+error;
			}
		}
		
		//Vista de Trámite de pedido
		@RequestMapping("/user/tramitar")
		public String tramitar(String mensaje) {
		  
			return "user/tramitar";
		} 
		
}
