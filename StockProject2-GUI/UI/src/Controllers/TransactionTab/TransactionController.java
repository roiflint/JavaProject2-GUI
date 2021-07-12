package Controllers.TransactionTab;

import Controllers.MainWindow.LayoutController;
import com.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class TransactionController {

    private LayoutController mainController;
    public void setMainController(LayoutController controller){
        this.mainController = controller;
    }

    @FXML
    private BorderPane BorderPaneID;

    @FXML
    private Label TransactionMessageLabel;

    @FXML
    private ChoiceBox<String> TransactionUserSelector;

    @FXML
    private ChoiceBox<String> TransactionStockSelector;

    @FXML
    private RadioButton BuyRadio;

    @FXML
    private ToggleGroup BuySell;

    @FXML
    private RadioButton SellRadio;

    @FXML
    private RadioButton LMTRadio;

    @FXML
    private ToggleGroup Action;

    @FXML
    private RadioButton MKTRadio;

    @FXML
    private TextField PriceTextField;

    @FXML
    private TextField QuantityTextField;

    @FXML
    private TableView<TransactionDTO> TransactionTable;

    @FXML
    private TableColumn<TransactionDTO, String> TransactionSymbolCollum;

    @FXML
    private TableColumn<TransactionDTO, String> TransactionCompanyCollum;

    @FXML
    private TableColumn<TransactionDTO, Integer> TransactionPriceCollum;

    @FXML
    private TableColumn<TransactionDTO, String> TransactionActionCollum;

    @FXML
    private TableColumn<TransactionDTO, Integer> TransactionCycleCollum;

    @FXML
    private TableColumn<TransactionDTO, Integer> TransactionAmountCollum;

    @FXML
    private TableColumn<TransactionDTO, String > TransactionBuyerCollum;

    @FXML
    private TableColumn<TransactionDTO, String> TransactionSellerCollum;

    @FXML
    private TableColumn<TransactionDTO, String> TransactionDateCollum;

    @FXML
    private Button TransactionOkButton;

    @FXML
    private Button TransactionCancelButton;

    @FXML
    public void initialize(){
        TransactionSymbolCollum.setCellValueFactory(new PropertyValueFactory<>("Symbol"));
        TransactionCompanyCollum.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        TransactionPriceCollum.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        TransactionActionCollum.setCellValueFactory(new PropertyValueFactory<>("Action"));
        TransactionCycleCollum.setCellValueFactory(new PropertyValueFactory<>("Cycle"));
        TransactionAmountCollum.setCellValueFactory(new PropertyValueFactory<>("NumberOfStocks"));
        TransactionBuyerCollum.setCellValueFactory(new PropertyValueFactory<>("Buyer"));
        TransactionSellerCollum.setCellValueFactory(new PropertyValueFactory<>("Seller"));
        TransactionDateCollum.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }


    @FXML
    public void makeTransaction() throws InterruptedException {     //get data from form and forward it to makeTransaction method in the engine, then update the table of all transactions that was made
        ErrorDto error = Main.eng.checkTransactionForm(TransactionUserSelector.getValue(),TransactionStockSelector.getValue(),PriceTextField.getText(),QuantityTextField.getText());
        Thread.sleep(200);
        if (error.getSuccess()){
            Transaction transaction;
            List<TransactionDTO> dto;
            String action;
            if (LMTRadio.isSelected())
                action = "LMT";
            else
                action = "MKT";
            if (BuyRadio.isSelected()) {
                transaction = new Transaction(Integer.parseInt(QuantityTextField.getText()),Integer.parseInt(PriceTextField.getText()),TransactionStockSelector.getValue(),action,TransactionUserSelector.getValue(),"",Main.eng.getCompanyNameBySymbol(TransactionStockSelector.getValue()));
                dto = Main.eng.MakeTransaction(transaction,true);
            }
            else{
                transaction = new Transaction(Integer.parseInt(QuantityTextField.getText()),Integer.parseInt(PriceTextField.getText()),TransactionStockSelector.getValue(),action,"",TransactionUserSelector.getValue(),Main.eng.getCompanyNameBySymbol(TransactionStockSelector.getValue()));
                dto = Main.eng.MakeTransaction(transaction,false);
            }
            if (dto.get(0).getSuccess()){//a deal was made
                ObservableList<TransactionDTO> lst = FXCollections.observableArrayList(dto);
                TransactionTable.setItems(lst);
                TransactionMessageLabel.setText(dto.get(dto.size()-1).getMessage());
            }
            else{//no deals were made
                TransactionTable.setItems(clearTable);
                TransactionMessageLabel.setText(dto.get(0).getMessage());
            }
        }
        else{
            TransactionMessageLabel.setText(error.getMessage());
        }


    }

    public void bindUsersSelector(ObservableList<String> lst){      //bind the choice box with the users list
        TransactionUserSelector.setItems(lst);
        TransactionUserSelector.setValue(lst.get(0));
    }

    public void bindStockSelector(ObservableList<String> lst){
        TransactionStockSelector.setItems(lst);
        TransactionStockSelector.setValue(lst.get(0));
    }

    @FXML
    public void backToDefault(){
        TransactionUserSelector.setValue(Main.eng.getObservableUsers().get(0));
        TransactionStockSelector.setValue(Main.eng.getObservableStocks().get(0));
        BuyRadio.setSelected(true);
        LMTRadio.setSelected(true);
        PriceTextField.setText("0");
        PriceTextField.setDisable(false);
        QuantityTextField.setText("0");
        TransactionMessageLabel.setText("");
        TransactionTable.setItems(clearTable);
    }
    private ObservableList<TransactionDTO> clearTable = FXCollections.observableArrayList();

    @FXML
    private void bindUserStocksChoiceBox(){     //bind the stocks of the selected user to the stocks choice box
        if (!BuyRadio.isSelected()){
            if (TransactionUserSelector.getValue().equals("--SELECT--")){
                TransactionStockSelector.setItems(EmptyStocks);
                TransactionStockSelector.setValue(EmptyStocks.get(0));
            }
            else{
                ObservableList<String> lst = Main.eng.getObservableUserStockSymbols(TransactionUserSelector.getValue());
                TransactionStockSelector.setItems(lst);
                TransactionStockSelector.setValue(lst.get(0));
            }
        }
        else{
            TransactionStockSelector.setItems(Main.eng.getObservableStocks());
            TransactionStockSelector.setValue(Main.eng.getObservableStocks().get(0));
        }
    }
    private ObservableList<String> EmptyStocks = FXCollections.observableArrayList("No Stocks");

    @FXML
    public void LMTChooser(){
        PriceTextField.setDisable(false);
    }

    @FXML
    public void MKTChooser(){
        PriceTextField.setDisable(true);
        setMKTPrice();
    }

    @FXML
    public void setMKTPrice(){      //if mkt is selected get the price stock
        if (MKTRadio.isSelected()){
            if (Main.eng.IsStockExist(TransactionStockSelector.getValue())){
                PriceTextField.setText(Integer.toString(Main.eng.getStockPrice(TransactionStockSelector.getValue())));
            }
        }
    }

}
