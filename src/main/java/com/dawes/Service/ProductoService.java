package com.dawes.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dawes.modelo.ProductoVO;

public interface ProductoService {

	<S extends ProductoVO> S save(S entity);

	Optional<ProductoVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<ProductoVO> findAll();

	long count();

	void deleteById(Integer id);

	Optional<ProductoVO> findByNombreAndPlataforma(String nombre, String plataforma);
	
	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage);

	public List<ProductoVO> findByNombre(String nombre);

	
}