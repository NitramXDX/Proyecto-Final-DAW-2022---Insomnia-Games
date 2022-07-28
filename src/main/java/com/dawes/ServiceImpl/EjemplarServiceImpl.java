package com.dawes.ServiceImpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.EjemplarService;
import com.dawes.modelo.EjemplarVO;
import com.dawes.repository.EjemplarRepository;

@Service
public class EjemplarServiceImpl implements EjemplarService {
	@Autowired
	EjemplarRepository er;

	@Override
	public <S extends EjemplarVO> S save(S entity) {
		return er.save(entity);
	}

	@Override
	public Optional<EjemplarVO> findById(Integer id) {
		return er.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return er.existsById(id);
	}

	@Override
	public Iterable<EjemplarVO> findAll() {
		return er.findAll();
	}

	@Override
	public long count() {
		return er.count();
	}

	@Override
	public void deleteById(Integer id) {
			er.deleteById(id);
	}

	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return er.pagination(params, itemPerPage);
	}
	
	
	
	
}
