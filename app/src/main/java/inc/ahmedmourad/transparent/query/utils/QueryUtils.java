package inc.ahmedmourad.transparent.query.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.query.elements.model.QueryElement;

public final class QueryUtils {

	public static List<QueryElement> fix(@NonNull final List<QueryElement> elements) {

		final List<QueryElement> clone = new ArrayList<>(elements);

		trim(elements);

		return clone;
	}

	private static void trim(@NonNull final List<QueryElement> elements) {

		while (elements.size() > 0 && (elements.get(0).isRelation() || !elements.get(0).isValid()))
			elements.remove(0);

		while (elements.size() > 0 && (elements.get(elements.size() - 1).isRelation() || !elements.get(elements.size() - 1).isValid()))
			elements.remove(elements.size() - 1);
	}

	@NonNull
	public static CardView createView(@NonNull final Context context, @ColorRes final int backgroundColor, @NonNull final String value) {

		final CardView cardView = new CardView(context);

		cardView.setClickable(true);
		cardView.setFocusable(true);

		final TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
		cardView.setBackgroundResource(typedValue.resourceId);

		cardView.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColor));

		cardView.setRadius(context.getResources().getDimension(R.dimen.queryElementCardCornerRadius));

		cardView.setCardElevation(context.getResources().getDimension(R.dimen.queryElementCardElevation));

		cardView.setPadding(context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingStart),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingTop),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingEnd),
				context.getResources().getDimensionPixelSize(R.dimen.queryElementPaddingBottom)
		);

		final TextView textView = new TextView(context);

		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

		textView.setText(value);

		cardView.addView(textView, 0);

		return cardView;
	}

	public static void updateView(@NonNull final ViewGroup view, @NonNull final String newValue) {
		((TextView) view.getChildAt(0)).setText(newValue);
	}

	private QueryUtils() {

	}
}
