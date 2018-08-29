package inc.ahmedmourad.transparent.view.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.custom.QueryPreference;
import inc.ahmedmourad.transparent.query.Query;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_filter);
		setHasOptionsMenu(true);

		bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_query)));
	}

	private void bindPreferenceSummaryToValue(Preference preference) {

		preference.setOnPreferenceChangeListener(this);

		onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
				.getString(preference.getKey(), Query.empty().toJson())
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

			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue(stringValue);

			preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

		} else if (QueryPreference.class.isInstance(preference)) {

			final Query query = Query.fromJson(stringValue);

			preference.setSummary(query.isEmpty() ? getString(R.string.all_categories) : query.toString());
		}

		return true;
	}
}
