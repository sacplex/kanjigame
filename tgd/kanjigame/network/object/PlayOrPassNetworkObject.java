package com.tgd.kanjigame.network.object;

public class PlayOrPassNetworkObject extends NetworkObject
{
    public enum PLAY_STATE {PLAY, PASS};
    private PLAY_STATE play_state;

    private CardHolderNetworkObject cardHolderNetworkObject;
    private String position;

    public PlayOrPassNetworkObject(CardHolderNetworkObject cardHolderNetworkObject, String position)
    {
        this.cardHolderNetworkObject = cardHolderNetworkObject;
        this.position = position;

        if(cardHolderNetworkObject == null)
            play_state = PLAY_STATE.PASS;
        else
            play_state = PLAY_STATE.PLAY;
    }

    public PLAY_STATE getPlayState()
    {
        return play_state;
    }

    public CardHolderNetworkObject getCardHolderNetworkObject()
    {
        return cardHolderNetworkObject;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
