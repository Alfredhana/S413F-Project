package comps413f.searchsystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;


// Fragment for show list of courses
public class CourseListFragment extends Fragment {
    static final String COURSE_ADAPTER = "COURSE_ADAPTER";
    CourseListToActivityListener callback;

    // Interface for supporting communication between activity and fragment
    public interface CourseListToActivityListener {
        // Callback method
        void courseListToActivity(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  Setup ListView for the CourseListFragment in additional to item click event handling

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.course_list_layout, container, false);

        // Get adapter from activity
        SimpleAdapter adapter  = (SimpleAdapter) getArguments().getSerializable(COURSE_ADAPTER);

        // ListView setup
        ListView listView = view.findViewById(R.id.course_listview);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // ListView event handler
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Return selected item position to activity
                        callback.courseListToActivity(position);
                    }
                }
        );

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (SearchSystem) context;
    }
}