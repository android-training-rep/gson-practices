package com.example.okhttppractices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.okhttppractices.model.Person;
import com.example.okhttppractices.model.Wrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity {
    public static final String myUrl = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";
    private static final String TAG = "Network Activity";

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
        Observer observer = new Observer<Wrapper>(){

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Wrapper wrapper) {
                List<Person> data = wrapper.getData();
                if (data.size() >= 1) {
                    Toast.makeText(NetworkActivity.this, data.get(0).getName(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(NetworkActivity.this, "Get Data Failure", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };

        Observable observable = Observable.create(new ObservableOnSubscribe<Wrapper>(){
            OkHttpClient client = new OkHttpClient();

            @Override
            public void subscribe(ObservableEmitter<Wrapper> emitter) throws Exception {
                try {
                    Request request = new Request.Builder()
                            .url(myUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String respStr = response.body().string();
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<Wrapper>() {}.getType();
                        Wrapper wrapper = gson.fromJson(respStr, type);
                        emitter.onNext(wrapper);
                        emitter.onComplete();
                    } else if (!response.isSuccessful()) {
                        emitter.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    emitter.onError(e);
                }
            }
        });

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }
}