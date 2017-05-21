package com.example.g50.chitr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityLanding extends Activity {

    Button openCamera, openGallery;
    private static final int SELECT_PICTURE = 1;
    private static final int OPEN_CAMERA = 1;
    String selectedImagePath;
    String filemanagerstring;

    Bitmap photoTakenFromCamera;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        openCamera = (Button) findViewById(R.id.openCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("camera", "startCameraActivity()");
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    }catch(IOException ioe){
                        Log.d("IOException", "Create Image File IOE:" + ioe.toString());
                    }
                    if(photoFile!=null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(intent, OPEN_CAMERA);
                    }
                }
            }
        });
        openGallery = (Button) findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == SELECT_PICTURE && data !=null) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if (selectedImagePath != null) {
                    System.out.println(selectedImagePath);
                    System.out.println(selectedImagePath);
                } else
                    System.out.println("selectedImagePath is null");

                if (filemanagerstring != null)
                    System.out.println(filemanagerstring);
                else System.out.println("filemanagerstring is null");


                if (selectedImagePath != null) {
                    Intent activityFilters = new Intent(getApplicationContext(), ActivityFilters.class);
                    activityFilters.putExtra("imagePath", selectedImageUri.toString());
                    startActivity(activityFilters);
                    finish();
                }
            }else if(requestCode == OPEN_CAMERA) {
                Log.i("YO! to Camera", "resultCode: " + resultCode);
                try {
                    photoTakenFromCamera = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                            Uri.parse(photoPath));
                    if (photoTakenFromCamera != null) {
                        Intent activityFilters = new Intent(getApplicationContext(), ActivityFilters.class);
                        activityFilters.putExtra("imagePath", Uri.parse(photoPath).toString());
                        startActivity(activityFilters);
                        finish();
                    }
                } catch (IOException ioe) {
                    Log.d("IOException", "Camera IOE:" + ioe.toString());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FILTER_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        photoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String selectedImagePath;
        //1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
        String[] projection = { MediaStore.Images.Media.DATA };
        //Cursor cursor = managedQuery(uri, projection, null, null, null);
        Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        }else{
            selectedImagePath = null;
        }

        if(selectedImagePath == null){
            //2:OI FILE Manager --- call method: uri.getPath()
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

}
