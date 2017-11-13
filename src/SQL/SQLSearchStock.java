package SQL;

import java.sql.*;
import java.util.Vector;

public class SQLSearchStock {
    private static final String ALL_SUBTYPES = "All Types";
    private static final String ALL_STORES = "All Stores";
    private final Connection con;

    public SQLSearchStock() {
        con = DatabaseConnection.getConnection();
    }

    public ResultSet selectBySKU(int sku, boolean tax, String storeName) throws SQLException {
        PreparedStatement ps;
        if (tax) {
            ps = con.prepareStatement(
                    "SELECT i.sku, s.name as storeName, i.name, i.price + i.price * i.tax + i.deposit as price, i.description, si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si "
                            + "WHERE i.sku = si.sku AND s.store_id = si.store_id AND i.sku = ? AND s.name LIKE ? ");
        } else {
            ps = con.prepareStatement(
                    "SELECT i.sku, s.name as storeName, i.name, i.price, i.description, si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si "
                            + "WHERE i.sku = si.sku AND s.store_id = si.store_id AND i.sku = ? AND s.name LIKE ? ");
        }
        ps.setInt(1, sku);
        setStoreVariable(ps, storeName);
        return ps.executeQuery();
    }

    public ResultSet selectByName(String name, boolean tax, String storeName) throws SQLException {
        PreparedStatement ps;
        if (tax) {
            ps = con.prepareStatement(
                    "SELECT i.sku, s.name as storeName, i.name, i.price + i.price * i.tax + i.deposit as price, i.description, si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si "
                            + "WHERE i.sku = si.sku AND s.store_id = si.store_id AND UPPER(i.name) LIKE ? AND s.name LIKE ? ");
        } else {
            ps = con.prepareStatement(
                    "SELECT i.sku, s.name as storeName, i.name, i.price, i.description, si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si "
                            + "WHERE i.sku = si.sku AND s.store_id = si.store_id AND UPPER(i.name) LIKE ? AND s.name LIKE ? ");
        }

        ps.setString(1, "%" + name.toUpperCase() + "%");
        setStoreVariable(ps, storeName);
        return ps.executeQuery();
    }

    public ResultSet selectByType(String subType, String type, String storeName, boolean tax) throws SQLException{
        PreparedStatement ps;
        if (tax) {
            ps = con.prepareStatement(
                    "SELECT t.sku, s.name as storeName, t.company, i.name, t.type, " +
                            "i.price + i.price * i.tax + i.deposit as price,si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si, " + type + " t "
                            + "WHERE t.sku = si.sku AND i.sku = t.sku AND s.store_id = si.store_id AND " +
                            "t.type LIKE ? AND s.name LIKE ? ");
        } else {
            ps = con.prepareStatement(
                    "SELECT t.sku, s.name as storeName, t.company, i.name, t.type, i.price, si.stock_quantity "
                            + "FROM ITEMS i, STORES s, STOREITEMS si, " + type + " t "
                            + "WHERE t.sku = si.sku AND i.sku = t.sku AND s.store_id = si.store_id AND " +
                            "t.type LIKE ? AND s.name LIKE ? ");
        }

        setSubTypeVariable(ps, subType);
        setStoreVariable(ps, storeName);
        return ps.executeQuery();
    }

    public Vector<String> getStoreNames() throws SQLException {
        Vector<String> storeNames = new Vector<String>();
        storeNames.add(ALL_STORES);
        PreparedStatement ps = con.prepareStatement("SELECT s.name " + "FROM STORES s ");

        ResultSet results = ps.executeQuery();
        while (results.next()) {
            storeNames.add(results.getString(1));
        }

        return storeNames;
    }

    public Vector<String> getSubTypeNames(String type) throws SQLException {
        Vector<String> subTypeNames = new Vector<>();
        subTypeNames.add(ALL_SUBTYPES);
        PreparedStatement ps = con.prepareStatement("SELECT DISTINCT type " + "FROM " + type);

        ResultSet results = ps.executeQuery();
        while (results.next()) {
            subTypeNames.add(results.getString(1));
        }

        return subTypeNames;
    }

    public void setSubTypeVariable(PreparedStatement ps, String subType) throws SQLException {
        if (subType.equals(ALL_SUBTYPES)) {
            ps.setString(1, "%");
        } else {
            ps.setString(1, subType);
        }
    }

    public void setStoreVariable(PreparedStatement ps, String storeName) throws SQLException {
        if (storeName.equals(ALL_STORES)) {
            ps.setString(2,  "%");
        } else {
            ps.setString(2, storeName);
        }
    }
}