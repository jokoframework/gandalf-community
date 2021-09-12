package py.com.sodep.mf.license_server.web.json;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonObjectMapperFactory {

	public ObjectMapper createInstance() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new JsonDateSerializer());
		module.addSerializer(new JsonTimestampSerializer());
		module.addDeserializer(Timestamp.class, new JsonTimestampDeserializer());
		module.addDeserializer(Date.class, new JsonDateDeserializer());
		mapper.registerModule(module);
		return mapper;
	}
}
