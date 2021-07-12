package com;

import com.ErrorDto;
import com.StocksDTO;
import com.TransactionDTO;
import com.sun.javaws.IconUtil;
import generated.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class Engine implements EngineInter {
    private final StockDB DB;
    private final Map<String, Stock> stocks;
    private final Map<String, User> Users;
    private ObservableList<String> observableStocks;
    private ObservableList<String> observableUsers;


    public Engine()
    {
        this.DB = new StockDB();
        this.stocks = new HashMap<>();
        this.Users = new HashMap<>();
        this.observableStocks = FXCollections.observableArrayList("--SELECT--");
        this.observableUsers = FXCollections.observableArrayList("--SELECT--");
    }
    public List<StocksDTO> GetAllStocksInfo()
    {
        List<StocksDTO> dto = new LinkedList<>();
        for (Stock s: this.stocks.values())
        {
            dto.add(s.getDto());
        }
        return dto;
    }

    public int getStockPrice(String symbol) {
        if (!IsStockExist(symbol.toUpperCase()))    //check if the stock exist
        {
            throw new InvalidParameterException("Stock does not exist in the system, please enter a different stock");
        }
        return stocks.get(symbol.toUpperCase()).getPrice();     //return the stock price
    }

    public StocksDTO GetSingleStockInfo(String StockName) {
            Stock s = this.stocks.get(StockName.toUpperCase());
            if (s == null)
                return null;
            return s.getDto();  //return a generated dto that represent the stock information
    }

    public void generateObservables(){
        this.observableUsers.setAll("--SELECT--");  //push select as first object
        this.observableStocks.setAll("--SELECT--"); //push select as first object
        this.observableStocks.addAll(this.stocks.keySet()); //add the rest of the stocks
        this.observableUsers.addAll(this.Users.keySet());   //add the rest of the users

    }

    public ErrorDto LoadXML(String FileName, Buyer<Double,Double> progress, Consumer<String> message) throws Exception {
        try {
            InputStream inputStream = new FileInputStream((new File(FileName)));
            RizpaStockExchangeDescriptor items = ItemsdeserializeFrom(inputStream);
            RseStocks stocks = items.getRseStocks();
            RseUsers users = items.getRseUsers();
            Map<String, Stock> tmpStock = new HashMap<>(this.stocks);   //save current stocks as backup
            Map<String, User> tmpUsers = new HashMap<>(this.Users);     //save current users as backup
            this.stocks.clear();    //clear data
            this.Users.clear();     //clear data

            progress.accept(10.0,100.0);
            message.accept("Loading Stocks...");
            double stockProgress = 45 / stocks.getRseStock().size();        //calculate progress percentage jumps
            int i = 1;
            for (RseStock o:stocks.getRseStock())       //go over all the stocks in the file
            {
                progress.accept(10 + (stockProgress * i),100.0);        //new progress
                i++;
                Thread.sleep(250);

                Stock s = new Stock(o);
                for (Stock stock:this.stocks.values())      //check if a stock already exist in the system
                {
                    if (stock.equals(s))        /////error multiple stocks, return data from backup
                    {
                        this.stocks.clear();
                        this.stocks.putAll(tmpStock);
                        this.Users.clear();
                        this.Users.putAll(tmpUsers);
                        message.accept("Duplicate Stocks in file, load a new XML");
                        return new ErrorDto(false,"Duplicate Stocks in file, load a new XML");
                    }
                }
                this.stocks.put(s.getSymbol().toUpperCase(),s);     //add stock to system
            }
            message.accept("Loading users...");
            double userProgress = 45/users.getRseUser().size();     //calculate progress percentage jumps
            i=1;
            for (RseUser o:users.getRseUser())          //go over all users in file
            {
                progress.accept(55 + (userProgress * i),100.0);
                i++;
                Thread.sleep(250);
                User u = new User(o);
                if (this.Users.containsKey(u.getName())){       //check if user already exist in file
                    this.stocks.clear();
                    this.stocks.putAll(tmpStock);
                    this.Users.clear();
                    this.Users.putAll(tmpUsers);
                    message.accept("Duplicate users in file");
                    return new ErrorDto(false,"Duplicate users in file");
                }
                else{
                    for (String stock:u.getUserStocks().keySet())       //check if user holds stocks not in the system
                    {
                        if (!this.stocks.containsKey(stock.toUpperCase()))/////
                        {
                            this.stocks.clear();
                            this.stocks.putAll(tmpStock);
                            this.Users.clear();
                            this.Users.putAll(tmpUsers);
                            message.accept("User hold stock that does not exist in the system");
                            return new ErrorDto(false,"User hold stock that does not exist in the system");
                        }
                    }
                }
                this.Users.put(u.getName(),u);
            }
            progress.accept(100.0,100.0);
            message.accept("File loaded successfully");
            this.DB.ClearDB();
            return new ErrorDto(true,"File loaded successfully");
        } catch (JAXBException | FileNotFoundException | InvalidParameterException e) {
            System.out.println(e.getMessage());
            return new ErrorDto(false,e.getMessage());
        }
    }

    private static RizpaStockExchangeDescriptor ItemsdeserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) u.unmarshal(in);
    }


    public List<TransactionDTO> GetBuyList(String Symbol){return this.DB.GetBuyTransactions(Symbol); }

    public List<TransactionDTO> GetSellList(String Symbol){ return this.DB.GetSellTransactions(Symbol);}

    public List<TransactionDTO> GetPreviousList(String Symbol){return this.DB.GetPreviousTransactions(Symbol); }

    public List<TransactionDTO> MakeTransaction(Transaction transaction, boolean buy) {
        List<TransactionDTO> dto = new ArrayList<>();
        if (!IsStockExist(transaction.getSymbol().toUpperCase())) {
            dto.add(new TransactionDTO(false, "The stock name is not in the system, please enter a different stock"));
            return dto;
        }
        if (transaction.getCost() < 1) {

            dto.add(new TransactionDTO(false, "Enter a positive price only"));
            return dto;
        }
        if (transaction.getNumberOfStocks() < 1) {

            dto.add(new TransactionDTO(false, "Number of stocks must be positive"));
            return dto;
        }
        if (!transaction.getBuyer().equals("") && !this.Users.containsKey(transaction.getBuyer())) {
            dto.add(new TransactionDTO(false, "Buyer does not exist in the system, please enter a different buyer"));
            return dto;
        }
        if (!transaction.getSeller().equals("") && !this.Users.containsKey(transaction.getSeller())){
            dto.add(new TransactionDTO(false, "Seller does not exist in the system, please enter a different buyer"));
            return dto;
        }
        if (!transaction.getSeller().equals("") && this.Users.get(transaction.getSeller()).getUserStocks().get(transaction.getSymbol()) < transaction.getNumberOfStocks()){
            dto.add(new TransactionDTO(false,"Seller does not have enough stocks to sell"));
            return dto;
        }
        if (buy){
            return BUY(transaction);
        }
        else{
            return SELL(transaction);
        }
    }

    public List<TransactionDTO> BUY(Transaction transaction)  {
        int NewNumberOfStocks;
        String companyName;
        List<TransactionDTO> dto = FXCollections.observableArrayList();
        List<Transaction> remove = new ArrayList<>();
        List<Transaction> sell = DB.getSell(transaction.getSymbol().toUpperCase());
        if (sell==null) {       //no items in sell list to buy from
            DB.insertBuyTransaction(transaction);       //insert to buy pending
            DB.getBuy(transaction.getSymbol().toUpperCase()).sort(new TransactionComperator());
            dto.add(new TransactionDTO(false,"Transaction added to the pending buy actions"));
            return dto;
        }
        for (Object o:sell) {       //go over sell pending list
            Transaction t = (Transaction) o;
            if (t.getCost() <= transaction.getCost()) {     //if the sell cost is lower than the buy cost
                if (t.getNumberOfStocks() >= transaction.getNumberOfStocks()) {     //check number of stocks to sell
                    t.setNumberOfStocks(t.getNumberOfStocks()- transaction.getNumberOfStocks());        //set number of stocks after the action
                    NewNumberOfStocks = this.Users.get(t.getSeller()).getUserStocks().get(t.getSymbol().toUpperCase()) - transaction.getNumberOfStocks();
                    if (NewNumberOfStocks <= 0){        //check if the seller has any stocks left, if not remove the stocks from his holdings list
                        this.Users.get(t.getSeller()).getUserStocks().remove(t.getSymbol().toUpperCase()); //seller has no more stocks
                        this.Users.get(t.getSeller()).RemoveUserObservableStock(t.getSymbol());
                    }
                    else{       //if not update the amount of the current stock
                        this.Users.get(t.getSeller()).getUserStocks().put(t.getSymbol().toUpperCase(),NewNumberOfStocks); //update seller number of stocks
                    }
                    if (this.Users.get(transaction.getBuyer()).getUserStocks().containsKey(transaction.getSymbol().toUpperCase())){ //buyer has the stock
                        NewNumberOfStocks = this.Users.get(transaction.getBuyer()).getUserStocks().get(transaction.getSymbol().toUpperCase()) + transaction.getNumberOfStocks();
                        this.Users.get(transaction.getBuyer()).getUserStocks().put(transaction.getSymbol().toUpperCase(),NewNumberOfStocks);
                    }
                    else{//buyer doesnt have the stock
                        this.Users.get(transaction.getBuyer()).getUserStocks().put(transaction.getSymbol().toUpperCase(),transaction.getNumberOfStocks());
                        this.Users.get(transaction.getBuyer()).AddUserObservableStock(transaction.getSymbol());
                    }
                    companyName = getCompanyNameBySymbol(transaction.getSymbol().toUpperCase());
                    Transaction newTrans = new Transaction(transaction.getNumberOfStocks(),t.getCost(),transaction.getSymbol(),transaction.getAction(),transaction.getBuyer(),t.getSeller(),companyName);
                    dto.add(newTrans.getDto(true,""));
                    this.DB.insertDoneTransaction(newTrans);
                    this.stocks.get(t.getSymbol().toUpperCase()).setPrice(t.getCost());
                    this.stocks.get(t.getSymbol().toUpperCase()).setNumberOfTransactions();
                    this.stocks.get(t.getSymbol().toUpperCase()).setCycle(transaction.getNumberOfStocks()*t.getCost());
                    if (t.getNumberOfStocks() == 0) {       //remove the transaction from the sell pending
                        sell.remove(o);
                    }
                    this.DB.getSell(transaction.getSymbol().toUpperCase()).removeAll(remove);
                    remove.clear();
                    return dto;
                }
                else {      //make partial transaction
                    companyName = getCompanyNameBySymbol(transaction.getSymbol()).toUpperCase();
                    Transaction newTrans = new Transaction(t.getNumberOfStocks(),t.getCost(), transaction.getSymbol(), transaction.getAction(),transaction.getBuyer(),t.getSeller(),companyName);
                    dto.add(newTrans.getDto(true,""));
                    NewNumberOfStocks = this.Users.get(t.getSeller()).getUserStocks().get(t.getSymbol().toUpperCase()) - transaction.getNumberOfStocks();
                    if (NewNumberOfStocks <= 0){
                        this.Users.get(t.getSeller()).getUserStocks().remove(t.getSymbol().toUpperCase()); //seller has no more stocks
                        this.Users.get(t.getSeller()).RemoveUserObservableStock(t.getSymbol());
                    }
                    else{
                        this.Users.get(t.getSeller()).getUserStocks().put(t.getSymbol().toUpperCase(),NewNumberOfStocks); //update seller number of stocks
                    }
                    if (this.Users.get(transaction.getBuyer()).getUserStocks().containsKey(transaction.getSymbol().toUpperCase())){ //buyer has the stock
                        NewNumberOfStocks = this.Users.get(transaction.getBuyer()).getUserStocks().get(transaction.getSymbol().toUpperCase()) + t.getNumberOfStocks();
                        this.Users.get(transaction.getBuyer()).getUserStocks().put(transaction.getSymbol().toUpperCase(),NewNumberOfStocks);
                    }
                    else{//buyer doesnt have the stock
                        this.Users.get(transaction.getBuyer()).getUserStocks().put(transaction.getSymbol().toUpperCase(),t.getNumberOfStocks());
                        this.Users.get(transaction.getBuyer()).AddUserObservableStock(transaction.getSymbol());
                    }
                    this.DB.insertDoneTransaction(newTrans);
                    remove.add(t);
                    transaction.setNumberOfStocks(transaction.getNumberOfStocks()-t.getNumberOfStocks());
                    this.stocks.get(t.getSymbol().toUpperCase()).setPrice(t.getCost());
                    this.stocks.get(t.getSymbol().toUpperCase()).setNumberOfTransactions();
                    this.stocks.get(t.getSymbol().toUpperCase()).setCycle(t.getNumberOfStocks()*t.getCost());
                    if (transaction.getNumberOfStocks() == 0) {
                        int location = dto.size();
                        if (location ==0){
                            dto.add(new TransactionDTO(false,"Transaction added to the pending sell actions"));
                            return dto;
                        }
                        dto.get(location-1).setMessage("All stocks bought successfully");
                        this.DB.getSell(transaction.getSymbol().toUpperCase()).removeAll(remove);
                        remove.clear();
                        return dto;
                    }
                }
            }
        }
        this.DB.getSell(transaction.getSymbol().toUpperCase()).removeAll(remove);
        remove.clear();
        if (transaction.getNumberOfStocks() != 0) {     //check if all the stocks were bought
            DB.insertBuyTransaction(transaction);
            DB.getBuy(transaction.getSymbol().toUpperCase()).sort(new TransactionComperator());
            int location = dto.size();
            if (location ==0){
                dto.add(new TransactionDTO(false,"Transaction added to the pending sell actions"));
                return dto;
            }
            dto.get(location-1).setMessage(transaction.getNumberOfStocks() + " Stocks were not bought and added to the pending buy actions");
            return dto;
        }
        return dto;
    }

    public List<TransactionDTO> SELL(Transaction transaction) {
        List<TransactionDTO> dto = FXCollections.observableArrayList();
        int NewNumberOfStocks;
        String companyName;
        List<Transaction> remove = new ArrayList<>();
        List<Transaction> buy = this.DB.getBuy(transaction.getSymbol().toUpperCase());
        if (buy==null) {        //check if there are transactions pending to sell to
            this.DB.insertSellTransaction(transaction);
            DB.getSell(transaction.getSymbol().toUpperCase()).sort(new TransactionComperator());
            dto.add(new TransactionDTO(false,"Transaction added to the pending sell actions"));
            return dto;
        }
        for (Object o:buy) {        //check all pending buy transactions
            Transaction t = (Transaction) o;
            if (t.getCost() >= transaction.getCost()) {     //check if the buy price is higher than the sell price
                if (t.getNumberOfStocks() >= transaction.getNumberOfStocks()) {     //check number of stocks to be bought
                    t.setNumberOfStocks(t.getNumberOfStocks()- transaction.getNumberOfStocks());
                    if (this.Users.get(t.getBuyer()).getUserStocks().containsKey(transaction.getSymbol().toUpperCase())){       //check if need to insert the stock to the buyer list holdings
                        NewNumberOfStocks = this.Users.get(t.getBuyer()).getUserStocks().get(t.getSymbol().toUpperCase()) + transaction.getNumberOfStocks();
                    }
                    else{
                        NewNumberOfStocks = transaction.getNumberOfStocks();
                        this.Users.get(t.getBuyer()).AddUserObservableStock(transaction.getSymbol());
                    }
                    this.Users.get(t.getBuyer()).getUserStocks().put(t.getSymbol().toUpperCase(),NewNumberOfStocks); //update Buyer number of stocks
                    NewNumberOfStocks = (this.Users.get(transaction.getSeller()).getUserStocks().get(transaction.getSymbol().toUpperCase()) - transaction.getNumberOfStocks());
                    if (NewNumberOfStocks <= 0){        //remove the stock from the seller
                        this.Users.get(transaction.getSeller()).getUserStocks().remove(transaction.getSymbol().toUpperCase());
                        this.Users.get(transaction.getSeller()).RemoveUserObservableStock(transaction.getSymbol());
                    }
                    else{       //update the seller number of stocks
                        this.Users.get(transaction.getSeller()).getUserStocks().put(transaction.getSymbol().toUpperCase(),NewNumberOfStocks);
                    }
                    companyName = getCompanyNameBySymbol(transaction.getSymbol().toUpperCase());
                    Transaction newTrans = new Transaction(transaction.getNumberOfStocks(),transaction.getCost(),transaction.getSymbol(),transaction.getAction(),t.getBuyer(),transaction.getSeller(),companyName);
                    dto.add((newTrans.getDto(true,"")));
                    this.DB.insertDoneTransaction(newTrans);
                    this.stocks.get(t.getSymbol().toUpperCase()).setPrice(t.getCost());
                    this.stocks.get(t.getSymbol().toUpperCase()).setNumberOfTransactions();
                    this.stocks.get(t.getSymbol().toUpperCase()).setCycle(transaction.getNumberOfStocks()*t.getCost());
                    if (t.getNumberOfStocks() == 0) {
                        buy.remove(o);
                    }
                    this.DB.getBuy(transaction.getSymbol().toUpperCase()).removeAll(remove);
                    remove.clear();
                    return dto;
                }
                else {      //make partial transaction
                    companyName = getCompanyNameBySymbol(transaction.getSymbol()).toUpperCase();
                    Transaction newTrans = new Transaction(t.getNumberOfStocks(),transaction.getCost(), transaction.getSymbol(), transaction.getAction(),t.getBuyer(),transaction.getSeller(),companyName);
                    dto.add(newTrans.getDto(true,""));
                    this.DB.insertDoneTransaction(newTrans);
                    if (this.Users.get(t.getBuyer()).getUserStocks().containsKey(transaction.getSymbol().toUpperCase())){
                        NewNumberOfStocks = this.Users.get(t.getBuyer()).getUserStocks().get(t.getSymbol().toUpperCase()) + t.getNumberOfStocks();
                    }
                    else{
                        NewNumberOfStocks = t.getNumberOfStocks();
                        this.Users.get(t.getBuyer()).AddUserObservableStock(t.getSymbol());
                    }
                    this.Users.get(t.getBuyer()).getUserStocks().put(t.getSymbol().toUpperCase(),NewNumberOfStocks); //update Buyer number of stocks
                    NewNumberOfStocks = this.Users.get(transaction.getSeller()).getUserStocks().get(transaction.getSymbol().toUpperCase()) - t.getNumberOfStocks();
                    if (NewNumberOfStocks <= 0){
                        this.Users.get(transaction.getSeller()).getUserStocks().remove(transaction.getSymbol().toUpperCase());
                        this.Users.get(transaction.getSeller()).RemoveUserObservableStock(transaction.getSymbol());
                    }
                    else{
                        this.Users.get(transaction.getSeller()).getUserStocks().put(transaction.getSymbol().toUpperCase(),NewNumberOfStocks);
                    }

                    remove.add(t);
                    transaction.setNumberOfStocks(transaction.getNumberOfStocks()-t.getNumberOfStocks());
                    this.stocks.get(t.getSymbol().toUpperCase()).setPrice(t.getCost());
                    this.stocks.get(t.getSymbol().toUpperCase()).setNumberOfTransactions();
                    this.stocks.get(t.getSymbol().toUpperCase()).setCycle(t.getNumberOfStocks()*t.getCost());
                    if (transaction.getNumberOfStocks() == 0) {
                        int location = dto.size();
                        dto.get(location-1).setMessage("All stocks sold successfully");
                        this.DB.getBuy(transaction.getSymbol().toUpperCase()).removeAll(remove);
                        remove.clear();
                        return dto;
                    }
                }
            }
        }
        this.DB.getBuy(transaction.getSymbol().toUpperCase()).removeAll(remove);
        remove.clear();
        if (transaction.getNumberOfStocks() != 0) {     //check if all stoccks were sold
            DB.insertSellTransaction(transaction);
            DB.getSell(transaction.getSymbol().toUpperCase()).sort(new TransactionComperator());
            int location = dto.size();
            if (location ==0){
                dto.add(new TransactionDTO(false,"Transaction added to the pending sell actions"));
                return dto;
            }

            dto.get(location-1).setMessage(transaction.getNumberOfStocks() + " Stocks were not sold and added to the pending sell actions");
            return dto;
        }
        return dto;
    }

    public boolean IsStockExist(String symbol) {
        return this.stocks.containsKey(symbol);
    }

    public String getCompanyNameBySymbol(String Symbol){return stocks.get(Symbol).getCompanyName(); }

    public ObservableList<String> getObservableUsers(){return this.observableUsers;}

    public ObservableList<String> getObservableStocks(){return this.observableStocks;}

    public String CalculateTotalCycle(String user){
        int sum = 0;
        if (this.Users.containsKey(user)){
            sum = 0;
            for (String s:this.Users.get(user).getUserStocks().keySet()) {      //sum the total cycle
                sum += getStockPrice(s.toUpperCase())*this.Users.get(user).getUserStocks().get(s);
            }
        }
        return Integer.toString(sum);
    }

    public ObservableList<ObservableUserStocks> getObservableUserStocks(String user){
        if (this.Users.containsKey(user)) {

            ObservableList<ObservableUserStocks> stocks = FXCollections.observableArrayList();
            for (String s:this.Users.get(user).getUserStocks().keySet()) {
                stocks.add(new ObservableUserStocks(s,getCompanyNameBySymbol(s),getStockPrice(s),this.Users.get(user).getUserStocks().get(s)));
            }
            return stocks;
        }
        else return FXCollections.observableArrayList();
    }

    public ObservableList<String> getObservableUserStockSymbols(String user){
        if (this.Users.containsKey(user)) {
            ObservableList<String> lst = this.Users.get(user).getObservableUserStocks();
            if(lst.size() == 0){
                lst.add("No Stocks");
            }
            return lst;
        }
        else return FXCollections.observableArrayList("No Stocks");
    }

    public ObservableList<Transaction> getObservableBuyList(String symbol){
        ObservableList<Transaction> lst = FXCollections.observableArrayList();
        if (IsStockExist(symbol)){
            if (this.DB.getBuy(symbol) != null)
                lst.addAll(this.DB.getBuy(symbol));
        }
        return lst;
    }
    public ObservableList<Transaction> getObservableSellList(String symbol){
        ObservableList<Transaction> lst = FXCollections.observableArrayList();
        if (IsStockExist(symbol)){
            if (this.DB.getSell(symbol) != null)
                lst.addAll(this.DB.getSell(symbol));
        }
        return lst;
    }
    public ObservableList<Transaction> getObservablePreviousList(String symbol){
        ObservableList<Transaction> lst = FXCollections.observableArrayList();
        if (IsStockExist(symbol)){
            if (this.DB.getPrevious(symbol) != null)
                lst.addAll(this.DB.getPrevious(symbol));
        }
        return lst;
    }
    public boolean isUserExist(String user){return this.Users.containsKey(user);}

    public ErrorDto checkTransactionForm(String user, String symbol, String price, String quantity){
        if (!isUserExist(user))
            return new ErrorDto(false,"Please choose a user");
        if (!IsStockExist(symbol))
            return new ErrorDto(false, "Please choose a stock");
        try {
            int cost = Integer.parseInt(price);
            int amount = Integer.parseInt(quantity);
            if (cost < 1)
                return new ErrorDto(false, "Price must be positive");
            if (amount < 1)
                return new ErrorDto(false, "Quantity must be positive");
            return new ErrorDto(true, "");
    } catch (NumberFormatException e) {
        return new ErrorDto(false, "Price and quantity must be round numbers");
    }
    }
}



