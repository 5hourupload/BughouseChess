package fhu.bughousechess;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.Nullable;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatDelegate;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends PreferenceActivity
{
    private AppCompatDelegate mDelegate;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //temp
        System.out.println(prefs.getString("player1", "0"));

        listener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
            {
                if(key.equals("player1"))
                {
                    if (prefs.getString("player1", "0").equals("0"))
                    {
                        GameActivity.position1 = true;
                    }
                    else
                    {
                        GameActivity.cpuLevel[0] = Integer.parseInt(prefs.getString("player1", "0")) - 1;
                        GameActivity.position1 = false;
                    }
                }
                if(key.equals("player2"))
                {
                    if (prefs.getString("player2", "0").equals("0"))
                    {
                        GameActivity.position2 = true;
                    }
                    else
                    {
                        GameActivity.cpuLevel[1] = Integer.parseInt(prefs.getString("player2", "0")) - 1;
                        GameActivity.position2 = false;
                    }
                }
                if(key.equals("player3"))
                {
                    if (prefs.getString("player3", "0").equals("0"))
                    {
                        GameActivity.position3 = true;
                    }
                    else
                    {
                        GameActivity.cpuLevel[2] = Integer.parseInt(prefs.getString("player3", "0")) - 1;
                        GameActivity.position3 = false;
                    }
                }
                if(key.equals("player4"))
                {
                    if (prefs.getString("player4", "0").equals("0"))
                    {
                        GameActivity.position4 = true;
                    }
                    else
                    {
                        GameActivity.cpuLevel[3] = Integer.parseInt(prefs.getString("player4", "0")) - 1;
                        GameActivity.position4 = false;
                    }
                }
                if(key.equals("time1") || key.equals("time2"))
                {
                    GameActivity.minute = prefs.getInt("time1", GameActivity.minute);
                    GameActivity.second = prefs.getInt("time2", GameActivity.second);
                    GameActivity.milliseconds = ((GameActivity.minute * 60) + GameActivity.second) * 1000;
                }
                if(key.equals("checking"))
                {
                    GameActivity.checking = prefs.getBoolean("checking", GameActivity.checking);
                }
                if(key.equals("placing"))
                {
                    GameActivity.placing = prefs.getBoolean("placing", GameActivity.placing);
                }
                if(key.equals("reverting"))
                {
                    GameActivity.reverting = prefs.getBoolean("reverting", GameActivity.reverting);
                }
                if(key.equals("firstrank"))
                {
                    GameActivity.firstrank = prefs.getBoolean("firstrank", GameActivity.firstrank);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                MainActivity.menu_code = 1;
                this.finish();
                return true;
            case R.id.new_game:
                MainActivity.menu_code = 2;
                this.finish();
                return true;
            case R.id.main_menu:
                MainActivity.menu_code = 3;
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        MainActivity.menu_code = 1;
        this.finish();
    }



    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

        }

    }
}