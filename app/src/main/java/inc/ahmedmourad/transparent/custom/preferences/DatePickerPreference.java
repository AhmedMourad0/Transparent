package inc.ahmedmourad.transparent.custom.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.custom.states.DateSavedState;

public class DatePickerPreference extends DialogPreference implements AlertDialog.OnClickListener {

	public static final String DEFAULT_VALUE = "";

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.date_datePicker)
	DatePicker datePicker;

	private String date = DEFAULT_VALUE;

	private Unbinder unbinder;

	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.preference_date_picker);

		setDialogIcon(null);
		setPersistent(true);
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder);
		builder.setPositiveButton(R.string.set, this);
		builder.setNegativeButton(android.R.string.cancel, this);
		builder.setNeutralButton(R.string.clear, this);
		builder.setTitle(null);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			date = getPersistedString(DEFAULT_VALUE);
		} else {
			date = (String) defaultValue;
			persistString(date);
			callChangeListener(date);
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

		if (unbinder == null) {
			unbinder = ButterKnife.bind(this, view);
			initDatePicker();
		} else {
			updateDatePicker();
		}
	}

	@Override
	protected void onDialogClosed(final boolean positiveResult) {
		unbindView();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final DateSavedState state = new DateSavedState(super.onSaveInstanceState());
		state.setDate(getDate());
		unbindView();
		return state;
	}

	@Override
	protected void onRestoreInstanceState(@Nullable final Parcelable restoredState) {

		if (restoredState == null)
			return;

		final DateSavedState state = (DateSavedState) restoredState;

		super.onRestoreInstanceState(state.getSuperState());

		date = state.getDate();
		updateDatePicker();
	}

	private void unbindView() {
		if (unbinder != null) {
			unbinder.unbind();
			unbinder = null;
		}
	}

	private void initDatePicker() {

		if (date.equals(DEFAULT_VALUE))
			return;

		final String[] values = date.split("-");

		if (values.length == 3 && datePicker != null)
			datePicker.init(Integer.valueOf(values[0]),
					(Integer.valueOf(values[1]) - 1),
					Integer.valueOf(values[2]),
					(view, year, monthOfYear, dayOfMonth) -> date = getDate()
			);
	}

	private void updateDatePicker() {

		if (date.equals(DEFAULT_VALUE))
			return;

		final String[] values = date.split("-");

		if (values.length == 3 && datePicker != null)
			datePicker.updateDate(Integer.valueOf(values[0]),
					(Integer.valueOf(values[1]) - 1),
					Integer.valueOf(values[2])
			);
	}

	private String getDate() {
		if (datePicker == null)
			return date;
		else
			return datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);

		switch (which) {

			case DialogInterface.BUTTON_POSITIVE:
				final String newValue = getDate();
				persistString(newValue);
				callChangeListener(newValue);
				break;

			case DialogInterface.BUTTON_NEUTRAL:
				persistString(DEFAULT_VALUE);
				callChangeListener(DEFAULT_VALUE);
				break;
		}
	}
}
