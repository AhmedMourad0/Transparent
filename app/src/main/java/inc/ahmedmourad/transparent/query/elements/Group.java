package inc.ahmedmourad.transparent.query.elements;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Group implements QueryElement {

	@ColorRes
	private static final int COLOR = android.R.color.holo_green_light;

	private List<QueryElement> elements = new ArrayList<>();

	private ViewGroup leadingView = null;
	private ViewGroup trailingView = null;

	@NonNull
	public static Group with(@NonNull final String parameter) {
		final Group group = new Group();
		group.param(parameter);
		return group;
	}

	@NonNull
	public static Group with(@NonNull final Group g) {
		final Group group = new Group();
		group.add(g);
		return group;
	}

	@NonNull
	public Group param(@NonNull final String parameter) {
		add(Parameter.of(parameter));
		return this;
	}

	@NonNull
	public Group group(@NonNull final Group group) {
		add(group);
		return this;
	}

	@NonNull
	public Group and() {
		add(Relation.of(Relation.TYPE_AND));
		return this;
	}

	@NonNull
	public Group or() {
		add(Relation.of(Relation.TYPE_OR));
		return this;
	}

	@NonNull
	public Group add(@NonNull final QueryElement element) {

		if (elements.size() > 0) {

			if (!element.isRelation() && !elements.get(elements.size() - 1).isRelation())
				throw new IllegalStateException("You must have a Relation between " +
						elements.get(elements.size() - 1).getClass().getSimpleName() +
						" and " +
						element.getClass().getSimpleName()
				);

			if (element.isRelation() && elements.get(elements.size() - 1).isRelation())
				elements.remove(elements.size() - 1);
		}

		elements.add(element);

		return this;
	}

	@Override
	public boolean isRelation() {
		return false;
	}

	@Override
	public boolean isValid() {
		return QueryUtils.fix(elements).size() > 0;
	}

	@Override
	public void display(@NonNull final ViewGroup viewGroup) {

		viewGroup.addView(getLeadingView(viewGroup.getContext()));

		displayElements(viewGroup);

		viewGroup.addView(getTrailingView(viewGroup.getContext()));
	}

	public void displayElements(@NonNull ViewGroup viewGroup) {
		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).display(viewGroup);
	}

	@NonNull
	public View getLeadingView(@NonNull Context context) {

		if (leadingView == null)
			leadingView = QueryUtils.createView(context, COLOR, "(");

		return leadingView;
	}

	@NonNull
	private View getTrailingView(@NonNull Context context) {

		if (trailingView == null)
			trailingView = QueryUtils.createView(context, COLOR, ")");

		return trailingView;
	}

	@NonNull
	@Override
	public String toString() {

		elements = QueryUtils.fix(elements);

		if (elements.size() == 0)
			return "";

		if (elements.size() == 1)
			return elements.get(0).toString();

		final StringBuilder builder = new StringBuilder();

		builder.append("(");

		for (int i = 0; i < elements.size(); ++i)
			builder.append(elements.get(i)).append(" ");

		builder.deleteCharAt(builder.length() - 1).append(")");

		return builder.toString();
	}
}
