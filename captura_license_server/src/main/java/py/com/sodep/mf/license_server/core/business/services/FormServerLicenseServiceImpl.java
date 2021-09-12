package py.com.sodep.mf.license_server.core.business.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.core.persistence.repositories.FormServerLicenseRepository;
import py.com.sodep.mf.license_server.core.translators.FormServerLicenseTranslator;
import py.com.sodep.mf.license_server.core.translators.Translator;
import py.com.sodep.mf.license_server.dtos.FormServerLicenseDTO;

@Service
@Transactional
public class FormServerLicenseServiceImpl extends BaseServiceImpl<FormServerLicense, FormServerLicenseDTO> implements
		FormServerLicenseService {

	@Autowired
	private FormServerLicenseRepository repository;

	@Autowired
	private FormServerLicenseTranslator translator;

	protected FormServerLicenseServiceImpl() {
		super(FormServerLicense.class);
	}

	@Override
	protected void validateBeforeUpdating(FormServerLicense entity) {

	}

	@Override
	public JpaRepository<FormServerLicense, Long> repository() {
		return repository;
	}

	@Override
	public Translator<FormServerLicense, FormServerLicenseDTO> translator() {
		return translator;
	}

	@Override
	protected void validateBeforeSaving(FormServerLicense entity) {

	}

	@Override
	public List<FormServerLicense> listServerLicenses(Long serverId) {
		return repository.findByFormServerId(serverId);
	}

	@Override
	public FormServerLicense save(FormServerLicenseDTO dto) {
		dto.setCreationDate(new Date());
		FormServerLicense saved = super.save(dto);
		return saved;
	}
	
	
	
}
