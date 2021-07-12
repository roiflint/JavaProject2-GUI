package com;

import generated.RseStock;

public class Stock {
    private  final String CompanyName;
    private final String Symbol;
    private int Price;
    private int Cycle;
    private int NumberOfTransactions;

    public Stock(String name,String symbol, int price)
    {
        this.CompanyName = name;
        this.Symbol = symbol.toUpperCase();
        this.Price = price;
        this.Cycle = 0;
        this.NumberOfTransactions = 0;
    }

    public Stock(RseStock s)
    {
        this.CompanyName = s.getRseCompanyName();
        this.Symbol = s.getRseSymbol();
        this.Price = s.getRsePrice();
        this.Cycle = 0;
        this.NumberOfTransactions = 0;
    }

    public void setPrice(int newPrice)
    {
        this.Price = newPrice;
    }
    public void setCycle(int newTransaction){Cycle+=newTransaction;}
    public void setNumberOfTransactions(){this.NumberOfTransactions++;}
    public int getPrice(){return this.Price;}
    public String getCompanyName(){return this.CompanyName;}
    public String getSymbol(){return this.Symbol;}
    public int getCycle(){return this.Cycle;}
    public int getNumberOfTransactions(){return this.NumberOfTransactions;}

    @Override
    public String toString()
    {
        String buffer = "Stock Symbol: " + this.Symbol + "\n";
        buffer += "Company Name: " + this.CompanyName + "\n";
        buffer += "Stock Price: " + this.Price + "\n";
        buffer += "Number of Transactions: " + this.NumberOfTransactions + "\n";
        buffer += "Stock Cycle: " + this.Cycle + "\n";
        return  buffer;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this.getClass() == obj.getClass())
        {
            Stock s = (Stock)obj;
            if (this.Symbol.equalsIgnoreCase(s.Symbol) || this.CompanyName.equalsIgnoreCase(s.CompanyName))
            {
                return  true;
            }
            else
            {
                return  false;
            }
        }
        else
        {
            return false;
        }
    }

    public StocksDTO getDto(){ return new StocksDTO(this.CompanyName,this.Symbol,this.Price,this.Cycle,this.NumberOfTransactions); }
}

