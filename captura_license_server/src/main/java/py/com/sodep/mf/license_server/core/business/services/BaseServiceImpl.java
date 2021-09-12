package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.business.exceptions.InvalidEntityException;
import py.com.sodep.mf.license_server.core.persistence.entities.SodepEntity;
import py.com.sodep.mf.license_server.core.translators.Translator;
import py.com.sodep.mf.license_server.dtos.DTO;

@Transactional
public abstract class BaseServiceImpl<E extends SodepEntity, D extends DTO> implements BaseService<E, D> {

	private Class<E> clazz;

	protected BaseServiceImpl(Class<E> clazz) {
		this.clazz = clazz;
	}

	@Override
	public E findOne(Long id) {
		return repository().findOne(id);
	}

	@Override
	public void delete(Long id) {
		JpaRepository<E, Long> repo = repository();
		E entity = repo.findOne(id);
		if (entity == null) {
			throw new InvalidEntityException("Invalid Id: " + id);
		}
		repo.delete(id);
	}

	@Override
	public E save(D dto) {
		E fromDTO = translator().fromDTO(dto);
		saveValidation(fromDTO);
		return repository().save(fromDTO);
	}

	@Override
	public E update(D dto) {
		JpaRepository<E, Long> repo = repository();
		Translator<E, D> translator = translator();
		E entity = repo.findOne(dto.getId());
		if (entity == null) {
			throw new InvalidEntityException("Invalid Id: " + dto.getId());
		}
		E updatedEntity = translator.fromDTO(dto, entity);
		udpateValidation(updatedEntity);
		return updatedEntity;
	}

	private void udpateValidation(E entity) {
		InvalidEntityException ex = null;
		if (entity.getId() == null) {
			ex = new InvalidEntityException();
			ex.addInvalidProperty("id", "To update an instance, id must not be null");
		}
		validateBeforeUpdating(entity);
	}

	protected abstract void validateBeforeUpdating(E entity);

	@Override
	public Long count() {
		return repository().count();
	}

	@Override
	public List<E> findAll() {
		return repository().findAll();
	}

	public abstract JpaRepository<E, Long> repository();

	public abstract Translator<E, D> translator();

	private void saveValidation(E entity) {
		InvalidEntityException ex = null;
		if (entity.getId() != null) {
			ex = new InvalidEntityException();
			ex.addInvalidProperty("id", "To save a new instance, id must be null");

		}
		validateBeforeSaving(entity);
	}

	protected abstract void validateBeforeSaving(E entity);

}
