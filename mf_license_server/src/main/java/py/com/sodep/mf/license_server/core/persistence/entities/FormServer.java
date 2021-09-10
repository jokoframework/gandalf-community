package py.com.sodep.mf.license_server.core.persistence.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "public", name = "form_servers")
@SequenceGenerator(name = "sequence_form_servers", sequenceName = "sequence_form_servers")
public class FormServer extends SodepEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_form_servers")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "mac_address", length = 48)
	private String macAddress;

	@Column(name = "hdd_serial", length = 255)
	private String hddSerial;

	@Column(name = "owner", length = 512, nullable = false)
	private String owner;

	@Column(name = "private_key", length = 4096)
	private String privateKey;

	@Column(name = "public_key", length = 4096)
	private String publicKey;

	@Column(name = "description", length = 4096)
	private String description;

	@OneToMany(mappedBy = "formServer", cascade = CascadeType.REMOVE)
	private List<FormServerLicense> licenses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getHddSerial() {
		return hddSerial;
	}

	public void setHddSerial(String hddSerial) {
		this.hddSerial = hddSerial;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public List<FormServerLicense> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<FormServerLicense> licenses) {
		this.licenses = licenses;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
