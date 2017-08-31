package com.tgd.kanjigame.database;

import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.card.NCard;
import com.tgd.kanjigame.card.StrokeCard;
import com.tgd.kanjigame.math.ComplexMath;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class LoadDatabase
{
    private static LoadDatabase instance = null;
    private static final String ACCESS_ALL_KANJI_DATA = "select * from kanji";

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> permanentCards = new ArrayList<>();

    private Random random = new Random();

    private LoadDatabase()
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int index = 0;

        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanji", "admin", "q2w3e4r5t6y7u8");

            preparedStatement = connection.prepareStatement(ACCESS_ALL_KANJI_DATA);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                buildCards(resultSet);
            }
        }
        catch(SQLException e)
        {
            System.out.println("<LoadData> - ERROR: Data could not be access from kanji database.");
            e.printStackTrace();
        }
    }

    private void buildCards(ResultSet resultSet)
    {
        try
        {
            if(resultSet.getString("colour").equals("white") || resultSet.getString("colour").equals("black"))
                buildStrokeCards(resultSet);
            else
                buildNCards(resultSet);
        }
        catch (SQLException e)
        {
            System.out.println("<LoadData> - ERROR: Data could not be access from the results.");
            e.printStackTrace();
        }
    }

    private void buildStrokeCards(ResultSet resultSet)
    {
        try
        {
            cards.add(new Card(new StrokeCard(
                    resultSet.getString("kanji_character"),
                    resultSet.getString("onyomi"),
                    resultSet.getString("kunyomi"),
                    resultSet.getString("kana")),
                    resultSet.getString("colour"),
                    resultSet.getString("stroke"),
                    resultSet.getString("english")
            ));

            permanentCards.add(new Card(new StrokeCard(
                    resultSet.getString("kanji_character"),
                    resultSet.getString("onyomi"),
                    resultSet.getString("kunyomi"),
                    resultSet.getString("kana")),
                    resultSet.getString("colour"),
                    resultSet.getString("stroke"),
                    resultSet.getString("english")
            ));
        }
        catch (SQLException e)
        {
            System.out.println("<LoadData> - ERROR: Data could not be assigned to the stroke cards.");
            e.printStackTrace();
        }
    }

    private void buildNCards(ResultSet resultSet)
    {
        try
        {
            cards.add(new Card(new StrokeCard(
                    resultSet.getString("kanji_character"),
                    resultSet.getString("onyomi"),
                    resultSet.getString("kunyomi"),
                    resultSet.getString("kana")
                    ),
                    new NCard(
                            resultSet.getString("kanji_character_JLPT"),
                            resultSet.getString("onyomi_JLPT"),
                            resultSet.getString("kunyomi_JLPT"),
                            resultSet.getString("nlevel")
                    ),
                    resultSet.getString("colour"),
                    resultSet.getString("stroke"),
                    resultSet.getString("english")
            ));

            permanentCards.add(new Card(new StrokeCard(
                    resultSet.getString("kanji_character"),
                    resultSet.getString("onyomi"),
                    resultSet.getString("kunyomi"),
                    resultSet.getString("kana")
                    ),
                    new NCard(
                            resultSet.getString("kanji_character_JLPT"),
                            resultSet.getString("onyomi_JLPT"),
                            resultSet.getString("kunyomi_JLPT"),
                            resultSet.getString("nlevel")
                    ),
                    resultSet.getString("colour"),
                    resultSet.getString("stroke"),
                    resultSet.getString("english")
            ));
        }
        catch (SQLException e)
        {
            System.out.println("<LoadData> - ERROR: Data could not be assigned to the stroke cards.");
            e.printStackTrace();
        }
    }

    public static LoadDatabase getInstance()
    {
        if(instance == null)
            instance = new LoadDatabase();

        return instance;
    }

    public ArrayList<Card> getAllCards()
    {
        return cards;
    }

    public Card popUniqueCard()
    {
        return cards.remove(random.nextInt(cards.size()-1));
    }

    public Card popSkewedUniqueCard()
    {
        return cards.remove((int) ComplexMath.nextSkewedBoundedDouble(1, cards.size() - 1, 1, -1));
    }

    public void reset()
    {
        cards.clear();

        for(int i=0; i < permanentCards.size(); i++)
            cards.add(permanentCards.get(i));
    }
}
