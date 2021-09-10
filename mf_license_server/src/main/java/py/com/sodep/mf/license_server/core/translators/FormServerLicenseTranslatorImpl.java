package py.com.sodep.mf.license_server.core.translators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.business.exceptions.InvalidDTOException;
import py.com.sodep.mf.license_server.core.business.exceptions.InvalidEntityException;
import py.com.sodep.mf.license_server.core.business.services.FormServerService;
import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.dtos.ApplicationLicenseDTO;
import py.com.sodep.mf.license_server.dtos.FormServerLicenseDTO;

@Component
@Transactional
public class FormServerLicenseTranslatorImpl extends TranslatorImp<FormServerLicense, FormServerLicenseDTO> implements
		FormServerLicenseTranslator {

	@Autowired
	private FormServerService formServerService;

	@Autowired
	private ApplicationLicenseTranslator applicationLicenseTranslator;
	
	@Override
	public FormServerLicenseDTO toDTO(FormServerLicense entity) {
		FormServerLicenseDTO dto = new FormServerLicenseDTO();
		FormServer fs = entity.getFormServer();
		if (fs == null) {
			throw new InvalidEntityException("formServer is null");
		}
		dto.setServerId(entity.getFormServer().getId());
		dto.setId(entity.getId());
		dto.setCreationDate(entity.getCreationDate());
		dto.setValidDays(entity.getValidDays());
		dto.setMaxApplications(entity.getMaxApplications());
		ApplicationLicense defaultApplicationLicense = entity.getDefaultApplicationLicense();
		ApplicationLicenseDTO defaultApplicationLicenseDTO = applicationLicenseTranslator.toDTO(defaultApplicationLicense);
		dto.setDefaultApplicationLicense(defaultApplicationLicenseDTO);
		return dto;
	}

	@Override
	public FormServerLicense fromDTO(FormServerLicenseDTO dto) {
		return fromDTO(dto, new FormServerLicense());
	}

	@Override
	public FormServerLicense fromDTO(FormServerLicenseDTO dto, FormServerLicense entity) {
		Long serverId = dto.getServerId();
		if (serverId == null) {
			throw new InvalidDTOException("serverId is null");
		}

		FormServer fs = formServerService.findOne(serverId);
		if (fs == null) {
			throw new InvalidDTOException("Invalid serverId");
		}
		ApplicationLicenseDTO defaultApplicationLicenseDTO = dto.getDefaultApplicationLicense();
		ApplicationLicense defaultApplicationLicense = applicationLicenseTranslator.fromDTO(defaultApplicationLicenseDTO);
		entity.setDefaultApplicationLicense(defaultApplicationLicense);
		entity.setFormServer(fs);
		entity.setId(dto.getId());
		entity.setCreationDate(dto.getCreationDate());
		entity.setValidDays(dto.getValidDays());
		entity.setMaxApplications(dto.getMaxApplications());
		return entity;
	}
}
