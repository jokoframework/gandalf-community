package py.com.sodep.mf.license_server.core.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;

public interface FormServerLicenseRepository extends JpaRepository<FormServerLicense, Long> {

	List<FormServerLicense> findByFormServerId(Long formServerId);
	
}
