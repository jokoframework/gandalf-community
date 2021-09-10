package py.com.sodep.mf.license_server.core.translators;

import java.util.ArrayList;
import java.util.List;

import py.com.sodep.mf.license_server.dtos.DTO;

public abstract class TranslatorImp<E, D extends DTO> implements Translator<E, D> {

	@Override
	public List<D> toDTO(List<E> entities) {
		List<D> list = new ArrayList<D>();
		for (E e : entities) {
			list.add(toDTO(e));
		}
		return list;
	}

	@Override
	public List<E> fromDTO(List<D> dtos) {
		List<E> list = new ArrayList<E>();
		for (D dto : dtos) {
			list.add(fromDTO(dto));
		}
		return list;
	}
}
