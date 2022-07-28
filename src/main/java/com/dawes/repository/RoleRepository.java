package com.dawes.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.RoleVO;

@Repository
public interface RoleRepository extends JpaRepository<RoleVO, Integer>{

	/* 	Método de paginación
	 * 
	 * params: Map de parametros que recive el controlador
	 * itemPerPage: int con la cantidad de items que se mostrarán por páginas 
	 * 
	 */
	public default Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		
		Map<String, Object> resultados = new HashMap<String, Object>();
		
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) -1 ) : 0;
		PageRequest pageRequest = PageRequest.of(page, itemPerPage);
		
		Page<RoleVO> pageItem = this.findAll(pageRequest);
		
		int totalPage = pageItem.getTotalPages();
		
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			resultados.put("pages", pages);	
		}
		
		resultados.put("list", pageItem.getContent());
		resultados.put("current", page + 1);
		resultados.put("next", page + 2);
		resultados.put("prev", page);
		resultados.put("last", totalPage);
		
		return resultados;
		
	}
	
	
	Optional<RoleVO> findByNombre(String nombre);

}
