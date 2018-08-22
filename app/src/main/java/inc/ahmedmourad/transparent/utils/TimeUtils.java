package inc.ahmedmourad.transparent.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import inc.ahmedmourad.transparent.R;

public final class TimeUtils {

	@NonNull
	public static String getRelativeDateTimeString(@NonNull final String date) throws ParseException {

		//2014-02-17T12:05:47Z
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);

		return DateUtils.getRelativeTimeSpanString(format.parse(date).getTime(),
				System.currentTimeMillis(),
				DateUtils.MINUTE_IN_MILLIS,
				DateUtils.FORMAT_NO_NOON).toString();
	}

	@NonNull
	public static String getReadTime(@NonNull final Context context, final int wordCount) {
		return context.getString(R.string.minutes, wordCount < 200? 1 : wordCount / 200);
	}

	private TimeUtils() {

	}
}
