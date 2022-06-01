package Klayze.Botmeister;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Sheet implements Comparator<Player> {
    public static boolean IsInSheet(long userID){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(l == userID){
                    return true;
                }
            }
            file.close();
            return false;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.IsInSheet(long userID); can't load.");
            return false;
        }
    }

    public static String FindCharacter(long userID){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(l == userID){
                    return name;
                }
            }
            file.close();
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.FindWoWChar(long userID) error; can't load.");
            return null;
        }
    }

    public static int FindPlayerGold(long userID){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(l == userID){
                    return gold;
                }
            }
            file.close();
            return 0;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.FindPlayerGold(long userID) error; can't load.");
            return 0;
        }
    }
    public static int CountZeroes(){
        int c = 0;
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(gold == 0){
                    c++;
                }
            }
            file.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.CountZeroes() error; can't load.");
        }
        return c;
    }
    public static void CleanSheet(){ //cleans zeroes from the sheet
        LoadSheet();
        Main.sheet.removeIf(p -> (p.GetGold() == 0));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            for (Player p : Main.sheet) {
                String s = p.GetUserID() + " " + p.GetWowChar() + " " + p.GetGold() + "\n";
                writer.write(s);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sheet.CleanSheet() error; can't clean properly.");
        }
    }
    public static Player FindPlayerViaID(long userID){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(l == userID){
                    return new Player(userID, name, gold);
                }
            }
            file.close();
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.FindPlayerGold(long userID) error; can't load.");
            return null;
        }
    }

    public static Player FindPlayerViaChar(String WoWChar){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                if(name.equals(WoWChar)){
                    return new Player(l, name, gold);
                }
            }
            file.close();
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.FindPlayerGold(long userID) error; can't load.");
            return null;
        }
    }
    public static void LoadSheet(){
        try {
            Scanner file = new Scanner(new File("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            Main.sheet = new ArrayList<Player>();
            while (file.hasNext()) {
                long l = file.nextLong();
                String name = file.next();
                int gold = file.nextInt();
                Player player = new Player(l, name, gold);
                Main.sheet.add(player);
            }
            file.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Sheet.LoadSheet() error; can't load.");
        }
    }

    public static void AddPlayer(Player p){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt", true));
            String s = p.GetUserID()+" "+p.GetWowChar()+" "+p.GetGold()+"\n";
            writer.append(s);
            Main.sheet.add(p);
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Sheet.AddNewPlayer() error; can't add.");
        }
    }

    public static void UpdatePlayerGold(Player player, int gold){
        LoadSheet();
        Main.sheet.removeIf(p -> (p.GetUserID() == player.GetUserID()));
        Player P = new Player(player);
        P.AddGold(gold);
        Main.sheet.add(P);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            for (Player p : Main.sheet) {
                String s = p.GetUserID() + " " + p.GetWowChar() + " " + p.GetGold() + "\n";
                writer.write(s);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sheet.RemovePlayer(long userID) error; can't clean properly.");
        }
    }

    public static void RemovePlayer(long userID){
        LoadSheet();
        Main.sheet.removeIf(p -> (p.GetUserID() == userID));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            for (Player p : Main.sheet) {
                String s = p.GetUserID() + " " + p.GetWowChar() + " " + p.GetGold() + "\n";
                writer.write(s);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sheet.RemovePlayer(long userID) error; can't clean properly.");
        }
    }

    public static void RemovePlayer(String character){
        LoadSheet();
        Main.sheet.removeIf(p -> (p.GetWowChar().equals(character)));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java-Projects\\Boozemeister\\src\\Klayze\\Botmeister\\Casino-spreadsheet.txt"));
            for (Player p : Main.sheet) {
                String s = p.GetUserID() + " " + p.GetWowChar() + " " + p.GetGold() + "\n";
                writer.write(s);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sheet.RemovePlayer(String character) error; can't clean properly.");
        }
    }

    @Override
    public int compare(Player p1, Player p2){
        if(p1.GetGold() != p2.GetGold()){
            return p2.GetGold() - p1.GetGold();
        }
        else{
            return p1.GetWowChar().compareTo(p2.GetWowChar());
        }
    }
}
