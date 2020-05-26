package com.example.cardgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardgame.AcyncTasks.RefreshTable;
import com.example.cardgame.Views.Card_View_Final;
import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.auxiliaryClasses.CardsAdapter;
import com.example.cardgame.auxiliaryClasses.WaitForMove;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity {

    public static ArrayList<Card> table = new ArrayList<>();
    public static ArrayList<Card> cards;
    @SuppressLint("StaticFieldLeak")
    public static WaitForMove waitForMove;
    @SuppressLint("StaticFieldLeak")
    public static Button startButton;
    Retrofit retrofit;
    MyServer server;
    @SuppressLint("StaticFieldLeak")
    public static CardsAdapter adapter;
    public static TextView score;
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
        cards = new ArrayList<>();
        token = intent.getStringExtra("token");
        myNick = intent.getStringExtra("nickname");

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        server = retrofit.create(MyServer.class);

        score = findViewById(R.id.score);
        list_cards = findViewById(R.id.cards);
        startButton = findViewById(R.id.startGame);
        card_views.add(findViewById(R.id.card_backGround));
        card_views.add(findViewById(R.id.card_forceGround));
        waitForMove = new WaitForMove(findViewById(R.id.wait1), findViewById(R.id.wait2), findViewById(R.id.button));

        RefreshTable task = new RefreshTable(this, Integer.parseInt(token));
        //noinspection unchecked
        task.execute();

        Call<UniverseResponse> call = server.getVip(UniverseRequest.GetVip(Integer.parseInt(token)));

        call.enqueue(new Callback<UniverseResponse>() {
            @Override
            public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                if(response.body() != null) {
                    if(response.body().vip) {
                        startButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UniverseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "IDK what went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        startButton.setOnClickListener(v -> {
            Call<UniverseResponse> call1 = server.startGame(UniverseRequest.StartGame(Integer.parseInt(token)));

            call1.enqueue(new Callback<UniverseResponse>() {
                @Override
                public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                    if(response.body() != null) {
                        Log.d("DEBUG", "Success start game");
                        startButton.setVisibility(View.GONE);
                        waitForMove.hide();
                    }
                }

                @Override
                public void onFailure(Call<UniverseResponse> call, Throwable t) {
                    Log.d("DEBUG", "IDK but can't start game");
                }
            });
        });
    }

    public void refresh() {
        try {
            card_views.get(1).changeCard(table.get(0));
            card_views.get(0).changeCard(table.get(1));
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
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    GameActivity.super.onBackPressed();
                    adapter = null;
                }).create().show();
    }

    public void takeCard(View view) {
        Call<UniverseResponse> call = server.getRooms(UniverseRequest.TakeCard(Integer.parseInt(token)));
        call.enqueue(new Callback<UniverseResponse>() {
            @Override
            public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                UniverseResponse response1 = response.body();
                // TODO: Брать карту с сервера

                cards.add(0, response1.card);

                if(adapter == null) {
                    adapter = new CardsAdapter(cards, Integer.parseInt(token), getApplicationContext());
                    list_cards.setAdapter(new SlideInBottomAnimationAdapter(adapter));
                    list_cards.setItemAnimator(new SlideInUpAnimator());
                } else {
                    adapter.notifyItemInserted(3);
                }
                Log.d("DEBUG", String.valueOf(response1.card));

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

    public void onBackPressed(boolean b) {
        GameActivity.super.onBackPressed();
    }
}
