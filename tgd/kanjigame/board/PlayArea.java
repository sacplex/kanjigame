package com.tgd.kanjigame.board;

import com.tgd.kanjigame.Game;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.sprite.CardSprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class PlayArea
{
    private int x, y;
    private int width, height;
    private Rectangle box;

    private static ArrayList<CardIndex> cardIndices;

    private static int numberOfCardsPlaying = 0;
    private static boolean dragingCard = false;

    public PlayArea(int x, int y, int width, int height)
    {
        box = new Rectangle(x, y, width, height);

        cardIndices = new ArrayList<>();

        for(int i=0; i<PlayerBoard.NUMBER_OF_STARTING_CARDS; i++)
        {
            CardIndex cardIndex = new CardIndex(i);

            cardIndices.add(cardIndex);
        }
    }

    public boolean hasCardIntersectedWithPlayArea(Card card)
    {
        boolean retIntersection = false;
        System.out.println("Has card intersected with playing area");

        if(card != null)
        {
            card.updateBoxLocation();
            PlayArea.setDragingCard(false);

            if (box.intersects(card.getFrontOfCard().getBox()))
            {
                System.out.println("Card has intersected with playing area");
                card.reverseBoxLocation();
                card.toFrontOfCardLarge();
                //card.setCardIndex(PlayArea.getNumberOfCardsPlaying());
                int cardIndex = 0;

                for(int i=0; i < PlayerBoard.NUMBER_OF_STARTING_CARDS; i++)
                {
                    if(!cardIndices.get(i).hasLock())
                    {
                        card.setCardIndex(cardIndices.get(i).getNextIndex());

                        break;
                    }
                }

                card.buildPlayingBox(cardIndex);

                card.setCardState(Card.CARD_STATE.Playing);
                retIntersection = true;
            }
            else
            {
                card.reverseBoxLocation();
            }
        }
        else
        {
            System.out.println("Card is null or not interactive");
        }

        return retIntersection;
    }

    public static void setNumberOfCardsPlaying(int numberOfCardsPlaying) { PlayArea.numberOfCardsPlaying = numberOfCardsPlaying; }

    public static int getNumberOfCardsPlaying() { return numberOfCardsPlaying; }

    public static void setDragingCard(boolean dragingCard) { PlayArea.dragingCard = dragingCard; }

    public static boolean getDragingCard() { return dragingCard; }

    public static ArrayList<CardIndex> getCardIndices() { return cardIndices; }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.RED);

        gc.strokeRect(box.x, box.y, box.width, box.height);
    }

    public class CardIndex
    {
        private int index;
        private boolean lock;

        public CardIndex(int index)
        {
            this.index = index;
        }

        public int getNextIndex()
        {
            lock = true;

            return index;
        }

        public boolean hasLock()
        {
            return lock;
        }

        public boolean reLock(int index)
        {
            System.out.println("Remove card Index: " + index);

            if(this.index == index)
            {
                lock = false;

                return true;
            }

        return false;
        }

        public void reLock2()
        {
            lock = false;
        }

        public String toString()
        {
            return "Card Index: " + index + ", Lock: " + lock;
        }
    }
}
