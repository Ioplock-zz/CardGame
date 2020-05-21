package com.example.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cardgame.Card_Vars.Colors;
import com.example.cardgame.Card_Vars.Type;
import com.example.cardgame.Views.Card_View_Final;
import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.cardgame.GameActivity.API_URL;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    MyServer server;
    Card_View_Final card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card = findViewById(R.id.temp_card);
        card.changeCard(new Card("red", "default", 1, "red", "default", 1));
        /*
        Все возможные значения я записал в классе com.example.cardgame.auxiliaryClasses.Card
        Сам View лежит в com.example.cardgame.Views.Card_View_Final
        Так же подписал некоторые методы чтобы было проще разобраться
        */
    }

    public void Connect(View v) {

        Intent intent = new Intent(getApplicationContext(), RoomChoose.class);
        startActivity(intent);

    }

    public void Create(View v) {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        server = retrofit.create(MyServer.class);

        Call<UniverseResponse> call = server.getRooms(UniverseRequest.RegRoom());
        call.enqueue(new Callback<UniverseResponse>() {
            @Override
            public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                if(response.body() != null && !response.body().room.equals("")) {
                    UniverseResponse response1 = response.body();
                    Call<UniverseResponse> call1 = server.getRooms(UniverseRequest.Register("Vip", response1.room));

                    call1.enqueue(new Callback<UniverseResponse>() {
                        @Override
                        public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                            UniverseResponse response1 = response.body();
                            assert response1 != null;
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("nickname", "Vip");
                            intent.putExtra("token", response1.token);
                            Log.d("DEBUG", response1.token);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<UniverseResponse> call, Throwable t) {
                            Log.w("ERDEBUG", Objects.requireNonNull(t.getMessage()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UniverseResponse> call, Throwable t) {
                Log.w("ERDEBUG", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
