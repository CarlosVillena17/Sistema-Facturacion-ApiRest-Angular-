package com.springboot.panecillos.app.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.panecillos.app.models.entity.Cliente;
import com.springboot.panecillos.app.models.entity.Region;
import com.springboot.panecillos.app.models.service.IClienteService;
import com.springboot.panecillos.app.models.service.IUploadFileService;
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {
	
	
	private final Logger log=LoggerFactory.getLogger(ClienteController.class);
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.listar();
	}
	
	@GetMapping("/clientes/page/{pag}")
	public Page<Cliente> index(@PathVariable Integer pag){
		return clienteService.listar(PageRequest.of(pag, 4));
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente=null;
		
		Map<String, Object> response=new HashMap<String, Object>();
		try {
			cliente=clienteService.findById(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Hubo un error en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(cliente==null) {
			response.put("mensaje", "No existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK );
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNew=null;
		Map<String, Object> response=new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			//List<String> errores=new ArrayList<String>();
			//for(FieldError err: result.getFieldErrors()) {
				//errores.add(err.getDefaultMessage());
			//}
			List<String> errores=result.getFieldErrors()
					.stream()
					.map( err-> 
						err.getField()+" "+  err.getDefaultMessage()
					)
					.collect(Collectors.toList());
			
			return new ResponseEntity<List<String>>(errores, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteNew=clienteService.save(cliente);
		}catch (DataAccessException e) {
			response.put("mensaje", "Hubo un error en realizar el insert en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//response.put("mensaje", "Cliente creado con exito");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(code = HttpStatus.CREATED) //201
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual=null;
		Cliente clienteUpdate=null;
		clienteActual=clienteService.findById(id);
		
		Map<String, Object> response=new HashMap<String, Object>();
		
		if(clienteActual==null) {
			response.put("mensaje", "No se encontró el cliente");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			clienteActual.setNombre(cliente.getNombre()); 
			clienteActual.setEmail(cliente.getEmail()); 
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setRegion(cliente.getRegion());
			clienteUpdate=clienteService.save(clienteActual);
			
			
		}catch (DataAccessException e) {
			response.put("mensaje", "Hubo un error al actualizar el cliente ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//response.put("mensaje", "Cliente actualizado con exito");
		response.put("cliente", clienteUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT) //204
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response=new HashMap<String, Object>();
		try {
			Cliente cliente=clienteService.findById(id);
			
			String nombreFotoAnterior=cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
			clienteService.delete(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Hubo un error al eliminar en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Cliente eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,
			@RequestParam("id") Long id){
		Map<String, Object> response=new HashMap<String, Object>();
		Cliente cliente=clienteService.findById(id);
		if(!archivo.isEmpty()) {
			String nombreArchivo=null;
			try {
				nombreArchivo=uploadFileService.copiar(archivo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				response.put("mensaje", "error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior=cliente.getFoto();
			
			uploadFileService.eliminar(nombreFotoAnterior);
			
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen");
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<org.springframework.core.io.Resource> verFoto(@PathVariable String nombreFoto){
		
		
		Resource recurso=null;
		try {
			recurso=uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpHeaders cabecera=new HttpHeaders(); 
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"");
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	@GetMapping("/clientes/regiones")
	public List<Region> regiones(){
		return clienteService.findAllRegiones();
	}
	
	
}
