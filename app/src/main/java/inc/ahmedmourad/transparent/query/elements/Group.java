package inc.ahmedmourad.transparent.query.elements;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Group implements QueryElement {

	@ColorRes
	private static final transient int COLOR = R.color.colorGroup;

	private List<QueryElement> elements = new ArrayList<>();

	private transient ViewGroup leadingView = null;
	private transient ViewGroup trailingView = null;

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
		if (group.isValid())
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
		return QueryUtils.trim(elements).size() > 0;
	}

	public void validate() {

		elements = QueryUtils.trim(elements);

		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).validate();
	}

	@Override
	public void display(@NonNull final FlexboxLayout flexbox) {
		flexbox.addView(getLeadingView(flexbox.getContext()));
		displayElements(flexbox);
		flexbox.addView(getTrailingView(flexbox.getContext()));
	}

	@Override
	public int getElementType() {
		return TYPE_GROUP;
	}

	public void displayElements(@NonNull FlexboxLayout flexbox) {
		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).display(flexbox);
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

	public QueryElement get(final int index) {
		return elements.get(index);
	}

	public int size() {
		return elements.size();
	}

	@NonNull
	@Override
	public String toString() {

		validate();

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

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		final Group group = (Group) o;

		if (elements.size() != group.elements.size())
			return false;

		for (int i = 0; i < elements.size(); ++i)
			if (!elements.get(i).equals(group.elements.get(i)))
				return false;

		return true;
	}

	@Override
	public int hashCode() {

		int code = 30;

		for (int i = 0; i < elements.size(); ++i)
			code *= elements.get(i).hashCode();

		return code;
	}
}
