package inc.ahmedmourad.transparent.query.elements.model;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

public interface QueryElement {

	@NonNull
	String toString();

	boolean isRelation();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean isValid();

	void display(@NonNull ViewGroup viewGroup);
}
