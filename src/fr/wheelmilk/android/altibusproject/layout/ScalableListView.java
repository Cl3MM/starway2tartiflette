package fr.wheelmilk.android.altibusproject.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ScalableListView extends ListView {
	public ScalableListView (Context context) {
	    super(context);
	}

	public ScalableListView (Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public ScalableListView (Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
	    super.onSizeChanged(xNew, yNew, xOld, yOld);

	    setSelection(getCount());

	}
}
