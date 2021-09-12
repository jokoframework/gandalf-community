package py.com.sodep.mf.license_server.dtos;

import java.sql.Timestamp;

public class FormServerNotificationDTO extends DTO {

	private Long serverId;

	private Long serverLicenseId;

	private String macAddress;

	private String remoteAddress;

	private Integer notificationType;

	private Timestamp receivedAt;

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public Integer getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}

	public Timestamp getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Timestamp receivedAt) {
		this.receivedAt = receivedAt;
	}

	public Long getServerLicenseId() {
		return serverLicenseId;
	}

	public void setServerLicenseId(Long serverLicenseId) {
		this.serverLicenseId = serverLicenseId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

}
