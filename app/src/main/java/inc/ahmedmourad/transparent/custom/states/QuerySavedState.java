package inc.ahmedmourad.transparent.custom.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;

public class QuerySavedState extends Preference.BaseSavedState {

	private String query;

	public QuerySavedState(Parcelable superState) {
		super(superState);
	}

	private QuerySavedState(Parcel parcel) {
		super(parcel);
		setQuery(parcel.readString());
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(getQuery());
	}

	public static final Parcelable.Creator<QuerySavedState> CREATOR = new Parcelable.Creator<QuerySavedState>() {

		public QuerySavedState createFromParcel(Parcel parcel) {
			return new QuerySavedState(parcel);
		}

		public QuerySavedState[] newArray(int size) {
			return new QuerySavedState[size];
		}
	};
}
