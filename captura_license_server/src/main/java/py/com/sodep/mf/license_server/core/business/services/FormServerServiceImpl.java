package py.com.sodep.mf.license_server.core.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.repositories.FormServerRepository;
import py.com.sodep.mf.license_server.core.translators.FormServerTranslator;
import py.com.sodep.mf.license_server.core.translators.Translator;
import py.com.sodep.mf.license_server.dtos.FormServerDTO;

@Service
@Transactional
public class FormServerServiceImpl extends BaseServiceImpl<FormServer, FormServerDTO> implements FormServerService {

	@Autowired
	private FormServerTranslator translator;

	@Autowired
	private FormServerRepository repository;

	protected FormServerServiceImpl() {
		super(FormServer.class);
	}

	@Override
	protected void validateBeforeUpdating(FormServer entity) {

	}

	@Override
	public JpaRepository<FormServer, Long> repository() {
		return repository;
	}

	@Override
	public Translator<FormServer, FormServerDTO> translator() {
		return translator;
	}

	@Override
	protected void validateBeforeSaving(FormServer entity) {

	}

}
