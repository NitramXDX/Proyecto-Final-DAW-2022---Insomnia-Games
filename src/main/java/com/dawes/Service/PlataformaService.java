package com.dawes.Service;

import java.util.Map;
import java.util.Optional;

import com.dawes.modelo.PlataformaVO;

public interface PlataformaService {

	<S extends PlataformaVO> S save(S entity);

	Optional<PlataformaVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<PlataformaVO> findAll();

	long count();

	void deleteById(Integer id);

	Map<String, Object> pagination(Map<String, Object> params, int i);

}