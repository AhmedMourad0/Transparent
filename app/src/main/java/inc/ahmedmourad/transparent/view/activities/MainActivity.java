package inc.ahmedmourad.transparent.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;
import inc.ahmedmourad.transparent.view.controllers.HomeController;

public class MainActivity extends AppCompatActivity {

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.controller_container)
	ChangeHandlerFrameLayout container;

	private Router router;

	private Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		unbinder = ButterKnife.bind(this);

		router = Conductor.attachRouter(this, container, savedInstanceState);

		if (!router.hasRootController())
			router.setRoot(RouterTransaction.with(HomeController.newInstance()));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		if (item.getItemId() == R.id.settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (!router.handleBack())
			super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}
}
