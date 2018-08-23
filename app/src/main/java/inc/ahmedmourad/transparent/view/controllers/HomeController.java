package inc.ahmedmourad.transparent.view.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.adapter.ArticlesRecyclerAdapter;
import inc.ahmedmourad.transparent.pojo.SimpleArticle;
import inc.ahmedmourad.transparent.utils.NetworkUtils;
import inc.ahmedmourad.transparent.view.controllers.base.BaseController;

public class HomeController extends BaseController implements LoaderManager.LoaderCallbacks<List<SimpleArticle>> {

	private static final int LOADER_ID = 0;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.home_refresh)
	SwipeRefreshLayout refreshLayout;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.home_metadata)
	TextView metaTextView;

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.home_recycler)
	RecyclerView recyclerView;

	private ArticlesRecyclerAdapter adapter;

	private Context context;

	private Unbinder unbinder;

	@NonNull
	public static HomeController newInstance() {
		return new HomeController();
	}

	@SuppressWarnings("WeakerAccess")
	public HomeController() {

	}

	@NonNull
	@Override
	protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {

		final View view = inflater.inflate(R.layout.controller_home, container, false);

		unbinder = ButterKnife.bind(this, view);

		context = view.getContext();

		setSupportActionBar(toolbar);

		initializeRecyclerView();

		initializeRefreshLayout();

		metaTextView.setOnClickListener(v -> {
			if (v.getVisibility() == View.VISIBLE)
				loadData();
		});

		loadData();

		return view;
	}

	private void initializeRecyclerView() {
		adapter = new ArticlesRecyclerAdapter(articleId ->
				startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.generateWebUri(articleId)))
		);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
		recyclerView.setVerticalScrollBarEnabled(true);
	}


	private void initializeRefreshLayout() {

		refreshLayout.setColorSchemeResources(
				R.color.colorRefresh_1,
				R.color.colorRefresh_2,
				R.color.colorRefresh_3
		);

		refreshLayout.setOnRefreshListener(this::loadData);
	}

	private void loadData() {

		metaTextView.setVisibility(View.GONE);
		refreshLayout.setRefreshing(true);

		final Activity activity = getActivity();

		if (activity != null) {

			final ConnectivityManager connectivityManager =
					(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivityManager != null) {

				final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

				if (networkInfo != null && networkInfo.isConnected())
					((FragmentActivity) activity).getSupportLoaderManager().initLoader(LOADER_ID, null, this);
				else
					displayConnectionFailed();

			} else {
				displayError();
			}

		} else {
			displayError();
		}
	}

	private void displayConnectionFailed() {

		refreshLayout.setRefreshing(false);

		if (getView() != null && adapter.getItemCount() > 0) {
			Snackbar.make(getView(), R.string.no_connection, Snackbar.LENGTH_LONG).show();
		} else {
			metaTextView.setVisibility(View.VISIBLE);
			metaTextView.setText(R.string.no_connection_refresh);
		}
	}

	private void displayError() {

		refreshLayout.setRefreshing(false);

		if (getView() != null && adapter.getItemCount() > 0) {
			Snackbar.make(getView(), R.string.error, Snackbar.LENGTH_LONG).show();
		} else {
			metaTextView.setVisibility(View.VISIBLE);
			metaTextView.setText(R.string.error_refresh);
		}
	}

	private void displayNoData() {
		adapter.updateList(new ArrayList<>(0));
		refreshLayout.setRefreshing(false);
		metaTextView.setVisibility(View.VISIBLE);
		metaTextView.setText(R.string.no_data_refresh);
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}

	@NonNull
	@Override
	public Loader<List<SimpleArticle>> onCreateLoader(int id, @Nullable Bundle args) {
		refreshLayout.setRefreshing(true);
		return new SimpleArticlesAsyncTaskLoader(context);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<List<SimpleArticle>> loader, @Nullable List<SimpleArticle> results) {

		refreshLayout.setRefreshing(false);

		if (results == null)
			return;

		if (results.isEmpty()) {
			displayNoData();
		} else {
			metaTextView.setVisibility(View.GONE);
			adapter.updateList(results);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<List<SimpleArticle>> loader) {

	}

	private static class SimpleArticlesAsyncTaskLoader extends AsyncTaskLoader<List<SimpleArticle>> {

		SimpleArticlesAsyncTaskLoader(Context context) {
			super(context);
		}

		@Override
		protected void onStartLoading() {
			super.onStartLoading();
			forceLoad();
		}

		@Override
		public List<SimpleArticle> loadInBackground() {
			return NetworkUtils.fetchSimpleArticles();
		}
	}
}
