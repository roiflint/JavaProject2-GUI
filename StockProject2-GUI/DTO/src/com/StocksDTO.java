package com;

public class StocksDTO {
    private String CompanyName;
    private String Symbol;
    private int Price;
    private int Cycle;
    private int NumberOfTransactions;
    public StocksDTO(String CompanyName, String Symbol, int Price, int Cycle, int NumberOfTransactions) {
        this.CompanyName = CompanyName;
        this.Symbol = Symbol;
        this.Price = Price;
        this.Cycle = Cycle;
        this.NumberOfTransactions = NumberOfTransactions;;
    }

    public String getCompanyName() { return CompanyName; }
    public String getSymbol(){return Symbol;}
    public int getPrice(){return Price;}
    public int getCycle(){return Cycle;}
    public int getNumberOfTransactions(){return NumberOfTransactions;}
}
