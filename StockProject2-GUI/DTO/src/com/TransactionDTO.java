package com;

public class TransactionDTO {
    private String Date;
    private int NumberOfStocks;
    private int Cost;
    private String Symbol;
    private  String CompanyName;
    private String Action;
    private String Message;
    private String Buyer;
    private String Seller;
    private Boolean Success;
    private int Cycle;
    public TransactionDTO(String date, int numberOfStocks, int cost, String symbol, String companyName, String action, String message, String buyer, String seller, Boolean success,int cycle)
    {
        this.Date = date;
        this.NumberOfStocks = numberOfStocks;
        this.Cost = cost;
        this.Symbol = symbol;
        this.CompanyName = companyName;
        this.Action = action;
        this.Message = message;
        this.Buyer = buyer;
        this.Seller = seller;
        this.Cycle = cycle;
        this.Success = success;
    }
    public TransactionDTO(Boolean success, String message){
        this("",0,0,"","","",message,"","",success,0);
    }
    public String getDate(){return Date;}
    public int getNumberOfStocks(){return NumberOfStocks;}
    public int getCost(){return Cost;}
    public String getSymbol(){return Symbol;}
    public String getCompanyName(){return CompanyName;}
    public String getAction(){return Action;}
    public String getMessage(){return Message;}
    public String getBuyer(){return this.Buyer;}
    public String getSeller(){return this.Seller;}
    public void setMessage(String message){this.Message = message;}
    public boolean getSuccess(){return this.Success;}
    public int getCycle(){return this.Cycle;}
}
