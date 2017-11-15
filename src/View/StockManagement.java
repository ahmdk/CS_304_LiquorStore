package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import SQL.SQLStockManagement;

public class StockManagement {
    private JTextField nameTextField;
    private JPanel panelSM;
    private JTextField taxTextField;
    private JTextField depositTextField;
    private JTextField priceTextFieldTextField;
    private JTextField descriptionTextField;
    private JButton addNewItemButton;
    private JTextField storeIDTextField;
    private JTextField alcoholPercentageTextField;
    private JTextField packQuantityTextField;
    private JTextField typeTextField;
    private JTextField regionTextField;
    private JTextField companyTextField;
    private JTextField subtypeTextField;
    private JRadioButton beerRadioButton;
    private JRadioButton wineRadioButton;
    private JTextField removeItemTextField;
    private JButton removeItemButton;
    private JTextField quantityTextField;
    private JTextField updateItemTextField;
    private JTextField newDescriptionTextField;
    private JButton updateDescriptionButton;
    private JTextField newQuantityTextField;
    private JLabel numberErrorLabel;
    private JButton updateQuantityButton;
    private JTextField storeIdUpdateTextField;
    private JLabel itemRemoveErrorLabel;
    private SQLStockManagement sqlStockManagement;

    public StockManagement() {
        sqlStockManagement = new SQLStockManagement();
        createListeners();
    }

    private void createListeners(){
        addNewItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeErrorLabels();
                String name = nameTextField.getText();
                String description = descriptionTextField.getText();
                Integer storeID;
                String type = typeTextField.getText();
                String region = regionTextField.getText();
                String company = companyTextField.getText();
                Double tax;
                Double deposit;
                Double price;
                Double percentage;
                Integer quantity;
                Integer packQuantity;

                try {
                    storeID = Integer.parseInt(storeIDTextField.getText());
                } catch (NumberFormatException nfe){
                    numberErrorLabel.setVisible(true);
                    return;
                }

                try {
                    tax = getTextFieldForDouble(taxTextField);
                    deposit = getTextFieldForDouble(depositTextField);
                    price = getTextFieldForDouble(priceTextFieldTextField);
                    percentage = getTextFieldForDouble(alcoholPercentageTextField);
                    quantity = null;
                    packQuantity = null;
                    if (getTextFieldForDouble(quantityTextField) != null)
                        quantity = getTextFieldForDouble(quantityTextField).intValue();
                    if (getTextFieldForDouble(packQuantityTextField) != null)
                        packQuantity = getTextFieldForDouble(packQuantityTextField).intValue();
                } catch (UnsupportedOperationException uoe){
                    return;
                }

                System.out.println(name+" "+tax+" "+deposit+" "+price+" "+description+" "+storeID+" "+percentage+" "+type+" "+region+" "+company+" "+quantity);

                if (beerRadioButton.isSelected()){
                    //TODO insert beer
                    //insertBeer(name, tax, deposit, price, description, storeID, percentage, type, region, company, packQuantity);
                    //TODO check if everything was fine then
                    JOptionPane.showMessageDialog(null, "New beer was added!");
                } else if (wineRadioButton.isSelected()){
                    String subtype = subtypeTextField.getText();
                    //TODO insert wine
                    //insertWine(name, tax, deposit, price, description, storeID, percentage, type, region, company, subtype); //Don't forget to insert into Item table too
                    //TODO check if everything was fine then
                    JOptionPane.showMessageDialog(null, "New wine was added!");
                } else {
                    //TODO insert nonalcoholic
                    //insertNonAlcoholicItem(name, tax, deposit, price, description, storeID);
                    //TODO check if everything was fine then
                    JOptionPane.showMessageDialog(null, "New item was added!");
                }
            }
        });


        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeErrorLabels();
                int sku;
                try {
                    sku = Integer.parseInt(removeItemTextField.getText());
                } catch (NumberFormatException nfe){
                    itemRemoveErrorLabel.setVisible(true);
                    return;
                }
                try{
                    sqlStockManagement.removeItem(sku);
                } catch (UnsupportedOperationException e1){
                    JOptionPane.showMessageDialog(null, "Item with sku "+sku+" does not exist");
                    return;
                } catch (SQLException e1){
                    JOptionPane.showMessageDialog(null, "Could not delete item "+sku+". Message: "+e1.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(null, "Item, and all rows referencing it, was removed!");
            }
        });


        updateDescriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private Double getTextFieldForDouble(JTextField textField){
        Double value = null;
        if (!textField.getText().equals("")){
            try {
                value = Double.parseDouble(textField.getText());
            } catch (NumberFormatException nfe){
                numberErrorLabel.setVisible(true);
                throw new UnsupportedOperationException();
            }
        }
        return value;
    }

    private void removeErrorLabels(){
        itemRemoveErrorLabel.setVisible(false);
        numberErrorLabel.setVisible(false);
//        idSalaryErrorLabel.setVisible(false);
//        salaryErrorLabel.setVisible(false);
    }

    public JPanel getPanelSM() {
        return panelSM;
    }
}
