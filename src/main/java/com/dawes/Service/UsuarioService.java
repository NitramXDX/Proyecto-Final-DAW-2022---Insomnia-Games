package com.dawes.Service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.dawes.modelo.UsuarioVO;

public interface UsuarioService {

	<S extends UsuarioVO> S save(S entity);

	Optional<UsuarioVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<UsuarioVO> findAll();

	long count();

	void deleteById(Integer id);

	Optional<UsuarioVO> findUsuarioVOByNick(String nick);
	
	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage);

}