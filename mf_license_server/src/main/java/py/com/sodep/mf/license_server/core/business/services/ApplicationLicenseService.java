package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;
import py.com.sodep.mf.license_server.dtos.ApplicationLicenseDTO;

public interface ApplicationLicenseService extends BaseService<ApplicationLicense, ApplicationLicenseDTO> {

	List<ApplicationLicense> list(Long formServerLicenseId);
	
}
