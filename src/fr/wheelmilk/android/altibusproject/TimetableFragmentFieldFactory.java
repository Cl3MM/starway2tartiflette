package fr.wheelmilk.android.altibusproject;

import java.lang.reflect.InvocationTargetException;

import fr.wheelmilk.android.altibusproject.layout.AutoResizeTextView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class TimetableFragmentFieldFactory {
	String leftText;
	String rightText;
	Activity activity;
	View layout;
	DialogAdapterFactory dialogInstance;

	public TimetableFragmentFieldFactory( View layout, Activity activity, int leftText, int rightText, Class klass) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.leftText = activity.getResources().getString( leftText );
		this.rightText = activity.getResources().getString( rightText);
		this.activity = activity;
		this.layout = layout;
//		Object that will hold onClick event handler, and dialog creation
		this.dialogInstance = DialogAdapterFactory.class.getConstructor(Activity.class).newInstance(activity);
	}
	
//	Return timetable_fragment_field layout
	private View getTimetableFragmentLayout() {
		LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View timetableFragmentFieldlayout = inflater.inflate(R.layout.timetable_fragment_field, null);
		return timetableFragmentFieldlayout;
	}
	
//	Set value for left and right textviews.
//	attach onClick event to DialogAdapter
	public View getTimetableFragmentFieldView() {
		
		View timetableFragmentFieldlayout = this.getTimetableFragmentLayout();
		
		View leftTextView = timetableFragmentFieldlayout.findViewById(R.id.timetableFragmentTextField);
	    View rightTextView = timetableFragmentFieldlayout.findViewById(R.id.timetableFragmentSearchField);

	    ((TextView) leftTextView).setText(leftText);
	    ((AutoResizeTextView) rightTextView).setText( rightText);

	    this.dialogInstance.attachTextView(rightTextView);

		return timetableFragmentFieldlayout;
		
	}
}






















//    View.OnClickListener onclicklistener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//        	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        	Log.v("Altibus onClick: ", "Creating Dialog");
//
//        	// 2. Chain together various setter methods to set the dialog characteristics
//        	builder.setTitle("Choisissez une gare")
//            	.setItems(new String[] { "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", "This", "Is", 
//            			"Demo" }, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int position) {
//                // The 'which' argument contains the index position
//                // of the selected item
//                Log.v("Altibus onClickListener Result: ", "position: " + position);
//            }
//            });
//
//        	// 3. Get the AlertDialog from create()
//        	AlertDialog dialog = builder.create();
//        	dialog.show();
//        }
//    }; 
	
	
//    View testTimetableFragmentField = getLayoutInflater(savedInstanceState).inflate(R.layout.timetable_fragment_field, null);
//    View leftTextView = testTimetableFragmentField.findViewById(R.id.timetableFragmentTextField);
//    View rightTextView = testTimetableFragmentField.findViewById(R.id.timetableFragmentSearchField);
//    String leftText = getResources().getString((Integer) mEntry.getKey());
//    String rightText = getResources().getString((Integer) mEntry.getValue());
//
////    setting textview
//    ((TextView) leftTextView).setText(leftText);
//    ((AutoResizeTextView) rightTextView).setText( rightText);
//    rightTextView.setClickable(true);
//    rightTextView.setOnClickListener(onclicklistener);
//    
//    testTimetableLinearLayout.addView(testTimetableFragmentField);