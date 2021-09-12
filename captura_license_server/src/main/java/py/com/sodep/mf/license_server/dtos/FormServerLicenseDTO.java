package py.com.sodep.mf.license_server.dtos;

import java.util.Date;
import java.util.Map;

import py.com.sodep.mobileforms.license.MFFormServerLicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class FormServerLicenseDTO extends DTO implements MFFormServerLicense {

	private Long maxApplications;

	private Date creationDate;

	private Long validDays;

	private Map<String, String> properties;

	private Long serverId;

	private ApplicationLicenseDTO defaultApplicationLicense;

	public Long getMaxApplications() {
		return maxApplications;
	}

	public void setMaxApplications(Long maxApplications) {
		this.maxApplications = maxApplications;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public ApplicationLicenseDTO getDefaultApplicationLicense() {
		return defaultApplicationLicense;
	}

	public void setDefaultApplicationLicense(ApplicationLicenseDTO defaultApplicationLicense) {
		this.defaultApplicationLicense = defaultApplicationLicense;
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

}
