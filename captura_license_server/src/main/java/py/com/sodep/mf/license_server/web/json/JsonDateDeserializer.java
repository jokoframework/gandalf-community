package py.com.sodep.mf.license_server.web.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import py.com.sodep.mobileforms.license.MFLicense;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonDateDeserializer extends StdDeserializer<Date> {

	protected JsonDateDeserializer() {
		super(Date.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MFLicense.DATE_FORMAT);
		String valueAsString = parser.getValueAsString();
		try {
			return dateFormat.parse(valueAsString);
		} catch (ParseException e) {
			throw new IOException(valueAsString + " is not a valid date format: " + MFLicense.DATE_FORMAT);
		}
	}

}
