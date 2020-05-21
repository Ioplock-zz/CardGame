package com.example.cardgame.auxiliaryClasses.forRetrofit;

import com.example.cardgame.auxiliaryClasses.Card;

public class UniverseRequest {

    public Card card;
    public int token;
    public String nickname;
    public String action;
    public String room;

    private UniverseRequest(String action, int token) {
        this.action = action;
        this.token = token;
    }

    private UniverseRequest(String action) {
        this.action = action;
    }

    public UniverseRequest(String action, String nickname, String room) {
        this.action = action;
        this.nickname = nickname;
        this.room = room;
    }

    public UniverseRequest(String action, int token, Card card) {
        this.action = action;
        this.token = token;
        this.card = card;
    }

    public static UniverseRequest Register(String nickname, String room) {
        return new UniverseRequest("register", nickname, room);
    }

    public static UniverseRequest FetchCards(int token) {
        return new UniverseRequest("fetch_cards", token);
    }

    public static UniverseRequest GetRooms() {
        return new UniverseRequest("get_rooms");
    }

    public static UniverseRequest TakeCard(int token) {
        return new UniverseRequest("take_card", token);
    }

    public static UniverseRequest RegRoom() {
        return new UniverseRequest("reg_room");
    }

    public static UniverseRequest ThrowCard(int token, Card card) {
        return new UniverseRequest("throw_card", token, card);
    }

    public static UniverseRequest GetWhoMove(int token) {
        return new UniverseRequest("get_who_move", token);
    }
}
