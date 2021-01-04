package comps413f.searchsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchSystem extends AppCompatActivity implements CourseListFragment.CourseListToActivityListener, CourseDetailsFragment.CourseDetailsToActivityListener {
    private CourseList courseList;
    CourseListFragment courseListFragment;
    CourseDetailsFragment courseDetailsFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MaterialSearchView searchView;
    TextView nameTv, ageTv, refreshTv;
    SharedPreferences sharedPreferences;

    Button refresh;
    ProgressBar progressBar;
    List<Map<String, String>> lstfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTv = findViewById(R.id.nameTv);
        ageTv = findViewById(R.id.ageTv);

        refreshTv = findViewById(R.id.refreshTv);

        refresh = findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTask(v);
            }
        });
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = sharedPreferences.getString("Name", "");
        nameTv.setText("Name: "+name);
        Integer age = sharedPreferences.getInt("Age", 0);
        ageTv.setText("Age: "+Integer.toString(age));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        courseList = new CourseList();

        lstfound = new ArrayList<>();

        createCourseListFragment(CourseList.getCourseList());

        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                refresh.setVisibility(View.INVISIBLE);
                refresh.setHeight(0);
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setY(0);
            }

            @Override
            public void onSearchViewClosed() {
                refresh.setVisibility(View.VISIBLE);
                refresh.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setY(LinearLayout.LayoutParams.WRAP_CONTENT);
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
                    Log.i(TAG,"newText： "+ newText);
                    for (Map<String, String> item : CourseList.getCourseList()){
                        String key = item.get(item.keySet().toArray()[1]);
                        Log.i(TAG,item.keySet().toArray()[1] + "=" + key);
                        if (key.equals(newText)){
                            Log.i(TAG,"Yes");
                            Map<String, String> course = CourseList.getCourseMap(newText);
                            System.out.print("Course : "+course.values());
                            lstfound.add(course);
                            break;
                        }
                    }
                    createCourseListFragment(lstfound);
                    lstfound = new ArrayList<>();
                }
                return true;
            }
        });
    }

    public void refreshTask(View view){
        final String TAG = SearchSystem.class.getSimpleName();
        Log.i(TAG,  "refreshTask");
        Toast.makeText(SearchSystem.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        RefreshTask refreshTask = new RefreshTask();
        refreshTask.execute();
    }

    class RefreshTask extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPreExecute() {
            refreshTv.setText("Refreshing...");
            progressBar.setVisibility(View.VISIBLE);
            final String TAG = SearchSystem.class.getSimpleName();
            Log.i(TAG,  "onPreExecute");
        }

        @Override
        protected Void doInBackground(String... voids) {
            for (int i = 0; i < 10; i++){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refreshTv.setText("Course Information Refreshed!");
            progressBar.setVisibility(View.INVISIBLE);
            createCourseListFragment(CourseList.getCourseList());
        }
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

            case R.id.settings:
                Intent intent = new Intent(SearchSystem.this, SearchSystemPreferenceActivity.class);
                startActivity(intent);
                break;

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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        LinearLayout course_list_layout;
        // update background color
        String bgColor = prefs.getString(getString(R.string.background_color_key),
                getString(R.string.background_color_default));
        course_list_layout = findViewById(R.id.course_list_layout);
        course_list_layout.setBackgroundColor(Color.parseColor(bgColor));

    }

    }
