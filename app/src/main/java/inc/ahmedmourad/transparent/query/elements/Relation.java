package inc.ahmedmourad.transparent.query.elements;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Relation implements QueryElement {

	@ColorRes
	private static final int COLOR = android.R.color.holo_blue_light;

	public static final int TYPE_AND = 0;
	public static final int TYPE_OR = 1;

	private int type;

	private ViewGroup view = null;

	@NonNull
	public static Relation of(@RelationType final int type) {
		final Relation relation = new Relation();
		relation.setType(type);
		return relation;
	}

	private Relation() {

	}

	@RelationType
	public int getType() {
		return type;
	}

	private void setType(@RelationType final int type) {
		this.type = type;
	}

	@Override
	public boolean isRelation() {
		return true;
	}

	@Override
	public boolean isValid() {
		return type == TYPE_AND || type == TYPE_OR;
	}

	@Override
	public void display(@NonNull final ViewGroup viewGroup) {
		viewGroup.addView(getView(viewGroup.getContext()));
	}

	@NonNull
	private View getView(@NonNull Context context) {

		if (view == null)
			view = QueryUtils.createView(context, COLOR, toString().toLowerCase());
		else
			QueryUtils.updateView(view, toString().toLowerCase());

		return view;
	}

	@NonNull
	@Override
	public String toString() {

		switch (getType()) {

			case TYPE_AND:
				return "AND";

			case TYPE_OR:
				return "OR";

			default:
				return "AND";
		}
	}

	@IntDef({TYPE_AND, TYPE_OR})
	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.PARAMETER, ElementType.METHOD})
	@interface RelationType {}
}
