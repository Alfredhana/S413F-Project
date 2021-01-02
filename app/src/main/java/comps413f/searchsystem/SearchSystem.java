package comps413f.searchsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSystem extends AppCompatActivity implements CourseListFragment.CourseListToActivityListener, CourseDetailsFragment.CourseDetailsToActivityListener {
    private CourseList courseList;
    CourseListFragment courseListFragment;
    CourseDetailsFragment courseDetailsFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        courseList = new CourseList();

        createCourseListFragment(CourseList.getCourseList());

        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                createCourseListFragment(CourseList.getCourseList());
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String TAG = SearchSystem.class.getSimpleName();
                if (newText != null && !newText.isEmpty()){
                    Log.i(TAG,  "I recognized the Query Text");
                    List<Map<String, String>> lstfound = new ArrayList<>();
                    for (Map<String, String> item : CourseList.getCourseList()){
                        Log.i(TAG,"This is a loop");
                        if (item.containsValue(newText)){
                            Map<String, String> course = CourseList.getCourseMap(CourseList.getCourse(newText));
                            System.out.print("Course : "+course.values());
                            lstfound.add(course);
                        }
                    }
                    createCourseListFragment(lstfound);
                }
                return true;
            }
        });
    }

    // Create and show course list fragment
    public void createCourseListFragment(List<Map<String, String>> list) {
        final List<Map<String, String>> courseMap = list;
        // Adapter of data list to view
        SerializableSimpleAdapter adapter = new SerializableSimpleAdapter(this, courseMap, R.layout.list_view_item,
                new String[] { CourseList.COURSE_CODE, CourseList.COURSE_TITLE },
                new int[] { R.id.course_code_textview, R.id.course_title_textview }
        );

        //  CourseListFragment object setup, creation and apply to the activity

        // Create bundle with adapter object
        Bundle bundle = new Bundle();
        // Pass the adapter object to the bundle with method putSerializable
        bundle.putSerializable(CourseListFragment.COURSE_ADAPTER, adapter);
        // Create course list fragment
        courseListFragment = new CourseListFragment();
        // Send adapter object to fragment
        courseListFragment.setArguments(bundle);

        // Setup FragmentManager object
        // Setup FragmentTransaction object
        // Replace fragment with method replace
        // Commit of transaction with method commit
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.course_list_layout, courseListFragment);
        fragmentTransaction.commit();
    }

    // Receive selected item position from course list fragment
    @Override
    public void courseListToActivity(int position) {
        // Override the callback method of the interface CourseListToActivityListener
        // i. Obtain a course map with static method getCourseList of the class CourseList
        // ii. Retrieve the corresponding course code with input parameter position
        // iii. Show the specific course details with method showCourseDetails

        final List<Map<String, String>> courseMap = CourseList.getCourseList();
        String courseCode = courseMap.get(position).get(CourseList.COURSE_CODE);
        showCourseDetails(courseCode);
    }

    // Invoke once the "Back" button of the course details fragment is clicked
    @Override
    public void courseDetailsToActivity() {
        createCourseListFragment(CourseList.getCourseList());
    }

    // Show corresponding details of course
    public void showCourseDetails(String courseCode) {
        // Get corresponding course with specific course code
        Course course = CourseList.getCourse(courseCode);

        // Create bundle with course object
        Bundle bundle = new Bundle();
        bundle.putSerializable(CourseDetailsFragment.COURSE, course);

        // Create course details fragment
        courseDetailsFragment = new CourseDetailsFragment();
        // Send course object to fragment
        courseDetailsFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // Fragment replacement
        // Check if landscape or not
        if (findViewById(R.id.course_details_layout) != null) {  // If landscape
            fragmentTransaction.replace(R.id.course_details_layout, courseDetailsFragment);
        } else {  // If portrait
            fragmentTransaction.replace(R.id.course_list_layout, courseDetailsFragment);
        }
        fragmentTransaction.commit();
    }



    /** Called when the options menu is first invoked. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    /** Called when an options menu item is selected. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                new AlertDialog.Builder(this)
                        .setTitle("SearchSystem")
                        .setMessage("In order to convenience computing students in finding their corresponding course.\n\n" +
                                "Including time,date,schedule in class.\n\n" +
                                "We want to develop an application to help students more easily get their class information.")
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
                break;
        }
        return false;
    }


}
