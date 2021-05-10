package enums;

public enum Commands {

    QUIT("/Quit"),
    INIT_CLIENT("/InitClient"),
    GET_CLIENT_LIST("/getClientList"),
    GET_PERSON("/GetPerson"),
    SEND_GAME_REQUEST("/GameRequest"),
    REPLY_SEND_GAME_REQUEST("/ReplyGameRequest"),
    ACCEPT_GAME_REQUEST("/Accept"),
    SEND_OPPONENT_LOST("/opponentLost"),
    SEND_TEXT_MESSAGE("/SendTextMessage"),
    RECEIVE_MESSAGE("/recMessage"),
    REPLY_INIT_CLIENT("/ReplyInitClient");

    public final String action;

    Commands(String action){
        this.action = action;
    }
}
