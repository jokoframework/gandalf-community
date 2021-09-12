package py.com.sodep.mf.license_server.core.translators;

import org.springframework.stereotype.Component;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.dtos.FormServerDTO;

@Component
public class FormServerTranslatorImpl extends TranslatorImp<FormServer, FormServerDTO> implements FormServerTranslator {

	@Override
	public FormServerDTO toDTO(FormServer entity) {
		FormServerDTO dto = new FormServerDTO();
		dto.setId(entity.getId());
		dto.setHddSerial(entity.getHddSerial());
		dto.setMacAddress(entity.getMacAddress());
		dto.setOwner(entity.getOwner());
// don't expose the privateKey		
//		dto.setPrivateKey(entity.getPrivateKey());
		dto.setPublicKey(entity.getPublicKey());
		dto.setDescription(entity.getDescription());
		return dto;
	}

	@Override
	public FormServer fromDTO(FormServerDTO dto) {
		return fromDTO(dto, new FormServer());
	}

	@Override
	public FormServer fromDTO(FormServerDTO dto, FormServer entity) {
		entity.setId(dto.getId());
		entity.setHddSerial(dto.getHddSerial());
		entity.setMacAddress(dto.getMacAddress());
		entity.setOwner(dto.getOwner());
		entity.setDescription(dto.getDescription());
		// The keys should never be set other than
		// by the KeyPairService
		// entity.setPrivateKey(privateKey)
		// entity.setPublicKey(publicKey)
		return entity;
	}

}
