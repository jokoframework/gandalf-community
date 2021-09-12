package py.com.sodep.mf.license_server.core.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;

public interface ApplicationLicenseRepository extends JpaRepository<ApplicationLicense, Long> {

	List<ApplicationLicense> findByFormServerLicenseId(Long formServerLicenseId);
	
}
