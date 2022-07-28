package com.dawes.Service;

import java.util.Optional;

import com.dawes.modelo.RoleVO;

public interface RoleService {

	<S extends RoleVO> S save(S entity);

	Optional<RoleVO> findById(Integer id);

	Iterable<RoleVO> findAll();

	long count();

	void deleteById(Integer id);
	
	boolean existsById(Integer id);

	Optional<RoleVO> findByNombre(String nombre);

}