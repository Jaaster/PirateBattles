package me.jaaster.plugin.game.classes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plado on 8/25/2016.
 */
public enum SpecialClasses {
    CAPTAIN{
        public ArrayList<String> getItemLore() {
          ArrayList<String> l = new ArrayList<>();
            l.add("Captian is one of the most powerful");
            l.add("classes. He is the only class with a gun");
            l.add("which is his prized possession, he also has a bat");
            l.add("that stays near its trusted master.");
            return l;
        }

        public Material getMaterial(){
            return Material.GOLD_INGOT;
        }
    }, FIRST_MATE{
        public ArrayList<String> getItemLore() {
            ArrayList<String> l = new ArrayList<>();
            l.add("First_Mate is second in command.");
            l.add("He is stronger than most of the other players");
            l.add("doing an extra heart of damage!");

            return l;

        }
        public Material getMaterial(){
            return Material.IRON_SWORD;
        }

    }, BOATS_SWAIN{
        public ArrayList<String> getItemLore() {
            ArrayList<String> l = new ArrayList<>();
            l.add("Boats_Swain is the boat mechanic,");
            l.add("Being the only one able to repair");
            l.add("broken canons!");

            return l;

        }
        public Material getMaterial(){
            return Material.WOOD_HOE;
        }
    }, SURGEON{
        public ArrayList<String> getItemLore() {
            ArrayList<String> l = new ArrayList<>();
            l.add("Surgeon is the boat medic,");
            l.add("Being the only one able to heal other players");
            l.add("by tapping them with his Cloth");
            return l;

        }
        public Material getMaterial(){
            return Material.PAPER;
        }
    }, STRIKER{
        public ArrayList<String> getItemLore() {
            ArrayList<String> l = new ArrayList<>();
            l.add("As a Striker increase your FHR(fire to hit ratio)");
            l.add("in order to do more damage!");
            l.add("Your FHR is displayed as your XP LEVEL.");
            return l;

        }
        public Material getMaterial(){
            return Material.BOW;
        }

    }, POWDER_MONKEY{
        public ArrayList<String> getItemLore() {
            ArrayList<String> l = new ArrayList<>();
            l.add("Powder Monkey is definitely the black");
            l.add("sheep of the team, with no special abilities :(");
            return l;
        }

        public Material getMaterial(){
            return Material.DIRT;
        }
    };


    public abstract ArrayList<String> getItemLore();

    public abstract Material getMaterial();

}
