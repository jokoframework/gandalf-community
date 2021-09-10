package py.com.sodep.mf.license_server.dtos;

import java.util.Date;

import py.com.sodep.mobileforms.license.MFApplicationLicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ApplicationLicenseDTO extends DTO implements MFApplicationLicense {

	private Long applicationId;

	private Long maxDevices;

	private Long maxUsers;

	private String owner;

	private Date creationDate;

	private Long validDays;

	private Long formServerLicenseId;

	public Long getMaxDevices() {
		return maxDevices;
	}

	public void setMaxDevices(Long maxDevices) {
		this.maxDevices = maxDevices;
	}

	public Long getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(Long maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	// @JsonSerialize(using = JsonDateSerializer.class)
	// @JsonDeserialize(using = JsonDateDeserializer.class)
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

	public Long getFormServerLicenseId() {
		return formServerLicenseId;
	}

	public void setFormServerLicenseId(Long formServerLicenseId) {
		this.formServerLicenseId = formServerLicenseId;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

}
