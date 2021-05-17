package enums;

public enum Commands {

    /*
     * Client: GET | SEND
     * Server: FORWARD| ANSWER
     */

    SEND_LOGOUT("/Quit"),
    SEND_LOGIN("/InitClient"),
    ANSWER_INIT_CLIENT("/ReplyInitClient"),
    GET_CLIENT_LIST("/GetClientList"),
    ANSWER_CLIENT_LIST("/AnswerClientList"),
    GET_PERSON("/GetPerson"),
    ANSWER_PERSON("/AnswerPerson"),
    SEND_GAME_REQUEST("/SendGameRequest"),
    FORWARD_GAME_REQUEST("/ForwardGameRequest"),
    SEND_RESPONSE_GAME_REQUEST("/SendResponseGameRequest"),
    FORWARD_RESPONSE_GAME_REQUEST("/ForwardResponseGameRequest"),
    SEND_GAME_OVER("/SendGameOver"),
    FORWARD_GAME_OVER("/ForwardGameOver"),
    SEND_TEXT_MESSAGE("/SendTextMessage"),
    FORWARD_MESSAGE("/ForwardMessage"),
    ANSWER_DEFAULT("/AnswerDefault");

    public final String value;

    Commands(String value) {
        this.value = value;
    }
}
