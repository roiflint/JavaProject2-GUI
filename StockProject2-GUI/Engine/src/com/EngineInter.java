package com;

import com.ErrorDto;
import com.StocksDTO;
import com.TransactionDTO;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Consumer;

public interface EngineInter {
    //
    //    //Return a string containing information n the fields of every stock in the system ready to be printed
    public List<StocksDTO> GetAllStocksInfo();

    //Receives a stock symbol, returns a string containing information on the stock fields and every
    //transaction made on that stock ready to be printed
    StocksDTO GetSingleStockInfo(String StockName);

    //Receives a path for a xml file, if the file is applicable the file is loaded to the system and
    //override any information stored before the file was loaded
    public ErrorDto LoadXML(String FileName, Buyer<Double,Double> progress, Consumer<String> message) throws InterruptedException, Exception;

    public List<TransactionDTO> GetBuyList(String Symbol);

    public List<TransactionDTO> GetSellList(String Symbol);

    public List<TransactionDTO> GetPreviousList(String Symbol);

    public List<TransactionDTO> MakeTransaction(Transaction transaction, boolean buy);

    //Receives a stock symbol and checking if the stock exists in the system
    public boolean IsStockExist(String symbol);

    //Receives a stock symbol and return the stock current price
    public int getStockPrice(String symbol);

    //Receives a stock symbol and return the company name of that sock symbol
    public String getCompanyNameBySymbol(String Symbol);

    //Returns the observable users list from the engine
    public ObservableList<String> getObservableUsers();

    //Returns the observable stocks list from the engine
    public ObservableList<String> getObservableStocks();

    //Returns a generated observable list that represent the stock holdings of a specific user
    public ObservableList<ObservableUserStocks> getObservableUserStocks(String user);

    //Calculate the total cycle of a specific user
    public String CalculateTotalCycle(String user);

    //Generate an observable list of the buy pending list
    public ObservableList<Transaction> getObservableBuyList(String symbol);

    //Generate an observable list of the sell pending list
    public ObservableList<Transaction> getObservableSellList(String symbol);

    //Generate an observable list of the previous transactions list
    public ObservableList<Transaction> getObservablePreviousList(String symbol);

    //Returns a generated observable list of the stock symbols that specific user hold
    public ObservableList<String> getObservableUserStockSymbols(String user);

    //Check the transaction form for legality and transfer the data to the buy or sell action
    public ErrorDto checkTransactionForm(String user, String symbol, String price, String quantity);

    //Generate the observableStockList and observableUsersList for the engine after loading a xml file
    public void generateObservables();


}

