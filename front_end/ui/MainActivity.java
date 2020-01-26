package ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import client.model.Model;
import jordanrj.familymap.R;

public class MainActivity extends AppCompatActivity
{
    public static final int LOGIN_FRAGMENT = 1;
    public static final int MAP_FRAGMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify iconify = new Iconify();
        iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            fragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.menu, menu);

        if (!Model.instance().isLoggedIn()) {
            invalidateOptionsMenu();
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);
            menu.findItem(R.id.settings).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
/*
            case android.R.id.home:
                Utils.startTopActivity(this, false);
                return true;
*/
            case R.id.search:
                MainActivity.this.startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.filter:
                MainActivity.this.startActivity(new Intent(this, FilterActivity.class));
                return true;
            case R.id.settings:
                MainActivity.this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void swapFragment(int fragType)
    {
        Fragment fragment = null;
        if (fragType == LOGIN_FRAGMENT) {
            fragment = new LoginFragment();
        }
        else {
            fragment = new MapFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        //Fragment fragment = new MapFragment();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
