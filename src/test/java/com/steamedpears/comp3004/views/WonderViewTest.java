package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.*;

import com.google.gson.*;
import org.apache.log4j.*;
import org.junit.Ignore;

import javax.swing.*;
import java.io.*;

@Ignore
public class WonderViewTest extends JFrame {
    public WonderViewTest() throws Exception {
        setBounds(0, 0, 1000, 1000);
        JsonArray wonders = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/wonderlist.json"))
                .getAsJsonObject()
                .get("wonders")
                .getAsJsonArray();
        Wonder testWonder = new Wonder(wonders.get(0).getAsJsonObject());
        WonderView view = new WonderView(testWonder);
        add(view);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        new WonderViewTest();
    }
}