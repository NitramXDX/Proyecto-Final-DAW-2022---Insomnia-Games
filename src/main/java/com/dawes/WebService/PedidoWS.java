package com.dawes.WebService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawes.DTO.LineaPedidoDTO;
import com.dawes.DTO.PedidoDTO;
import com.dawes.Service.LineaPedidoService;
import com.dawes.Service.PedidoService;
import com.dawes.Service.ProductoService;
import com.dawes.Service.UsuarioService;
import com.dawes.modelo.LineaPedidoVO;
import com.dawes.modelo.PedidoVO;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoWS {

	@Autowired PedidoService ps;
	@Autowired LineaPedidoService lps;
	@Autowired UsuarioService us;
	@Autowired ProductoService prods;
	
	//Insertar
	@PostMapping(path="/user/persistencia")
	public ResponseEntity<?> insertar(@RequestBody PedidoDTO pedidodto, Principal principal) {
		try {
			
			Double importeTotal = 0D;
			PedidoVO pedido = new PedidoVO();
			pedido.setUsuario(us.findUsuarioVOByNick(principal.getName()).get());
			ps.save(pedido);
			
			for (LineaPedidoDTO item : pedidodto.getLineaspedido()) {
				
				//Validador de producto existente
				if(!prods.findByNombreAndPlataforma(item.getNombre(), item.getPlataforma()).isEmpty()) {
					
					for(int count = item.getCantidad() ; count>0 ; count--) {
						LineaPedidoVO linea = new LineaPedidoVO();
						linea.setNombreprod(item.getNombre());
						linea.setPlataforma(item.getPlataforma());
						linea.setImporte(item.getPrecio());
						linea.setPedido(ps.findById(pedido.getIdpedido()).get());
						linea.setImagen(item.getImagen().substring(item.getImagen().lastIndexOf("/")+1,item.getImagen().length()));
						lps.save(linea);
						importeTotal += item.getPrecio();
					}
					
				}
				
			}
			
			importeTotal = Math.round(importeTotal * 100.0) / 100.0;
			pedido.setImporte(importeTotal);
			
			//Evita persistir pedidos sin lineas de pedido
			if(pedido.getImporte()==0D) {
								
				ps.deleteById(pedido.getIdpedido());
				
			} else {
				
				ps.save(pedido);
				
			}
			
			return new ResponseEntity<String> ("{}",HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println("Error en la insercción: "+e.getMessage());
			return new ResponseEntity<String> ("{ Error en la insercion: "+e.getMessage()+" }", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
