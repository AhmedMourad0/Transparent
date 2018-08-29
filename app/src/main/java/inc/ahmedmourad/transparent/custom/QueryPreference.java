package inc.ahmedmourad.transparent.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.flexbox.FlexboxLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.custom.state.QuerySavedState;
import inc.ahmedmourad.transparent.query.Query;
import inc.ahmedmourad.transparent.query.ViewStateManager;

public class QueryPreference extends DialogPreference {

	private static final String DEFAULT_VALUE = Query.empty().toJson();

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_flexbox)
	FlexboxLayout flexbox;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_begin_group)
	Button beginGroupButton;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_end_group)
	Button endGroupButton;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_and)
	Button andButton;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_or)
	Button orButton;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_clear)
	Button clearButton;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_keyword)
	EditText keywordEditText;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.query_enter)
	Button enterButton;

	private Query query = Query.empty();

	private ViewStateManager viewStateManager;

	private Unbinder unbinder;

	public QueryPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.preference_query);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);
		setPersistent(true);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			query = Query.fromJson(getPersistedString(DEFAULT_VALUE));
		} else {
			final String value = (String) defaultValue;
			query = Query.fromJson(value);
			persistString(value);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray array, int index) {
		final String value = array.getString(index);
		return value != null ? value : DEFAULT_VALUE;
	}

	@Override
	protected void onBindDialogView(final View view) {
		super.onBindDialogView(view);

		if (unbinder == null)
			unbinder = ButterKnife.bind(this, view);

		if (viewStateManager == null)
			viewStateManager = ViewStateManager.Builder.with(query)
					.display(flexbox)
					.enter(enterButton)
					.beginGroup(beginGroupButton)
					.endGroup(endGroupButton)
					.and(andButton)
					.or(orButton)
					.keyword(keywordEditText)
					.clear(clearButton)
					.build();
	}

	@Override
	protected void onDialogClosed(final boolean positiveResult) {

		final String keyword = keywordEditText.getText().toString().trim();

		if (query.isEmpty() && keyword.length() > 0)
			query.param(keyword);

		if (positiveResult) {
			final String newValue = query.toJson();
			persistString(newValue);
			callChangeListener(newValue);
		}

		unbindView();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final QuerySavedState state = new QuerySavedState(super.onSaveInstanceState());
		state.setQuery(query.toJson());
		unbindView();
		return state;
	}

	@Override
	protected void onRestoreInstanceState(@Nullable final Parcelable restoredState) {

		if (restoredState == null)
			return;

		final QuerySavedState state = (QuerySavedState) restoredState;

		super.onRestoreInstanceState(state.getSuperState());

		query = Query.fromJson(state.getQuery());
	}

	//	@Override
//	public void onActivityDestroy() {
//		super.onActivityDestroy();
//		unbindView();
//	}
//
	private void unbindView() {
		viewStateManager.release();
		viewStateManager = null;
		unbinder.unbind();
		unbinder = null;
	}
}
