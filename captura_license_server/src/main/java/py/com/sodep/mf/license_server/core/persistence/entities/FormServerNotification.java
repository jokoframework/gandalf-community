package py.com.sodep.mf.license_server.core.persistence.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@Entity
@Table(schema = "public", name = "form_server_notifications")
@org.hibernate.annotations.Table(appliesTo = "form_server_notifications", indexes = { @Index(name = "form_server_IX", columnNames = "form_server_id"), })
@SequenceGenerator(name = "sequence_form_server_notifications", sequenceName = "sequence_form_server_notifications")
public class FormServerNotification extends SodepEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_form_server_notifications")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "received_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private Timestamp receivedAt;

	@ManyToOne
	@JoinColumn(name = "form_server_id", nullable = false)
	private FormServer formServer;

	@Column(name = "server_license_id")
	private Long serverLicenseId;

	@Column(name = "mac_address")
	private String macAddress;

	@Column(name = "remote_address")
	private String remoteAddress;

	@Column(name = "notification_type")
	private Integer notificationType; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Timestamp receivedAt) {
		this.receivedAt = receivedAt;
	}

	public Integer getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}

	public FormServer getFormServer() {
		return formServer;
	}

	public void setFormServer(FormServer formServer) {
		this.formServer = formServer;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Long getServerLicenseId() {
		return serverLicenseId;
	}

	public void setServerLicenseId(Long serverLicenseId) {
		this.serverLicenseId = serverLicenseId;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

}
