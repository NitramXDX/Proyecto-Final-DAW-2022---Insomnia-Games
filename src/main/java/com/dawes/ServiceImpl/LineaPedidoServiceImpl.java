package com.dawes.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.LineaPedidoService;
import com.dawes.modelo.EjemplarVO;
import com.dawes.modelo.LineaPedidoVO;
import com.dawes.modelo.PedidoVO;
import com.dawes.repository.LineaPedidoRepository;

@Service
public class LineaPedidoServiceImpl implements LineaPedidoService {
	@Autowired
	LineaPedidoRepository lpr;

	@Override
	public <S extends LineaPedidoVO> S save(S entity) {
		return lpr.save(entity);
	}

	@Override
	public Optional<LineaPedidoVO> findById(Integer id) {
		return lpr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return lpr.existsById(id);
	}

	@Override
	public Iterable<LineaPedidoVO> findAll() {
		return lpr.findAll();
	}

	@Override
	public long count() {
		return lpr.count();
	}

	@Override
	public void deleteById(Integer id) {
		lpr.deleteById(id);
	}

	//Métodos personalizados
	@Override
	public List<EjemplarVO> findClavesDisponibles(String nombre, String plataforma) {
		return lpr.findClavesDisponibles(nombre, plataforma);
	}

	@Override
	public boolean asignarClavesLP(PedidoVO pedido) {
		return lpr.asignarClavesLP(pedido);
	}

	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return lpr.pagination(params, itemPerPage);
	}
	
	
	
	
	
}
