package com;

import com.sun.javaws.Main;
import generated.RseItem;
import generated.RseUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class User {
    private String Name;
    private Map<String,Integer> UserStocks;
    private ObservableList<String> observableUserStocks;

    public User(String name, Map<String,Integer> stocks, ObservableList<String> observ){
        this.Name = name;
        this.UserStocks = stocks;
        this.observableUserStocks = observ;
    }

    public User(RseUser r){
        this.observableUserStocks = FXCollections.observableArrayList();
        this.Name = r.getName();
        this.UserStocks = new HashMap<>();
        for (RseItem i: r.getRseHoldings().getRseItem()) {
            this.UserStocks.put(i.getSymbol().toUpperCase(),i.getQuantity());
            this.observableUserStocks.add(i.getSymbol().toUpperCase());
        }
    }

    public String getName(){return this.Name;}
    public Map<String,Integer> getUserStocks(){return this.UserStocks;}
    public ObservableList<String> getObservableUserStocks(){return this.observableUserStocks;}
    public void RemoveUserObservableStock(String symbol){observableUserStocks.remove(symbol);}
    public void AddUserObservableStock(String symbol){observableUserStocks.add(symbol);}



}
