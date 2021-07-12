package com;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Transaction {
    private String Date;
    private int NumberOfStocks;
    private int Cost;
    private String Symbol;
    private String Action;
    private String Buyer;
    private String Seller;
    private String CompanyName;
    private int Cycle;

    public Transaction(String date, int numberofstocks, int cost, String symbol, String action,String buyer, String seller, String companyName) {
        this.Date = date;
        this.NumberOfStocks = numberofstocks;
        this.Cost = cost;
        this.Symbol = symbol.toUpperCase();
        this.Action = action;
        this.Buyer = buyer;
        this.Seller = seller;
        this.CompanyName = companyName;
        this.Cycle = this.Cost * this.NumberOfStocks;
    }
    public Transaction(int numberofstocks, int cost, String symbol, String action, String buyer,String seller, String companyName) {
        this.Date  = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        this.NumberOfStocks = numberofstocks;
        this.Cost = cost;
        this.Symbol = symbol.toUpperCase();
        this.Action = action;
        this.Buyer = buyer;
        this.Seller = seller;
        this.CompanyName = companyName;
        this.Cycle = this.Cost * this.NumberOfStocks;
    }
    public String getCompanyName(){return this.CompanyName;}
    public String getBuyer(){return  this.Buyer;}
    public String getSeller(){return this.Seller;}
    public String getDate() {
        return this.Date;
    }
    public void setBuyer(String buyer){this.Buyer = buyer;}
    public void setSeller(String seller){this.Seller=seller;}

    public int getNumberOfStocks() {
        return this.NumberOfStocks;
    }

    public int getCost() {
        return this.Cost;
    }

    public String getSymbol() {return this.Symbol;}

    public void setNumberOfStocks(int newNumberOfStocks){this.NumberOfStocks = newNumberOfStocks;}

    public String getAction(){return this.Action;}

    public int getCycle(){return this.Cycle;}


    @Override
    public String toString() {
        String buffer = "Transaction Date: " + this.Date + "\n";
        buffer += "Action: " + this.Action+"\n";
        buffer += "Number of Stocks: " + this.NumberOfStocks + "\n";
        buffer += "Price per Stock: " + this.Cost + "\n";
        buffer += "Transaction Value: " + (this.getNumberOfStocks() * this.Cost) + "\n\n";
        return buffer;
    }

    public TransactionDTO getDto(Boolean success, String message){
        if (success) {
            return new TransactionDTO(this.Date, this.NumberOfStocks, this.Cost, this.Symbol, this.CompanyName,this.Action,message,this.Buyer,this.Seller,true,this.getCycle());
        }
        else{
            return new TransactionDTO("",0,0,"","","",message,"","",false,0);
        }

    }
}

class TransactionComperator implements Comparator<Transaction>
{
    @Override
    public int compare(Transaction o1, Transaction o2) {
        if (o1.getAction().compareToIgnoreCase("sell")==0) {
            if (o1.getCost() > o2.getCost()) {
                return 1;
            } else if (o1.getCost() == o2.getCost()) {
                if (o1.getDate().compareTo(o2.getDate()) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return -1;
            }
        } else {
            if (o1.getCost() < o2.getCost()) {
                return 1;
            } else if (o1.getCost() == o2.getCost()) {
                int comp = o1.getDate().compareTo(o2.getDate());
                if (comp < 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return -1;
            }
        }
    }
}