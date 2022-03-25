package com.springboot.panecillos.app.models.service;

import java.util.List;

import com.springboot.panecillos.app.models.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.panecillos.app.models.entity.Cliente;
import com.springboot.panecillos.app.models.entity.Region;

public interface IClienteService {
	public List<Cliente> listar(); 
	public Page<Cliente> listar(Pageable pageable); 
	public Cliente save(Cliente cliente);
	public void delete(Long id); 
	Cliente findById(Long id);
	public List<Region> findAllRegiones();
	public Factura findFacturaById(Long id);
	public Factura saveFactura(Factura factura);
	public void deleteFacturaById(Long id);
}
