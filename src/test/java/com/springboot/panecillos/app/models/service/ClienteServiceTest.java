package com.springboot.panecillos.app.models.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springboot.panecillos.app.models.dao.IClienteDao;
import com.springboot.panecillos.app.models.entity.Cliente;

@DataJpaTest
class ClienteServiceTest {
	
	@Autowired
	IClienteDao clienteDao;
	
	@Test
	void testListar() {
		List<Cliente> clientes=clienteDao.findAll();
		clientes.forEach(n-> System.out.println(n.getNombre()));
	}

	@Test
	void testSave() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testListarPageable() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAllRegiones() {
		fail("Not yet implemented");
	}

}
