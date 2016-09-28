package dheeraj.lunchsap;

import java.util.ArrayList;

/**
 * Created by I320939 on 23/07/2016.
 */

public class MenuTable {
    Menu menus[];
    public MenuTable()
    {
        menus = new Menu[3];
        menus[0] = new Menu("lunch");
        menus[1] = new Menu("snacks");
        menus[2] = new Menu("Dinner");
    }
}

class Menu
{
    public String name;
    public ArrayList<String> categoryList;
    public ArrayList<ArrayList<String>> menuList;

    public Menu(String name)
    {
        this.name = name;
        categoryList = new ArrayList<>();
        menuList = new ArrayList<>(5);
    }
}