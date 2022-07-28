package com.dawes.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.EjemplarVO;
import com.dawes.modelo.LineaPedidoVO;
import com.dawes.modelo.PedidoVO;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedidoVO, Integer>{

	
	//Comprobador unitario de stock (Devuelve una lista de ejemplares que coinciden con el producto/plataforma buscado)
	@Query("select ej from EjemplarVO ej where ej.producto.nombre = ?1 and ej.producto.plataforma.nombre = ?2 and "
			+ "ej not in (select lp.ejemplar from LineaPedidoVO lp)")
	List<EjemplarVO> findClavesDisponibles(String nombre, String plataforma); //No funciona con producto
	
	
	//Asignador de claves
	default boolean asignarClavesLP(PedidoVO pedido) {
		List<LineaPedidoVO> lps = pedido.getLineapedido();
		
		//Si no tiene lineas de pedido devuelve false
		if(lps.size()==0) return false;
		
		//Si no hay stock, devuelve false
		HashMap<String, Integer> lineas = new HashMap<String, Integer>();
		for (LineaPedidoVO lp : lps) {
			//Carga de cantidades de cada producto/lineapededido
			if (lineas.containsKey(lp.getNombreprod()+lp.getPlataforma())) {
				lineas.put(lp.getNombreprod()+lp.getPlataforma(),lineas.get(lp.getNombreprod()+lp.getPlataforma())+1);
			} else {
				lineas.put(lp.getNombreprod()+lp.getPlataforma(), 1);
			}
			
			//Si hay más solicitudes de ejemplares que ejemplares registrados devuelve null
			if(findClavesDisponibles(lp.getNombreprod(), lp.getPlataforma()).size()<lineas.get(lp.getNombreprod()+lp.getPlataforma())) {
				return false;
			}
		};
		
		//Si se cumple lo anterior se asignan las claves
		for (LineaPedidoVO lp : lps) {
			List<EjemplarVO> ejemplares = findClavesDisponibles(lp.getNombreprod(), lp.getPlataforma());
			lp.setEjemplar(ejemplares.get(0));
			save(lp);
		};

		
		return true;
	}
	
	/* 	Método de paginación
	 * 
	 * params: Map de parametros que recive el controlador
	 * itemPerPage: int con la cantidad de items que se mostrarán por páginas 
	 * 
	 */
	public default Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		
		Map<String, Object> resultados = new HashMap<String, Object>();
		
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) -1 ) : 0;
		PageRequest pageRequest = PageRequest.of(page, itemPerPage);
		
		Page<LineaPedidoVO> pageItem = this.findAll(pageRequest);
		
		int totalPage = pageItem.getTotalPages();
		
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			resultados.put("pages", pages);	
		}
		
		resultados.put("list", pageItem.getContent());
		resultados.put("current", page + 1);
		resultados.put("next", page + 2);
		resultados.put("prev", page);
		resultados.put("last", totalPage);
		
		return resultados;
		
	}
}
