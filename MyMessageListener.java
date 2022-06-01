package Klayze.Botmeister;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.time.Instant;
import java.util.*;

public class MyMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        String msg = message.getContentDisplay();
        String[] args = event.getMessage().getContentRaw().split(" ");
        boolean bot = author.isBot();

        if(event.isFromType(ChannelType.TEXT) && !bot) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            //###########################//
            //## Game Master functions ##//
            //###########################//

            if(Main.gameMaster.equals(author.getAsTag())){
                if(msg.equalsIgnoreCase(Main.prefix+"gm info") || msg.equalsIgnoreCase(Main.prefix+"gm man")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Casino Master Commands");
                    eb.setDescription("Possible casino-master commands");

                    eb.addField("`" + Main.prefix + "removePlayer @DiscordTag`", "Kicks the player `@DiscordTag` from " +
                            "the casino regardless of the amount of gold. Thread carefully! " + Main.pepeLULEmoji, false);
                    eb.addField("`" + Main.prefix + "clean`", "Clears the sheet from players with **0** gold.", false);
                    eb.addField("`" + Main.prefix + "add gold @DiscordTag Gold`", "Adds `Gold` gold to the player with `@DiscordTag`. Screenshot " +
                            "of the gold transaction must be provided by the casino master.", false);
                    eb.addField("`"+Main.prefix+"sheet`", "Prints the current casino score sheet.", false);
                    eb.addField("`"+Main.prefix+"add player @DiscordTag Character`", "Registers a new player with the respective `@DiscordTag` and in-" +
                            "game character `Character`. " +
                            "Newly registered player starts with **0** gold.", false);
                    eb.addField("`"+Main.prefix+"player Name`", "Tries to find a player via `Name` being either `@DiscordTag` or in-game `Character` name.", false);
                    eb.setTimestamp(new Date().toInstant());
                    eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
                else if(msg.equalsIgnoreCase(Main.prefix+"clean")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Cleaning");
                    int c = Sheet.CountZeroes();
                    Sheet.CleanSheet();
                    eb.setDescription("Removing all 0 gold players from the sheet... Removed **"+c+"** players.");
                    eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }

                else if(args[0].equalsIgnoreCase(Main.prefix+"removePlayer")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    try {
                        long userID = Long.parseLong(args[1].replaceAll("[<>@]", ""));
                        if (!Sheet.IsInSheet(userID)) {throw new Exception();}
                        eb.setTitle("Kicking player");
                        Sheet.RemovePlayer(userID);
                        eb.setDescription("Kicking the player from the casino. Bye bye! " + Klayze.RandomLoserEmoji());
                        eb.setThumbnail(Main.DeadgePic);
                    }
                    catch(Exception e){
                        eb.setTitle("Kicking player failed.  "+Klayze.RandomLoserEmoji());
                        eb.setDescription("Command: "+"`"+Main.prefix+"kick @DiscordTag`; maybe the player isn't in the casino?");
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
                else if(msg.equals(Main.prefix+"testroll")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();

                }
                else if(args[0].equals(Main.prefix+"player") && args.length > 1){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    try{
                        if(args[1].contains("@")){
                            long userID = Klayze.DiscordTagToUserID(args[1]);
                            Player p = Sheet.FindPlayerViaID(userID);
                            String charName = p.GetWowChar();
                            int gold = p.GetGold();
                            eb.setTitle("Player information");
                            eb.addField("In-game char", charName, true);
                            eb.addField("Gold standing: ", gold+" "+Main.GoldCoin, true);
                            String avatarURL = author.getAvatarUrl();
                            eb.setThumbnail(avatarURL);
                        }
                        else{
                            Player p = Sheet.FindPlayerViaChar(args[1]);
                            int gold = p.GetGold();
                            eb.setTitle("Player information");
                            eb.addField("In-game char", args[1], true);
                            eb.addField("Gold standing", gold+" "+Main.GoldCoin, true);
                            String avatarURL = author.getAvatarUrl();
                            eb.setThumbnail(avatarURL);
                        }
                    }
                    catch (Exception e){
                        eb.setTitle("Query failed  "+Klayze.RandomLoserEmoji());
                        eb.setDescription("Incorrect semantic or player not in the casino.");
                        eb.addField("Command", "`"+Main.prefix+"player @DiscordTag|Character`", false);
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }

                if(args[0].equals(Main.prefix+"add") && args[1].equalsIgnoreCase("gold") && args.length > 2) {
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    try {
                        long userID = Long.parseLong(args[2].replaceAll("[<>@]", ""));
                        Player player = Sheet.FindPlayerViaID(userID);
                        int gold = Klayze.ParseGoldString(args[3]);
                        Sheet.UpdatePlayerGold(player, gold);
                        eb.setTitle("Casino update");
                        if (gold > 0) {
                            eb.setDescription("Adding " + gold + " gold for " + args[2] + ". Have fun! " + Klayze.RandomWinnerEmoji());
                        } else {
                            eb.setDescription("Removing " + -gold + " gold from " + args[2] + ". Get rekt. " + Klayze.RandomLoserEmoji());
                        }
                        eb.addField(player.GetWowChar(), "Available gold: " + Sheet.FindPlayerGold(userID) + " " + Main.GoldCoin, false);
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                    } catch (Exception e) {
                        eb.setTitle("Casino update failed");
                        eb.setDescription("Couldn't add deposit for: " + args[2]);
                        eb.addField("Command:", "`" + Main.prefix + "add gold @DiscordTag GoldAmount`", false);
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
                if(msg.equalsIgnoreCase(Main.prefix+"sheet")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    Sheet.LoadSheet();
                    if(Main.sheet.size() > 0){
                        eb.setTitle("Casino score sheet");
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                        Sheet comp = new Sheet();
                        Collections.sort(Main.sheet, comp);
                        int n = 1;
                        for(Player p : Main.sheet){
                            eb.addField(n+": "+p.GetWowChar(), p.GetGold()+" "+Main.GoldCoin, false);
                            n++;
                        }
                    }
                    else{
                        eb.setTitle("Casino score sheet");
                        eb.setDescription("Casino empty as fuck.");
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }

                if(args[0].equals(Main.prefix+"add") && args[1].equals("player")){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    try {
                        long userID = Long.parseLong(args[2].replaceAll("[<>@]", ""));
                        if(!Klayze.IsAlpha(args[3])){
                            throw new Exception();
                        }
                        if(Sheet.IsInSheet(userID)){
                            eb.setTitle("Error adding player");
                            eb.setDescription("Player already present in the casino.");
                            eb.addField("Character", Sheet.FindCharacter(userID) , true);
                            eb.addField("", Sheet.FindPlayerGold(userID)+" "+Main.GoldCoin , true);
                            eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                        }
                        else{
                            String WoWChar = Klayze.ToName(args[3]);
                            Player player = new Player(userID, WoWChar);
                            Sheet.AddPlayer(player);
                            eb.setTitle("Adding a player");
                            eb.setDescription("Player "+player.GetDiscordTag()+" added. "+ "In-game character: "+player.GetWowChar());
                            eb.addField("Character", Sheet.FindCharacter(userID) , true);
                            eb.addField("", Sheet.FindPlayerGold(userID)+" "+Main.GoldCoin , true);
                            eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                        }
                    }
                    catch (Exception e){
                        eb.setTitle("Error in adding player");
                        eb.setDescription("Command: `!add player @DiscordTag 'Character'`");
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
            }

            //#########################//
            //#### PLAYER COMMANDS ####//
            //#########################//

            if(args[0].equalsIgnoreCase(Main.prefix+"join")){
                channel.sendTyping().queue();
                EmbedBuilder eb = new EmbedBuilder();
                try {
                    long userID = author.getIdLong();
                    if (!Klayze.IsAlpha(args[1]) || args.length > 2) {
                        throw new Exception();
                    }
                    String WoWChar = Klayze.ToName(args[1]);
                    Player player = new Player(userID, WoWChar);

                    if (Sheet.IsInSheet(userID)) {
                        eb.setTitle("Error in adding player");
                        eb.setDescription(player.GetDiscordTag() + " you are already in the casino.");
                        eb.addField("Character", Sheet.FindCharacter(userID), true);
                        eb.addField("Gold: " , Sheet.FindPlayerGold(userID) + " " + Main.GoldCoin, true);
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    }
                    else{
                        Sheet.AddPlayer(player);
                        eb.setTitle("New player added");
                        eb.setDescription("Added "+player.GetDiscordTag()+" to the casino "+Klayze.RandomWinnerEmoji());
                        eb.addField("To roll with other players", "`"+Main.prefix+"roll @DiscordTag 'Amount'`." +
                                " After that the other player has to confirm the challenge by writing `"+Main.prefix+
                                "y`"+" or `"+Main.prefix+"go`. The challenge is valid if I reacted to it.", false);
                        eb.setThumbnail(Klayze.RandomWinnerImage());
                    }
                }
                catch (Exception e){
                        eb.setTitle("Error in adding player");
                        String stringTag = "<@"+author.getIdLong()+">";
                        eb.setDescription(stringTag + " use command `!join 'Character'` to enter the casino. Example: `!join Magpia`." +
                                "Character name must be only letters.");
                        eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                }
                eb.setTimestamp(new Date().toInstant());
                eb.setColor(Main.col);
                eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                channel.sendMessageEmbeds(eb.build()).queue();
            }

            if(args[0].equalsIgnoreCase(Main.prefix+"status")){
                channel.sendTyping().queue();
                EmbedBuilder eb = new EmbedBuilder();
                try {
                    String avatarURL = author.getAvatarUrl();
                    long userID = author.getIdLong();
                    if(!Sheet.IsInSheet(userID)){
                        throw new Exception();
                    }
                    eb.setTitle("Casino status ");
                    eb.setThumbnail(avatarURL);
                    int gold = Sheet.FindPlayerViaID(author.getIdLong()).GetGold();
                    eb.setDescription(Player.GetDiscordTag(userID) + " your current standing is **" + gold + "** " + Main.GoldCoin);
                }
                catch (Exception e) {
                    eb.setTitle("Status error ");
                    eb.setDescription("Can't find the player in the sheet");
                    eb.addField("To enter the casino write:", "`"+Main.prefix+"join 'Character'`. " +
                            "E.g. "+Main.prefix+"join Klayze", false);
                    eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                }
                eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                eb.setTimestamp(new Date().toInstant());
                eb.setColor(Main.col);

                channel.sendMessageEmbeds(eb.build()).queue();
            }

            else if (args[0].equalsIgnoreCase(Main.prefix + "roll") && args.length > 1) {
                boolean GoldInValid = false;
                try {
                    long p1ID = author.getIdLong();
                    long p2ID = Klayze.DiscordTagToUserID(args[1]);
                    if(p1ID == p2ID){
                        channel.sendMessage("You can't roll against yourself. " + Main.pepeLULEmoji).queue();
                        event.getMessage().addReaction(Main.pepeLULReact).queue();
                        throw new Exception();
                    }
                    Player p2 = Sheet.FindPlayerViaID(p2ID);
                    Player p1 = Sheet.FindPlayerViaID(p1ID);
                    int gold = Klayze.ParseGoldString(args[2]);
                    if(gold == 0){
                        throw new Exception();
                    }
                    if(p1.GetGold()-gold < 0 || p2.GetGold()-gold < 0){
                        GoldInValid = true;
                        throw new Exception();
                    }
                    else {
                        Main.lastQueryGold = gold;
                        Main.lastQueryUserID = p1ID;
                        Main.lastQueryTargetUserID = p2ID;
                        event.getMessage().addReaction(Main.Check).queue();
                    }
                }
                catch (Exception e){
                    channel.sendTyping().queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Rolling attempt failed.");
                    if(GoldInValid){
                        long p1ID = author.getIdLong();
                        long p2ID = Klayze.DiscordTagToUserID(args[1]);
                        Player p2 = Sheet.FindPlayerViaID(p2ID);
                        Player p1 = Sheet.FindPlayerViaID(p1ID);
                        eb.addField("Player 1", p1.GetDiscordTag() + " " + p1.GetGold()+" "+Main.GoldCoin, true);
                        eb.addField("Player 2", p2.GetDiscordTag() + " " + p2.GetGold()+" "+Main.GoldCoin, true);
                        eb.addField("", "One player doesn't have enough gold", false);
                    }
                    eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                    eb.setDescription("Gold can be a number (e.g. `5000`) or a " +
                            "literal (e.g. `5k`). Make sure both players are registered in the casino and have enough gold. " +
                            "You can check your gold status by writing: "+'`'+Main.prefix+"status`"+" in the chat.");
                    eb.addField("Command", "`"+Main.prefix+"roll @DiscordTag Gold`", false);
                    eb.setTimestamp(new Date().toInstant());
                    eb.setColor(Main.col);
                    eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                    eb.setThumbnail(Klayze.RandomLoserImage());
                    channel.sendMessageEmbeds(eb.build()).queue();
                }
            }
            //#################################################################//
            //### 2nd PLAYER NEEDS TO REPLY WITH !y or !go or !roll or !yes ###//
            //#################################################################//

            else if(msg.equalsIgnoreCase(Main.prefix+"y") || msg.equalsIgnoreCase(Main.prefix+"go") ||
                    msg.equalsIgnoreCase(Main.prefix+"roll") || msg.equalsIgnoreCase(Main.prefix+"yes")){

                long p2ID = author.getIdLong();
                try {
                    if(Main.lastQueryTargetUserID != p2ID){
                        channel.sendMessage("<@"+p2ID+">"+ " you are not partaking in current roll.").queue();
                        channel.sendMessage("<@"+Main.lastQueryTargetUserID+">"+ " needs to partake in the roll.").queue();
                        event.getMessage().addReaction(Main.pepeLULReact).queue();
                    }
                    else{ //game can start!
                        channel.sendTyping().queue();
                        EmbedBuilder eb = new EmbedBuilder();
                        Main.gameNumber++;
                        eb.setTitle("Game #"+Main.gameNumber);
                        eb.setDescription("Rolling game, winner takes all");
                        try {
                            long p1ID = Main.lastQueryUserID;
                            Player p1 = Sheet.FindPlayerViaID(p1ID);
                            Player p2 = Sheet.FindPlayerViaID(p2ID);
                            int gold = Main.lastQueryGold;
                            int roll1 = (int) (Math.random() * 100);
                            int roll2 = (int) (Math.random() * 100);
                            if (roll1 > roll2) {
                                Sheet.UpdatePlayerGold(p1, gold);
                                Sheet.UpdatePlayerGold(p2, -gold);
                                eb.addField(Klayze.RandomWinnerEmoji()+ " Winner: " + Integer.toString(roll1), p1.GetDiscordTag() , false);
                                eb.addField(Klayze.RandomLoserEmoji() + " Loser: " + Integer.toString(roll2), p2.GetDiscordTag(), false);
                                eb.addField("Winner status ", Sheet.FindPlayerGold(p1ID)+" "+Main.GoldCoin, false);
                                eb.addField("Loser status ", Sheet.FindPlayerGold(p2ID)+" "+Main.GoldCoin, false);
                            }
                            else if (roll1 < roll2) {
                                Sheet.UpdatePlayerGold(p1, -gold);
                                Sheet.UpdatePlayerGold(p2, gold);
                                eb.addField(Klayze.RandomLoserEmoji() + " Loser: " + Integer.toString(roll1), p1.GetDiscordTag() , false);
                                eb.addField(Klayze.RandomWinnerEmoji()+ " Winner: " + Integer.toString(roll2), p2.GetDiscordTag(), false);
                                eb.addField("Loser status ", Sheet.FindPlayerGold(p1ID)+" "+Main.GoldCoin, false);
                                eb.addField("Winner status ", Sheet.FindPlayerGold(p2ID)+" "+Main.GoldCoin, false);
                            }
                            else { //even roll
                                eb.addField(Main.CheckEmoji + " Draw " + p1.GetDiscordTag(), Integer.toString(roll1), false);
                                eb.addField(Main.CheckEmoji + " Draw " + p1.GetDiscordTag(), Integer.toString(roll2), false);
                            }
                            eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");
                        }
                        catch (Exception e){
                            eb.addField(Klayze.RandomLoserEmoji() + " I'm broken ", "Try me again.", false);
                            eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser1.png");
                        }
                        eb.setTimestamp(new Date().toInstant());
                        eb.setColor(Main.col);
                        eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                        channel.sendMessageEmbeds(eb.build()).queue();
                    }
                }
                catch (NullPointerException e){
                    channel.sendMessage("No pending query to confirm. " + Klayze.RandomLoserEmoji()).queue();
                    e.printStackTrace();
                }
            }

            else if (args[0].equalsIgnoreCase(Main.prefix + "manual") || args[0].equalsIgnoreCase(Main.prefix + "info")
            || args[0].equalsIgnoreCase(Main.prefix + "man")) {
                channel.sendTyping().queue();
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Gambling Commands");

                eb.setDescription("Possible commands:");
                eb.addField("`"+Main.prefix+"join Character`", "Join the casino with in-game character `Character`, and linking it to your discord account. " +
                        "Example: `!join Magpia`", false);
                eb.addField("To challenge a player: "+"`" + Main.prefix + "roll @Player Gold`", "Winner of the roll wins `Gold` amount of gold." +
                        " Tagged player must accept the challenge by writing `!y` or `!go` or `!roll` in the chat, before rolling can proceed.", false);
                eb.addField("`"+Main.prefix+"status`", "Checks your gold standing in the casino. " + Klayze.RandomWinnerEmoji(), false);
                eb.addField("Leaving the casino is only possible if your current gold is at 0.", "", false);

                eb.addField(Main.GoldCoin+" To deposit gold "+Main.GoldCoin, "Send in-game mail (with discord name) to Boozemeister (Icecrown), after that the" +
                        " game master "+"**"+Main.gameMaster+"** will update your standing.", false);

                eb.setTimestamp(new Date().toInstant());
                eb.setThumbnail("https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/Pepe-Business.png");

                eb.setTimestamp(new Date().toInstant());
                eb.setColor(Main.col);
                eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                channel.sendMessageEmbeds(eb.build()).queue();
            }

            //## SEND IMAGE OF A HOT GIRL ##//
            else if (msg.equalsIgnoreCase(Main.prefix + "hotgirl") || msg.equalsIgnoreCase(Main.prefix + "hotchick")
                    || msg.equalsIgnoreCase(Main.prefix + "girl")) {
                channel.sendTyping().queue();
                EmbedBuilder eb = new EmbedBuilder();

                eb.setColor(Main.col);
                String[] piclist = new String[]{
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e95f80f17110f9ee.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e8e5ef516541c86e.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hot-girl-picture-5e910b1b1863fb6f8.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hump-girl-5e978a664238a8d60.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e99c39137c6d8cfa.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e9a40176360f4751.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e8e5e312b5a3e5e5.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e95f536f15c7f37d.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/lingerie-girl-5e949d0268d406d5a.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hump-girl-5e978a73f2b5c0f1c.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e99c35fce3ddb4ad.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e99c3262589ebff8.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hump-girl-5e978ab858ae0dba4.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e8e5e31145cf6145.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hump-girl-5e978ae8ebfcba838.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e9377fc22351e390.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e93781763fff46a5.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/dank-meme-5e975d8132fd4ab0b.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/busty-girl-5e9117396e6c86662.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e95f855073c7d466.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e95f5af29964ec49.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e9a404616625e138.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/dank-meme-5e975d510670d557d.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/food-porn-5e98c118833bb8874.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e99c3ba3bf9f2eae.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e8e5e30b58de096e.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e9499bb02acb779d.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2016/04/awesome-bioshock-infinite-cosplay.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/bikini-girl-5e9907201c33721aa.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/girl-tug-5e95da3f7434dc9b3.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2016/05/zero-gravity-funny-20160503.gif",
                        "https://breakbrunch.com/wp-content/uploads/2019/06/busty-girl-061019-18.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/sexy-asian-girl-1230181032-32.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-selfie-5e9e42f1064bcdea9.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-girl-5e95f6cf82c116844.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/01/cute-brunettes-1020191121-5.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/10/sexy-beautiful-girl-201419-12.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e8618a67bb9cf30d.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/12/beautiful-girl-1228181042-3.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2016/10/you-had-one-job-123115-11.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76282e019e15201116.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e777c2d01df15205616.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77b70001e51520db16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77890d017715202b16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e7764a201cc15209a16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e75ec4101021520e716.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77396901981520f116.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76d54c011c15209a16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e771fe4013015209516.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e766822013c15205116.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76a48b01d41520c016.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e776276014515201916.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e7799af011515206716.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76ef6601531520f816.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e75dedf013c15201f16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76a6b7012215201916.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76b590019a15202e16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77d679019315203d16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e75e4ad01441520f916.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77c5b801e015200016.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e77285b019d15209316.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/225e1fd6e76aae901421520ac16.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/overlord-albedo-cosplay-aqua-5ea5069ddd19ec4d4.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/hump-girl-5ea0c3e6303b91aeb.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2018/12/girl-with-dark-hair-0911181003-22.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/10/sexy-beautiful-girl-201419-12.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/09/sexy-girl-with-cute-puppies-090319-12.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/05/hot-girl-5e9e3b8749fcfb972.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e891b4050c92f951.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2018/09/cute-and-hot-girl-061418-34.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/10/sexy-girl-in-white-tshirt-102419-14.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e8113ae2fdbdcd56.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/03/sexy-girl-in-lingerie-0103190934-30.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e8d1950de092ab9c.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ee138aad1e98.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f1207be745d2.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ecc92a3d5c43.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f3520b2f35dc.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f5e3f58b4835.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f3bcd6cf5240.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f578e1622362.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3eb7bf708a541.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ed38d595a5eb.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f276f09aea99.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3eeeb76b0530e.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3eda6bf5689c4.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f1f80565efaf.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f189a23026ad.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f2e34135d2ce.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ef583abf1737.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f509cc55056b.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3efc85340d274.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f040756fdd6b.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ee7b6b29d8f2.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f0aed63acd84.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ec5a7864551a.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f49a761ecd03.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3ebe8302dccd8.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/shantal-monique-5e9e3f427a14c0c99.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2017/01/cute-bunny-riven-cosplay-lol.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2018/12/girl-with-dark-hair-0911181003-22.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/01/sexy-asian-girl-120319-96.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e9a40c42ea94dfbb.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/12/beautiful-girl-in-tight-dress-122119-36.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/11/sexy-underboob-girl-112119-19.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-asian-girl-5e9cd49674b818cbd.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-bikini-girl-5e910f25ac4c4f4b6.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/5e8618a83c3d24115.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/09/girl-in-bath-towels-090319-19.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2020/04/sexy-london-shay-picture-5e9f6a9b9d6d5c417.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/11/prety-girl-110519-20.jpg",
                        "https://breakbrunch.com/wp-content/uploads/2019/03/sexy-brunette-0130191102-5.jpg",
                };
                int num = (int) Math.floor(Math.random() * piclist.length);
                String picUrl;
                try {
                    picUrl = piclist[num];
                } catch (Exception e) {
                    num = (num < 0) ? 0 : num - 1;
                    picUrl = piclist[num];
                }
                eb.setImage(picUrl);
                eb.setTimestamp(new Date().toInstant());
                channel.sendMessageEmbeds(eb.build()).queue();
            }
            else if (args[0].equals(Main.prefix+"echo")) {
                System.out.println(args[1]);
                channel.sendMessage("Echo test: " + args[1]).queue();
            }
            else if (args[0].equals(Main.prefix+"testName")) {
                if(args[1].charAt(0) == '<'){
                    channel.sendMessage("Echo test: " + args[1]).queue();
                    channel.sendMessage("Think you send the @Mention").queue();
                    try {
                        long L = Long.parseLong(args[1].replace("@", "").replace("<", "").replace(">", ""));
                        channel.sendMessage("Your discord tag is: " + Main.RetrieveUserNameById(L)).queue();
                    }
                    catch (Exception e){
                        channel.sendMessage("Couldn't work out the long tag").queue();
                    }
                }
            }
            else if(args[0].equals(Main.prefix+"guy")){
                channel.sendTyping().queue();
                channel.sendMessage("Stop being gay, my friend. " + Main.pepeLULEmoji).queue();
            }

            else if(args[0].equals(Main.prefix+"armory")){
                channel.sendTyping().queue();
                channel.sendMessage("I'm not your fucking armory bot ... " + Main.pepeLULEmoji).queue();
                event.getMessage().addReaction(Klayze.RandomLoserReact()).queue();
            }

            else if(args[0].equals(Main.prefix+"tits")){
                channel.sendTyping().queue();
                channel.sendMessage("I'm not your fucking porn bot ... " + Klayze.RandomLoserEmoji()+" . Go fap to pornhub or something.").queue();
                event.getMessage().addReaction(Klayze.RandomLoserReact()).queue();
            }
            else if(args[0].equals(Main.prefix+"porn")){
                channel.sendTyping().queue();
                channel.sendMessage("I'm not your fucking porn bot ... " + Klayze.RandomLoserEmoji()+" . Go fap to pornhub or something.").queue();
                event.getMessage().addReaction(Klayze.RandomLoserReact()).queue();
            }
            else if(args[0].equals(Main.prefix+"whoami")){
                channel.sendTyping().queue();
                EmbedBuilder eb = new EmbedBuilder();
                String[] replylist = new String[]{
                        "Piece of shit",
                        "Retarded as fuck",
                        "Addicted to gambling",
                        "Dog",
                        "Loves to receive cock",
                        "Giga chad based and blackpilled",
                        "300 IQ genius god",
                        "Fucks cunts like Mel-fucking-Gibson",
                        "God of homo sapiens"
                };
                int num = (int) Math.floor(Math.random() * replylist.length);
                int emojiNum = (int)  Math.floor(Math.random() * 5);
                String str;
                try {
                    str = replylist[num];
                }
                catch (Exception e){
                    num = (num < 0) ? 0 : num - 1;
                    str = replylist[num];
                }
                eb.setTitle(str);
                eb.setDescription("Thats what you are "+ "<@"+author.getIdLong()+">");
                if(num<=4) {
                    eb.setThumbnail(Klayze.RandomLoserImage());
                }
                else{
                    eb.setThumbnail(Klayze.RandomWinnerImage());
                }
                eb.setTimestamp(new Date().toInstant());
                eb.setColor(Main.col);
                eb.setFooter("Botting is life.", "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner4.png");
                channel.sendMessageEmbeds(eb.build()).queue();
            }
        }
    }
}
