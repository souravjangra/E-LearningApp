package androiddev.com.elearning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mToogle;
    private DrawerLayout mDrawerLayout;
    private TextView tv_Name;
    public static final String SHARED_PREFS = "sharedPrefs";
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mToogle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_Name=findViewById(R.id.StudentName);
        isLoggedIn();

        // code for adding event listeners in nav bar
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.db:
                        Toast.makeText(DashboardActivity.this, "DashBoard",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        Intent intent;
                        intent = new Intent(DashboardActivity.this,Explore.class);
                        startActivity(intent);
                        break;
                    case R.id.recommend:
                        Toast.makeText(DashboardActivity.this, "Recommend",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Toast.makeText(DashboardActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logoutId:
                        Toast.makeText(DashboardActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        logOut();
                        default:
                        return true;
                }
                return  true;
            }
        });
    }
    private void isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String a = sharedPreferences.getString("id", "");
        String b = sharedPreferences.getString("password", "");

        if(a.length()==0 && b.length()==0){
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        String Name;
        Name=sharedPreferences.getString("FirstName", "");
        Name+=" "+sharedPreferences.getString("LastName","");
        Toast.makeText(this, Name, Toast.LENGTH_SHORT).show();
        tv_Name.setText(Name);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void logOut(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }


}
