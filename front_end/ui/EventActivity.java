package ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.UUID;

import client.model.Model;
import jordanrj.familymap.R;

/**
 * Created by jordanrj on 12/8/18.
 */

public class EventActivity extends AppCompatActivity {
    private String eventID;

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
            eventID = getIntent().getStringExtra("EVENT_ID_OF_EVENT_CLICKED_ON");
            Bundle bundle = new Bundle();
            bundle.putString("EVENT_ID", eventID);
            // set Fragmentclass Arguments

/*            String parentActName = (String) getIntent()
                    .getCharSequenceExtra("event_activity");

            fragment = MapFragment.newInstance(parentActName);*/
            fragment = new MapFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startTopActivity(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
