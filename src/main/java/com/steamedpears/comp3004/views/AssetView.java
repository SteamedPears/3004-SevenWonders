package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.assets.Asset;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.players.Player;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AssetView extends JPanel {
    public static final String IMAGE_TYPE_SUFFIX = ".png";
    public static final String PATH_ICON = SevenWonders.PATH_IMG + "icons" + File.separator;
    public static final String WOOD_ICON = PATH_ICON + "res_wood" + IMAGE_TYPE_SUFFIX;
    public static final String STONE_ICON = PATH_ICON + "res_stone" + IMAGE_TYPE_SUFFIX;
    public static final String CLAY_ICON = PATH_ICON + "res_clay" + IMAGE_TYPE_SUFFIX;
    public static final String ORE_ICON = PATH_ICON + "res_ore" + IMAGE_TYPE_SUFFIX;
    public static final String LOOM_ICON = PATH_ICON + "res_loom" + IMAGE_TYPE_SUFFIX;
    public static final String PAPYRUS_ICON = PATH_ICON + "res_papyrus" + IMAGE_TYPE_SUFFIX;
    public static final String GLASS_ICON = PATH_ICON + "res_glass" + IMAGE_TYPE_SUFFIX;
    public static final String SCIENCE1_ICON = PATH_ICON + "science_cune" + IMAGE_TYPE_SUFFIX;
    public static final String SCIENCE2_ICON = PATH_ICON + "science_comp" + IMAGE_TYPE_SUFFIX;
    public static final String SCIENCE3_ICON = PATH_ICON + "science_gear" + IMAGE_TYPE_SUFFIX;
    public static final String GOLD_ICON = PATH_ICON + "gold1" + IMAGE_TYPE_SUFFIX;
    public static final String VICTORY_ICON = PATH_ICON + "victory" + IMAGE_TYPE_SUFFIX;
    public static final String SHIELD_ICON = PATH_ICON + "shield" + IMAGE_TYPE_SUFFIX;
    public static final String STAGE_ICON = PATH_ICON + "wonder" + IMAGE_TYPE_SUFFIX;

    protected List<Player> players;
    protected Player thisPlayer;
    private int width = 60;

    static Logger log = Logger.getLogger(AssetView.class);

    public static final String MIG_CONFIG_1 = "w ";
    public static final String MIG_CONFIG_2 = "!,gaptop 0, gap 0! 0! 0! 0!";
    public static final String MIG_CONFIG = "w 60!,gaptop 0, gap 0! 0! 0! 0!";

    public AssetView() {
        setLayout(new MigLayout(
                "aligny top,wrap 15, gap 0! 0!", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
        ));

        add(newJLabel(WOOD_ICON, 60), MIG_CONFIG);
        add(newJLabel(STONE_ICON, 60), MIG_CONFIG);
        add(newJLabel(CLAY_ICON, 60), MIG_CONFIG);
        add(newJLabel(ORE_ICON, 60), MIG_CONFIG);
        add(newJLabel(LOOM_ICON, 60), MIG_CONFIG);
        add(newJLabel(PAPYRUS_ICON, 60), MIG_CONFIG);
        add(newJLabel(GLASS_ICON, 60), MIG_CONFIG);
    }

    /**
     * Display a given player's resources
     * @param p The player to be listed
     */
    public AssetView(Player p) {
        players = Arrays.asList(p);
        thisPlayer = null;
        width = 40;
        update();
    }

    /**
     * Display the resources of a list of players, but label p as "You".
     * @param ps The players to be listed
     * @param p The player to be labeled
     */
    public AssetView(List<Player> ps, Player p) {
        players = ps;
        thisPlayer = p;
        update();
    }

    /**
     * Display the resources of a list of players
     * @param ps The players to be listed
     */
    public AssetView(List<Player> ps) {
        players = ps;
        thisPlayer = null;
        update();
    }

    /**
     * Display the resources of an AssetMap
     * @param assets An Asset Map
     */
    public AssetView(AssetMap assets) {
        setLayout(new MigLayout(
                "aligny top,wrap 15", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
        ));

        for(String asset : Asset.TRADEABLE_ASSET_TYPES) {
            add(newTextJLabel("" + assets.get(asset)),MIG_CONFIG);
        }
    }

    public void update() {
        setLayout(new MigLayout(
                "aligny top,wrap 15, gap 0", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
        ));

        if(players.size() > 1) add(new JLabel("Player"), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        else add(new JLabel());
        add(newJLabel(WOOD_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(STONE_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(CLAY_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(ORE_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(LOOM_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(PAPYRUS_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(GLASS_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(SCIENCE1_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(SCIENCE2_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(SCIENCE3_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(GOLD_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(VICTORY_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        add(newJLabel(SHIELD_ICON, width), MIG_CONFIG_1 + width + MIG_CONFIG_2);
        JLabel stageLabel = newJLabel(STAGE_ICON, width);
        resizeJLabelIcon(stageLabel,width,-1);
        add(stageLabel, MIG_CONFIG_1 + width + MIG_CONFIG_2);
        for(Player player : players) {
            if(players.size() > 1) {

                if (thisPlayer != null && thisPlayer.getPlayerId() == player.getPlayerId()) {
                    add(new JLabel("You"));
                } else if (thisPlayer != null && player.equals(thisPlayer.getPlayerLeft())) {
                    add(new JLabel("Left"));
                } else if (thisPlayer != null && player.equals(thisPlayer.getPlayerRight())) {
                    add(new JLabel("Right"));
                } else {
                    add(new JLabel(player.toString()));
                }
            }
            else add(new JLabel());
            AssetMap playerAssets = player.getAssets();
            for(String asset : Asset.ASSET_TYPES) {
                add(newTextJLabel("" + playerAssets.get(asset)),MIG_CONFIG_1 + width + MIG_CONFIG_2);
            }
        }
    }

    private static void resizeJLabelIcon(JLabel label,int width,int height) {
        label.setIcon(
                new ImageIcon(((ImageIcon)label.getIcon()).getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH)));
    }


    private static URL getImagePath(String image){
        URL result = AssetView.class.getResource(image);
        if(result==null){
            log.warn("Missing file " + image);
            try {
                result = new URL("file://");
            } catch (MalformedURLException e) {
                log.error("Null file is malformed, file loading compromised", e);
                System.exit(-1);
            }
        }
        return result;
    }

    private static ImageIcon newImageIcon(String image, int width) {
        ImageIcon imageIcon = new ImageIcon(getImagePath(image));
        imageIcon.getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH);
        return imageIcon;
    }

    public static JLabel newJLabel(String image, int width) {
        JLabel label = new JLabel(newImageIcon(image, width),JLabel.CENTER);
        label.setPreferredSize(new Dimension(width,10));
        return label;
    }

    public static JLabel newTextJLabel(String text) {
        JLabel label = new JLabel(text,JLabel.CENTER);
        label.setPreferredSize(new Dimension(60,10));
        return label;
    }
}
