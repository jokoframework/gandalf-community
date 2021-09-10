package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.dtos.FormServerLicenseDTO;

public interface FormServerLicenseService extends BaseService<FormServerLicense, FormServerLicenseDTO> {

	List<FormServerLicense> listServerLicenses(Long serverId);
}
