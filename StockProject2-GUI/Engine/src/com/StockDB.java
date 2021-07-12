package com;


import javafx.collections.ObservableList;

import java.util.*;

public class StockDB {

   // private List<Transaction> PreviousTransactions = new LinkedList<Transaction>();
   // private List<Transaction> BuyTransactions = new LinkedList<Transaction>();
   // private  List<Transaction> SellTransactions = new LinkedList<Transaction>();
    private final Map<String,List<Transaction>> BuyDB;
    private final Map<String,List<Transaction>> SellDB;
    private final Map<String,List<Transaction>> PreviousDB;

    public StockDB()
    {
        this.BuyDB = new HashMap<>();
        this.SellDB = new HashMap<>();
        this.PreviousDB = new HashMap<>();
    }

    public List<Transaction> getBuy(String symbol)
    {
        return this.BuyDB.get(symbol.toUpperCase());
    }
    public List<Transaction> getSell(String symbol) { return this.SellDB.get(symbol.toUpperCase()); }
    public List<Transaction> getPrevious(String symbol)
    {
        return this.PreviousDB.get(symbol.toUpperCase());
    }

    //insert a transaction into the previous transactions list
    public void insertDoneTransaction(Transaction done)
    {
        if (PreviousDB.get(done.getSymbol().toUpperCase())==null)
        {
            List<Transaction> lst = new ArrayList<>();
            lst.add(done);
            PreviousDB.put(done.getSymbol().toUpperCase(),lst);
        }
        else
        {
            PreviousDB.get(done.getSymbol().toUpperCase()).add(done);
        }
    }

    //insert a transaction into the buy pending list
    public void insertBuyTransaction(Transaction buy)
    {
        if (BuyDB.get(buy.getSymbol().toUpperCase())==null)
        {
            List<Transaction> lst = new ArrayList<>();
            lst.add(buy);
            BuyDB.put(buy.getSymbol().toUpperCase(),lst);
        }
        else
        {
            BuyDB.get(buy.getSymbol().toUpperCase()).add(buy);
        }

    }

    //insert a transaction into the sell pending list
    public void insertSellTransaction(Transaction sell)
    {
        if (SellDB.get(sell.getSymbol().toUpperCase())==null)
        {
            List<Transaction> lst = new ArrayList<>();
            lst.add(sell);
            SellDB.put(sell.getSymbol().toUpperCase(),lst);
        }
        else
        {
            SellDB.get(sell.getSymbol().toUpperCase()).add(sell);
        }
    }

    //return a string of all the previous transactions to be printed
    public List<TransactionDTO> GetPreviousTransactions(String StockName)
    {
        List<TransactionDTO> dto = new LinkedList<>();
        List<Transaction> lst = PreviousDB.get(StockName);
        if (lst==null)
        {
            return null;
        }
        for (Object o: lst)
        {
            Transaction t = (Transaction) o;
            dto.add(t.getDto(true,""));
        }
        return dto;
    }

    //return a string of all the buy pending transactions to be printed
    public List<TransactionDTO> GetBuyTransactions(String StockName)
    {
        List<TransactionDTO> dto = new LinkedList<>();
        List<Transaction> lst = BuyDB.get(StockName);
        if (lst==null)
        {
            return null;
        }
        for (Object o: lst)
        {
            Transaction t = (Transaction)o;
            dto.add(t.getDto(true,""));
        }
        return dto;
    }

    //return a string of all the sell pending transactions to be printed
    public List<TransactionDTO> GetSellTransactions(String StockName)
    {
        List<TransactionDTO> dto = new LinkedList<>();
        List<Transaction> lst = SellDB.get(StockName);
        if (lst==null)
        {
            return null;
        }
        for (Object o: lst)
        {
            Transaction t = (Transaction) o;
            dto.add(t.getDto(true,""));
        }
        return dto;
    }

    //clears the data base
    public void ClearDB()
    {
        this.BuyDB.clear();
        this.SellDB.clear();
        this.PreviousDB.clear();
    }


}

