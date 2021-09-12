package py.com.sodep.mf.license_server.core.persistence.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(schema="public", name = "server_licenses")
@SequenceGenerator(name = "sequence_server_licenses", sequenceName = "sequence_server_licenses")
public class FormServerLicense extends SodepEntity {

	@Id
	@GeneratedValue(generator = "sequence_server_licenses", strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "form_server_id", nullable = false)
	private FormServer formServer;

	@Column(name = "max_applications", nullable = false)
	private Long maxApplications;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "valid_days")
	private Long validDays;

	@OneToMany(mappedBy = "formServerLicense", cascade = CascadeType.ALL)
	private List<ApplicationLicense> applicationLicenses;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "default_application_license_id", nullable = false)
	private ApplicationLicense defaultApplicationLicense;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMaxApplications() {
		return maxApplications;
	}

	public void setMaxApplications(Long maxApplications) {
		this.maxApplications = maxApplications;
	}

	// @JsonSerialize(using = JsonDateDeserializer.class)
	// @JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonIgnore
	public FormServer getFormServer() {
		return formServer;
	}

	public void setFormServer(FormServer formServer) {
		this.formServer = formServer;
	}

	public List<ApplicationLicense> getApplicationLicenses() {
		return applicationLicenses;
	}

	public void setApplicationLicenses(List<ApplicationLicense> applicationLicenses) {
		this.applicationLicenses = applicationLicenses;
	}

	public Long getValidDays() {
		return validDays;
	}

	public void setValidDays(Long validDays) {
		this.validDays = validDays;
	}

	public ApplicationLicense getDefaultApplicationLicense() {
		return defaultApplicationLicense;
	}

	public void setDefaultApplicationLicense(ApplicationLicense defaultApplicationLicense) {
		this.defaultApplicationLicense = defaultApplicationLicense;
	}

}
