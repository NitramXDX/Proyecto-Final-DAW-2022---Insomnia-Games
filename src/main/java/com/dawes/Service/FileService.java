package com.dawes.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	//Guardar un archivo indicando Url, el archivo pasado en formato MultipartFile y el nombre del archivo
	void fileSave(String url, MultipartFile file, String fileName);

	//Borrar un archivo indicando la url en la que se encuentra
	boolean fileDelete(String url, String nombreArchivo);

}