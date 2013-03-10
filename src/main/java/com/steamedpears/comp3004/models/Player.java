package com.steamedpears.comp3004.models;

import com.steamedpears.comp3004.models.players.AIPlayer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.steamedpears.comp3004.models.PlayerCommand.PlayerCardAction.*;
import static com.steamedpears.comp3004.models.Asset.*;

public abstract class Player implements Runnable{
    //static variables//////////////////////////////////////////////////////
    private static int currentId = 0;
    private static Logger log = Logger.getLogger(Player.class);
    private Thread thread;

    //static methods////////////////////////////////////////////////////////
    private static int getNextId(){
        return currentId++;
    }

    public static int getTotalSciencePoints(int science1, int science2, int science3) {
        int total = Math.min(Math.min(science1, science2), science3)*7;

        total += science1*science1;
        total += science2*science2;
        total += science3*science3;

        return total;
    }

    /**
     * Get a new AIPlayer
     * @param wonder the Wonder it should have
     * @param game the SevenWondersGame it is part of
     * @return an AIPlayer
     */
    public static Player newAIPlayer(Wonder wonder, SevenWondersGame game){
        return new AIPlayer(wonder, game);
    }

    //instance variables////////////////////////////////////////////////////
    private Wonder wonder;
    private List<Card> playedCards;
    private Player playerLeft;
    private Player playerRight;
    private SevenWondersGame game;
    private PlayerCommand currentCommand;
    private List<Card> hand;
    private List<Integer> militaryResults;
    private int gold;
    private int id;
    private Map<String, Integer> stagedCommandResult;

    //constructor///////////////////////////////////////////////////////////
    public Player(Wonder wonder, SevenWondersGame game, int id){
        this.wonder = wonder;
        this.game = game;
        this.playedCards = new ArrayList<Card>();
        this.hand = new ArrayList<Card>();
        this.gold = 3;
        this.militaryResults = new ArrayList<Integer>();
        this.id = id;
    }

    public Player(Wonder wonder, SevenWondersGame game){
        this(wonder, game, getNextId());
    }

    private void discardCard(Card card, boolean isFinal){
        log.debug("discarding card: " + isFinal);
        if(isFinal){
            game.discard(card);
            hand.remove(card);
        }else{
            Map<String, Integer> result = new HashMap<String, Integer>();
            result.put(ASSET_GOLD, 3);

            stagedCommandResult = sumAssets(stagedCommandResult, result);
        }

    }

    private void buildWonder(Card card, boolean isFinal){
        log.debug("building wonder: "+isFinal);
        Card stage = wonder.getNextStage();
        if(isFinal){
            wonder.buildNextStage(this);
            hand.remove(card);
        }else{
            stagedCommandResult = sumAssets(stagedCommandResult, stage.getAssets(this));
        }
    }

    private void playCard(Card card, boolean isFinal){
        log.debug("playing card: "+isFinal);
        if(isFinal){
            card.playCard(this);
            playedCards.add(card);
            hand.remove(card);
        }else{
            stagedCommandResult = sumAssets(stagedCommandResult, card.getAssets(this));
        }
    }

    private void undiscard(Card card, boolean isFinal){
        log.debug("undiscarding card: "+isFinal);
        if(isFinal){
            playedCards.add(card);
            game.undiscard(card);
        }else{
            stagedCommandResult = sumAssets(stagedCommandResult, card.getAssets(this));
        }
    }

    private void playFree(Card card, boolean isFinal){
        log.debug("playing free: "+isFinal);
        int oldGold = gold;
        playCard(card, isFinal);
        if(isFinal){
            wonder.expendLimitedAsset(ASSET_BUILD_FREE);
            gold = oldGold;
        }
    }

    public final void applyCommand(PlayerCommand command) throws Exception {
        log.debug("applying command");
        PlayerCommand temp = command;
        while(temp!=null){
            if(!isValid(command)){
                throw new Exception("Given command is invalid: "+ command);
            }
            temp = temp.followup;
        }
        stagedCommandResult = new HashMap<String, Integer>();
        applyCommandInternal(command);
    }

    private void applyCommandInternal(PlayerCommand command){
        if(command!=null){
            resolveCommand(command, false);
            if(command.followup!=null){
                applyCommandInternal(command.followup);
            }
        }
    }

    private void resolveCommand(PlayerCommand command, boolean isFinal){
        Card card = game.getCardById(command.card);
        if(command.action.equals(BUILD)){
            buildWonder(card, isFinal);
        }else if(command.action.equals(PLAY)){
            playCard(card, isFinal);
        }else if(command.action.equals(DISCARD)){
            discardCard(card, isFinal);
        }else if(command.action.equals(UNDISCARD)){
            undiscard(card, isFinal);
        }else if(command.action.equals(PLAY_FREE)){
            playFree(card, isFinal);
        }
    }

    public final void finalizeCommand(PlayerCommand command){
        log.debug("finalizing command");
        if(stagedCommandResult.containsKey(ASSET_GOLD)){
            this.gold+=stagedCommandResult.get(ASSET_GOLD);
        }
        while(command!=null){
            payForTrades(command);
            resolveCommand(command, true);
            command = command.followup;
        }
    }

    private void payForTrades(PlayerCommand command) {
        int total = 0;

        Set<String> leftDiscounts = new HashSet<String>();
        Set<String> rightDiscounts = new HashSet<String>();

        List<Card> cards = new ArrayList<Card>();
        cards.addAll(playedCards);

        List<Card> stages = wonder.getStages();
        for(int i=0; i<wonder.getCurrentStage(); ++i){
            cards.add(stages.get(i));
        }

        for(Card card: cards){
            if(card.getDiscountsTargets().contains(Card.PLAYER_LEFT)){
                leftDiscounts.addAll(card.getDiscountsAssets());
            }
            if(card.getDiscountsTargets().contains(Card.PLAYER_RIGHT)){
                rightDiscounts.addAll(card.getDiscountsAssets());
            }
        }

        total+=getCostOfTrade(command.leftPurchases, leftDiscounts);
        total+=getCostOfTrade(command.rightPurchases, rightDiscounts);
        gold-=total;
    }

    private int getCostOfTrade(Map<String,Integer> purchases, Set<String> discounts) {
        if(purchases==null){
            return 0;
        }else{
            int total = 0;
            for(String key: purchases.keySet()){
                total+= purchases.get(key)*(discounts.contains(key) ? 1 : 2);
            }
            return total;
        }
    }

    public void handleNewAge(){
        wonder.handleNewAge();
    }

    //extend with a GUI or AI
    //when handleTurn terminates, currentCommand should be set with the Player's desired command
    protected abstract void handleTurn();

    public final void run(){
        thread = Thread.currentThread();
        handleTurn();
    }

    public void wake() {
        thread.interrupt();
    }

    //setters///////////////////////////////////////////////////////////////////
    public final void setPlayerLeft(Player playerLeft){
        this.playerLeft = playerLeft;
    }

    public final void setPlayerRight(Player playerRight){
        this.playerRight = playerRight;
    }

    public final void setCurrentCommand(PlayerCommand currentCommand){
        this.currentCommand = currentCommand;
    }

    public final void setHand(List<Card> hand){
        this.hand = hand;
    }

    public void changeGold(int amount) {
        gold+=amount;
    }

    //getters///////////////////////////////////////////////////////////////////
    public final boolean isValid(PlayerCommand command){
        log.debug(this+" validating move "+command);
        //initial result: either they're doing one action, or their other action is UNDISCARD
        log.debug("validating number/types of moves");
        boolean result = command.followup==null
                || command.followup.action==UNDISCARD;

        if(!result){
            //if above is false, then they must be using an ASSET_DOUBLE_PLAY
            Map<String, Integer> assets = getAssets();
            result = assets.containsKey(ASSET_DOUBLE_PLAY)
                    && assets.get(ASSET_DOUBLE_PLAY)>0
                    && hand.size()==2
                    && !command.action.equals(UNDISCARD);
        }
        if(!result) log.debug("validating failed after move type sanity check");

        result = result && validateCanMakeTrades(command);
        if(!result) log.debug("validation failed after trade check");

        //if everything is good so far, perform specific validation
        if(command.action.equals(BUILD)){
            result = result && validateBuildWonder(command);
        }else if(command.action.equals(PLAY)){
            result = result && validatePlayCard(command);
        }else if(command.action.equals(DISCARD)){
            result = result && validateDiscard(command);
        }else if(command.action.equals(UNDISCARD)){
            result = result && validateUndiscard(command);
        }else if(command.action.equals(PLAY_FREE)){
            result = result && validatePlayFree(command);
        }
        if(!result) log.debug("validation failed after specific command check");

        return result;
    }

    private boolean validateHasCard(PlayerCommand command){
        log.debug("validating player has card");
        return hand.contains(game.getCardById(command.card));
    }

    private boolean validateHasNotPlayedCard(PlayerCommand command){
        log.debug("validating player hasn't already player that card");
        Card card = game.getCardById(command.card);
        for(Card playedCard: playedCards){
            if(playedCard.getName().equals(card.getName())){
                return false;
            }
        }
        return true;
    }

    private boolean validatePlayFree(PlayerCommand command) {
        log.debug("validating player can play this card for free");
        return validateHasCard(command)
                && validateHasNotPlayedCard(command)
                && getAsset(getAssets(), ASSET_BUILD_FREE)>0;
    }

    private boolean validateUndiscard(PlayerCommand command) {
        log.debug("validating player can undiscard this card");
        return game.getDiscard().contains(game.getCardById(command.card))
                && validateHasNotPlayedCard(command)
                && getAsset(getAssets(), ASSET_DISCARD)>0;
    }

    private boolean validateDiscard(PlayerCommand command) {
        log.debug("validating player can discard this card");
        return validateHasCard(command);
    }

    private boolean validatePlayCard(PlayerCommand command) {
        log.debug("validating player can play card");
        return validateHasCard(command)
                && validateHasNotPlayedCard(command)
                && game.getCardById(command.card).canAfford(this, command);
    }

    private boolean validateBuildWonder(PlayerCommand command) {
        log.debug("validating player can build out wonder with this card");
        return validateHasCard(command)
                && wonder.getNextStage()!=null
                && wonder.getNextStage().canAfford(this, command);
    }

    private boolean validateCanMakeTrades(PlayerCommand command){
        log.debug("validating player can make the trades they are trying to make");
        return validateCanMakeTradesInternal(command.leftPurchases, getPlayerLeft())
                && validateCanMakeTradesInternal(command.rightPurchases, getPlayerRight());
    }

    private boolean validateCanMakeTradesInternal(Map<String, Integer> purchases, Player player){
        if(purchases==null){
            return true;
        }else{
            Map<String, Integer> tradeables = player.getAssetsTradeable();
            Map<String, Integer> purchasesLessBase = subtractAssets(purchases, tradeables);
            return existsValidChoices(player.getOptionalAssetsCompleteTradeable(), purchasesLessBase);
        }
    }

    public final Player getPlayerLeft(){
        return playerLeft;
    }

    public final Player getPlayerRight(){
        return playerRight;
    }

    // probably not going to break anything if this isn't final
    public List<Card> getHand(){
        return hand;
    }

    // probably fine to mock this out
    public int getGold(){
        return gold;
    }

    // probably fine to mock this out
    public List<Integer> getMilitaryResults(){
        return militaryResults;
    }

    public final int getMilitaryDefeats(){
        int result = 0;
        for(int value: getMilitaryResults()){
            if(value==-1){
                ++result;
            }
        }

        return result;
    }

    public final PlayerCommand getCurrentCommand(){
        return currentCommand;
    }

    public final Wonder getWonder(){
        return wonder;
    }

    public final int getPlayerId(){
        return id;
    }

    @Override
    public int hashCode(){
        return getPlayerId();
    }

    /**
     * gets all the assets that aren't captured in the cards or wonder of the player
     * @return the map
     */
    public final Map<String, Integer> getPrivateAssets(){
        Map<String, Integer> privateAssets = new HashMap<String, Integer>();
        privateAssets.put(ASSET_GOLD, getGold());
        privateAssets.put(ASSET_MILITARY_DEFEAT, getMilitaryDefeats());
        privateAssets.put(ASSET_MILITARY_VICTORY, getMilitaryWins());

        return privateAssets;
    }

    /**
     * gets all the Assets the player definitely has before any decisions
     * @return the map
     */
    public Map<String, Integer> getAssets(){
        List<Map<String, Integer>> collectedAssets = new ArrayList<Map<String, Integer>>();

        for(Card card: playedCards){
            collectedAssets.add(card.getAssets(this));
        }

        collectedAssets.add(wonder.getAssets(this));

        collectedAssets.add(getPrivateAssets());

        Map<String, Integer> result = sumAssets(collectedAssets);
        result.put(ASSET_GOLD, gold);

        return result;
    }

    /**
     * get all the asset choices the player can make
     * @return the list of choices
     */
    public final List<Set<String>> getOptionalAssetsComplete(){
        List<Set<String>> collectedAssets = new ArrayList<Set<String>>();

        for(Card card: playedCards){
            Set<String> options = card.getAssetsOptional(this);
            if(options!=null && !options.isEmpty()){
                collectedAssets.add(options);
            }
        }

        collectedAssets.addAll(wonder.getOptionalAssetsComplete(this));

        return collectedAssets;
    }

    /**
     * get a map representing the total of all the asset choices a player can make
     * e.g. wood/stone and stone/clay -> wood:1, stone:2, clay:1
     * @return the map
     */
    public final Map<String, Integer> getOptionalAssetsSummary(){
        List<Set<String>> options = getOptionalAssetsComplete();

        Map<String, Integer> result = new HashMap<String, Integer>();

        for(Set<String> option: options){
            for(String key: option){
                if(result.containsKey(key)){
                    result.put(key, result.get(key)+1);
                }else{
                    result.put(key, 1);
                }
            }
        }

        return result;
    }

    /**
     * get a map of all the assets a player can definitely trade before making any decisions
     * @return the map
     */
    public final Map<String, Integer> getAssetsTradeable(){
        List<Map<String, Integer>> collectedAssets = new ArrayList<Map<String, Integer>>();

        for(Card card: playedCards){
            if(card.getColor().equals(COLOR_BROWN) || card.getColor().equals(COLOR_GREY)){
                collectedAssets.add(card.getAssets(this));
            }
        }

        collectedAssets.add(wonder.getTradeableAssets());

        return sumAssets(collectedAssets);
    }

    /**
     * get a list of all the tradeable asset choices a player can make
     * @return the list
     */
    public final List<Set<String>> getOptionalAssetsCompleteTradeable(){
        List<Set<String>> collectedAssets = new ArrayList<Set<String>>();

        for(Card card: playedCards){
            if(card.getColor().equals(COLOR_BROWN) || card.getColor().equals(COLOR_GREY)){
                Set<String> options = card.getAssetsOptional(this);
                if(options!=null && !options.isEmpty()){
                    collectedAssets.add(options);
                }
            }
        }

        collectedAssets.addAll(wonder.getOptionalAssetsComplete(this));

        return collectedAssets;
    }

    /**
     * Gets all the assets a player has that a multiplier might be interested in (avoids infinite loop)
     * @return multiplier assets
     */
    public Map<String, Integer> getConcreteAssets() {
        Map<String, Integer> result = new HashMap<String, Integer>();

        for(Card card: playedCards){
            String color = card.getColor();
            result.put(color, getAsset(result, color)+1);
        }

        result.put(ASSET_WONDER_STAGES, wonder.getCurrentStage());

        result.put(ASSET_MILITARY_DEFEAT, getMilitaryDefeats());

        return result;
    }

    /**
     * get the total value of all military wins
     * @return value of all military wins
     */
    public int getMilitaryWins() {
        int wins = 0;
        for(int i : getMilitaryResults()) {
            if(i > 0) {
                wins += i;
            }
        }
        return wins;
    }

    public void registerMilitaryVictory(int age) {
        militaryResults.add(age*2-1);
    }

    public void registerMilitaryDefeat(int age){
        militaryResults.add(-1);
    }

    public int getFinalVictoryPoints() {
        int total = 0;
        Map<String, Integer> assets = getAssets();
        List<Set<String>> optionalAssets = getOptionalAssetsComplete();

        total+=getGold()/3;
        total+=getAsset(assets, ASSET_MILITARY_VICTORY)-getAsset(assets, ASSET_MILITARY_DEFEAT);
        total+=getAsset(assets, ASSET_VICTORY_POINTS);
        total+=getMaximumPotentialSciencePoints(assets, optionalAssets);

        return total;
    }

    private int getMaximumPotentialSciencePoints(Map<String,Integer> assets, List<Set<String>> optionalAssets) {
        int total;

        int science1 = getAsset(assets, ASSET_SCIENCE_1);
        int science2 = getAsset(assets, ASSET_SCIENCE_2);
        int science3 = getAsset(assets, ASSET_SCIENCE_3);

        int numScienceChoices = 0;
        for(Set<String> choices: optionalAssets){
            if(choices.contains(ASSET_SCIENCE_1)){
                numScienceChoices++;
            }
        }

        total = Player.getTotalSciencePoints(science1, science2, science3);

        //totally legit
        for(int i=0; i<=numScienceChoices; i++){
            for(int j=0; j<=numScienceChoices-i; j++){
                for(int k=0; k<=numScienceChoices-j-i; k++){
                   total = Math.max(total, getTotalSciencePoints(science1+i, science2+j, science3+k));
                }
            }
        }

        return total;
    }

    @Override
    public String toString(){
        return "Player "+getPlayerId();
    }
}
