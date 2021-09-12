package py.com.sodep.mf.license_server.core.translators;

import org.springframework.stereotype.Component;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServerNotification;
import py.com.sodep.mf.license_server.dtos.FormServerNotificationDTO;

@Component
public class FormServerNotificationTranslatorImpl extends
		TranslatorImp<FormServerNotification, FormServerNotificationDTO> implements FormServerNotificationTranslator {

	@Override
	public FormServerNotificationDTO toDTO(FormServerNotification entity) {
		FormServerNotificationDTO dto = new FormServerNotificationDTO();
		dto.setServerId(entity.getFormServer().getId());
		dto.setId(entity.getId());
		dto.setNotificationType(entity.getNotificationType());
		dto.setReceivedAt(entity.getReceivedAt());
		dto.setRemoteAddress(entity.getRemoteAddress());
		dto.setMacAddress(entity.getMacAddress());
		dto.setServerLicenseId(entity.getServerLicenseId());
		return dto;
	}

	@Override
	public FormServerNotification fromDTO(FormServerNotificationDTO dto) {
		return fromDTO(dto, new FormServerNotification());
	}

	@Override
	public FormServerNotification fromDTO(FormServerNotificationDTO dto, FormServerNotification entity) {
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

}
