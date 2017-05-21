package com.example.g50.chitr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityExport extends Activity {

    Button exportButton;
    String exportPhotoPath;
    EditText customFileName;
    Bitmap imageToExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        byte[] pictureArray = getIntent().getByteArrayExtra("finalImage");
        imageToExport = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);

        customFileName = (EditText) findViewById(R.id.customFileName);
        exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imageFile = null;
                Boolean success = false;
                String customName = customFileName.getText().toString();
                try {
                    imageFile = createImageFile(customName);
                } catch (IOException ioe) {
                    Log.d("IOException", "Export IOE:" + ioe.toString());
                }
                if(imageFile != null){
                    try{
                        FileOutputStream fos = new FileOutputStream(imageFile);
                        imageToExport.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        success = true;
                        fos.close();
                    }catch(Exception any){
                        Log.d("FileNotFound E","E : " + any.toString());
                    }
                }

                if(success){
                    Toast.makeText(ActivityExport.this, "Image Saved to /Pictures", Toast.LENGTH_SHORT).show();
                    Intent activityFilters = new Intent(getApplicationContext(), ActivityLanding.class);
                    startActivity(activityFilters);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent activityFilters = new Intent(getApplicationContext(), ActivityFilters.class);
        startActivity(activityFilters);
        finish();
    }

    private File createImageFile(String customName) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = customName + "_" + timeStamp + "_CUSTOM_FILTER_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        exportPhotoPath = "file:" + image.getAbsolutePath();
        System.out.println(exportPhotoPath);
        return image;
    }
}
