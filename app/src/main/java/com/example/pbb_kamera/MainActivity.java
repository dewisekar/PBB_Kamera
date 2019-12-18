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
    Button button, predictbtn;
    ImageView imageView, logo;
    Bitmap bmg;
    EditText editText;
    String hasilPredict = "kertas";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askWritePermission();
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
//        imageView = (ImageView) findViewById(R.id.imageView);
        predictbtn = findViewById(R.id.predictbtn);
        logo = findViewById(R.id.logo);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        predictbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasilPredict.equals("kertas")) {
                    setContentView(R.layout.activity_paper);
                } else if(hasilPredict.equals("daun")){
                    setContentView(R.layout.activity_daun);
                }else if(hasilPredict.equals("botol")){
                    setContentView(R.layout.activity_botol);
                }else{

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        bmg = bitmap;
        logo.setImageBitmap(bitmap);
        predictbtn.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);


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
