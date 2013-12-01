package co.proxa.durabilitynotify;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class Notify {

    private final static String warning = ChatColor.DARK_RED + "[" + ChatColor.YELLOW + "Warning" + ChatColor.DARK_RED + "]";
    private final static String info = ChatColor.WHITE + "[" + ChatColor.GREEN + "Info" + ChatColor.WHITE + "]";
    private final static String reminder = ChatColor.WHITE + "[" + ChatColor.GREEN + "Reminder" + ChatColor.WHITE + "]";
    private final static DecimalFormat df = new DecimalFormat("#.#");
    private static String lastMessage = "";
    private static Player lastPlayer = null;

    // New way attempt

    public static void sendNotification(Player player, ItemStack item, int usesLeft) {
        createToolWarning(player, item, usesLeft);
    }

    // TODO: Hmm..this is going to suck to rewrite.  I should probably check to see if I need to create a notification
    // TODO: in the actual listener classes, instead of passing it here.

    public static void checkImproperToolForLowDurability(Player player, ItemStack item, int usesLeft) {
        if (Tools.isWoodTool(item) || Tools.isStoneTool(item) || Tools.isGoldTool(item) || Tools.isIronTool(item)
                || Tools.isStringTool(item)) {
            if (usesLeft == 10 || usesLeft == 11 || usesLeft == 2 || usesLeft == 1) {
                createToolWarning(player, item, usesLeft);
            }
        } else if (Tools.isDiamondTool(item)) {
            if (usesLeft == 500 || usesLeft == 501 || usesLeft == 201 || usesLeft == 200 || usesLeft == 51 ||
                    usesLeft == 50 || usesLeft == 2 || usesLeft == 1) {
                createToolWarning(player, item, usesLeft);
            }
        }
    }

    //Only known use for this is fishing rods being cast into mobs.  It deals 3 durability damage
    public static void checkReallyImproperToolForLowDurability(Player player, ItemStack item, int usesLeft) {
        if (Tools.isStringTool(item)) {
            if (usesLeft == 11 || usesLeft == 10 || usesLeft == 9 || usesLeft == 3 || usesLeft == 2 || usesLeft == 1) {
                createToolWarning(player, item, usesLeft);
            }
        }
    }

    public static void createArmorWarning(Player player, ItemStack item, int percentLeft) {
        ChatColor color = Armor.getArmorColor(item);
        String message;
        if (percentLeft > 0) {
            message = warning + ChatColor.YELLOW + " Your " + color + item.getType().name().toLowerCase().replace("_", " ")
                    + ChatColor.YELLOW + getGrammar(item) + ChatColor.GRAY +  df.format(percentLeft) + "%"
                    + ChatColor.YELLOW + " durability remaining";
        } else {
            message = warning + ChatColor.YELLOW + " Your " +
                    color + item.getType().name().toLowerCase().replace("_", " ") + ChatColor.YELLOW +
                    getGrammar(item) + "broken";
        }
        checkAndSendMessage(player, message);
    }

    public static void createArmorReminder(Player player, ItemStack item, int percentLeft) {
        ChatColor color = Armor.getArmorColor(item);
        if (percentLeft > 0) {
            String message = reminder + ChatColor.YELLOW + " Your " + color + item.getType().name().toLowerCase().replace("_", " ")
                    + ChatColor.YELLOW + getGrammar(item) + " less than " + ChatColor.GRAY +  df.format(percentLeft) + "%"
                    + ChatColor.YELLOW + " durability remaining";
            checkAndSendMessage(player, message);
        }
    }

    public static void createToolWarning(Player player, ItemStack item, int usesLeft) {
        ChatColor color = Tools.getToolColor(item);
        String message = warning + ChatColor.YELLOW + " Your " + color +
                item.getType().name().toLowerCase().replace("_", " ") + ChatColor.YELLOW + getGrammar(item) + ChatColor.GRAY
                +  usesLeft + ChatColor.YELLOW + (usesLeft == 1 ? " use" : " uses") + " left";
        checkAndSendMessage(player, message);
    }

    public static void createLiveNotification(Player player, ItemStack item, int usesLeft) {
        ChatColor color = Tools.getToolColor(item);
        String message;
        if (usesLeft > 0) {
            message = info + ChatColor.GREEN + " Your " + color + item.getType().name().toLowerCase().replace("_", " ")
                    + ChatColor.GREEN + getGrammar(item) + ChatColor.WHITE +  usesLeft + ChatColor.GREEN + " uses left";
        } else {
            message = info + ChatColor.GREEN + " Your " + color + item.getType().name().toLowerCase().replace("_", " ")
                    + ChatColor.GREEN + getGrammar(item) + "broken";
        }
        checkAndSendMessage(player, message);
    }

    private static void checkAndSendMessage(Player player, String message) {
        if ((!message.equals(lastMessage) || message.equals(lastMessage) && player != lastPlayer)) {
            player.sendMessage(message);
            lastMessage = message;
            lastPlayer = player;
        }
    }

    private static String getGrammar(ItemStack item) {
        switch (item.getType()) {
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case SHEARS:
                return " have ";
            default:
                return " has ";
        }
    }
}