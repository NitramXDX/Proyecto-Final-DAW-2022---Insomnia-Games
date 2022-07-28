package com.dawes.Service;

import java.util.Optional;

import com.dawes.modelo.EjemplarVO;

public interface EjemplarService {

	<S extends EjemplarVO> S save(S entity);

	Optional<EjemplarVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<EjemplarVO> findAll();

	long count();

	void deleteById(Integer id);

}