package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminTournamentGradingBtn extends AdminTournamentMenuButton {

    public AdminTournamentGradingBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.BIRCH_SIGN;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Grading");
        ArrayList<String> lore = new ArrayList<>();

        String title = "";
        String desc = "";

        switch(tournament.Grading){
            case LONGEST -> {
                title = Formatting.GetLanguageString("Tournament.Grading.Longest.title");
                desc = Formatting.GetLanguageString("Tournament.Grading.Longest.desc");
            }
            case SHORTEST -> {
                title = Formatting.GetLanguageString("Tournament.Grading.Shortest.title");
                desc = Formatting.GetLanguageString("Tournament.Grading.Shortest.desc");
            }
            case SCORE_HIGH -> {
                title = Formatting.GetLanguageString("Tournament.Grading.ScoreHigh.title");
                desc = Formatting.GetLanguageString("Tournament.Grading.ScoreHigh.desc");
            }
            case SCORE_LOW -> {
                title = Formatting.GetLanguageString("Tournament.Grading.ScoreLow.title");
                desc = Formatting.GetLanguageString("Tournament.Grading.ScoreLow.desc");
            }
        }

        lore.add(ChatColor.BLUE + title);
        lore.addAll(Formatting.ToLoreList(desc));


        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        TournamentType tournamentType = getTournamentType();

        int gradingIndex = Arrays.stream(TournamentType.GradingType.values()).toList().indexOf(tournamentType.Grading);

        if(gradingIndex >= TournamentType.GradingType.values().length - 1)
            tournamentType.Grading = TournamentType.GradingType.values()[0];
        else
            tournamentType.Grading = TournamentType.GradingType.values()[gradingIndex + 1];

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditPanel(tournamentType).Show(player);
    }


}
