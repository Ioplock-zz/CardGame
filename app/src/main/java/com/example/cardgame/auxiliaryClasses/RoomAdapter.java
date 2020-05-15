package com.example.cardgame.auxiliaryClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cardgame.GameActivity;
import com.example.cardgame.R;
import com.example.cardgame.RoomChoose;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.cardgame.GameActivity.API_URL;

public class RoomAdapter extends BaseAdapter {

    private ArrayList<Room> rooms;
    private Context ctx;

    public RoomAdapter(Context ctx, ArrayList<Room> rooms) {
        this.ctx = ctx;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = rooms.get(position);
        convertView = LayoutInflater.from(ctx).
                inflate(R.layout.room_item_list, parent, false);
        Button btw = convertView.findViewById(R.id.connect);
        btw.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "пока ещё не реализовано", Toast.LENGTH_SHORT);
            }
        });
        ((TextView) convertView.findViewById(R.id.roomName)).setText(room.getName());
        ((TextView) convertView.findViewById(R.id.roomId)).setText("ID: " + room.getId());
        ((TextView) convertView.findViewById(R.id.countPlayers)).setText(room.players.size() + "/8");
        convertView.findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoomChoose.nickname.getText().toString().equals("") || RoomChoose.nickname.getText() == null) {
                    Toast.makeText(ctx, "Вы не ввели ник", Toast.LENGTH_SHORT).show();
                } else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    MyServer server = retrofit.create(MyServer.class);
                    Call<UniverseResponse> call = server.getRooms(UniverseRequest.Register(RoomChoose.nickname.getText().toString(), room.getId()));

                    call.enqueue(new Callback<UniverseResponse>() {
                        @Override
                        public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                            UniverseResponse response1 = response.body();
                            assert response1 != null;
                            Intent intent = new Intent(ctx, GameActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("nickname", RoomChoose.nickname.getText().toString());
                            intent.putExtra("token", response1.token);
                            Log.d("DEBUG", response1.token);
                            ctx.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<UniverseResponse> call, Throwable t) {
                            Toast.makeText(ctx, "Ой, что-то пошло не так", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return convertView;
    }
}
