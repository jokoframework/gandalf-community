package py.com.sodep.mf.license_server.core.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import py.com.sodep.mobileforms.license.MFApplicationLicense;

@Entity
@Table(schema="public", name = "application_licenses")
@SequenceGenerator(sequenceName = "sequence_application_licenses", name = "sequence_application_licenses")
public class ApplicationLicense extends SodepEntity implements MFApplicationLicense {

	@Id
	@GeneratedValue(generator = "sequence_application_licenses", strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "form_server_license_id")
	private FormServerLicense formServerLicense;

	@Column(name = "max_devices", nullable = false)
	private Long maxDevices;

	@Column(name = "max_users", nullable = false)
	private Long maxUsers;

	@Column(name = "owner")
	private String owner;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "valid_days", nullable = false)
	private Long validDays;

	@Column(name = "application_id")
	private Long applicationId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getMaxDevices() {
		return maxDevices;
	}

	@Override
	public Long getMaxUsers() {
		return maxUsers;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setMaxDevices(Long maxDevices) {
		this.maxDevices = maxDevices;
	}

	public FormServerLicense getFormServerLicense() {
		return formServerLicense;
	}

	public void setFormServerLicense(FormServerLicense formServerLicense) {
		this.formServerLicense = formServerLicense;
	}

	public void setMaxUsers(Long maxUsers) {
		this.maxUsers = maxUsers;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getValidDays() {
		return validDays;
	}

	public void setValidDays(Long validDays) {
		this.validDays = validDays;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

}
