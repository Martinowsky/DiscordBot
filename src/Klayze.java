package Klayze.Botmeister;
import java.lang.Math;
abstract public class Klayze {
    public static boolean IsAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
    public static String ToName(String name){
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static long DiscordTagToUserID(String tag){
        try {
            return Long.parseLong(tag.replaceAll("[<>@]", ""));
        }
        catch (Exception e){
            return 0l;
        }
    }

    public static int ParseGoldString(String gold){
        int g;
        if(gold.substring(gold.length()-1).equals("k") || gold.substring(gold.length()-1).equals("K")){
            g = Integer.parseInt(gold.substring(0, gold.length() - 1))*1000;
        }
        else {
            g = Integer.parseInt(gold);
        }
        return g;
    }
    public static String RandomWinnerImage(){
        int num = (int) Math.floor(Math.random()*Main.winnerEmoteNumber);
        return "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/winner" + num + ".png";
    }
    public static String RandomLoserImage(){
        int num = (int) Math.floor(Math.random()*Main.loserEmoteNumber);
        return "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/loser" + num + ".png";
    }
    public static String RandomLoserEmoji(){
        int num = (int) Math.floor(Math.random()*Main.loserEmoteNumber);
        try {
            return Main.LoserLink.get(num);
        }
        catch (Exception e){
            return Main.LoserLink.get(0);
        }
    }
    public static String RandomWinnerEmoji(){
        int num = (int) Math.floor(Math.random()*Main.winnerEmoteNumber);
        try {
            return Main.WinnerLink.get(num);
        }
        catch (Exception e){
            return Main.WinnerLink.get(0);
        }
    }
    public static String RandomLoserReact(){
        int num = (int) Math.floor(Math.random()*Main.loserEmoteNumber);
        try {
            return Main.LoserLink.get(num).replaceAll("[<>]", "");
        }
        catch (Exception e){
            return Main.LoserLink.get(0);
        }
    }

    public static String RandomWinnerReact(){
        int num = (int) Math.floor(Math.random()*Main.winnerEmoteNumber);
        try {
            return Main.WinnerLink.get(num).replaceAll("[<>]", "");
        }
        catch (Exception e){
            return Main.WinnerLink.get(0);
        }
    }
}
