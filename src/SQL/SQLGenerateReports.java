package SQL;

import javax.swing.*;
import java.sql.*;

public class SQLGenerateReports {
    private final Connection con;

    public SQLGenerateReports(){
        con = DatabaseConnection.getConnection();
    }

    private void addReport(ResultSet rs, int storeId, Date startDate, Date endDate, String type){
        PreparedStatement ps;
        try{
            while(rs.next()){
                ps = con.prepareStatement("INSERT INTO REPORTS VALUES (SEQ_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)");
                ps.setDate(1, startDate);
                ps.setDate(2, endDate);
                ps.setInt(3, storeId);
                if (type == "sales report"){
                    ps.setDouble(4, rs.getDouble(2));
                    ps.setDouble(5, 0);
                    ps.setDouble(6, 0);
                } else if (type == "orders report"){
                    ps.setDouble(4, 0);
                    ps.setDouble(5, rs.getDouble(2));
                    ps.setDouble(6, 0);
                } else if (type == "wages report"){
                    ps.setDouble(4, 0);
                    ps.setDouble(5, 0);
                    ps.setDouble(6, rs.getDouble(2));
                }
                ps.executeUpdate();
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update reports table");
            try {
                con.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public void generateWagesReport(int storeId, Date startDate, Date endDate) {
        long diff = Math.abs(endDate.getTime() - startDate.getTime());
        long numberOfDaysWorked = diff / (24 * 60 * 60 * 1000);
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = con.prepareStatement("SELECT E.STORE_ID, SUM(E.SALARY * ?) " +
                                            "FROM EMPLOYEES E " +
                                            "WHERE E.STORE_ID = ? " +
                                            "GROUP BY E.STORE_ID");
            ps.setLong(1, numberOfDaysWorked);
            ps.setInt(2, storeId);
            rs = ps.executeQuery();
            addReport(rs, storeId, startDate, endDate, "wages report");
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to obtain list of salaries to include in report");
            e.printStackTrace();
            return;
        }
    }

    public void generateOrdersReport(int storeId, Date startDate, Date endDate){
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps = con.prepareStatement("SELECT E.STORE_ID, " +
                                            "SUM(OI.QUANTITY * I.PRICE) AS \"ORDER TOTAL\" " +   // TODO revisit price calculation
                                            "FROM ORDERITEMS OI, ORDERS O, EMPLOYEES E, ITEMS I " +
                                            "WHERE E.STORE_ID = ? AND " +
                                            "O.EMPLOYEE_ID = E.EMPLOYEE_ID AND " +
                                            "O.ORDER_NUMBER = OI.ORDER_NUMBER AND " +
                                            "I.SKU = OI.SKU AND " +
                                            "O.TIME_DATE_PLACED >= ? AND " +
                                            "O.TIME_DATE_RECEIVED IS NOT NULL " +
                                            "GROUP BY E.STORE_ID");
            ps.setInt(1, storeId);
            ps.setDate(2, startDate);
            rs = ps.executeQuery();
            addReport(rs, storeId, startDate, endDate, "orders report");
            ps.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to obtain list of orders to include in report");
            e.printStackTrace();
            return;
        }
    }

    public void generateSalesReport(int storeId, Date startDate, Date endDate){
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps = con.prepareStatement("SELECT E.STORE_ID, " +
                                            "SUM(S.TOTAL_PRICE) AS \"SALES TOTAL\" " +
                                            "FROM STORE_SALES S, EMPLOYEES E " +
                                            "WHERE E.STORE_ID = ? AND " +
                                            "S.EMPLOYEE_ID = E.EMPLOYEE_ID AND " +
                                            "S.SALE_DATE >= ? " +
                                            "GROUP BY E.STORE_ID");
            ps.setInt(1, storeId);
            ps.setDate(2, startDate);
            rs = ps.executeQuery();
            addReport(rs, storeId, startDate, endDate, "sales report");
            ps.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to obtain list of sales to include in report");
            e.printStackTrace();
            return;
        }
    }

  /*  public ResultSet viewWagesReport(int storeId, Date startDate, Date endDate){
        ResultSet result;

        return result;
    }

    public ResultSet viewOrdersReport(int storeId, Date startDate, Date endDate){
        ResultSet result;

        return result;
    }

    public ResultSet viewSalesReport(int storeId, Date startDate, Date endDate){
        ResultSet result;

        return result;
    }
    */

}