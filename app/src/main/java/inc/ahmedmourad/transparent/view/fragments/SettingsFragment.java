package inc.ahmedmourad.transparent.view.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.custom.preferences.DatePickerPreference;
import inc.ahmedmourad.transparent.custom.preferences.QueryPreference;
import inc.ahmedmourad.transparent.query.Query;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_filter);

		bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_query)), QueryPreference.DEFAULT_VALUE);
		bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_date)), DatePickerPreference.DEFAULT_VALUE);
	}

	private void bindPreferenceSummaryToValue(Preference preference, @NonNull final String defaultValue) {

		preference.setOnPreferenceChangeListener(this);

		onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
				.getString(preference.getKey(), defaultValue)
		);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object v) {

		String value = v.toString();

		if (ListPreference.class.isInstance(preference)) {

			final ListPreference listPreference = (ListPreference) preference;

			int index = listPreference.findIndexOfValue(value);

			preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

		} else if (QueryPreference.class.isInstance(preference)) {

			final Query query = Query.fromJson(value);

			preference.setSummary(query.isEmpty() ? getString(R.string.all_categories) : query.toString());

		} else if (DatePickerPreference.class.isInstance(preference)) {

			preference.setSummary(value.equals(DatePickerPreference.DEFAULT_VALUE) ? getString(R.string.date_summary) : value);
		}

		return true;
	}
}
