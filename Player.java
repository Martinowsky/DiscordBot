package Klayze.Botmeister;

public class Player {
    public long userID;
    public String WoWChar;     // Klayze
    public int gold;

    public Player(){this.gold = 0;};
    public Player(long userID, String WoWChar){
        this.userID = userID;
        this.WoWChar = WoWChar;
        this.gold = 0;
    }
    public Player(long userID, String WoWChar, int gold){
        this.userID = userID;
        this.WoWChar = WoWChar;
        this.gold = gold;
    }
    public Player(Player player){
        this.userID = player.userID;
        this.WoWChar = player.WoWChar;
        this.gold = player.gold;
    }
    public int GetGold(){
        return this.gold;
    }
    public void SetGold(int gold){
        this.gold = gold;
    }
    public void AddGold(int gold){
        this.gold += gold;
    }

    public long GetUserID(){
        return this.userID;
    }
    public void SetUserID(long userID){
        this.userID = userID;
    }

    public String GetWowChar(){
        return this.WoWChar;
    }
    public void SetWowChar(String WowChar){
        this.WoWChar = WowChar;
    }

    public String GetDiscordTag(){
        return "<@"+this.userID+">";
    }
    public static String GetDiscordTag(long userID){
        return "<@"+userID+">";
    }
}
