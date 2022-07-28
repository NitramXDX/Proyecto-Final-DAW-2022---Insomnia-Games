package com.dawes.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.ProductoService;
import com.dawes.modelo.ProductoVO;
import com.dawes.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {
	@Autowired
	ProductoRepository pr;

	@Override
	public <S extends ProductoVO> S save(S entity) {
		return pr.save(entity);
	}

	@Override
	public Optional<ProductoVO> findById(Integer id) {
		return pr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return pr.existsById(id);
	}

	@Override
	public Iterable<ProductoVO> findAll() {
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

	@Override
	public Optional<ProductoVO> findByNombreAndPlataforma(String nombre, String plataforma) {
		return pr.findByNombreAndPlataforma(nombre, plataforma);
	}

	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return pr.pagination(params, itemPerPage);
	}
	
	public List<ProductoVO> findByNombre(String nombre) {
		return pr.findByNombre(nombre);
	}

	
}
