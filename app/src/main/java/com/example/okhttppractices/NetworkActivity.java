package com.example.okhttppractices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity {
    public static final String myUrl = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Button getDataBtn = findViewById(R.id.btn_get_data);
        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        Observer observer = new Observer<Response>(){

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response response) {
                Toast.makeText(NetworkActivity.this, "Get Data Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(NetworkActivity.this, "Get Data Failure", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };

        Observable observable = Observable.create(new ObservableOnSubscribe<Response>(){
            OkHttpClient client = new OkHttpClient();

            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                try {
                    Request request = new Request.Builder()
                            .url(myUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        emitter.onNext(response);
                        emitter.onComplete();
                    } else if (!response.isSuccessful()) {
                        emitter.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    emitter.onError(e);
                }
            }
        });
    }
}