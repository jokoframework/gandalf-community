package py.com.sodep.mf.license_server.core.business.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.core.persistence.repositories.ApplicationLicenseRepository;
import py.com.sodep.mf.license_server.core.translators.ApplicationLicenseTranslator;
import py.com.sodep.mf.license_server.core.translators.Translator;
import py.com.sodep.mf.license_server.dtos.ApplicationLicenseDTO;

@Service
@Transactional
public class ApplicationLicenseServiceImpl extends BaseServiceImpl<ApplicationLicense, ApplicationLicenseDTO> implements
		ApplicationLicenseService {

	@Autowired
	private ApplicationLicenseTranslator translator;

	@Autowired
	private ApplicationLicenseRepository repository;

	protected ApplicationLicenseServiceImpl() {
		super(ApplicationLicense.class);
	}

	@Override
	public ApplicationLicense save(ApplicationLicenseDTO dto) {
		if (dto.getCreationDate() == null) {
			dto.setCreationDate(new Date());
		}
		return super.save(dto);
	}

	@Override
	protected void validateBeforeUpdating(ApplicationLicense entity) {

	}

	@Override
	public JpaRepository<ApplicationLicense, Long> repository() {
		return repository;
	}

	@Override
	public Translator<ApplicationLicense, ApplicationLicenseDTO> translator() {
		return translator;
	}

	private Object applicationCountLock = new Object();

	@Override
	protected void validateBeforeSaving(ApplicationLicense entity) {
		FormServerLicense formServerLicense = entity.getFormServerLicense();
		synchronized (applicationCountLock) {
			Long maxApplications = formServerLicense.getMaxApplications();
			List<ApplicationLicense> applicationLicenses = formServerLicense.getApplicationLicenses();
			if (applicationLicenses.size() > maxApplications) {
				throw new RuntimeException("Too many applications");
			}
		}
	}

	@Override
	public List<ApplicationLicense> list(Long formServerLicenseId) {
		return repository.findByFormServerLicenseId(formServerLicenseId);
	}
}
