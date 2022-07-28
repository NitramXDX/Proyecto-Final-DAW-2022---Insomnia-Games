package com.dawes.Service;

import org.springframework.stereotype.Service;

@Service
public interface SeguridadService {

	String encripta(String password);

}