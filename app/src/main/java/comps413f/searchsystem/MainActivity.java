package comps413f.searchsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Splash screen
public class MainActivity extends AppCompatActivity {
    EditText nameEt, ageEt;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);  //<- bug --cannot trun off splash screen
        Resources res = getResources();
        boolean isShowing = sharedPreferences.getBoolean(res.getString(R.string.pref_splash_key), res.getBoolean(R.bool.pref_splash_default));
        if (!isShowing) {
            showMainMenu();
            return;
        }
        setContentView(R.layout.splash);
        nameEt = findViewById(R.id.nameEt);
        ageEt = findViewById(R.id.ageEt);


    }

    // Called when touched.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            saveSharePreferences();
            showMainMenu();
        }
        return true;
    }

    // Shows the SearchSystem activty and finishes this activity.
    private void showMainMenu() {

        //  Create and start another activity with Intent

        Intent intent=new Intent(MainActivity.this,SearchSystem.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }

    private void saveSharePreferences(){
        String name;
        if (nameEt.getText().toString().matches("")){
            name = "User";
        }else{
            name = nameEt.getText().toString();
        }

        int age;
        if (ageEt.getText().toString().matches("")){
            age = 0;
        }else{
            age = Integer.parseInt(ageEt.getText().toString().trim());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("multiplication","Name"+name);
        Log.i("multiplication","Age"+age);
        editor.putString("Name", name);
        editor.putInt("Age",age);
        editor.apply();

        Toast.makeText(MainActivity.this, "Information Saved!", Toast.LENGTH_SHORT).show();
    }
}
