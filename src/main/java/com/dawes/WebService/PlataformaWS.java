package com.dawes.WebService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawes.DTO.PlataformaDTO;
import com.dawes.Service.PlataformaService;

@RestController
@RequestMapping("/api/plataformas")
public class PlataformaWS {
	
	@Autowired PlataformaService ps;
	
	@GetMapping("/")
	public ResponseEntity<?> allPlatforms() {
		List<PlataformaDTO> platformsList = new ArrayList<PlataformaDTO>();
		
		ps.findAll().forEach((platform) -> {
			platformsList.add(new PlataformaDTO(platform.getNombre(), platform.getImagen()));
		});
		
		return new ResponseEntity<List<PlataformaDTO>>(platformsList, HttpStatus.OK);
	}
	
}
