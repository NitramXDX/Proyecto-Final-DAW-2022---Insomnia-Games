package com.dawes.ServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dawes.Service.FileService;

@Service
public class FileServiceImpl implements FileService {

	//Guardar un archivo indicando Url, el archivo pasado en formato MultipartFile y el nombre del archivo
	@Override
	public void fileSave(String url, MultipartFile file, String fileName) {
		Path dirImage = Paths.get(url); //Ruta donde almacenar la file
		String rutaAbs = dirImage.toFile().getAbsolutePath(); //Ruta absoluta donde almacenar la file
		try {
			byte[] bytesImg = file.getBytes(); //Transformación de la file en bytes
			Path rutaCompleta = Paths.get(rutaAbs + "//"+fileName+
					(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))); //Ruta absoluta que incluye el nombre del archivo
			Files.write(rutaCompleta, bytesImg); //Escritura de los bytes en la ruta indicada, al tener el mismo nombre que el archivo anterior se sobreescribe
			
		} catch (IOException e) {
			System.out.println("Error al guardar el archivo: "+e.getMessage()); //Mensaje de error!
		}
	}
	
	//Borrar un archivo indicando la url en la que se encuentra el archivo y el nombre del archivo
	@Override
	public boolean fileDelete(String url, String fileName) {
		
		if(fileName.equals("default.jpg")) return true; //Evita el borrado de la imagen por defecto
		
		Path dirFile = Paths.get(url+"/"+fileName);
		try {
			return Files.deleteIfExists(dirFile);
		} catch (IOException e) {
			System.out.println("No se ha podido borrar el archivo indicado: "+e.getMessage());
			return false;
		}
	}
	
}
