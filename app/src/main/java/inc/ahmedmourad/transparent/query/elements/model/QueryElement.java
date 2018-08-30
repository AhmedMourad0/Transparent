package inc.ahmedmourad.transparent.query.elements.model;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface QueryElement {

	int TYPE_PARAMETER = 0;
	int TYPE_RELATION = 1;
	int TYPE_GROUP = 2;

	// ensuring toString is always annotated with NonNull
	@NonNull
	String toString();

	boolean isRelation();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean isValid();

	void validate();

	void display(@NonNull FlexboxLayout flexbox);

	@QueryElementType
	int getElementType();

	@IntDef({TYPE_PARAMETER, TYPE_RELATION, TYPE_GROUP})
	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD, ElementType.PARAMETER})
	@interface QueryElementType {
	}
}
