package com.dawes.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.Service.SeguridadService;
import com.dawes.seguridad.miseguridad;

@Service
public class SeguridadServicesImpl implements SeguridadService {

	
	@Autowired
	miseguridad ms;

	@Override
	public String encripta(String password) {
		return ms.encripta(password);
	}
	
}
