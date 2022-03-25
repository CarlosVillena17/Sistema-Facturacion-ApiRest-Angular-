package com.springboot.panecillos.app.models.service;

import java.util.List;
import java.util.Optional;

import com.springboot.panecillos.app.models.dao.IFacturaDao;
import com.springboot.panecillos.app.models.entity.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.panecillos.app.models.dao.IClienteDao;
import com.springboot.panecillos.app.models.entity.Cliente;
import com.springboot.panecillos.app.models.entity.Region;

@Service
public class ClienteService implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDao;

	@Autowired
	private IFacturaDao facturaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> listar() {
		// TODO Auto-generated method stub
	 return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		Optional<Cliente> optional=clienteDao.findById(id); 
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> listar(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		// TODO Auto-generated method stub
		return clienteDao.findAllRegiones();
	}

	@Transactional(readOnly = true)
	@Override
	public Factura findFacturaById(Long id) {
		Optional<Factura> optional=facturaDao.findById(id);
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}

	@Override
	public Factura saveFactura(Factura factura) {
		return facturaDao.save(factura);
	}

	@Override
	public void deleteFacturaById(Long id) {
		facturaDao.deleteById(id);
	}

}
