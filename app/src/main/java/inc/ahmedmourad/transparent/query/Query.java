package inc.ahmedmourad.transparent.query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.query.elements.Group;
import inc.ahmedmourad.transparent.query.elements.Parameter;
import inc.ahmedmourad.transparent.query.elements.Relation;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

public class Query {

	private List<QueryElement> elements = new ArrayList<>();

	private Group group = null;

	@NonNull
	public static Query with(@NonNull final String parameter) {
		final Query query = new Query();
		query.param(parameter);
		return query;
	}

	@NonNull
	public static Query with(@NonNull final Group group) {
		final Query query = new Query();
		query.setGroup(group);
		return query;
	}

	private Query() {

	}

	@NonNull
	public Query param(@NonNull final String parameter) {
		add(Parameter.of(parameter));
		return this;
	}

	@NonNull
	public Query group(@NonNull final Group group) {
		add(group);
		return this;
	}

	@NonNull
	public Query and() {
		add(Relation.of(Relation.TYPE_AND));
		return this;
	}

	@NonNull
	public Query or() {
		add(Relation.of(Relation.TYPE_OR));
		return this;
	}

	private void add(@NonNull final QueryElement element) {
		if (group == null)
			addElement(element);
		else
			group.add(element);
	}

	private void addElement(@NonNull final QueryElement element) {

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
	}

	@NonNull
	public Query beginGroup() {

		if (group != null)
			throw new IllegalStateException("You must end the group first");

		setGroup(new Group());

		return this;
	}

	@NonNull
	public Query endGroup() {

		if (group == null)
			return this;

		if (group.isValid())
			addElement(group);

		setGroup(null);

		return this;
	}

	private void setGroup(@Nullable final Group group) {
		this.group = group;
	}

	public void display(@NonNull final ViewGroup viewGroup) {

		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).display(viewGroup);

		if (group != null && group.isValid()) {
			viewGroup.addView(group.getLeadingView(viewGroup.getContext()));
			group.displayElements(viewGroup);
		}
	}

	@Override
	@NonNull
	public String toString() {

		endGroup();

		elements = QueryUtils.fix(elements);

		if (elements.size() == 0)
			return "";

		if (elements.size() == 1)
			return elements.get(0).toString();

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < elements.size(); ++i)
			builder.append(elements.get(i)).append(" ");

		return builder.toString().trim().replaceAll(" +", " ");
	}
}
