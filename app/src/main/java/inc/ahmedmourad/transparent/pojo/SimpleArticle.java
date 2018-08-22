package inc.ahmedmourad.transparent.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimpleArticle {

	private String id = "";
	private String section = "";
	private String date = "";
	private String title = "";
	private String thumbnail = "";
	private String byline = "";
	private int wordCount = -1;

	@NonNull
	public static List<SimpleArticle> fromJsonArray(@Nullable final JSONArray jsonArray) {

		if (jsonArray == null)
			return new ArrayList<>(0);

		final List<SimpleArticle> articles = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); ++i)
			articles.add(fromJsonObject(jsonArray.optJSONObject(i)));

		return articles;
	}

	@NonNull
	private static SimpleArticle fromJsonObject(@Nullable final JSONObject articleObject) {

		if (articleObject == null)
			return new SimpleArticle();

		final String KEY_ID = "id";
		final String KEY_SECTION_NAME = "sectionName";
		final String KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
		final String KEY_WEB_TITLE = "webTitle";

		final String KEY_FIELDS = "fields";
		final String KEY_THUMBNAIL = "thumbnail";
		final String KEY_BYLINE = "byline";
		final String KEY_WORD_COUNT = "wordcount";

		final SimpleArticle article = new SimpleArticle();

		article.setId(articleObject.optString(KEY_ID));
		article.setSection(articleObject.optString(KEY_SECTION_NAME));
		article.setDate(articleObject.optString(KEY_WEB_PUBLICATION_DATE));
		article.setTitle(articleObject.optString(KEY_WEB_TITLE));

		try {

			final JSONObject fieldsObject = articleObject.getJSONObject(KEY_FIELDS);

			article.setThumbnail(fieldsObject.optString(KEY_THUMBNAIL));
			article.setByline(fieldsObject.optString(KEY_BYLINE));
			article.setWordCount(fieldsObject.optInt(KEY_WORD_COUNT, -1));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return article;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getSection() {
		return section;
	}

	private void setSection(String section) {
		this.section = section;
	}

	public String getDate() {
		return date;
	}

	private void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	private void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getByline() {
		return byline;
	}

	private void setByline(String byline) {
		this.byline = byline;
	}

	public int getWordCount() {
		return wordCount;
	}

	private void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
}
