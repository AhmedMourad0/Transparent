package inc.ahmedmourad.transparent.query.elements;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Parameter implements QueryElement {

	@ColorRes
	private static final transient int COLOR = R.color.colorParameter;

	private String value = "";

	private transient ViewGroup view = null;

	@NonNull
	public static Parameter of(@NonNull final String value) {

		if (value.trim().length() == 0)
			throw new IllegalArgumentException("Parameter value cannot be an empty string");

		final Parameter parameter = new Parameter();
		parameter.setValue(value);
		return parameter;
	}

	private Parameter() {

	}

	@NonNull
	public String getValue() {
		return value.trim();
	}

	private void setValue(@NonNull final String value) {
		this.value = value;
	}

	@Override
	public boolean isRelation() {
		return false;
	}

	@Override
	public boolean isValid() {
		return getValue().length() > 0;
	}

	@Override
	public void validate() {
		// Can't be validated without producing unexpected results
	}

	@Override
	public void display(@NonNull final FlexboxLayout flexbox) {
		flexbox.addView(getView(flexbox.getContext()));
	}

	@Override
	public int getElementType() {
		return TYPE_PARAMETER;
	}

	@NonNull
	private View getView(@NonNull final Context context) {

		if (view == null)
			view = QueryUtils.createView(context, COLOR, getValue());
		else
			QueryUtils.updateView(view, getValue());

		return view;
	}

	@NonNull
	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		final Parameter parameter = (Parameter) o;

		return getValue().equals(parameter.getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}
}
