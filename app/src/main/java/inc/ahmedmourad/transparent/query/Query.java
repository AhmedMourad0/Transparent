package inc.ahmedmourad.transparent.query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.query.elements.Group;
import inc.ahmedmourad.transparent.query.elements.Parameter;
import inc.ahmedmourad.transparent.query.elements.Relation;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.gson.deserializer.Deserializer;
import inc.ahmedmourad.transparent.query.gson.serializer.Serializer;
import inc.ahmedmourad.transparent.query.utils.QueryUtils;

@SuppressWarnings("UnusedReturnValue")
public class Query {

	private List<QueryElement> elements = new ArrayList<>();

	private final List<Group> groups = new ArrayList<>();

	@Nullable
	private transient OnElementsChangedListener listener = null;

	private static final transient Gson gson;

	static {
		gson = new GsonBuilder().registerTypeAdapter(List.class, new Serializer())
				.registerTypeAdapter(List.class, new Deserializer())
				.create();
	}

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

	@NonNull
	public static Query fromJson(@NonNull final String json) {
		return gson.fromJson(json, Query.class);
	}

	@NonNull
	public static Query empty() {
		return new Query();
	}

	private Query() {

	}

	@NonNull
	public Query param(@NonNull final String parameter) {
		add(Parameter.of(parameter));
		fireElementsChangedListener();
		return this;
	}

	@NonNull
	public Query group(@NonNull final Group group) {
		add(group);
		fireElementsChangedListener();
		return this;
	}

	@NonNull
	public Query and() {

		if (elements.size() == 0 && (groups.size() == 0 || groups.get(groups.size() - 1).size() == 0))
			throw new IllegalStateException("First element of a query or a group can't be a relation.");

		add(Relation.of(Relation.TYPE_AND));
		fireElementsChangedListener();

		return this;
	}

	@NonNull
	public Query or() {

		if (elements.size() == 0 && (groups.size() == 0 || groups.get(groups.size() - 1).size() == 0))
			throw new IllegalStateException("First element of a query or a group can't be a relation.");

		add(Relation.of(Relation.TYPE_OR));

		fireElementsChangedListener();

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
		fireElementsChangedListener();
		return this;
	}

	@NonNull
	public Query endGroup() {

		if (groups.size() == 0)
			throw new IllegalStateException("You must begin a group first before ending it.");

		final Group group = groups.get(groups.size() - 1);

		group.validate();

		if (groups.size() > 1)
			groups.get(groups.size() - 2).group(group);
		else if (group.isValid())
			addElement(group);

		groups.remove(group);

		fireElementsChangedListener();

		return this;
	}

	public Query clear() {
		elements.clear();
		groups.clear();
		fireElementsChangedListener();
		return this;
	}

	private void validate() {

		while (groups.size() > 0)
			endGroup();

		elements = QueryUtils.trim(elements);

		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).validate();
	}

	public void display(@NonNull final FlexboxLayout flexbox) {

		flexbox.removeAllViews();

		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).display(flexbox);

		for (int i = 0; i < groups.size(); ++i) {

			final Group group = groups.get(i);

			flexbox.addView(group.getLeadingView(flexbox.getContext()));

			if (group.isValid())
				group.displayElements(flexbox);
		}
	}

	public boolean isEmpty() {
		return QueryUtils.trim(elements).size() == 0 && QueryUtils.trim(groups).size() == 0;
	}

	void setOnElementsChangedListener(@Nullable final OnElementsChangedListener listener) {
		this.listener = listener;
		fireElementsChangedListener();
	}

	private void fireElementsChangedListener() {
		if (listener != null)
			listener.onElementsChanged(elements, groups);
	}

	@NonNull
	public String toJson() {
		return toJson(false);
	}

	@NonNull
	public String toJson(final boolean validate) {
		if (validate)
			validate();
		return gson.toJson(this);
	}

	@Override
	@NonNull
	public String toString() {

		validate();

		if (elements.size() == 0)
			return "";

		if (elements.size() == 1)
			return elements.get(0).toString();

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < elements.size(); ++i)
			builder.append(elements.get(i)).append(" ");

		return builder.toString().trim().replaceAll(" +", " ");
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		final Query query = (Query) o;

		if (elements.size() != query.elements.size() || groups.size() != query.groups.size())
			return false;

		for (int i = 0; i < elements.size(); ++i)
			if (!elements.get(i).equals(query.elements.get(i)))
				return false;

		for (int i = 0; i < groups.size(); ++i)
			if (!groups.get(i).equals(query.groups.get(i)))
				return false;

		return true;
	}

	@Override
	public int hashCode() {

		int code = 30;

		for (int i = 0; i < elements.size(); ++i)
			code *= elements.get(i).hashCode();

		for (int i = 0; i < groups.size(); ++i)
			code *= groups.get(i).hashCode();

		return code;
	}

	@FunctionalInterface
	interface OnElementsChangedListener {
		void onElementsChanged(@NonNull List<QueryElement> elements, @NonNull List<Group> groups);
	}
}
