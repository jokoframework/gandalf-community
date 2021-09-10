package py.com.sodep.mf.license_server.core.business.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerNotification;
import py.com.sodep.mf.license_server.core.persistence.repositories.FormServerNotificationRepository;
import py.com.sodep.mf.license_server.core.translators.FormServerNotificationTranslator;
import py.com.sodep.mf.license_server.core.translators.Translator;
import py.com.sodep.mf.license_server.dtos.FormServerNotificationDTO;
import py.com.sodep.mobileforms.license.json.notification.MFFormServerNotification;

@Service
@Transactional
public class FormServerNotificationServiceImpl extends
		BaseServiceImpl<FormServerNotification, FormServerNotificationDTO> implements FormServerNotificationService {

	@Autowired
	private FormServerNotificationTranslator translator;

	@Autowired
	private FormServerNotificationRepository repository;

	protected FormServerNotificationServiceImpl() {
		super(FormServerNotification.class);
	}

	@Override
	public JpaRepository<FormServerNotification, Long> repository() {
		return repository();
	}

	@Override
	public Translator<FormServerNotification, FormServerNotificationDTO> translator() {
		return translator();
	}

	@Override
	protected void validateBeforeUpdating(FormServerNotification entity) {

	}

	@Override
	protected void validateBeforeSaving(FormServerNotification entity) {

	}

	@Override
	public void save(FormServer fs, String remoteAddress, MFFormServerNotification mfNotification) {
		FormServerNotification notification = new FormServerNotification();
		notification.setServerLicenseId(mfNotification.getServerLicenseId());
		notification.setMacAddress(mfNotification.getMacAddress());
		notification.setFormServer(fs);
		notification.setRemoteAddress(remoteAddress);
		notification.setNotificationType(mfNotification.getNotificationType());
		repository.save(notification);
	}

	@Override
	public List<FormServerNotificationDTO> listAllChronologically() {
		List<FormServerNotification> listAllChronologically = repository.listAllChronologically();
		return translator.toDTO(listAllChronologically);
	}

	@Override
	public List<FormServerNotificationDTO> listAllChronologically(Long formServerId) {
		List<FormServerNotification> listAllChronologically = repository.listAllChronologically(formServerId);
		return translator.toDTO(listAllChronologically);
	}
}
