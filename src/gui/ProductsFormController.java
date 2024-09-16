package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Products;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.ProductsService;
import model.services.SellerService;

public class ProductsFormController implements Initializable {

	private Products entity;

	private ProductsService service;

	private SellerService sellerService;

	private List<DataChangeListener> dataChengeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtCategory;

	@FXML
	private DatePicker dpReleaseDate;

	@FXML
	private TextField txtPrice;

	@FXML
	private ComboBox<Seller> comboBoxSeller;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorCategory;

	@FXML
	private Label labelErrorReleaseDate;

	@FXML
	private Label labelErrorPrice;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Seller> obsList;

	public void setProducts(Products entity) {
		this.entity = entity;
	}

	public void setServices(ProductsService service, SellerService sellerService) {
		this.service = service;
		this.sellerService = sellerService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChengeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Servico nulo");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChengeListeners) {
			listener.onDataChanged();
		}
	}

	private Products getFormData() {
		Products obj = new Products();

		ValidationException exception = new ValidationException("Erro de validação");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O campo não pode estar vazio");
		}
		obj.setName(txtName.getText());

		if (txtCategory.getText() == null || txtCategory.getText().trim().equals("")) {
			exception.addError("category", "O campo não pode estar vazio");
		}
		obj.setCategory(txtCategory.getText());
		
		if (dpReleaseDate.getValue() == null) {
			exception.addError("releaseDate", "O campo não pode estar vazio");
		} else {
			Instant instant = Instant.from(dpReleaseDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setReleaseDate(Date.from(instant));
		}
			
		if (txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("price", "O campo não pode estar vazio");
		}
		obj.setPrice(Utils.tryParseToDouble(txtPrice.getText()));
		
		obj.setSeller(comboBoxSeller.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtCategory, 30);
		Utils.formatDatePicker(dpReleaseDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtPrice);
		
		
		initializeComboBoxSeller();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtCategory.setText(entity.getCategory());
		if (entity.getReleaseDate() != null) {
			dpReleaseDate.setValue(LocalDate.ofInstant(entity.getReleaseDate().toInstant(), ZoneId.systemDefault()));
		}
		Locale.setDefault(Locale.US);
		txtPrice.setText(String.format("%.2f", entity.getPrice()));
		
		if (entity.getSeller() == null) { 
			comboBoxSeller.getSelectionModel().selectFirst();
		} else {
			comboBoxSeller.setValue(entity.getSeller());
		}
	}

	public void loadAssociatedObjects() {
		if (sellerService == null) {
			throw new IllegalStateException("Colaborador estava nulo");
		}
		List<Seller> list = sellerService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxSeller.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
	
		labelErrorCategory.setText(fields.contains("category") ? errors.get("category") : "");
		
		labelErrorReleaseDate.setText(fields.contains("releaseDate") ? errors.get("releaseDate") : "");	

		labelErrorPrice.setText(fields.contains("price") ? errors.get("price") : "");
	
	
	}

	private void initializeComboBoxSeller() {
		Callback<ListView<Seller>, ListCell<Seller>> factory = lv -> new ListCell<Seller>() {
			@Override
			protected void updateItem(Seller item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxSeller.setCellFactory(factory);
		comboBoxSeller.setButtonCell(factory.call(null));
	}
}
