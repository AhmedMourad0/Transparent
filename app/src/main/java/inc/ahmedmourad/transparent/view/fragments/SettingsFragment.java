package inc.ahmedmourad.transparent.view.fragments;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import inc.ahmedmourad.transparent.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_filter);
		setHasOptionsMenu(true);

		// Bind the summaries of EditText/List/Dialog/Ringtone settings
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		bindPreferenceSummaryToValue(findPreference("text"));
		bindPreferenceSummaryToValue(findPreference("list"));
		bindPreferenceSummaryToValue(findPreference("query"));
	}

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 */
	private void bindPreferenceSummaryToValue(Preference preference) {

		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(this);

		// Trigger the listener immediately with the preference's current value.
		onPreferenceChange(preference,
				PreferenceManager.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), "")
		);
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == android.R.id.home) {
//			startActivity(new Intent(getActivity(), SettingsActivity.class));
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {

		String stringValue = value.toString();

		if (ListPreference.class.isInstance(preference)) {
			// For list settings, look up the correct display value in
			// the preference's 'entries' list.
			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue(stringValue);

			// Set the summary to reflect the new value.
			preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

		} else if (RingtonePreference.class.isInstance(preference)) {
			// For ringtone settings, look up the correct display value
			// using RingtoneManager.
			if (TextUtils.isEmpty(stringValue)) {
				// Empty values correspond to 'silent' (no ringtone).
				preference.setSummary(R.string.pref_ringtone_silent);

			} else {
				Ringtone ringtone = RingtoneManager.getRingtone(
						preference.getContext(), Uri.parse(stringValue));

				if (ringtone == null) {
					// Clear the summary if there was a lookup error.
					preference.setSummary(null);
				} else {
					// Set the summary to reflect the new ringtone display
					// name.
					String name = ringtone.getTitle(preference.getContext());
					preference.setSummary(name);
				}
			}

		} else {
			// For all other settings, set the summary to the value's
			// simple string representation.
			preference.setSummary(stringValue);
		}
		return true;
	}
}
