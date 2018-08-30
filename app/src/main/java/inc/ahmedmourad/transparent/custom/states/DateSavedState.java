package inc.ahmedmourad.transparent.custom.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;

public class DateSavedState extends Preference.BaseSavedState {

	private String date;

	public DateSavedState(Parcelable superState) {
		super(superState);
	}

	private DateSavedState(Parcel parcel) {
		super(parcel);
		setDate(parcel.readString());
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(getDate());
	}

	public static final Creator<DateSavedState> CREATOR = new Creator<DateSavedState>() {

		public DateSavedState createFromParcel(Parcel parcel) {
			return new DateSavedState(parcel);
		}

		public DateSavedState[] newArray(int size) {
			return new DateSavedState[size];
		}
	};
}
