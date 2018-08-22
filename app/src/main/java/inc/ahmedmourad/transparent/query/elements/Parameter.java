package inc.ahmedmourad.transparent.query.elements;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Parameter implements QueryElement {

	@ColorRes
	private static final int COLOR = android.R.color.holo_orange_light;

	private String value = "";

	private ViewGroup view = null;

	@NonNull
	public static Parameter of(@NonNull final String value) {

		if (value.trim().length() == 0)
			throw new IllegalStateException("Parameter value cannot be an empty string");

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
	public void display(@NonNull final ViewGroup viewGroup) {
		viewGroup.addView(getView(viewGroup.getContext()));
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
}
