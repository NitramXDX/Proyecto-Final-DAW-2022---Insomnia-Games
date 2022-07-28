package com.dawes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dawes.Service.EjemplarService;
import com.dawes.Service.LineaPedidoService;
import com.dawes.Service.PedidoService;
import com.dawes.Service.ProductoService;
import com.dawes.Service.RoleService;
import com.dawes.Service.UsuarioService;
import com.dawes.modelo.RoleVO;
import com.dawes.modelo.UsuarioVO;

@SpringBootTest
class ProyectoDawFinalApplicationTests {

	// 1º Usuario findUsuarioVOByNick
	// 2º Role findByNombre
	// 3º Producto findByNombreAndPlataforma
	//PENDIENTE:
	//Pedido findLPS
	//Linea pedido: asignarClaves, findClavesDisponibles
	
	@Autowired UsuarioService us;
	@Autowired RoleService rs;
	@Autowired EjemplarService es;
	@Autowired PedidoService ps;
	@Autowired LineaPedidoService lps;
	@Autowired ProductoService  prods;
	
	static UsuarioVO usuario = new UsuarioVO();
	static RoleVO role = new RoleVO(0, "Prueba", null);
	
	@Test
	void contextLoads() {
				
	}
	
	//USUARIOS
	@Test 
	public void test01() {
		usuario.setNick("PruebaUsuario");
		us.save(usuario);
		assertEquals("PruebaUsuario", us.findUsuarioVOByNick("PruebaUsuario").get().getNick());
	}
	
	//ROLES
	@Test
	public void test02() {
		rs.save(role);
		assertEquals("Prueba",rs.findByNombre("Prueba").get().getNombre());
	}
	
	//PRODUCTOS
	@Test
	public void test03() {
		assertEquals("Elden Ring", prods.findByNombreAndPlataforma("Elden Ring", "PC").get().getNombre());
	}
	
}
