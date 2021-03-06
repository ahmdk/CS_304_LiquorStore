package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import SQL.SQLEmployeeManagement;

public class EmployeeManagement {
    private JButton addNewEmployeeButton;
    private JPanel panelEM;
    private JTextField nameTextField;
    private JTextField usernameTextField;
    private JTextField salaryTextField;
    private JTextField storeIDTextField;
    private JPasswordField passwordField;
    private JRadioButton clerkRadioButton;
    private JRadioButton managerRadioButton;
    private JTextField employeeRemoveTextField;
    private JButton removeEmployeeButton;
    private JTextField employeeChangeSalaryTextField;
    private JTextField newSalaryTextField;
    private JButton changeEmployeeSalaryButton;
    private JLabel storeIdErrorLabel;
    private JLabel removeIdErrorLabel;
    private JLabel idSalaryErrorLabel;
    private JLabel salaryErrorLabel;
    private SQLEmployeeManagement search;


    public EmployeeManagement() {
        search = new SQLEmployeeManagement();
        createButtonListeners();
    }

    private void createButtonListeners(){
        addNewEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeErrorLabels();
                String name = nameTextField.getText();
                String username = usernameTextField.getText();
                char[] password = passwordField.getPassword();
                Double salary = null;
                int storeID;
                String type = null;

                if (!salaryTextField.getText().equals("")){
                    try {
                        salary = Double.parseDouble(salaryTextField.getText());
                    } catch (NumberFormatException nfe){
                        salaryErrorLabel.setVisible(true);
                        return;
                    }
                }

                try {
                    storeID = Integer.parseInt(storeIDTextField.getText());
                } catch (NumberFormatException nfe){
                    storeIdErrorLabel.setVisible(true);
                    return;
                }

                if (clerkRadioButton.isSelected()){
                    type = "Clerk";
                } else if (managerRadioButton.isSelected()){
                    type = "Manager";
                }
                System.out.println("Adding "+name+" "+username+" "+password+" "+salary+" "+storeID+" "+type);

                int employeeID;
                try {
                    employeeID = search.insertNewEmployee(name, username, password, salary, storeID, type);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(null, "New employee with ID "+employeeID+" was added!");
                clearTextFields();
            }
        });

        removeEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeErrorLabels();
                int employeeID;
                try {
                    employeeID = Integer.parseInt(employeeRemoveTextField.getText());
                } catch (NumberFormatException nfe){
                    removeIdErrorLabel.setVisible(true);
                    return;
                }
                try{
                    search.removeEmployee(employeeID);
                } catch (UnsupportedOperationException e1){
                    JOptionPane.showMessageDialog(null, "Employee with ID "+employeeID+" does not exist");
                    return;
                } catch (SQLException e1){
                    JOptionPane.showMessageDialog(null, "Employee could not be removed. Message:"+e1.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(null, "Employee with ID "+employeeID+" was removed!");
                clearTextFields();
            }
        });

        changeEmployeeSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeErrorLabels();
                int employeeID;
                double newSalary;
                try {
                    employeeID = Integer.parseInt(employeeChangeSalaryTextField.getText());
                    newSalary = Double.parseDouble(newSalaryTextField.getText());
                } catch (NumberFormatException nfe){
                    idSalaryErrorLabel.setVisible(true);
                    return;
                }
                try {
                    search.changeSalary(employeeID, newSalary);
                } catch (UnsupportedOperationException e1){
                    JOptionPane.showMessageDialog(null, "Employee with ID "+employeeID+" does not exist");
                    return;
                }
                JOptionPane.showMessageDialog(null, "Employee's salary was changed!");
                clearTextFields();
            }
        });
    }

    private void removeErrorLabels(){
        storeIdErrorLabel.setVisible(false);
        removeIdErrorLabel.setVisible(false);
        idSalaryErrorLabel.setVisible(false);
        salaryErrorLabel.setVisible(false);
    }

    private void clearTextFields(){
        nameTextField.setText("");
        usernameTextField.setText("");
        passwordField.setText("");
        salaryTextField.setText("");
        storeIDTextField.setText("");
        employeeRemoveTextField.setText("");
        employeeChangeSalaryTextField.setText("");
        newSalaryTextField.setText("");
    }

    public JPanel getPanelEM() {
        return panelEM;
    }
}
