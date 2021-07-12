package com;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class ObservableUserStocks {
    private SimpleStringProperty Symbol;
    private SimpleStringProperty CompanyName;
    private SimpleIntegerProperty Price;
    private SimpleIntegerProperty Quantity;

    public ObservableUserStocks(String symbol, String companyName, int price, int quantity){
        this.CompanyName = new SimpleStringProperty(companyName);
        this.Price = new SimpleIntegerProperty(price);
        this.Symbol = new SimpleStringProperty(symbol);
        this.Quantity = new SimpleIntegerProperty(quantity);
    }

    public String getSymbol(){return Symbol.get();}
    public String getCompanyName() { return CompanyName.get(); }
    public int getPrice() { return Price.get(); }
    public int getQuantity() { return Quantity.get(); }
}
