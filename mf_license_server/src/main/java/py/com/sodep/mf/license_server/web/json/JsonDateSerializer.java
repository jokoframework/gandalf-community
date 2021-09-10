package py.com.sodep.mf.license_server.web.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import py.com.sodep.mobileforms.license.MFLicense;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonDateSerializer extends StdSerializer<Date> {

	protected JsonDateSerializer() {
		super(Date.class, false);
	}

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MFLicense.DATE_FORMAT);
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}

}
