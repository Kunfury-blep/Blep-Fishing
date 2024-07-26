package com.kunfury.blepfishing.items.recipes;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentHornRecipe {

    static NamespacedKey entityKey = new NamespacedKey(BlepFishing.getPlugin(), "blep");


    public static MerchantRecipe Horn(TournamentType type){

        int costAmt = 5; //TODO: Move to generation method

        var recipe =  new MerchantRecipe(
                type.getHorn(), 1, 999, true);


        recipe.addIngredient(new ItemStack(Material.EMERALD, costAmt));

        return  recipe;
    }

    public static void UpdateFishermanTrades(Villager fisherman){
        PersistentDataContainer dataContainer = fisherman.getPersistentDataContainer();

        if(dataContainer.has(entityKey))
            return;

        int profLevel = fisherman.getVillagerLevel();

        if(dataContainer.has(ItemHandler.TourneyTypeId)){
            var tournament = TournamentType.FromId(dataContainer.get(ItemHandler.TourneyTypeId, PersistentDataType.STRING));
            if(tournament == null || profLevel < tournament.HornLevel|| !tournament.VillagerHorn)
                return;

            GiveRecipe(fisherman, dataContainer, tournament);

            return;
        }

        Random rand = new Random();
        var hornList = TournamentType.GetHornTournaments();
        var hornTournament  = hornList.get(rand.nextInt(hornList.size()));

        if(profLevel >= hornTournament.HornLevel){
            GiveRecipe(fisherman, dataContainer, hornTournament);
            return;
        }

        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, hornTournament.Id);


    }

    private static void GiveRecipe(Villager fisherman, PersistentDataContainer dataContainer, TournamentType tournamentType){
        dataContainer.set(entityKey, PersistentDataType.BOOLEAN, true);
        var recipes = fisherman.getRecipes();
        List<MerchantRecipe> newRecipes = new ArrayList<>(recipes);

        newRecipes.add(Horn(tournamentType));

        fisherman.setRecipes(newRecipes);
    }
}