package com.example.cardgame.AcyncTasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cardgame.GameActivity;
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

@SuppressWarnings("NullableProblems")
public class RefreshTable extends AsyncTask {

    private final int token;
    public static boolean running;
    @SuppressLint("StaticFieldLeak")
    private GameActivity activity;

    @Override
    protected Void doInBackground(Object[] objects) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        MyServer server = retrofit.create(MyServer.class);
        do {
            if(running) return null;
            Call<UniverseResponse> call = server.fetchCards(UniverseRequest.FetchCards(token));

            call.enqueue(new Callback<UniverseResponse>() {
                @Override
                public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                    UniverseResponse response1 = response.body();
                    if(response1 != null) {
                        if (GameActivity.table != null && GameActivity.table.size() > 0 && response1.cards.size() > 0 && !response1.cards.get(0).identical(GameActivity.table.get(0))) {
                            Log.d("DEBUG", "Стол изменился");
                            // TODO: Сделать действия в зависимости от брошенной карты
                        }
                        if (response1.cards != null)
                            GameActivity.table = new ArrayList<>(response1.cards);
                        //Log.d("DEBUG", String.valueOf(response1.cards));
                        activity.refresh();
                    }
                }

                @Override
                public void onFailure(Call<UniverseResponse> call, Throwable t) {
                    Log.w("ERDEBUG", Objects.requireNonNull(t.getMessage()));
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (this.getStatus().equals(Status.RUNNING));
        return null;
    }

    public RefreshTable(GameActivity activity, int token) {
        this.activity = activity;
        this.token = token;
        running = false;
    }
}
