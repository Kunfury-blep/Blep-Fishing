package com.kunfury.blepfishing.ui.scoreboards;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class DisplayFishInfo {

    public static void ShowFish(FishObject fish, Player p){
        if(ConfigHandler.instance.baseConfig.getShowScoreboard())
            ShowScoreboardFish(fish, p);
    }

    private static void ShowScoreboardFish(FishObject fish, Player p){
        Bukkit.getServer().getScheduler().runTaskLater(BlepFishing.getPlugin(), () -> {
            FishType type = fish.getType();

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            final Scoreboard board = Objects.requireNonNull(manager).getNewScoreboard();
            final Objective objective = board.registerNewObjective("test", "dummy", "blep");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatColor.RED + type.Name);

            Rarity rarity = fish.getRarity();
            Score score2 = objective.getScore(Formatting.formatColor(rarity.Prefix + rarity.Name));
            score2.setScore(0);

            Score score = objective.getScore(Formatting.getMessage("Fish Object.Scoreboard.size")
                    .replace("{size}", Formatting.DoubleFormat(fish.Length)));
            score.setScore(1);

//                if(BlepFishing.econEnabled) { //Checks that an economy is installed
//                    Score score1 = objective.getScore(Formatting.getMessage("Fish Object.Scoreboard.value")
//                            .replace("{value}", Formatting.DoubleFormat(fish.RealCost)));
//                    score1.setScore(2);
//                }

//            List<FishObject> caughtFishList = FishObject.GetAllCaughtFish(type.Id);
//            caughtFishList.sort(Collections.reverseOrder());
//
//            Score score3 = objective.getScore(Formatting.getMessage("Fish Object.Scoreboard.rank")
//                    .replace("{rank}", String.valueOf (caughtFishList.indexOf(fish) + 1)));
//            score3.setScore(3);


            p.setScoreboard(board);
            Bukkit.getServer().getScheduler().runTaskLater(BlepFishing.getPlugin(), () -> {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
//                for (String s : board.getEntries()) {
//                    board.resetScores(s);
//                }
            }, 200);
        }, 2);
    }


}
