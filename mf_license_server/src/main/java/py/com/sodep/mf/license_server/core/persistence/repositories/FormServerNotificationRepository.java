package py.com.sodep.mf.license_server.core.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServerNotification;

public interface FormServerNotificationRepository extends JpaRepository<FormServerNotification, Long> {

	@Query("SELECT n FROM FormServerNotification n ORDER BY n.receivedAt DESC")
	public List<FormServerNotification> listAllChronologically();

	@Query("SELECT n FROM FormServerNotification n WHERE n.formServer.id = :formServerId ORDER BY n.receivedAt DESC")
	public List<FormServerNotification> listAllChronologically(@Param("formServerId") Long formServerId);
	
}
