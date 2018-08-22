package inc.ahmedmourad.transparent.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import inc.ahmedmourad.transparent.BuildConfig;
import inc.ahmedmourad.transparent.pojo.SimpleArticle;

public final class NetworkUtils {

	private static final String BASE_URL_WEB = "https://www.theguardian.com/";

	@NonNull
	public static Uri generateWebUri(@NonNull final String articleId) {
		return Uri.parse(BASE_URL_WEB + articleId);
	}

	@NonNull
	public static List<SimpleArticle> fetchSimpleArticles() {

		//https://content.guardianapis.com/search?show-fields=thumbnail,byline,wordcount&api-key=THE_GUARDIAN_API_KEY
		final String BASE_URL = "https://content.guardianapis.com/";
		final String PATH_SEARCH = "search";
		final String PARAM_SHOW_FIELDS = "show-fields";
		final String PARAM_PAGE_SIZE = "page-size";
		final String PARAM_API_KEY = "api-key";

		return JsonUtils.extractSimpleArticles(getJsonStringFromUri(Uri.parse(BASE_URL).buildUpon()
				.appendPath(PATH_SEARCH)
				.appendQueryParameter(PARAM_SHOW_FIELDS, "thumbnail,byline,wordcount")
				.appendQueryParameter(PARAM_PAGE_SIZE, "20")
				.appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_GUARDIAN_API_KEY)
				.build()));
	}

	// I don't know what Retrofit does to make this thing so damn fast .. probably black magic.
	@Nullable
	private static String getJsonStringFromUri(@NonNull final Uri uri) {

		// These two need to be declared outside the try/catch
		// so that they can be closed in the finally block.
		HttpURLConnection urlConnection = null;
		BufferedReader bufferedReader = null;

		// Will contain the raw JSON response as a string.
		String json = "";

		try {

			final URL url = new URL(uri.toString());

			// Create the request and open the connection
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Read the input stream into a String
			final InputStream inputStream = urlConnection.getInputStream();

			if (inputStream == null)
				return null;

			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			json = extractJsonString(bufferedReader);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (urlConnection != null)
				urlConnection.disconnect();

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

		return json;
	}

	@Nullable
	private static String extractJsonString(@NonNull final BufferedReader bufferedReader) throws IOException {

		final StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
			// But it does make debugging a *lot* easier if you print out the completed
			// buffer for debugging.
			stringBuilder.append(line).append("\n");
		}

		if (stringBuilder.length() == 0)
			return null;

		return stringBuilder.toString();
	}

	private NetworkUtils() {

	}
}
