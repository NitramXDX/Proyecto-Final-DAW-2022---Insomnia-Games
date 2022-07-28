package com.dawes.ServiceImpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.RoleService;
import com.dawes.modelo.RoleVO;
import com.dawes.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository rr;

	@Override
	public <S extends RoleVO> S save(S entity) {
		return rr.save(entity);
	}

	@Override
	public Optional<RoleVO> findById(Integer id) {
		return rr.findById(id);
	}

	@Override
	public Iterable<RoleVO> findAll() {
		return rr.findAll();
	}

	@Override
	public long count() {
		return rr.count();
	}

	@Override
	public void deleteById(Integer id) {
		rr.deleteById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return rr.existsById(id);
	}

	@Override
	public Optional<RoleVO> findByNombre(String nombre) {
		return rr.findByNombre(nombre);
	}

	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return rr.pagination(params, itemPerPage);
	}
	
}
