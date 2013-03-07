package com.steamedpears.comp3004;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.routing.Router;
import org.apache.log4j.BasicConfigurator;
import org.junit.*;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

public class CardTests {

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    @Test
    public void testCards() {
        JsonParser parser = new JsonParser();
        JsonArray cardJSON = parser
                .parse(new InputStreamReader(CardTests.class.getResourceAsStream(SevenWonders.PATH_CARDS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_CARDS)
                .getAsJsonArray();
        JsonArray wonderJSON = parser
                .parse(new InputStreamReader(CardTests.class.getResourceAsStream(SevenWonders.PATH_WONDERS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_WONDERS)
                .getAsJsonArray();

        List<Card> cards = Card.parseDeck(cardJSON);
        Map<String, Wonder> wonders = Wonder.parseWonders(wonderJSON);
        URL blankurl = null;
        try {
            blankurl = new URL("file://");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for(Card c : cards) {
            assertNotEquals(c.getImagePath().getPath(), blankurl.getPath());
        }

        for(Wonder w : wonders.values()) {
            w.setSide(Wonder.PROP_WONDER_SIDE_A);
            assertNotEquals(w.getImagePath().getPath(), blankurl.getPath());
            w.setSide(Wonder.PROP_WONDER_SIDE_B);
            assertNotEquals(w.getImagePath().getPath(), blankurl.getPath());
        }
    }
}
