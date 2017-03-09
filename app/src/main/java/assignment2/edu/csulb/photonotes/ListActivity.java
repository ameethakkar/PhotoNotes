package assignment2.edu.csulb.photonotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ImageData imgData;
    ArrayList<String> notes = new ArrayList<String>();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
//        notes.addAll(imgData.getPhotoCaption());
        populateListView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePhoto = new Intent(getApplicationContext(), TakePhoto.class);
                startActivity(takePhoto);
            }
        });


    }

    private void populateListView() {
        try {
            if(imgData != null) {
                imgData.open();
                Cursor cursor = imgData.getAllRows();

                String[] fromFieldNames = new String[]{imgData.COLUMN_CAPTION};
                SimpleCursorAdapter myCursorAdaptor;
                int[] toViewIDs = new int[]{android.R.id.text1};
                myCursorAdaptor = new SimpleCursorAdapter(getBaseContext(), R.layout.activity_list, cursor, fromFieldNames, toViewIDs, 0);
                ListView myListView = (ListView) findViewById(R.id.listView);

                myListView.setAdapter(myCursorAdaptor);
            }
        } catch (Exception e) {
            Log.d("Populate List View","cursor exception details: " + e);
        }
        finally {
            if(imgData != null) {
                imgData.close();
            }
        }
    }
//        imgData = new ImageData(getApplicationContext());
//        notes.addAll(imgData.getPhotoCaption());
//        SimpleAdapter simpleAdpt = new SimpleAdapter(this, notes, android.R.layout.simple_list_item_1,
//                new String[] {"planet"}, new int[] {android.R.id.text1});


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
