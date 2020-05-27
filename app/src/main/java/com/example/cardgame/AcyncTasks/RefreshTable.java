package com.example.cardgame.AcyncTasks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.cardgame.GameActivity;
import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.auxiliaryClasses.CardsAdapter;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
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

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(Object[] objects) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GameActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        MyServer server = retrofit.create(MyServer.class);
        do {
            if(Integer.parseInt(GameActivity.score.getText().toString()) >= 500) {
                Call<UniverseResponse> call3 = server.fetchCards(UniverseRequest.EndGame(token));

                call3.enqueue(new Callback<UniverseResponse>() {
                    @Override
                    public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                        if(response.body() != null) {
                            activity.onBackPressed(true);
                            AlertDialog dialog = new AlertDialog.Builder(activity).setMessage("Ты выйграл, поздравляю!").setTitle("Ты выиграл!").setPositiveButton("Ура", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UniverseResponse> call, Throwable t) {

                    }
                });
                return null;
            }
            if(running) return null;
            Call<UniverseResponse> call = server.fetchCards(UniverseRequest.FetchCards(token));

            call.enqueue(new Callback<UniverseResponse>() {
                @Override
                public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                    UniverseResponse response1 = response.body();
                    if(response1 != null) {
                        if (GameActivity.table != null && GameActivity.table.size() > 0 && response1.cards.size() > 0 && !response1.cards.get(0).identical(GameActivity.table.get(0))) {
                            Log.d("DEBUG", "Стол изменился");
                            GameActivity.waitForMove.show();
                            if(response1.cards.get(0).getTypeNow().equals("end")) {
                                activity.onBackPressed(true);
                                AlertDialog dialog = new AlertDialog.Builder(activity).setMessage("Ты проиграл, но нечего повезёт в слейдущий раз").setTitle("Ты проиграл").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                                dialog.show();
                            }

                            if(response1.cards.get(0).getTypeNow().equals("flip")) {
                                Card.flip = !Card.flip;
                                if(GameActivity.adapter != null) GameActivity.adapter.notifyDataSetChanged();
                            }

                            Call<UniverseResponse> call1 = server.getWhoMove(UniverseRequest.GetWhoMove(token));

                            call1.enqueue(new Callback<UniverseResponse>() {
                                @Override
                                public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                                    if(response.body() != null) {
                                        if (response.body().token.equals(String.valueOf(token))) {
                                            GameActivity.waitForMove.hide();
                                            if(response.body().cards != null) {
                                                GameActivity.cards.addAll(response.body().cards);
                                                if(GameActivity.adapter == null) {
                                                    GameActivity.adapter = new CardsAdapter(GameActivity.cards, token, activity.getApplicationContext());
                                                    GameActivity.list_cards.setAdapter(new SlideInBottomAnimationAdapter(GameActivity.adapter));
                                                    GameActivity.list_cards.setItemAnimator(new SlideInUpAnimator());
                                                } else GameActivity.adapter.notifyItemRangeInserted(0, response.body().cards.size());
                                            }
                                        } else if(response.body().token.equals("-1") && response.body().vip && !response.body().isStarted) {
                                            GameActivity.startButton.setVisibility(View.VISIBLE);
                                        } else {
                                            GameActivity.waitForMove.show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<UniverseResponse> call, Throwable t) {

                                }
                            });
                        }
                        if (response1.cards != null)
                            GameActivity.table = new ArrayList<>(response1.cards);
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
