package inc.ahmedmourad.transparent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceUtils {

	/**
	 * default shared preferences object
	 *
	 * @param context i ran out of jokes
	 * @return default shared preferences object
	 */
	public static SharedPreferences defaultPrefs(final Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * cool way to edit our preferences
	 *
	 * @param context   not context
	 * @param operation edit preferences here
	 */
	public static void edit(final Context context, final PreferencesEditor operation) {
		final SharedPreferences.Editor editor = defaultPrefs(context).edit();
		operation.edit(editor);
		editor.apply();
	}

	private PreferenceUtils() {

	}

	@FunctionalInterface
	public interface PreferencesEditor {
		void edit(SharedPreferences.Editor e);
	}
}
