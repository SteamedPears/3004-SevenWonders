package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.*;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.ParseException;

public class TradesView extends JPanel {
    private Player player;
    private AssetMap leftTrades;
    private AssetMap rightTrades;
    private TradeChangeListener listener;
    private String MIG_CONFIG = "w 60!";

    private static Logger log = Logger.getLogger(TradesView.class);

    public TradesView(Player player) {
        this.player = player;
        this.leftTrades = new AssetMap();
        this.rightTrades = new AssetMap();
        setLayout(new MigLayout("gap 0, inset 0"));
        update();
    }

    /**
     * Sets the trade change listener on this view
     * @param listener the trade change listener to handle trade changes
     */
    public void setListener(TradeChangeListener listener) { this.listener = listener; }

    /**
     * Updates this trade view
     */
    public void update() {
        removeAll();

        add(new JLabel("Neighbor"));
        add(new AssetView(),"span");

        add(new JLabel("Left"));
        Player leftNeighbor = player.getPlayerLeft();
        add(new AssetView(leftNeighbor.getAssetsTradeable()),"span");

        add(new JLabel(""));
        for(final String s : Asset.TRADEABLE_ASSET_TYPES) {
            final JFormattedTextField assetField = new JFormattedTextField(new NumberFormatterFactory(),new Integer(0));
            assetField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    log.info("adding to left trades: " + assetField.getValue() + " " + s);
                    leftTrades.put(s,(Integer)assetField.getValue());
                    if(listener != null) listener.handleChange(leftTrades,rightTrades);
                }
            });
            add(assetField, MIG_CONFIG);
        }

        add(new JLabel("Right"),"newline");
        Player rightNeighbor = player.getPlayerRight();
        add(new AssetView(rightNeighbor.getAssetsTradeable()
        ),
                "span");

        add(new JLabel(""),"newline");
        for(final String s : Asset.TRADEABLE_ASSET_TYPES) {
            final JFormattedTextField assetField = new JFormattedTextField(new NumberFormatterFactory(),new Integer(0));
            assetField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    log.info("adding to right trades: " + assetField.getValue() + " " + s);
                    rightTrades.put(s,(Integer)assetField.getValue());
                    if(listener != null) listener.handleChange(leftTrades,rightTrades);
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

    /**
     * An interface for listening for and handling trade changes
     */
    public interface TradeChangeListener {
        public void handleChange(AssetMap leftTrades,AssetMap rightTrades);
    }
}
