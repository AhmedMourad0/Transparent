package inc.ahmedmourad.transparent.view.controllers.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Controller;

public abstract class BaseController extends Controller {

	protected BaseController() {

	}

	protected void setSupportActionBar(final @NonNull Toolbar toolbar) {
		if (getActivity() != null)
			((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
	}
}
