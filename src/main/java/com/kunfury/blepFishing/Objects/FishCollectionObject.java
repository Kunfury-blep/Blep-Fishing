package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Miscellaneous.Formatting;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class FishCollectionObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 7585599468383877950L;
    String name;
    boolean isCaught;
    int amountCaught = 0;
    public LocalDateTime dateCaught;
    double largest;
    double smallest;


    FishCollectionObject(FishObject fish){
        name = fish.Name;
        Caught(fish);
    }


    FishCollectionObject(String _fishName, boolean _isCaught){
        name = _fishName;
        isCaught = _isCaught;
        dateCaught = LocalDateTime.now();
    }

    public boolean IsCaught() {
        return isCaught;
    }

    public void Caught(FishObject fish){
        isCaught = true;
        amountCaught++;

        if(dateCaught == null) dateCaught = fish.DateCaught;
        if(largest == 0.00 || fish.RealSize > largest) largest = fish.RealSize;
        if(smallest == 0.00 || fish.RealSize < smallest) smallest = fish.RealSize;
    }

    public String getName(){ return name; }

    public String getPage(){
        String pageStr = name;
        pageStr += "\nCaught: " + isCaught;
        if(isCaught){
            pageStr += "\nBiggest: " + Formatting.DoubleFormat(largest);
            pageStr += "\nSmallest: " + Formatting.DoubleFormat(smallest);
            pageStr += "\nCaught: " + dateCaught.toLocalDate();
        }

        return pageStr;
    }
}
