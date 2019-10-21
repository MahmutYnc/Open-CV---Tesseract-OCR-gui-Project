package main.java.com.company;

import java.sql.*;
import java.util.ArrayList;

public class Connect {


    public static Connection c;

    public void connectDatabase() {
        c = getConnection();
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

        public ArrayList<ForTable> urunArrayList(int index) {
        ArrayList<ForTable> mList = new ArrayList<>();
        try {
            Connection con = getConnection();
            String queryDef = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice from bills, company, billtable where" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY bills.billNo";
            String queryCom = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice from bills, company, billtable where" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY company.companyName";
            String queryDate = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice FROM bills, company, billtable WHERE" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY `bills`.`date` ASC";
            String queryProd = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice FROM bills, company, billtable WHERE" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY `bills`.`Products` ASC";
            String queryTot = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice FROM bills, company, billtable WHERE" +
                    " billtable.billsID = bills.ID AND billtable.companyID = company.ID ORDER BY `bills`.`totalPrice` ASC";

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(queryDef);
            switch (index) {
                case 0:
                    rs = statement.executeQuery(queryCom);
                    break;

                case 1:
                    rs = statement.executeQuery(queryDef);
                    break;

                case 2:
                    rs = statement.executeQuery(queryDate);
                    break;

                case 3:
                    rs = statement.executeQuery(queryProd);
                    break;

                case 4:
                    rs = statement.executeQuery(queryTot);
                    break;
                default:
                    rs = statement.executeQuery(queryDef);
            }

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
