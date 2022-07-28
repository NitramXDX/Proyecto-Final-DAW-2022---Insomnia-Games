package com.dawes.WebService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawes.DTO.ProductoDTO;
import com.dawes.Service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductosWS {
	
		@Autowired ProductoService ps;
		
		@GetMapping("/")
		public ResponseEntity<?> allProducts() {
			List<ProductoDTO> productsList = new ArrayList<ProductoDTO>();
			
			ps.findAll().forEach((product) -> {
				productsList.add(new ProductoDTO(product.getImagen(), product.getNombre(), product.getPlataforma().getNombre(), product.getPrecio(), product.getDescuento(), product.getPlataforma().getImagen()));
			});
			
			return new ResponseEntity<List<ProductoDTO>>(productsList, HttpStatus.OK);
		}
}
