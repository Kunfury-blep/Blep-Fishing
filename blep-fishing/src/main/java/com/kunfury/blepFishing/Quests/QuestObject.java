package com.kunfury.blepFishing.Quests;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.CollectionLog.CollectionHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuestObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 5759815909931820588L;
    private final String name;
    private final int catchAmount;
    private final String fishTypeName;
    private final double maxSize;
    private final double minSize;



    private final double duration; //Hours
    private LocalDateTime startDate;
    private LocalDateTime lastRan;
    private LocalDateTime endDate;
    private final double cooldown;
    private final boolean announceProgress;

    private boolean completed;

    private HashMap<String, Integer> catchMap;
    private List<String> winners;
    public final List<String> rewards; //The rewards given to players. The integer is place and the List is serialized items

    public QuestObject(String name, int catchAmount, String fishTypeName, double maxSize, double minSize, double duration, double cooldown, List<String> rewards,
                        boolean announceProgress){
        this.name = name;

        if(catchAmount <= 0){
            catchAmount = 1;
        }
        this.catchAmount = catchAmount;

        this.fishTypeName = fishTypeName;
        this.minSize = minSize;
        this.maxSize = maxSize;

        if(duration <= 0){
            duration = 24;
        }
        this.duration = duration;
        this.cooldown = cooldown;
        this.rewards = rewards;
        this.announceProgress = announceProgress;
    }

    public String getName(){
        return name;
    }

    public int getCatchAmount(){
        return catchAmount;
    }

    public HashMap<String, Integer> getCatchMap(){
        return catchMap;
    }

    public LocalDateTime getLastRan(){
        if(lastRan == null){
            lastRan = LocalDateTime.MIN;
        }

        return lastRan;
    }

    public void setLastRan(LocalDateTime date){
        lastRan = date;
    }

    public boolean isValid(FishObject fish){

        if(isCompleted() || !fishTypeName.equalsIgnoreCase("ALL") && !fishTypeName.equalsIgnoreCase("ANY") && !fish.Name.equalsIgnoreCase(fishTypeName)){
            return false;
        }

        boolean maxBool = (maxSize == 0 || fish.getSize() <= maxSize);
        boolean minBool = (minSize == 0 || fish.getSize() >= minSize);

        return maxBool && minBool;
    }

    public void Start(){
        startDate = LocalDateTime.now();
        winners = new ArrayList<>();
        catchMap = new HashMap<>();
        completed = false;
        setEndDate();

        if(BlepFishing.configBase.getAnnounceQuests()){
            for(var p : Bukkit.getOnlinePlayers()){
                p.sendMessage(Formatting.formatColor(
                        Formatting.getMessage("Quests.progress")
                                .replace("{quest}", getName())
                                .replace("{caught}", String.valueOf(0))
                                .replace("{required}", String.valueOf(getCatchAmount()))));
            }
        }
    }

    public boolean canStart(){
        return getDowntime() >= cooldown;
    }

    public double getDowntime(){
        return ChronoUnit.MINUTES.between(getLastRan(), LocalDateTime.now()) / 60.0;
    }

    public void Finish(){
        setLastRan(LocalDateTime.now());
        completed = true;


        //
        //QuestHandler.ActiveQuests.remove(this);
    }

    public void AddFish(FishObject fish, Player p){
        String uuid = p.getUniqueId().toString();

        if(!catchMap.containsKey(uuid)){
            catchMap.put(uuid, 1);
        }else{
            int caughtAmt = catchMap.get(uuid) + 1;
            catchMap.put(uuid, caughtAmt);
        }

        int catchAmount = catchMap.get(uuid);

        if(announceProgress && catchAmount <= getCatchAmount()){

            p.sendMessage(Formatting.formatColor(
                    Formatting.getMessage("Quests.progress")
                            .replace("{quest}", getName())
                            .replace("{caught}", String.valueOf(catchAmount))
                            .replace("{required}", String.valueOf(getCatchAmount()))));
        }

        if(catchMap.get(uuid) >= getCatchAmount() && !winners.contains(uuid)){
            winners.add(uuid);
            p.sendMessage(Formatting.formatColor(Formatting.getFormattedMesage("Quests.completed")
                            .replace("{quest}", getName())
            ));

            new CollectionHandler().CompletedQuest(p, this);

            GiveRewards(uuid);
        }
    }

    private void GiveRewards(String uuid){
        for(var r : rewards){
            ItemHandler.parseReward(r, UUID.fromString(uuid));
        }
    }

    public String getFishTypeName(){
        String name = fishTypeName;

        if(fishTypeName.equalsIgnoreCase("ALL") || fishTypeName.equalsIgnoreCase("ANY")){
            name = "Fish";
        }

        return name;
    }

    public ItemStack getItemStack(boolean admin, String uuid){
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.formatColor(getName()));

        ArrayList<String> lore = new ArrayList<>();

        String fishName;


        if(getFishTypeName().equalsIgnoreCase("ALL"))
            fishName = Formatting.getMessage("Tournament.allFish");
        else{
            BaseFishObject base = BaseFishObject.getBase(getFishTypeName());
            if(base != null)
                m.setCustomModelData(base.ModelData);

            fishName = getFishTypeName();
        }

        int caughtAmt = 0;

        if(getCatchMap().containsKey(uuid)){
            caughtAmt = getCatchMap().get(uuid);
        }

        String desc = Formatting.getMessage("Quests.description")
                .replace("{caught}", String.valueOf(caughtAmt))
                .replace("{required}", String.valueOf(getCatchAmount()))
                .replace("{fish}", fishName);

        lore.add(desc);

        if(maxSize > 0){
            lore.add(Formatting.getMessage("Quests.maxDesc")
                    .replace("{size}", Formatting.DoubleFormat(maxSize)));
        }

        if(minSize > 0){
            lore.add(Formatting.getMessage("Quests.minDesc")
                    .replace("{size}", Formatting.DoubleFormat(minSize)));
        }

        lore.add("");

        if(!isCompleted()){
            lore.add(ChatColor.WHITE + Formatting.asTime(getTimeRemaining()));
            if(admin){
                lore.add("");
                lore.add(Formatting.getMessage("Quests.cancel"));
            }
        }else{
            lore.add(Formatting.getMessage("Quests.expired"));
        }


        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    public boolean isCompleted(){
        return completed;
    }

    public boolean canFinish(){
        return getTimeRemaining() <= 0;
    }

    public Long getTimeRemaining(){
        LocalDateTime now = LocalDateTime.now();

        if(endDate == null) setEndDate();

        return ChronoUnit.MILLIS.between(now, endDate);
    }

    private void setEndDate(){
        long seconds = (long) (duration * 60 * 60);

        endDate = startDate.plusSeconds(seconds);
    }

    public double getDuration() {
        return duration;
    }

    public int getModelData(){
        BaseFishObject base = BaseFishObject.getBase(getFishTypeName());
        if(base != null)
            return base.ModelData;
        return 0;
    }

}
