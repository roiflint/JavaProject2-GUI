package Controllers.AdminTab;

import Controllers.MainWindow.LayoutController;
import com.Main;
import com.ObservableUserStocks;
import com.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.omg.PortableInterceptor.INACTIVE;

public class AdminController {

    private LayoutController mainController;
    public void setMainController(LayoutController controller){
        this.mainController = controller;
    }

    @FXML
    private GridPane GridPaneID;

    @FXML
    private ChoiceBox<String> AdminStockSelector;

    @FXML
    private TableView<Transaction> AdminBuyTable;

    @FXML
    private TableColumn<Transaction, String> AdminBuySymbolCollum;

    @FXML
    private TableColumn<Transaction, String> AdminBuyCompanyCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminBuyPriceCollum;

    @FXML
    private TableColumn<Transaction, String> AdminBuyActionCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminBuyAmountCollum;

    @FXML
    private TableColumn<Transaction, String> AdminBuyBuyerCollum;

    @FXML
    private TableColumn<Transaction, String> AdminBuySellerCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminBuyCycleCollum;

    @FXML
    private TableColumn<Transaction, String> AdminBuyDateCollum;

    @FXML
    private TableView<Transaction> AdminSellTable;

    @FXML
    private TableColumn<Transaction, String> AdminSellSymbolCollum;

    @FXML
    private TableColumn<Transaction, String> AdminSellCompanyCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminSellPriceCollum;

    @FXML
    private TableColumn<Transaction, String> AdminSellActionCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminSellAmountCollum;

    @FXML
    private TableColumn<Transaction, String> AdminSellBuyerCollum;

    @FXML
    private TableColumn<Transaction, String> AdminSellSellerCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminSellCycleCollum;

    @FXML
    private TableColumn<Transaction, String> AdminSellDateCollum;

    @FXML
    private TableView<Transaction> AdminPreviousTable;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousSymbolCollum;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousCompanyCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminPreviousPriceCollum;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousActionCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminPreviousAmountCollum;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousBuyerCollum;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousSellerCollum;

    @FXML
    private TableColumn<Transaction, Integer> AdminPreviousCycleCollum;

    @FXML
    private TableColumn<Transaction, String> AdminPreviousDateCollum;

    @FXML
    private Label TotalDealsLabel;

    @FXML
    public void initialize(){
        AdminBuySymbolCollum.setCellValueFactory(new PropertyValueFactory<>("Symbol"));
        AdminBuyCompanyCollum.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        AdminBuyPriceCollum.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        AdminBuyActionCollum.setCellValueFactory(new PropertyValueFactory<>("Action"));
        AdminBuyAmountCollum.setCellValueFactory(new PropertyValueFactory<>("NumberOfStocks"));
        AdminBuyBuyerCollum.setCellValueFactory(new PropertyValueFactory<>("Buyer"));
        AdminBuySellerCollum.setCellValueFactory(new PropertyValueFactory<>("Seller"));
        AdminBuyCycleCollum.setCellValueFactory(new PropertyValueFactory<>("Cycle"));
        AdminBuyDateCollum.setCellValueFactory(new PropertyValueFactory<>("Date"));

        AdminSellSymbolCollum.setCellValueFactory(new PropertyValueFactory<>("Symbol"));
        AdminSellCompanyCollum.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        AdminSellPriceCollum.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        AdminSellActionCollum.setCellValueFactory(new PropertyValueFactory<>("Action"));
        AdminSellAmountCollum.setCellValueFactory(new PropertyValueFactory<>("NumberOfStocks"));
        AdminSellBuyerCollum.setCellValueFactory(new PropertyValueFactory<>("Buyer"));
        AdminSellSellerCollum.setCellValueFactory(new PropertyValueFactory<>("Seller"));
        AdminSellCycleCollum.setCellValueFactory(new PropertyValueFactory<>("Cycle"));
        AdminSellDateCollum.setCellValueFactory(new PropertyValueFactory<>("Date"));

        AdminPreviousSymbolCollum.setCellValueFactory(new PropertyValueFactory<>("Symbol"));
        AdminPreviousCompanyCollum.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        AdminPreviousPriceCollum.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        AdminPreviousActionCollum.setCellValueFactory(new PropertyValueFactory<>("Action"));
        AdminPreviousAmountCollum.setCellValueFactory(new PropertyValueFactory<>("NumberOfStocks"));
        AdminPreviousBuyerCollum.setCellValueFactory(new PropertyValueFactory<>("Buyer"));
        AdminPreviousSellerCollum.setCellValueFactory(new PropertyValueFactory<>("Seller"));
        AdminPreviousCycleCollum.setCellValueFactory(new PropertyValueFactory<>("Cycle"));
        AdminPreviousDateCollum.setCellValueFactory(new PropertyValueFactory<>("Date"));

    }
    public void bindStockSelector(ObservableList<String> lst){
        AdminStockSelector.setItems(lst);
        AdminStockSelector.setValue(lst.get(0));
    }

    public void setTableData(){

        if (AdminStockSelector.getItems().size() > 1 &&!AdminStockSelector.getValue().equals("") && !AdminStockSelector.getValue().equals("--SELECT--")) {
            ObservableList<Transaction> buyLst = Main.eng.getObservableBuyList(AdminStockSelector.getValue());
            ObservableList<Transaction> sellLst = Main.eng.getObservableSellList(AdminStockSelector.getValue());
            ObservableList<Transaction> prevLst = Main.eng.getObservablePreviousList(AdminStockSelector.getValue());
            TotalDealsLabel.setText(String.valueOf(prevLst.size()));
            AdminBuyTable.setItems(buyLst);
            AdminSellTable.setItems(sellLst);
            AdminPreviousTable.setItems(prevLst);
        }
        else{
            AdminBuyTable.setItems(clearTable);
            AdminSellTable.setItems(clearTable);
            AdminPreviousTable.setItems(clearTable);
            TotalDealsLabel.setText("0");
        }

    }

    public void backToDefault(){
        TotalDealsLabel.setText("0");
        AdminStockSelector.setValue(Main.eng.getObservableStocks().get(0));
        AdminBuyTable.setItems(clearTable);
        AdminSellTable.setItems(clearTable);
        AdminPreviousTable.setItems(clearTable);
        TotalDealsLabel.setText("0");
    }

    private ObservableList<Transaction> clearTable = FXCollections.observableArrayList();
}

