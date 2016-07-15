/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

/**
 *
 * @author Kousa
 */
public class PackageType {
    
    Integer itemType;
    String itemName;
    String itemSize;
    Double itemWeight;
    boolean itemIsFragile;
    
    public PackageType(Integer iType, String n, String size, Double weight, boolean fragile){
        
        itemType = iType;
        itemName = n;
        itemSize = size;
        itemWeight = weight;
        itemIsFragile = fragile;
        
    }
 
    @Override
    public String toString(){
        String info = itemType.toString() + " " + itemName + ": " + itemSize + " " + itemWeight.toString() + " Hajoaa:" + String.valueOf(itemIsFragile);
        return info;
    }

    public Integer getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public Double getItemWeight() {
        return itemWeight;
    }

    public boolean isItemIsFragile() {
        return itemIsFragile;
    }
    
    
}
