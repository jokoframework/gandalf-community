package py.com.sodep.mf.license_server.web.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import py.com.sodep.mobileforms.license.MFLicense;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonTimestampDeserializer extends StdDeserializer<Timestamp> {

	protected JsonTimestampDeserializer() {
		super(Timestamp.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Timestamp deserialize(JsonParser parser, DeserializationContext ctx) throws IOException,
			JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MFLicense.TIMESTAMP_FORMAT);
		String valueAsString = parser.getValueAsString();
		try {
			Date parsed = dateFormat.parse(valueAsString);
			Timestamp t = new Timestamp(parsed.getTime());
			return t;
		} catch (ParseException e) {
			throw new IOException(valueAsString + " is not a valid date format: " + MFLicense.TIMESTAMP_FORMAT);
		}
	}

}
