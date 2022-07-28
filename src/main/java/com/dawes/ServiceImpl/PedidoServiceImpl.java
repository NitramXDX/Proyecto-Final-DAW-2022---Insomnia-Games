package com.dawes.ServiceImpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.PedidoService;
import com.dawes.modelo.PedidoVO;
import com.dawes.repository.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	PedidoRepository pr;

	@Override
	public <S extends PedidoVO> S save(S entity) {
		return pr.save(entity);
	}

	@Override
	public Optional<PedidoVO> findById(Integer id) {
		return pr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return pr.existsById(id);
	}	

	@Override
	public Iterable<PedidoVO> findAll() {
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
