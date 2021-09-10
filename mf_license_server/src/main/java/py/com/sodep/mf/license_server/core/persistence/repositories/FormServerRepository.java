package py.com.sodep.mf.license_server.core.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;

public interface FormServerRepository extends JpaRepository<FormServer, Long> {

}
