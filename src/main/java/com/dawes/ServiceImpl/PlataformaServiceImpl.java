package com.dawes.ServiceImpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.PlataformaService;
import com.dawes.modelo.PlataformaVO;
import com.dawes.repository.PlataformaRepository;

@Service
public class PlataformaServiceImpl implements PlataformaService {
	@Autowired
	PlataformaRepository pr;

	@Override
	public <S extends PlataformaVO> S save(S entity) {
		return pr.save(entity);
	}

	@Override
	public Optional<PlataformaVO> findById(Integer id) {
		return pr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return pr.existsById(id);
	}

	@Override
	public Iterable<PlataformaVO> findAll() {
		return pr.findAll();
	}

	@Override
	public long count() {
		return pr.count();
	}

	@Override
	public void deleteById(Integer id) {
		pr.deleteById(id);
	}

	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return pr.pagination(params, itemPerPage);
	}
	
}
