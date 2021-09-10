package py.com.sodep.mf.license_server.core.translators;

import java.util.List;

import py.com.sodep.mf.license_server.dtos.DTO;

public interface Translator<E, D extends DTO> {
	
	D toDTO(E entity);

	List<D> toDTO(List<E> entities);

	E fromDTO(D dto);

	E fromDTO(D dto, E entity);

	List<E> fromDTO(List<D> dtos);

}
