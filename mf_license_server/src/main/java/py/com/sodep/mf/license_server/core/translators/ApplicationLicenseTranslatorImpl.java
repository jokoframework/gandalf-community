package py.com.sodep.mf.license_server.core.translators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.business.exceptions.InvalidDTOException;
import py.com.sodep.mf.license_server.core.business.services.FormServerLicenseService;
import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.dtos.ApplicationLicenseDTO;

@Component
@Transactional
public class ApplicationLicenseTranslatorImpl extends TranslatorImp<ApplicationLicense, ApplicationLicenseDTO>
		implements ApplicationLicenseTranslator {

	@Autowired
	private FormServerLicenseService formServerLicenseService;

	@Override
	public ApplicationLicenseDTO toDTO(ApplicationLicense entity) {
		ApplicationLicenseDTO dto = new ApplicationLicenseDTO();
		FormServerLicense formServerLicense = entity.getFormServerLicense();
		if (formServerLicense != null) {
			dto.setFormServerLicenseId(formServerLicense.getId());
		}
		dto.setId(entity.getId());
		dto.setCreationDate(entity.getCreationDate());
		dto.setMaxDevices(entity.getMaxDevices());
		dto.setMaxUsers(entity.getMaxUsers());
		dto.setOwner(entity.getOwner());
		dto.setValidDays(entity.getValidDays());
		dto.setApplicationId(entity.getApplicationId());
		return dto;
	}

	@Override
	public ApplicationLicense fromDTO(ApplicationLicenseDTO dto) {
		return fromDTO(dto, new ApplicationLicense());
	}

	@Override
	public ApplicationLicense fromDTO(ApplicationLicenseDTO dto, ApplicationLicense entity) {
		Long formServerLicenseId = dto.getFormServerLicenseId();
		if (formServerLicenseId != null) {
			FormServerLicense formServerLicense = formServerLicenseService.findOne(formServerLicenseId);
			if (formServerLicense == null) {
				throw new InvalidDTOException("Invalid formServerLicenseId " + formServerLicenseId);
			}
			entity.setFormServerLicense(formServerLicense);
		}
		entity.setApplicationId(dto.getApplicationId());
		entity.setId(dto.getId());
		entity.setCreationDate(dto.getCreationDate());
		entity.setMaxDevices(dto.getMaxDevices());
		entity.setMaxUsers(dto.getMaxUsers());
		entity.setOwner(dto.getOwner());
		entity.setValidDays(dto.getValidDays());
		return entity;
	}

}
