package inc.ahmedmourad.transparent.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.transparent.R;

public class SettingsActivity extends AppCompatActivity {

	@SuppressWarnings("WeakerAccess")
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	private Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		unbinder = ButterKnife.bind(this);

		setSupportActionBar(toolbar);
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}
}
