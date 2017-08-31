package com.tgd.kanjigame.card;

import com.tgd.kanjigame.Game;
import com.tgd.kanjigame.board.PlayArea;
import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.debug.Debug;
import com.tgd.kanjigame.io.ImageIO;
import com.tgd.kanjigame.network.object.CardNetworkObject;
import com.tgd.kanjigame.network.object.InitialCardNetworkObject;
import com.tgd.kanjigame.sprite.CardSprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.Comparator;

public class Card implements Comparable<Card>
{
    public enum CARD_STATE {Hide, Pre_Play, Playing, Played, Post_Play};
    private CARD_STATE card_state = CARD_STATE.Pre_Play;

    private NCard nCard;
    private StrokeCard strokeCard;
    private CardSprite backOfCard;
    private CardSprite frontOfCard;
    private CardSprite frontOfCardLarge;

    private String colour;
    private String strokes;
    private String english;
    private String shortEnglish;
    private String shorterEnglish;
    private String kanji;
    private String kunyomi;
    private String onyomi;

    private Rectangle playingBox;

    private static final String CARD_FILENAME_EXTENSION = "_v_card.png";
    private static final int NORMAL_ENGLISH_SIZE = 16;
    private static final int SMALL_ENGLISH_SIZE = 12;
    private static final int MULTICARDOFFSETX = 40;

    private enum PLAY_STATE {COVERED, EXPOSED, PLAYING, PLAYED}
    private PLAY_STATE state = PLAY_STATE.COVERED;

    private boolean drawFront;
    private boolean drawSmaller;
    private boolean mouseDown;

    private int playingOffsetX, playingOffsetY;
    private int originalX, originalY;
    private int moveUpY;
    private int cardIndex;
    private int destUpY;

    private boolean original;
    private boolean drag;

    public Card(StrokeCard strokeCard, String colour, String strokes, String english)
    {
        this.strokeCard = strokeCard;
        this.colour = colour;
        this.strokes = strokes;
        this.english = english;

        shortEnglish = english.split(" ")[0];

        if(shortEnglish.substring(shortEnglish.length() - 1).equals(","))
            shortEnglish = shortEnglish.substring(0, shortEnglish.length()-1);

        String [] words = english.split(" ");

        shorterEnglish = words[0];

        if(words.length >= 2)
            shorterEnglish = shorterEnglish + words[1];

        if(words.length >= 3)
            shorterEnglish = shorterEnglish + words[2];

        if (shorterEnglish.substring(shorterEnglish.length()-1).equals(","))
            shorterEnglish = shorterEnglish.substring(0, shorterEnglish.length() - 1);

        if(strokeCard != null)
        {
            if(strokeCard.getKunyomi() != null && strokeCard.getKanjiCharacter().length() != 0) {
                String[] kunyomiArray = strokeCard.getKunyomi().split(".");

                if (kunyomiArray.length > 0)
                    kunyomi = kunyomiArray[0];
                else
                    kunyomi = strokeCard.getKunyomi();
            }
        }

        if(strokeCard != null )
        {
            if(strokeCard.getOnyomi() != null && strokeCard.getOnyomi().length() != 0)
                onyomi = strokeCard.getOnyomi();
        }

        if(strokeCard!= null)
        {
            if(strokeCard.getKanjiCharacter() != null && strokeCard.getKanjiCharacter().length() != 0)
                kanji = strokeCard.getKanjiCharacter();
        }

        drawSmaller = shortEnglish.length() > 12 ? true : false;
        drawFront = false;

        original = true;
    }

    public Card(StrokeCard strokeCard, NCard nCard, String colour, String strokes, String english)
    {
        this.strokeCard = strokeCard;
        this.nCard = nCard;
        this.colour = colour;
        this.strokes = strokes;
        this.english = english;

        shortEnglish = english.split(" ")[0];

        if(shortEnglish.substring(shortEnglish.length() - 1).equals(","))
            shortEnglish = shortEnglish.substring(0, shortEnglish.length()-1);

        String [] words = english.split(" ");

        shorterEnglish = words[0];

        if(words.length >= 2)
            shorterEnglish = shorterEnglish + " " + words[1];

        if(words.length >= 3)
            shorterEnglish = shorterEnglish + " " + words[2];

        if (shorterEnglish.substring(shorterEnglish.length()-1).equals(","))
            shorterEnglish = shorterEnglish.substring(0, shorterEnglish.length() - 1);

        if(nCard != null && nCard.getKunyomi().length() != 0)
            kunyomi = nCard.getKunyomi();

        if(nCard != null && nCard.getOnyomi().length() != 0)
            onyomi = nCard.getOnyomi();

        if(strokeCard!= null && strokeCard.getKanjiCharacter().length() != 0)
            kanji = strokeCard.getKanjiCharacter();

        drawSmaller = shortEnglish.length() > 12 ? true : false;
        drawFront = false;

        original = true;
    }

    public Card(InitialCardNetworkObject initialCardNetworkObject)
    {
        this(new StrokeCard(
                        initialCardNetworkObject.getKanji_character(),
                        initialCardNetworkObject.getOnyomi(),
                        initialCardNetworkObject.getKunyomi(),
                        "default kana"
                ),
                initialCardNetworkObject.getColour(),
                initialCardNetworkObject.getStroke() + "",
                initialCardNetworkObject.getEnglish());
    }

    public Card(CardNetworkObject cardNetworkObject)
    {
        this.strokes = "" + cardNetworkObject.getStrokeNumber();
        this.colour = cardNetworkObject.getColour();
        this.kanji = cardNetworkObject.getKanji();
        this.kunyomi = cardNetworkObject.getKunyomi();
        this.onyomi = cardNetworkObject.getOnyomi();
        this.shortEnglish = cardNetworkObject.getEnglsih();
        this.cardIndex = cardNetworkObject.getCardIndex();
    }

    public void buildGraphics(ImageIO imageIO, int x, int y)
    {
        backOfCard = new CardSprite(imageIO.getImage(colour + CARD_FILENAME_EXTENSION), x, y);
        frontOfCard = new CardSprite(imageIO.getImage("blank_card.png"), x, y);
        frontOfCardLarge = new CardSprite(imageIO.getImage("large_blank_card.png"),0,0);
    }

    public void draw(GraphicsContext gc)
    {
        if(card_state == CARD_STATE.Pre_Play)
            drawFontCard(gc, frontOfCard, 0);
        else if(card_state == CARD_STATE.Playing || card_state == CARD_STATE.Played || card_state == CARD_STATE.Post_Play)
            drawPlayingCard(gc, frontOfCardLarge);

        /*if(state == PLAY_STATE.COVERED)
            backOfCard.draw(gc);
        else if(state == PLAY_STATE.EXPOSED)
            drawFontCard(gc, frontOfCard, 0);
        else if(state == PLAY_STATE.PLAYING)
            drawFontCard(gc, frontOfCard, PLAYINGOFFSETY);*/
    }

    private void drawFontCard(GraphicsContext gc, CardSprite card, int offsetY)
    {
        setColour(gc);
        gc.drawImage(card.getImage(), card.getX() - playingOffsetX, card.getY() - playingOffsetY);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(12));
        gc.fillText(strokes, card.getX() + 90 - playingOffsetX, card.getY() + 15 - playingOffsetY);
        gc.setFont(Font.font(52));
        gc.fillText(strokeCard.getKanjiCharacter(), card.getX() + 50 - playingOffsetX, card.getY() + 65 - playingOffsetY);

        if(drawSmaller)
            gc.setFont(Font.font(SMALL_ENGLISH_SIZE));
        else
            gc.setFont(Font.font(NORMAL_ENGLISH_SIZE));

        gc.fillText(shortEnglish, card.getX() + 50 - playingOffsetX, card.getY() + 120 - playingOffsetY);

        if(Debug.DRAW_PLAYING_BOX && card.getBox() != null) {
            gc.setFill(Color.RED);
            gc.fillRect(card.getBox().x, card.getBox().y, card.getBox().width, card.getBox().height);
        }
    }

    private void drawPlayingCard(GraphicsContext gc, CardSprite card)
    {
        setColour(gc);
        gc.drawImage(card.getImage(), card.getX() - (MULTICARDOFFSETX * cardIndex), card.getY() - moveUpY + destUpY);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(24));
        gc.fillText(strokes, card.getX() + 150 - (MULTICARDOFFSETX * cardIndex), card.getY() + 35 - moveUpY + destUpY);
        gc.setFont(Font.font(100));
        gc.fillText(kanji, card.getX() + 86 - (MULTICARDOFFSETX * cardIndex), card.getY() + 120 - moveUpY + destUpY);

        if(drawSmaller)
            gc.setFont(Font.font(SMALL_ENGLISH_SIZE));
        else
            gc.setFont(Font.font(NORMAL_ENGLISH_SIZE));

        gc.fillText(shortEnglish, card.getX() + 86 - (MULTICARDOFFSETX * cardIndex), card.getY() + 160 - moveUpY + destUpY);

        if(Debug.DRAW_PLAYING_BOX && card.getBox() != null) {
            gc.setFill(Color.RED);
            gc.fillRect(frontOfCard.getBox().x, frontOfCard.getBox().y, frontOfCard.getBox().width, frontOfCard.getBox().height);
        }

        if(Debug.DRAW_MOVE_UP_BOX && playingBox != null) {
            gc.setFill(Color.BLUE);
            gc.fillRect(playingBox.x, playingBox.y, playingBox.width, playingBox.height);
        }
    }

    private void setColour(GraphicsContext gc)
    {
        if(colour.equals("black"))
            gc.setFill(Color.BLACK);
        else if(colour.equals("white"))
            gc.setFill(Color.rgb(137,137,137)); // gray
        else if(colour.equals("purple"))
            gc.setFill(Color.rgb(193,128,214)); // purple
        else if(colour.equals("blue"))
            gc.setFill((Color.rgb(59,85, 255))); // blue
        else if(colour.equals("green"))
            gc.setFill((Color.rgb(73,189, 49))); // green
        else if(colour.equals("orange"))
            gc.setFill((Color.rgb(255,139, 62))); // orange
        else if(colour.equals("red"))
            gc.setFill((Color.rgb(255,57, 57))); // red
    }

    public int getStrokesValue()
    {
        return Integer.parseInt(strokes);
    }

    public String getColour() { return colour; }

    public String getKanji() { return kanji; }

    public String getKunyomi() { return kunyomi; }

    public String getOnyomi() { return onyomi; }

    public String getEnglish() { return shortEnglish; }

    public boolean isIntersected(int x, int y)
    {
        boolean intersected = false;

        if(frontOfCard.getBox().contains(x, y))
            intersected = true;

        return intersected;
    }

    public boolean updatePlayingState(int x, int y)
    {
        return isIntersected(x, y);
     }

    public void move(int x, int y)
    {
        playingOffsetX = playingOffsetX + x;
        playingOffsetY = playingOffsetY + y;
    }

    public void setDrag(boolean drag)
    {
        this.drag = drag;
    }

    public boolean getDrag()
    {
        return drag;
    }

    public void dragCard(int mouseX, int mouseY)
    {
        if(drag)
        {
            if (original) {
                originalX = mouseX;
                originalY = mouseY;
                original = false;
                originalX = originalX + playingOffsetX;
                originalY = originalY + playingOffsetY;

            } else {
                playingOffsetX = originalX - mouseX;
                playingOffsetY = originalY - mouseY;
            }
        }
    }

    public void resetOriginal()
    {
        System.out.println("Outside Playing Area");

        original = true;
        playingOffsetY = 0;
        playingOffsetX = 0;

        drag = true;
        PlayArea.setDragingCard(!PlayArea.getDragingCard());
    }

    public void updateBoxLocation()
    {
        System.out.println("Box X: " + frontOfCard.getBox().x + ", Y: " + frontOfCard.getBox().y);
        System.out.println("Offset X: " + playingOffsetX + ", Y: " + playingOffsetY);

        frontOfCard.getBox().x = frontOfCard.getBox().x - playingOffsetX;
        frontOfCard.getBox().y = frontOfCard.getBox().y - playingOffsetY;
    }

    public void toFrontOfCardLarge()
    {
        frontOfCardLarge.setLocation((int)(Game.WIDTH/2 - frontOfCardLarge.getImage().getWidth()/2 + Game.WIDTH/6), 176);
    }

    public void reverseBoxLocation()
    {
        frontOfCard.getBox().x = frontOfCard.getBox().x + playingOffsetX;
        frontOfCard.getBox().y = frontOfCard.getBox().y + playingOffsetY;

        frontOfCardLarge.setLocation(0,0);

        System.out.println("Box X: " + frontOfCard.getBox().x + ", Y: " + frontOfCard.getBox().y);
        System.out.println("Offset X: " + playingOffsetX + ", Y: " + playingOffsetY);
    }

    public void backToFrontOfCard()
    {
        System.out.println("Box X: " + frontOfCard.getBox().x + ", Y: " + frontOfCard.getBox().y);
        frontOfCard.setLocation(frontOfCard.getBox().x, frontOfCard.getBox().y);
        frontOfCardLarge.setLocation(0,0);
        playingOffsetX = 0;
        playingOffsetY = 0;
        moveUpY = 0;
    }

    public void buildPlayingBox()
    {
        //if(cardIndex == PlayArea.getNumberOfCardsPlaying())
            playingBox = new Rectangle(frontOfCardLarge.getX() + 95 - (MULTICARDOFFSETX * (cardIndex-1)),
                    frontOfCardLarge.getY(),
                    30,
                    220);
        /*else
            playingBox = new Rectangle(frontOfCardLarge.getX() + 95,
                    frontOfCardLarge.getY(),
                    200,
                    220);*/
    }

    public boolean isPlayingIntersected(int x, int y, PlayerBoard playerBoard)
    {
        boolean playing = false;

        if(playingBox!= null && playingBox.contains(x,y))
        {
            Card card = playerBoard.getUpCard();

            System.out.println("Card Index: " + cardIndex);

            if(card != null && frontOfCardLarge.getY() == 156 && card.getFrontOfCardLarge().getY() == 156/*&& moveUpY == 20 && card.getCardState() == CARD_STATE.Playing*/)
            {
                System.out.println("Swap Card Index: " + card.getCardIndex());
                playerBoard.resetUpCard();
                playerBoard.swapCard(this, card);
            }
            else
            {
                playing = true;
            }

            System.out.println("playing: " + playing);
        }



        return playing;
    }

    /*public boolean isPlayingIntersected(int x, int y, PlayArea playArea, Card swapCard)
    {
        boolean playing = false;

        if(playingBox != null && playingBox.contains(x, y))
        {
            if(moveUpY == 20) {
                {}

        playing = true;
        }

        return playing;
    }*/

    public void moveUp()
    {
        if(moveUpY == 0 && card_state == CARD_STATE.Playing)
            moveUpY = 20;
        else
            moveUpY = 0;
    }

    public static Comparator<Card> sortCards()
    {
        return new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.cardIndex - o2.getCardIndex();
            }
        };
    }

    public static Comparator<Card> sortWithOtherPlayersCards()
    {
        return new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                Integer y = o1.getDestUpY();
                int result = y.compareTo(o2.getDestUpY());

                if (result != 0)
                    return y;
                else
                    return o1.cardIndex - o2.getCardIndex();

            }
        };
    }

    @Override
    public int compareTo(Card card) {


            /*String x1 = ((Person) o1).getName();
            String x2 = ((Person) o2).getName();
            int sComp = x1.compareTo(x2);

            if (sComp != 0) {
                return sComp;
            } else {
                Integer x1 = ((Person) o1).getAge();
                Integer x2 = ((Person) o2).getAge();
                return x1.compareTo(x2);
            }*/

        /*if(destUpY == 0)
        {
            Integer index = cardIndex;
            return index.compareTo(card.getCardIndex());
        }
        else
        {*/
            /*Integer y = destUpY;
            int result = y.compareTo(card.getDestUpY());

            if (result != 0)
                return y;
            else {*/
                //Integer y = destUpY;
                //return y.compareTo(card.getDestUpY());

                Integer index = cardIndex;
                return index.compareTo(card.getCardIndex());
            //}
        //}
    }

    public void flipCard() { drawFront = !drawFront; }

    public void setCardState(CARD_STATE card_state) { this.card_state = card_state; }

    public CARD_STATE getCardState() { return card_state; }

    public CardSprite getFrontOfCard() { return frontOfCard; }

    public CardSprite getFrontOfCardLarge() { return frontOfCardLarge; }

    public void setCardIndex(int cardIndex) { this.cardIndex = cardIndex; }

    public int getCardIndex() { return cardIndex; }

    public  int getMoveUpY() { return moveUpY; }

    public void resetMoveUp() { this.moveUpY = 0; }

    public Rectangle getPlayingBox() { return playingBox; }

    public void nextPlayerPlayed() { destUpY = -20; }

    public int getDestUpY() { return destUpY; }

    public String toString()
    {
        String retString = "";

        if(nCard != null)
            retString = "JLPT Card: " + nCard.toString() + "\n" + "Addition information: " + strokeCard.toString() + "\n" + "Colour: " + colour + ", Stroke: " + strokes + ", English: " + english;
        else
            retString = "Card information: " + strokeCard.toString() + "\n" + "Colour: " + colour + ", Stroke: " + strokes + ", English: " + english;

        return retString;
    }
}
