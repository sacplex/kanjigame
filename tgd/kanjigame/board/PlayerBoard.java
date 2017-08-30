package com.tgd.kanjigame.board;

import com.tgd.kanjigame.Game;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.control.Button;
import com.tgd.kanjigame.database.LoadDatabase;
import com.tgd.kanjigame.io.ImageIO;
import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.players.Player;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerBoard
{
    public static final int NUMBER_OF_STARTING_CARDS = 12;
    private ArrayList<Card> cards = new ArrayList<>(NUMBER_OF_STARTING_CARDS);
    private PlayArea playArea;
    private Button playingButton;
    private ImageIO imageIO;

    private Player player;

    public PlayerBoard(LoadDatabase database, ImageIO imageIO)
    {
        this.imageIO = imageIO;
        Card card;
        playArea = new PlayArea(192, 144, Game.WIDTH/2 + Game.WIDTH/6, Game.HEIGHT/3);
        playingButton = new Button(imageIO.getImage("play_button.png"), Game.WIDTH/2 - 100, 100);

        for(int i=0; i < NUMBER_OF_STARTING_CARDS; i++)
        {
            if (i <= PlayerBoard.NUMBER_OF_STARTING_CARDS - 2)
                card = database.popSkewedUniqueCard();
            else
                card = database.popUniqueCard();

            cards.add(card);
        }

        for(int i=0; i< NUMBER_OF_STARTING_CARDS/2; i++)
            cards.get(i).buildGraphics(imageIO, 260 + (i*125), 560);

        for(int i=NUMBER_OF_STARTING_CARDS/2; i< NUMBER_OF_STARTING_CARDS; i++)
            cards.get(i).buildGraphics(imageIO,260 + ((i-NUMBER_OF_STARTING_CARDS/2)*125), 420);
    }

    public void draw(GraphicsContext gc)
    {
        playArea.draw(gc);

        playingButton.draw(gc);

        for(int i=0; i < cards.size(); i++)
        {
            cards.get(i).draw(gc);
        }
    }

    public Card hasMouseIntersectedWithCard(int x, int y)
    {
        Card card;
        int i;

        Collections.sort(cards, Card.sortWithOtherPlayersCards());
        //Collections.sort(cards);

        /*for(int j=0; j<NUMBER_OF_STARTING_CARDS; j++)
            cards.get(j).resetMoveUp();*/

        for(i=0; i < cards.size(); i++)
        {
            if (cards.get(i).isPlayingIntersected(x, y, this)) {

                System.out.println("Pre PlayArea.getDragingCard(): " + PlayArea.getDragingCard());

                if(!PlayArea.getDragingCard()) {
                    System.out.println("drag card");
                    cards.get(i).moveUp();
                }

                System.out.println("Post PlayArea.getDragingCard(): " + PlayArea.getDragingCard());
            }

            if(cards.get(i).isIntersected(x, y))
            {
                System.out.println(cards.get(i).getCardState());

                if(cards.get(i).getCardState() == Card.CARD_STATE.Pre_Play)
                {
                    PlayArea.setDragingCard(true);
                    System.out.println("PlayArea.getDragingCard(): " + PlayArea.getDragingCard());

                    card = cards.get(i);
                    card.setDrag(!card.getDrag());

                    cards.remove(card); // Remove the selected card from the board, and then
                    cards.add(card); // Re-add the selected card to the back of the list

                    return card;
                }
                else if(cards.get(i).getCardState() == Card.CARD_STATE.Playing)
                {
                    //Collections.sort(cards, Card.sortWithOtherPlayersCards());

                    if(!PlayArea.getDragingCard())
                    {
                        System.out.println("Back To Front");
                        System.out.println(cards.get(i).getCardIndex());

                        cards.get(i).setCardState(Card.CARD_STATE.Pre_Play);
                        cards.get(i).backToFrontOfCard();
                        PlayArea.getCardIndices().get(cards.get(i).getCardIndex()).reLock2();

                        /*for (int j = 0; j < NUMBER_OF_STARTING_CARDS; j++) {
                            if (PlayArea.getCardIndices().get(j).reLock(cards.get(i).getCardIndex()))
                                break;
                        }*/
                    }
                }
            }
        }

        for(i=0; i < cards.size(); i++)
            cards.get(i).setDrag(false);

        return null;
    }

    public void hasMouseIntersectedWithButton(int x, int y)
    {
        if(playingButton.intersected(x, y))
        {
            if(!PlayArea.getDragingCard()) {
                System.out.println("Button Pressed"); // Play Button Pressed

                for(int i=0; i<cards.size(); i++)
                {
                    if(cards.get(i).getCardState() == Card.CARD_STATE.Playing)
                        cards.get(i).setCardState(Card.CARD_STATE.Played);
                }

                //validator.validate();

                CardHolderNetworkObject cardHolderNetworkObject = new CardHolderNetworkObject();

                for(int i=0; i<cards.size(); i++)
                {
                    if(cards.get(i).getCardState() == Card.CARD_STATE.Played)
                        cardHolderNetworkObject.add(cards.get(i));
                }

                player.getClient().sendCardsToServer(cardHolderNetworkObject);
            }
        }
    }

    public Card getUpCard()
    {
        Card card = null;

        for(int i = 0; i < NUMBER_OF_STARTING_CARDS; i++)
        {
            if(cards.get(i).getMoveUpY() == 20) {
                System.out.println("I am up");
                card = cards.get(i);
                break;
            }
        }

        return card;
    }

    public void resetUpCard()
    {
        for(int i = 0; i < NUMBER_OF_STARTING_CARDS; i++)
            cards.get(i).resetMoveUp();
    }

    public synchronized void sortPlayingCardToFront(Card playingCard)
    {
        cards.remove(playingCard);
        cards.add(playingCard);
    }

    public void sortCards()
    {
        Collections.sort(cards);
    }

    public void setPlayer(Player player)
    {
        player.getClient().addPlayerBoard(this);

        this.player = player;
    }

    public PlayArea getPlayArea() { return playArea; }

    public void swapCard(Card card, Card theOtherCard)
    {
        int tempX = theOtherCard.getPlayingBox().x;
        int tempY = theOtherCard.getPlayingBox().y;
        int tempCardIndex = theOtherCard.getCardIndex();

        theOtherCard.getFrontOfCard().setLocation(card.getFrontOfCard().getX(), card.getFrontOfCard().getY());
        theOtherCard.getPlayingBox().x = card.getPlayingBox().x;
        theOtherCard.getPlayingBox().y = card.getPlayingBox().y;
        theOtherCard.setCardIndex(card.getCardIndex());

        card.getFrontOfCard().setLocation(tempX, tempY);
        card.getPlayingBox().x = tempX;
        card.getPlayingBox().y = tempY;
        //card.setCardIndex(tempCardIndex);

        Collections.sort(cards);
    }

    public void addOtherPlayersCards(ArrayList<Card> cards)
    {
        System.out.println("add Other Players Cards");
        System.out.println(cards.size());

        boolean moveIncomingCardsUp = true;

        /*for(int i=0; i<this.cards.size(); i++)
        {
            if(this.cards.get(i).getCardState() == Card.CARD_STATE.Played && this.cards.get(i).getDestUpY() == 0)
            {
                PlayArea.getCardIndices().get(this.cards.get(i).getCardIndex()).reLock2();
                this.cards.remove(i);
            }
        }*/

        for(int i=0; i<this.cards.size(); i++)
        {
            if(this.cards.get(i).getCardState() != Card.CARD_STATE.Pre_Play)
            {
                PlayArea.getCardIndices().get(this.cards.get(i).getCardIndex()).reLock2();
                this.cards.remove(i);
                i--;
            }
        }

        PlayArea.resetCardIndices();

        for(int i=0; i<cards.size(); i++)
        {
            cards.get(i).buildGraphics(this.imageIO, 0,0);
            cards.get(i).toFrontOfCardLarge();
            cards.get(i).setCardState(Card.CARD_STATE.Post_Play);

            //if(moveIncomingCardsUp)
                cards.get(i).nextPlayerPlayed();

            this.cards.add(cards.get(i));
        }

        /*for(int i=0; i<this.cards.size(); i++)
        {
            if(this.cards.get(i).getCardState() == Card.CARD_STATE.Playing)
                this.cards.get(i).nextPlayerPlayed();
        }*/

        /*for(int i=0; i<this.cards.size(); i++)
        {
            if(this.cards.get(i).getCardState() == Card.CARD_STATE.Post_Play)
            {
                System.out.println("Moving Up Played Card");

            }

        }*/
    }
}
