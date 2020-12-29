package comps413f.searchsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

// Fragment for show course details
public class CourseDetailsFragment extends Fragment implements View.OnClickListener {
    static final String COURSE = "COURSE";
    CourseDetailsToActivityListener callback;

    public interface CourseDetailsToActivityListener {
        void courseDetailsToActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.course_details_layout, container, false);

        // Get adapter from activity
        Course course = (Course) getArguments().getSerializable(COURSE);

        // Show course detials
        TextView textView;
        textView = view.findViewById(R.id.course_code);
        textView.setText(course.getCourseCode());
        textView = view.findViewById(R.id.course_title);
        textView.setText(course.getCourseTile());
        textView = view.findViewById(R.id.course_date);
        textView.setText(course.getDate());
        textView = view.findViewById(R.id.course_time);
        textView.setText(Integer.toString(course.getTime()));

        Button button = view.findViewById(R.id.back_button);
        // Register button event handling
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        callback.courseDetailsToActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (SearchSystem) context;
    }
}