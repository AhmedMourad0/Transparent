package inc.ahmedmourad.transparent.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.pojo.SimpleArticle;

class JsonUtils {

	@NonNull
	static List<SimpleArticle> extractSimpleArticles(@Nullable final String json) {

		if (json == null)
			return new ArrayList<>(0);

		try {

			final String KEY_RESPONSE = "response";
			final String KEY_RESULTS = "results";

			final JSONObject responseObject = new JSONObject(json).optJSONObject(KEY_RESPONSE);

			return SimpleArticle.fromJsonArray(responseObject.optJSONArray(KEY_RESULTS));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList<>(0);
	}

	private JsonUtils() {

	}
}
