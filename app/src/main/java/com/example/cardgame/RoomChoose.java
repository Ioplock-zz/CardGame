package com.example.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cardgame.auxiliaryClasses.Room;
import com.example.cardgame.auxiliaryClasses.RoomAdapter;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.cardgame.GameActivity.API_URL;

public class RoomChoose extends AppCompatActivity {

    ListView list;
    Retrofit retrofit;
    @SuppressLint("StaticFieldLeak")
    public static EditText nickname;
    List<Room> roomList;
    RoomAdapter adapter;
    MyServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_choose);
        nickname = findViewById(R.id.nickname);
        list = findViewById(R.id.list);
        DoIt();
    }

    public void RefreshRooms(View view) {
        DoIt();
    }

    private void DoIt() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        server = retrofit.create(MyServer.class);
        Call<UniverseResponse> call = server.getRooms(UniverseRequest.GetRooms());

        call.enqueue(new Callback<UniverseResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                UniverseResponse response1 = response.body();
                if(response1 != null && response1.rooms != null ) roomList = response1.rooms;
                Toast.makeText(getApplicationContext(), "Количество комнат: " + roomList.size(), Toast.LENGTH_SHORT).show();
                adapter = new RoomAdapter(getApplicationContext(), new ArrayList<>(roomList));
                Log.d("DEBUG", String.valueOf(response1.rooms));
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<UniverseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ой что-то пошло не так", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
