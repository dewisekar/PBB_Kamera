package com.example.pbb_kamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE = 223;
    private static final String TAG = MainActivity.class.getSimpleName();
    //    private static final int REQUEST_IMAGE_CAPTURE = 101;
    Button button;
    Button uploadButton;
    ImageView imageView;
    Bitmap bmg;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askWritePermission();
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.labelText);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = imageView.getDrawable();
                boolean hasImage = (drawable != null);

                if (hasImage && (drawable instanceof BitmapDrawable)) {

//                    Toast.makeText(getApplicationContext(), "error anjeng", Toast.LENGTH_LONG).show();
                    sendPost();


                } else {
                    Toast.makeText(getApplicationContext(), "take a photo", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        bmg = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    private void askWritePermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int cameraPermission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "not granted", Toast.LENGTH_LONG).show();
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
            }
        }
    }

    public void sendPost() {
        if(editText.length()==0 ||  editText.getText().toString().trim().equals(".")){
            editText.setError("Label tidak boleh kosong!");
        }
//        Toast.makeText(getApplicationContext(), "masuk sendpost", Toast.LENGTH_LONG).show();
       else{
            String img_b64 = "data:image/jpeg;base64," + encodeToBase64(bmg, Bitmap.CompressFormat.JPEG, 100);

            ApiInterface apiService =  RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

            Log.i(TAG, img_b64.toString());
            Call<FileUpload> call = apiService.uploadFile(editText.getText().toString().trim(), img_b64);
            call.enqueue(new Callback<FileUpload>() {
                @Override
                public void onResponse(Call<FileUpload> call, Response<FileUpload> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "sukses", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "post submitted to API." + response.body().toString());
                    }
                    else{
                        Log.i(TAG, "post failed to be submitted to API." + response.toString());
                        Toast.makeText(getApplicationContext(), "salah format", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<FileUpload> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "gagal ngepost", Toast.LENGTH_LONG).show();
                    Log.e("kenapa gagal ngepost " + TAG, t.toString());
                }
            });
        }

    }

    private static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
