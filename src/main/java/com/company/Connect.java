package main.java.com.company;


import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class Connect {

    public GuiClass g;

    public static Connection c;

    public void connectDatabase() {
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/billsdb?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey",
                    "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String query;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM billsdb.bills ");
            System.out.println("ID     -Tarih         -Ürünler          -Toplam Fiyat");

            //ürünler tablosunu çıktıla
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getDate(2) + " | "+ rs.getInt(3) +
                        " | "+ rs.getString(4) +" | "+ rs.getInt(5));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public Connection getConnection()
    {
        Connection con;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/billsdb?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey",
                    "root", "");
            return con;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        public ArrayList<ForTable> urunArrayList() {
        ArrayList<ForTable> mList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/billsdb?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey",
                    "root", "");
            String query1 = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice from bills, company, billtable where" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY bills.billNo";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query1);
            ForTable urun;
            while (rs.next()) {
                urun = new ForTable(rs.getString(1), rs.getInt(2), rs.getDate(3),
                        rs.getString(4), rs.getInt(5));
                mList.add(urun);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }
    public void showOnTable() {
        ArrayList<ForTable> tableList = urunArrayList();
        DefaultTableModel tableModel = (DefaultTableModel) g.table.getModel();

        Object[] row = new Object[5];
        for (int i = 0; i < tableList.size(); i++) {
            row[0] = tableList.get(i).getCompanyName();
            row[1] = tableList.get(i).getDate();
            row[2] = tableList.get(i).getBillNo();
            row[3] = tableList.get(i).getProducts();
            row[4] = tableList.get(i).getTotalPrice();
            tableModel.addRow(row);
        }

    }


}


class Urun {
    private int id, billNo, totalPrice;
    private Date date;
    private String products;

    public Urun(int id, Date date, int billNo, String products, int totalPrice) {
        this.id = id;
        this.billNo = billNo;
        this.date = date;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}

class Company {
    private int id;
    private String companyName;

    public Company(int id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

class ForTable {
    private String companyName;
    int billNo, totalPrice;
    private Date date;
    private String products;

    public ForTable(String companyName, int billNo,  Date date, String products, int totalPrice) {
        this.companyName = companyName;
        this.billNo = billNo;
        this.totalPrice = totalPrice;
        this.date = date;
        this.products = products;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
