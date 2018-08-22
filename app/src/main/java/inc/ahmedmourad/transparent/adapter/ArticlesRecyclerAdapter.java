package inc.ahmedmourad.transparent.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.pojo.SimpleArticle;
import inc.ahmedmourad.transparent.utils.TimeUtils;

public class ArticlesRecyclerAdapter extends RecyclerView.Adapter<ArticlesRecyclerAdapter.ViewHolder> {

	private final List<SimpleArticle> articlesList = new ArrayList<>();
	private final OnArticleSelectedListener listener;

	public ArticlesRecyclerAdapter(@NonNull final OnArticleSelectedListener listener) {
		this.listener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull final ViewGroup container, final int viewType) {
		return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_article, container, false));
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		holder.bind(articlesList.get(position));
	}

	@Override
	public int getItemCount() {
		return articlesList.size();
	}

	public void updateList(@NonNull final List<SimpleArticle> list) {
		articlesList.clear();
		articlesList.addAll(list);
		notifyDataSetChanged();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.article_title)
		TextView titleTextView;

		@BindView(R.id.article_section)
		TextView sectionTextView;

		@BindView(R.id.article_read_time)
		TextView readTimeTextView;

		@BindView(R.id.article_byline_date)
		TextView bylineDateTextView;

		@BindView(R.id.article_thumbnail)
		ImageView thumbnailImageView;

		private final Picasso picasso;

		private final Context context;

		ViewHolder(final View view) {
			super(view);
			ButterKnife.bind(this, view);
			picasso = Picasso.get();
			context = view.getContext();
		}

		private void bind(@NonNull final SimpleArticle article) {

			if (!TextUtils.isEmpty(article.getThumbnail()))
				picasso.load(article.getThumbnail())
						.placeholder(R.drawable.placeholder)
						.error(R.drawable.placeholder)
						.into(thumbnailImageView);

			titleTextView.setText(article.getTitle());
			sectionTextView.setText(article.getSection());
			readTimeTextView.setText(TimeUtils.getReadTime(context, article.getWordCount()));

			try {
				if (TextUtils.isEmpty(article.getByline()))
					bylineDateTextView.setText(TimeUtils.getRelativeDateTimeString(article.getDate()));
				else
					bylineDateTextView.setText(context.getString(R.string.byline_date,
							article.getByline(),
							TimeUtils.getRelativeDateTimeString(article.getDate())
					));
			} catch (ParseException e) {
				bylineDateTextView.setText(article.getByline());
				e.printStackTrace();
			}

			itemView.setOnClickListener(v -> listener.onArticleSelectedListener(article.getId()));
		}
	}

	@FunctionalInterface
	public interface OnArticleSelectedListener {
		void onArticleSelectedListener(@NonNull final String articleId);
	}
}
