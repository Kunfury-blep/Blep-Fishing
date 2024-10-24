package com.kunfury.blepfishing.objects.treasure;

import java.time.LocalDateTime;

public class TreasureDrop {

    public final String TreasureId;
    public final String PlayerId;
    public final LocalDateTime DateCaught;

    public TreasureDrop(String treasureId, String playerId, LocalDateTime dateCaught){
        TreasureId = treasureId;
        PlayerId = playerId;
        DateCaught = dateCaught;
    }

}
