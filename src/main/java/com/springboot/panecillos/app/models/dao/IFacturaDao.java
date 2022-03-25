package com.springboot.panecillos.app.models.dao;

import com.springboot.panecillos.app.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura,Long> {

}
