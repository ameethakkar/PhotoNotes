package assignment2.edu.csulb.photonotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListActivity extends AppCompatActivity {

    private ImageData imgData;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    ListView myListView;
    public String path;
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
        myListView = (ListView) findViewById(R.id.listView);
        populateListView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePhoto = new Intent(getApplicationContext(), TakePhoto.class);
                startActivity(takePhoto);
            }
        });
//
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewPhoto = new Intent(getApplicationContext(), ViewPhoto.class);
                Cursor c = (Cursor)parent.getAdapter().getItem(1);

                String listItem = c.getString(position);
                Log.d("ListActivity", "Selected Item position " +
                        position + " Selected Item: " + listItem);
                viewPhoto.putExtra("caption", listItem.toString());
                path = imgData.getPhotoURI(listItem);
                viewPhoto.putExtra("path", path);
                Log.d("ListActivity: ", "Selected URI " + path);
                startActivity(viewPhoto);
            }
        });
       /* myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewPhoto = new Intent(getApplicationContext(), ViewPhoto.class);
                String listItem =  myListView.getItemAtPosition(position).toString();
                //Object listItem = getString(view.get);
                Log.d("ListActivity", "Selected Item position " +
                        position + " Selected Item: " + listItem);
                path = imgData.getPhotoURI(listItem);
                viewPhoto.putExtra("imagePath", path);
                Log.d("ListActivity: ", "Selected URI " + path);
                viewPhoto.putExtra("caption", listItem.toString());
                startActivity(viewPhoto);
            }
        });*/

    }

    private void populateListView() {
        try {
            imgData = new ImageData(getApplicationContext());
            if(imgData != null) {
                imgData.open();
                Cursor cursor = imgData.getAllRows();
                String[] fromFieldNames = new String[]{PhotoDBHelper.COLUMN_CAPTION};
                SimpleCursorAdapter myCursorAdaptor;
                int[] toViewIDs = new int[]{android.R.id.text1};
                myCursorAdaptor = new SimpleCursorAdapter(getBaseContext(), R.layout.content_list, cursor, fromFieldNames, toViewIDs, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

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
        if (id == R.id.action_uninstall) {
            Uri packageURI = Uri.parse("package:assignment2.edu.csulb.photonotes");
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            startActivity(uninstallIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
