package utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import models.MessageTwitter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MessageTwitterJsonSerializer implements JsonSerializer<MessageTwitter> {

	private static MessageTwitterJsonSerializer instance;

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
	static {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private MessageTwitterJsonSerializer() {
	}

	public synchronized static MessageTwitterJsonSerializer get() {
		if (instance == null) {
			instance = new MessageTwitterJsonSerializer();
		}
		return instance;
	}

	@Override
	public JsonElement serialize(MessageTwitter messageTwitter, Type type, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("dateCreation", DATE_FORMAT.format(messageTwitter.getDateCreation()));
		obj.addProperty("texte", messageTwitter.getTexte());
		return obj;
	}

}
