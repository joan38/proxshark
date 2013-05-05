package com.jojo.proxshark;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import android.util.Log;

public final class Tools {

	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	public static final CharsetDecoder DECODER = CHARSET.newDecoder();

	public static String decodeBuffer(ByteBuffer buffer) throws CharacterCodingException {
		try {
			return DECODER.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			Log.e(ClientConnectionHandler.class.getName(), "Unable to read the buffer");
			throw e;
		} finally {
			buffer.rewind();
		}
	}
}
