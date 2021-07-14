package com.story.egypt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_play)
    ImageButton btn_play;
    @BindView(R.id.btn_set_avatar)
    ImageButton btn_set_avatar;
    @BindView(R.id.btn_info)
    ImageButton btn_info;
    @BindView(R.id.btn_exit)
    ImageButton btn_exit;

    @BindView(R.id.avatar)
    ImageView avatar;

    private final int PICK_IMAGE = 1;
    private int CAMERA_CAPTURE;
    private Uri pUri;
    final int PIC_CROP = 2;
    private int choise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameLoopActivity.class);
                startActivity(intent);
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWindowExit();
            }
        });
        btn_set_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWindow();
                if (choise == 0) {
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (ActivityNotFoundException cant) {
                        String errorMessage = "Your device cant take photo!";
                        Toast toast = Toast.makeText(btn_set_avatar.getContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (choise == 1) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGE);
                }
            }
        });
    }

    public void createDialogWindow(){
        new AlertDialog.Builder(this)
                .setTitle("Chose action")
                .setMessage("What you want to do?")
                .setNegativeButton(getString(R.string.take_photo), listenChoise(0))
                .setPositiveButton(getString(R.string.chose_photo_from_galary), listenChoise(1));
    }

    public DialogInterface.OnClickListener listenChoise(int c){
        if(c == 0) choise = 0;
        if(c == 1) choise = 1;
        return null;
    }

    public void createDialogWindowExit(){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you realy want exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> MainActivity.super.onBackPressed()).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(choise == 0){
            if (requestCode == CAMERA_CAPTURE){
                pUri=imageReturnedIntent.getData();
                cropImage();
            }
            else if (requestCode == PIC_CROP) {
                Bundle extras = imageReturnedIntent.getExtras();
                Bitmap ava = extras.getParcelable("imageReturnedIntent");
                avatar.setImageBitmap(ava);
            }
        }

        if(choise == 1){
            switch (requestCode) {
                case PICK_IMAGE:
                    if (resultCode == RESULT_OK) {
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            avatar.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    }

    private void cropImage() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(pUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException cant){
            String errorMessage = "Your device cant take photo!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        createDialogWindowExit();
    }
}