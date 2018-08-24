package inc.ahmedmourad.transparent.query.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.query.elements.Group;
import inc.ahmedmourad.transparent.query.elements.Parameter;
import inc.ahmedmourad.transparent.query.elements.Relation;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement.QueryElementType;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static inc.ahmedmourad.transparent.query.elements.model.QueryElement.TYPE_GROUP;
import static inc.ahmedmourad.transparent.query.elements.model.QueryElement.TYPE_PARAMETER;
import static inc.ahmedmourad.transparent.query.elements.model.QueryElement.TYPE_RELATION;

public final class QueryUtils {

	public static List<QueryElement> trim(@NonNull final List<QueryElement> elements) {

		final List<QueryElement> clone = new ArrayList<>(elements);

		while (clone.size() > 0 && (clone.get(0).isRelation() || !clone.get(0).isValid()))
			clone.remove(0);

		while (clone.size() > 0 && (clone.get(clone.size() - 1).isRelation() || !clone.get(clone.size() - 1).isValid()))
			clone.remove(clone.size() - 1);

		return clone;
	}

	@NonNull
	public static CardView createView(@NonNull final Context context, @ColorRes final int backgroundColor, @NonNull final String value) {

		final CardView cardView = new CardView(context);

		cardView.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColor));

		cardView.setRadius(context.getResources().getDimension(R.dimen.queryElementCardCornerRadius));

		cardView.setCardElevation(context.getResources().getDimension(R.dimen.queryElementCardElevation));

		final TextView textView = new TextView(context);

		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

		textView.setPadding(context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingStart),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingTop),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingEnd),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingBottom)
		);

		textView.setText(value);

		cardView.addView(textView, 0);

		final FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

		params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.queryCardMargin),
				context.getResources().getDimensionPixelSize(R.dimen.queryCardMargin),
				context.getResources().getDimensionPixelSize(R.dimen.queryCardMargin),
				context.getResources().getDimensionPixelSize(R.dimen.queryCardMargin)
		);

		cardView.setLayoutParams(params);

		return cardView;
	}

	public static void updateView(@NonNull final ViewGroup view, @NonNull final String newValue) {
		((TextView) view.getChildAt(0)).setText(newValue);
	}

	@NonNull
	public static Class<? extends QueryElement> resolveElementType(@QueryElementType final int type) {

		switch (type) {

			case TYPE_PARAMETER:
				return Parameter.class;

			case TYPE_RELATION:
				return Relation.class;

			case TYPE_GROUP:
				return Group.class;

			default:
				throw new IllegalArgumentException("Unsupported Query Element Type: " + type);
		}
	}

	private QueryUtils() {

	}
}
