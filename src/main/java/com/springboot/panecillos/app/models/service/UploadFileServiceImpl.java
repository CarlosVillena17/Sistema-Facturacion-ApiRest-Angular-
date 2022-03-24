package com.springboot.panecillos.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	private final Logger log=LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String DIRECCTORIO_UPLOADS="uploads";
	@Override
	public Resource cargar(String nombre) throws MalformedURLException {
		Path rutaArchivo=getPath(nombre);
		Resource recurso=new UrlResource(rutaArchivo.toUri());
		log.info(rutaArchivo.toString());
		if(!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo=Paths.get("src/main/resources/static/images").resolve("nouser.png").toAbsolutePath();
			recurso=new UrlResource(rutaArchivo.toUri());
			log.error("Error, la imagen no se pudo cargar");
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		String nombreArchivo= UUID.randomUUID().toString()
				.concat(archivo.getOriginalFilename().replace(" ", ""));
		
		Path rutaArchivo=getPath(nombreArchivo);
		log.info(rutaArchivo.toString());
		//copia el archivo subido a la ruta escogida
		Files.copy(archivo.getInputStream(), rutaArchivo); 
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombre) {
		if(nombre!=null && nombre.length()>0) {
			Path rutaFotoAnterior=getPath(nombre);
			File archivoFotoAnteior=rutaFotoAnterior.toFile();
			if(archivoFotoAnteior.exists() &&  archivoFotoAnteior.canRead()) {
				archivoFotoAnteior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECCTORIO_UPLOADS).resolve(nombreFoto).toAbsolutePath();
	}

}
