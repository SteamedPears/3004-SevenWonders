package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Asset;
import com.steamedpears.comp3004.models.AssetMap;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.ParseException;
import java.util.ArrayList;

public class TradesView extends JPanel {
    private Player player;
    private AssetMap leftTrades;
    private AssetMap rightTrades;
    private String MIG_CONFIG = "w 60!";

    private static Logger log = Logger.getLogger(TradesView.class);

    public TradesView(Player player) {
        this.player = player;
        this.leftTrades = new AssetMap();
        this.rightTrades = new AssetMap();
        setLayout(new MigLayout("gap 0, inset 0"));
        update();
    }

    public void update() {
        removeAll();

        add(new JLabel("Neighbor"));
        add(new AssetView(),"span");

        add(new JLabel("Left"));
        Player leftNeighbor = player.getPlayerLeft();
        add(new AssetView(leftNeighbor.getAssetsTradeable(),
                leftNeighbor.getOptionalAssetsCompleteTradeable()),
                "span");

        add(new JLabel(""));
        for(final String s : Asset.TRADEABLE_ASSET_TYPES) {
            final JFormattedTextField assetField = new JFormattedTextField(new NumberFormatterFactory(),new Integer(0));
            assetField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    log.info("adding to left trades: " + assetField.getValue() + " " + s);
                    leftTrades.put(s,(Integer)assetField.getValue());
                }
            });
            add(assetField, MIG_CONFIG);
        }

        add(new JLabel("Right"),"newline");
        Player rightNeighbor = player.getPlayerRight();
        add(new AssetView(rightNeighbor.getAssetsTradeable(),
                rightNeighbor.getOptionalAssetsCompleteTradeable()),
                "span");

        add(new JLabel(""),"newline");
        for(final String s : Asset.TRADEABLE_ASSET_TYPES) {
            final JFormattedTextField assetField = new JFormattedTextField(new NumberFormatterFactory(),new Integer(0));
            assetField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    log.info("adding to right trades: " + assetField.getValue() + " " + s);
                    rightTrades.put(s,(Integer)assetField.getValue());
                }
            });
            add(assetField, MIG_CONFIG);
        }
    }

    private class NumberFormatterFactory extends JFormattedTextField.AbstractFormatterFactory {

        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jFormattedTextField) {
            return new JFormattedTextField.AbstractFormatter() {
                @Override
                public Object stringToValue(String s) throws ParseException {
                    Integer val = new Integer(0);
                    try{
                        val = Integer.parseInt(s,10);
                    } catch(Exception e) {
                        log.info("User entered invalid value");
                    }
                    return val;
                }

                @Override
                public String valueToString(Object o) throws ParseException {
                    return o.toString();
                }
            };
        }
    }

    private class DocumentListenerAdapter implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {}

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {}

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {}
    }
}
