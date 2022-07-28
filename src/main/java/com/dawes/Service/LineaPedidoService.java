package com.dawes.Service;

import java.util.List;
import java.util.Optional;

import com.dawes.modelo.EjemplarVO;
import com.dawes.modelo.LineaPedidoVO;
import com.dawes.modelo.PedidoVO;

public interface LineaPedidoService {

	<S extends LineaPedidoVO> S save(S entity);

	Optional<LineaPedidoVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<LineaPedidoVO> findAll();

	long count();

	void deleteById(Integer id);
	
	//Métodos personalizados
	List<EjemplarVO> findClavesDisponibles(String nombre, String plataforma);
	
	boolean asignarClavesLP(PedidoVO pedido);
}