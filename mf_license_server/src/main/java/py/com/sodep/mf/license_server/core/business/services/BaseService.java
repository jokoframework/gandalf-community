package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import py.com.sodep.mf.license_server.dtos.DTO;

public interface BaseService<E, D extends DTO> {

	E findOne(Long id);

	void delete(Long id);

	E save(D dto);

	E update(D dto);

	Long count();

	List<E> findAll();
}
