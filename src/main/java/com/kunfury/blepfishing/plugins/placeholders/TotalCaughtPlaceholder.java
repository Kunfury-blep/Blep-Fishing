package com.kunfury.blepfishing.plugins.placeholders;

import com.kunfury.blepfishing.database.Database;

public class TotalCaughtPlaceholder extends Placeholder{
    @Override
    public String getName() {
        return "TOTAL_CAUGHT";
    }

    @Override
    public String getValue(String[] args) {
        String pUuid = null;
        if(args.length >= 2)
            pUuid = args[1];

        return String.format("%,d", Database.Fish.GetTotalCatchAmount(pUuid));
    }

    @Override
    public String getDefault() {
        return "";
    }
}
