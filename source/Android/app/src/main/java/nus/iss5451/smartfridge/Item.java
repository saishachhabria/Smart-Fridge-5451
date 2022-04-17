package nus.iss5451.smartfridge;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Item implements Serializable {
    String type;
    String addDate;
    String expiredDate;
    public Item(){
        this.type = "Unknown";
        this.expiredDate = "";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.addDate = ft.format(new Date());
    }
    public Item(Map map){
        this.type = map.get("type") == null?"Unknown":(String) map.get("type");
        this.expiredDate = map.get("expiredDate") == null?"":(String) map.get("expiredDate");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fd = ft.format(new Date());
        this.addDate = map.get("addDate") == null?"":(String) map.get("addDate");
    }
    public Item(String type){
        this.type = type;
        this.expiredDate = "";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.addDate = ft.format(new Date());
    }
    public Item(String type,String addDate){
        this.type = type;
        this.expiredDate = "";
        this.addDate = addDate;
    }
    public Item(String type,String addDate,String expiredDate){
        this.type = type;
        this.expiredDate = expiredDate;
        this.addDate = addDate;
    }

    public String getType(){
        return this.type;
    }

    public String getAddDate(){
        return this.addDate;
    }

    public String getExpiredDate(){
        return this.expiredDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type='" + type + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", addDate='" + addDate + '\'' +
                '}';
    }
}
