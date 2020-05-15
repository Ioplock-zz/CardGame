package com.example.cardgame.auxiliaryClasses.forRetrofit;

import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.auxiliaryClasses.Room;

import java.util.List;

public class UniverseResponse {

    public String status;
    public String token;
    public List<Card> cards;
    public List<Room> rooms;
    public String room;
    public Card card;

}
