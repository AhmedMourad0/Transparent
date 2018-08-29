package inc.ahmedmourad.transparent.query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.flexbox.FlexboxLayout;

import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.query.elements.Group;

public class ViewStateManager implements View.OnClickListener {

	private Query query = null;

	@Nullable
	private FlexboxLayout displayFlexbox = null;

	@Nullable
	private View beginGroupView = null;

	@Nullable
	private View endGroupView = null;

	@Nullable
	private View andView = null;

	@Nullable
	private View orView = null;

	@Nullable
	private View enterView = null;

	@Nullable
	private View clearView = null;

	@Nullable
	private EditText keywordEditText = null;

	private boolean canAcceptParameter = true;

	private ViewStateManager() {

	}

	private static ViewStateManager of(@NonNull final Query query) {
		final ViewStateManager manager = new ViewStateManager();
		manager.setQuery(query);
		return manager;
	}

	private void bind() {

		if (beginGroupView != null)
			beginGroupView.setOnClickListener(this);

		if (endGroupView != null)
			endGroupView.setOnClickListener(this);

		if (andView != null)
			andView.setOnClickListener(this);

		if (orView != null)
			orView.setOnClickListener(this);

		if (clearView != null)
			clearView.setOnClickListener(this);

		if (enterView != null)
			enterView.setOnClickListener(this);

		if (keywordEditText != null)
			keywordEditText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					if (enterView != null)
						enterView.setEnabled(canAcceptParameter && s.toString().trim().length() > 0);
				}
			});

		query.setOnElementAddedListener((elements, groups) -> {

			if (displayFlexbox != null)
				query.display(displayFlexbox);

			if (endGroupView != null)
				endGroupView.setEnabled(groups.size() > 0);

			if (elements.size() == 0 && groups.size() == 0) {

				setRelationViewsEnabled(false);
				setInputViewsEnabled(true);

			} else if (groups.size() == 0) {

				setInputViewsEnabled(elements.get(elements.size() - 1).isRelation());
				setRelationViewsEnabled(true);

			} else {

				final Group group = groups.get(groups.size() - 1);
				setInputViewsEnabled(group.size() == 0 || group.get(group.size() - 1).isRelation());

				setRelationViewsEnabled(group.size() > 0);
			}
		});
	}

	private void setRelationViewsEnabled(final boolean enabled) {

		if (orView != null)
			orView.setEnabled(enabled);

		if (andView != null)
			andView.setEnabled(enabled);
	}

	private void setInputViewsEnabled(final boolean enabled) {

		if (beginGroupView != null)
			beginGroupView.setEnabled(enabled);

		canAcceptParameter = enabled;

		if (enterView != null)
			enterView.setEnabled(canEnterKeyword());
	}

	private boolean canEnterKeyword() {
		return keywordEditText == null ? canAcceptParameter : canAcceptParameter && keywordEditText.getText().toString().trim().length() > 0;
	}

	public void release() {

		if (displayFlexbox != null)
			displayFlexbox.removeAllViews();

		query.setOnElementAddedListener(null);
	}

	private void setQuery(@NonNull final Query query) {
		this.query = query;
	}

	public void setDisplayFlexbox(@Nullable final FlexboxLayout displayFlexbox) {
		this.displayFlexbox = displayFlexbox;
	}

	public void setBeginGroupView(@Nullable final View beginGroupView) {
		this.beginGroupView = beginGroupView;
	}

	public void setEndGroupView(@Nullable final View endGroupView) {
		this.endGroupView = endGroupView;
	}

	public void setAndView(@Nullable final View andView) {
		this.andView = andView;
	}

	public void setOrView(@Nullable final View orView) {
		this.orView = orView;
	}

	public void setEnterView(@Nullable final View enterView) {
		this.enterView = enterView;
	}

	public void setClearView(@Nullable final View clearView) {
		this.clearView = clearView;
	}

	public void setKeywordEditText(@Nullable EditText keywordEditText) {
		this.keywordEditText = keywordEditText;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.query_begin_group:
				query.beginGroup();
				break;

			case R.id.query_end_group:
				query.endGroup();
				break;

			case R.id.query_and:
				query.and();
				break;

			case R.id.query_or:
				query.or();
				break;

			case R.id.query_clear:
				query.clear();
				break;

			case R.id.query_enter:
				if (keywordEditText != null) {
					query.param(keywordEditText.getText().toString());
					keywordEditText.setText("");
				}
				break;
		}
	}

	public static class Builder {

		private ViewStateManager manager;

		public static Builder with(@NonNull final Query query) {
			return Builder.of(ViewStateManager.of(query));
		}

		private Builder() {

		}

		private static Builder of(@NonNull final ViewStateManager manager) {
			final Builder builder = new Builder();
			builder.setManager(manager);
			return builder;
		}

		private void setManager(@NonNull final ViewStateManager manager) {
			this.manager = manager;
		}

		public Builder display(@Nullable final FlexboxLayout displayFlexbox) {
			manager.setDisplayFlexbox(displayFlexbox);
			return this;
		}

		public Builder beginGroup(@Nullable final View beginGroupView) {
			manager.setBeginGroupView(beginGroupView);
			return this;
		}

		public Builder endGroup(@Nullable final View endGroupView) {
			manager.setEndGroupView(endGroupView);
			return this;
		}

		public Builder and(@Nullable final View andView) {
			manager.setAndView(andView);
			return this;
		}

		public Builder or(@Nullable final View orView) {
			manager.setOrView(orView);
			return this;
		}

		public Builder enter(@Nullable final View enterView) {
			manager.setEnterView(enterView);
			return this;
		}

		public Builder clear(@Nullable final View clearView) {
			manager.setClearView(clearView);
			return this;
		}

		public Builder keyword(@Nullable final EditText keywordEditText) {
			manager.setKeywordEditText(keywordEditText);
			return this;
		}

		public ViewStateManager build() {
			manager.bind();
			return manager;
		}
	}
}
