package assignment2.edu.csulb.photonotes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ameethakkar on 3/5/17.
 */

public class TakePhoto extends Activity {

    static final int CAMERA_PIC_REQUEST = 1;
    public static String DEBUG_TAG = "TakePhotoActivity";
    EditText photoCaption;
    String imagePath;
    ImageData imgData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);

        //Retrieve Photo Caption Entered by User
        photoCaption = (EditText) findViewById(R.id.caption);

        //Launch Camera with Click Photo button
        Button clickPhoto = (Button) findViewById(R.id.clickPic);
        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });

        Button saveData = (Button) findViewById(R.id.savePic);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgData = new ImageData(getApplicationContext());

//                String strCaptionName = photoCaption.getText().toString();
//                if(strCaptionName.trim().equals("")) {
//                    photoCaption.setText("Note");
//                    return;
//                }
                imgData.insert(photoCaption.getText().toString(),imagePath);
                finish();
            }
        });

    }
    public void takePicture(){

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.d(DEBUG_TAG, "Error occured while creating the file for photo with value of: " + ex);
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "assignment2.edu.csulb.fileprovider", photoFile);
                    Log.d("PhotoUri", "photo uri: " + photoURI);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }

        }       catch(Exception e){
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        Log.d("CreateImageFile:", "Image path " + imagePath);
        return image;
    }
}
