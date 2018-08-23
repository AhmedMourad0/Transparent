package inc.ahmedmourad.transparent.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.custom.state.QuerySavedState;
import inc.ahmedmourad.transparent.query.Query;
import inc.ahmedmourad.transparent.query.elements.Group;

public class QueryPreference extends DialogPreference {

	private static final String DEFAULT_VALUE = "";

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_flexbox)
	FlexboxLayout flexbox;

	private String currentQuery = DEFAULT_VALUE;

	private Query query = Query.with("A")
			.and()
			.beginGroup()
			.param("B")
			.or()
			.param("C")
			.endGroup()
			.or()
			.beginGroup()
			.param("D")
			.and()
			.beginGroup()
			.param("E")
			.or()
			.param("F")
			.and()
			.beginGroup()
			.param("G")
			.and()
			.param("H")
			.endGroup()
			.and()
			.group(Group.with("I").or().param("J"))
			.or()
			.beginGroup()
			.param("K")
			.and()
			.param("L")
			.endGroup()
			.and()
			.group(Group.with("M").or().param("N"))
			.and()
			.param("O")
			.endGroup()
			.and()
			.param("P")
			.endGroup();

	private Unbinder unbinder;

	public QueryPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.preference_query);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);
		setPersistent(false);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			currentQuery = getPersistedString(DEFAULT_VALUE);
		} else {
			currentQuery = (String) defaultValue;
			persistString(currentQuery);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray array, int index) {
		final String value = array.getString(index);
		return value != null ? value : DEFAULT_VALUE;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		if (unbinder == null)
			unbinder = ButterKnife.bind(this, view);

		query.display(flexbox);
	}

	@Override
	protected void onDialogClosed(final boolean positiveResult) {
		if (positiveResult)
			persistString(query.toString());
	}

	@Override
	protected Parcelable onSaveInstanceState() {

		final Parcelable superState = super.onSaveInstanceState();

		if (isPersistent())
			return superState;

		final QuerySavedState state = new QuerySavedState(superState);

		state.setQuery(query.toString());

		return state;
	}

	@Override
	protected void onRestoreInstanceState(@Nullable final Parcelable restoredState) {

		if (restoredState == null || !QuerySavedState.class.isInstance(restoredState)) {
			super.onRestoreInstanceState(restoredState);
			return;
		}

		final QuerySavedState state = (QuerySavedState) restoredState;
		super.onRestoreInstanceState(state.getSuperState());

//		queryTextView.setText(state.getQuery());
	}

//	@Override
//	public void onActivityDestroy() {
//		super.onActivityDestroy();
//		unbindView();
//	}
//
//	private void unbindView() {
//		unbinder.unbind();
//		unbinder = null;
//	}
}
