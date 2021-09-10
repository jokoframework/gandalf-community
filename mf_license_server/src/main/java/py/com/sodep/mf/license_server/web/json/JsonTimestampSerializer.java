package py.com.sodep.mf.license_server.web.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import py.com.sodep.mobileforms.license.MFLicense;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonTimestampSerializer extends StdSerializer<Timestamp> {

	protected JsonTimestampSerializer() {
		super(Timestamp.class, false);
	}

	@Override
	public void serialize(Timestamp date, JsonGenerator gen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MFLicense.TIMESTAMP_FORMAT);
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}

}
