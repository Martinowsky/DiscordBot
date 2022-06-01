package Klayze.Botmeister;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static Map<Integer, String> LoserLink =  new HashMap<>(){{
        put(0, "<:loser0:978756289846988901>");
        put(1, "<:loser1:978756335606833212>");
        put(2, "<:loser2:978756348042965062>");
        put(3, "<:loser3:978756367391285259>");
        put(4, "<:loser4:978756379311480896>");
    }};
    public static Map<Integer, String> WinnerLink = new HashMap<>() {{
        put(0, "<:winner0:978756734048948264>");
        put(1, "<:winner1:978756276949516318>");
        put(2, "<:winner2:978756265058652242>");
        put(3, "<:winner3:978756252899373096>");
        put(4, "<:winner4:978756311586066462>");
    }};
    public static final String Check = ":thumbsUp:978767354697764864";
    public static final String CheckEmoji = "<:thumbsUp:978767354697764864>";
    public static final String GoldCoin = "\uD83E\uDE99";
    public static final String DeadgePic = "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/deadge.png";

    public static final String pepeLULEmoji = "<:pepeLUL:750620190030561290>";
    public static final String pepeLULReact = ":pepeLUL:750620190030561290";
    public static final String pepeLULImage = "https://raw.githubusercontent.com/Martinowsky/DiscordBot/main/pepelul.png";
    public static final int loserEmoteNumber = 5; //0,1,2...
    public static final int winnerEmoteNumber = 5;
    public static JDA bot;
    public static final String prefix = "!";
    public static final String gameMaster = "Klayze#3232";

    public static int lastQueryGold = 0;
    public static long lastQueryUserID = 0;
    public static long lastQueryTargetUserID = 0;
    public static int gameNumber = 0;
    public static List<Player> sheet = new ArrayList<Player>();

    public static final Color col = new Color(233, 255, 186);
    public static String token;
    public static void main(String[] args) {
        try {
            token = new BufferedReader(new FileReader("token.txt")).readLine();
            bot = (JDA) JDABuilder.createDefault(token)
                    .setActivity(Activity.playing("Chad Roulette"))
                    .build();
            bot.getPresence().setStatus(OnlineStatus.ONLINE);
            bot.addEventListener(new MyMessageListener());
            bot.awaitReady();
        }
        catch(LoginException | InterruptedException e){
            e.printStackTrace();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
            System.out.println("Can't find token");
        }
    }

    public static String RetrieveUserNameById(long userId){
        try{
            User user = bot.retrieveUserById(userId).complete();
            return user.getAsTag();
        }
        catch(Exception e){
            return null;
        }
    }
}
