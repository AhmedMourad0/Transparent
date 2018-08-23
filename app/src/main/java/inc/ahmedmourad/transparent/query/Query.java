package inc.ahmedmourad.transparent.query;

import android.support.annotation.NonNull;
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

	private List<Group> groups = new ArrayList<>();

	@NonNull
	public static Query with(@NonNull final String parameter) {
		final Query query = new Query();
		query.param(parameter);
		return query;
	}

	@NonNull
	public static Query withGroup() {
		final Query query = new Query();
		query.beginGroup();
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

		if (elements.size() == 0)
			throw new IllegalStateException("First element of a query can't be a relation.");

		add(Relation.of(Relation.TYPE_AND));
		return this;
	}

	@NonNull
	public Query or() {

		if (elements.size() == 0)
			throw new IllegalStateException("First element of a query can't be a relation.");

		add(Relation.of(Relation.TYPE_OR));
		return this;
	}

	private void add(@NonNull final QueryElement element) {
		if (groups.size() == 0)
			addElement(element);
		else
			groups.get(groups.size() - 1).add(element);
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
		groups.add(new Group());
		return this;
	}

	@NonNull
	public Query endGroup() {

		if (groups.size() == 0)
			throw new IllegalStateException("You must begin a group first before ending it.");

		final Group group = groups.get(groups.size() - 1);

		if (groups.size() > 1)
			groups.get(groups.size() - 2).group(group);
		else if (group.isValid())
			addElement(group);

		groups.remove(group);

		return this;
	}

	public void display(@NonNull final ViewGroup viewGroup) {

		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).display(viewGroup);

		for (int i = 0; i < groups.size(); ++i) {

			final Group group = groups.get(i);

			if (group != null) {
				viewGroup.addView(group.getLeadingView(viewGroup.getContext()));
				if (group.isValid())
					group.displayElements(viewGroup);
			}
		}
	}

	@Override
	@NonNull
	public String toString() {

		while (groups.size() > 0)
			endGroup();

		elements = QueryUtils.trim(elements);

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
