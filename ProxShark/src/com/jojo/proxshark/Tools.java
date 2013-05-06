package com.jojo.proxshark;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import android.util.Log;

public final class Tools {

	private static final Charset CHARSET = Charset.forName("ISO-8859-1");
	private static final CharsetDecoder DECODER = CHARSET.newDecoder();
	private static final CharsetEncoder ENCODER = CHARSET.newEncoder();

	public static String decodeBuffer(ByteBuffer buffer) throws CharacterCodingException {
		try {
			return DECODER.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			Log.e(Tools.class.getName(), "Unable to read the buffer");
			throw e;
		} finally {
			buffer.rewind();
		}
	}

	public static void encodeBuffer(ByteBuffer buffer, String data) throws CharacterCodingException {
		try {
			buffer.clear();
			buffer.put(ENCODER.encode(CharBuffer.wrap(data)));
			buffer.flip();
		} catch (CharacterCodingException e) {
			Log.e(Tools.class.getName(), "Unable to read the buffer");
			throw e;
		} finally {
			buffer.rewind();
		}
	}

	public static String doReplacements(String data) {
		int oldLenght = data.length();

		// data = data.replaceAll("\"inTrial\":false", "\"inTrial\":true");
		data = data.replaceAll("\"hasExpired\":true", "\"hasExpired\":false");
		// data = data.replaceAll("\"hasExceededTrialPlays\":true",
		// "\"hasExceededTrialPlays\":false");
		// data = data.replaceAll("\"isAnywhere\":false",
		// "\"isAnywhere\":true");
		// data = data.replaceAll("\"isPremium\":false", "\"isPremium\":true");
		// data = data.replaceAll("\"isPlus\":false", "\"isPlus\":true");
		// data = data.replaceAll("\"isAnywhere\":false",
		// "\"isAnywhere\":true");
		// data = data.replaceAll("\"subscriptionExpires\":0",
		// "\"subscriptionExpires\":9999999999");
		data = data.replaceAll("\"expires\":1367362812", "\"expires\":1368140412");
		// data = data.replaceAll("\"mobileID\":7047584",
		// "\"mobileID\":7023554");

		return addContentLength(data, data.length() - oldLenght);
	}

	private static String addContentLength(String data, int nbToAdd) {
		int indexOfContentLengthHeader = data.indexOf("Content-Length: ");
		if (indexOfContentLengthHeader == -1) {
			return data;
		}
		int indexOfContentLength = indexOfContentLengthHeader + "Content-Length: ".length();

		String contentLengthString = data.substring(indexOfContentLength).split("\r\n")[0];
		int contentLength = Integer.parseInt(contentLengthString);
		int newContentLength = contentLength + nbToAdd;

		return data.replace("Content-Length: " + contentLengthString, "Content-Length: " + newContentLength);
	}
}
