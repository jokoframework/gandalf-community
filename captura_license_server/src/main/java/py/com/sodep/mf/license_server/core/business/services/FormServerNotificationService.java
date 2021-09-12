package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import org.springframework.data.repository.query.Param;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerNotification;
import py.com.sodep.mf.license_server.dtos.FormServerNotificationDTO;
import py.com.sodep.mobileforms.license.json.notification.MFFormServerNotification;

public interface FormServerNotificationService extends BaseService<FormServerNotification, FormServerNotificationDTO> {

	void save(FormServer formServer, String remoteAddress, MFFormServerNotification notification);

	List<FormServerNotificationDTO> listAllChronologically();

	List<FormServerNotificationDTO> listAllChronologically(@Param("formServerId") Long formServerId);

}
