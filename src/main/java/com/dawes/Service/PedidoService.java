package com.dawes.Service;

import java.util.Map;
import java.util.Optional;

import com.dawes.modelo.PedidoVO;

public interface PedidoService {

	<S extends PedidoVO> S save(S entity);

	Optional<PedidoVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<PedidoVO> findAll();

	long count();

	void deleteById(Integer id);
	
	Map<String, Object> pagination(Map<String, Object> params, int i);

}