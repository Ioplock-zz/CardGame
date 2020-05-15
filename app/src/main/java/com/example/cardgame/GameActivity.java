package com.example.cardgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardgame.AcyncTasks.RefreshTable;
import com.example.cardgame.Views.Card_View_Final;
import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.auxiliaryClasses.CardsAdapter;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity {

    public static ArrayList<Card> table = new ArrayList<>();
    public static ArrayList<Card> cards = new ArrayList<>();
    Retrofit retrofit;
    MyServer server;
    public static CardsAdapter adapter;
    String myNick;
    ArrayList<Card_View_Final> card_views = new ArrayList<>();
    public static RecyclerView list_cards;
    String token;
    public static final String API_URL = "http://192.168.0.252:8050/"; // TODO: Наконец закинуть сервер на сервер и установить правильную константу

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        myNick = intent.getStringExtra("nickname");

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        server = retrofit.create(MyServer.class);

        card_views.add(findViewById(R.id.card_forceGround));
        card_views.add(findViewById(R.id.card_backGround));
        list_cards = findViewById(R.id.cards);

        RefreshTable task = new RefreshTable(this, Integer.parseInt(token));
        //noinspection unchecked
        task.execute();
    }

    public void refresh() {
        try {
            card_views.get(0).changeCard(table.get(0));
            card_views.get(1).changeCard(table.get(1));
        }catch (Exception ex) {
            Log.d("DEBUG", "No cards on table");
        }
    }

    @Override
    protected void onDestroy() {
        RefreshTable.running = true;
        super.onDestroy();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из игры?")
                .setMessage("Вы действительно хотите выйти?\nЕсли вы покинете вы не сможете вернуться назад")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> GameActivity.super.onBackPressed()).create().show();
    }

    public void takeCard(View view) {
        Call<UniverseResponse> call = server.getRooms(UniverseRequest.TakeCard(Integer.parseInt(token)));
        call.enqueue(new Callback<UniverseResponse>() {
            @Override
            public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                UniverseResponse response1 = response.body();
                // TODO: Брать карту с сервера
                cards.add(0, response1.card);
                adapter = new CardsAdapter(cards, Integer.parseInt(token), getApplicationContext());
                Log.d("DEBUG", String.valueOf(response1.card));
                list_cards.setAdapter(adapter);
                /*LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);*/
                list_cards.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<UniverseResponse> call, Throwable t) {
                Log.d("DEBUG", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
