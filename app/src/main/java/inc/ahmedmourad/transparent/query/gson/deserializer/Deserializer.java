package inc.ahmedmourad.transparent.query.gson.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.gson.serializer.Serializer;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Deserializer implements JsonDeserializer<List<QueryElement>> {

	@Override
	public List<QueryElement> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		final List<QueryElement> list = new ArrayList<>();

		for (JsonElement element : json.getAsJsonArray()) {

			final JsonObject object = element.getAsJsonObject();

			final int type = object.get(Serializer.PROPERTY_ELEMENT_TYPE).getAsInt();

			object.remove(Serializer.PROPERTY_ELEMENT_TYPE);

			list.add(context.deserialize(element, QueryUtils.resolveElementType(type)));
		}

		return list;
	}
}
