package main.java.com.company;

import java.sql.*;
import java.util.ArrayList;

public class Connect {

    ArrayList compName = new ArrayList();
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

    public ArrayList getCompName() {

        c = getConnection();
        try {
            Statement statement = c.createStatement();
            String query = "Select * from company where 1";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                String name = rs.getString("companyName");

                compName.add(name);
            }

            rs.close();
            statement.close();
            c.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compName;
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
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    public void checkDB (String companyName, String date, int billNo, String product, int totalPrice) throws SQLException {
        int companyID = 0;
        int billsID = 0, billtable = 0;

        Connection connection = getConnection();

        String query = "Select ID From company where companyName = " + companyName;
        Statement statement = connection.createStatement();
        ResultSet rs = null;
            rs = statement.executeQuery(query);

        while (rs.next()) {
            companyID = rs.getInt(1);
        }

        statement.close();
        rs.close();



        if (companyID == 0) {

            query = "SELECT `ID` FROM `company` WHERE 1";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                companyID = rs.getInt(1);
            }

            statement.close();
            rs.close();

            query = "SELECT `ID` FROM `bills` WHERE 1";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                billsID = rs.getInt(1);
            }

            statement.close();
            rs.close();

            query = "SELECT `ID` FROM `billtable` WHERE 1";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                billtable = rs.getInt(1);
            }

            statement.close();
            rs.close();

            //şirkettablosuna ekleme yapıldı
            query = "INSERT INTO `company` (`ID`, `companyName`) VALUES (?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            companyID += 3;

            pst.setInt(1, companyID);
            pst.setString(2, companyName);
            pst.executeUpdate();

            pst.close();

            //fiş tablosna ekleme yap
            query = "INSERT INTO bills (ID, date, billNo, Products, totalPrice) VALUES (?, ?)";
            pst =connection.prepareStatement(query);

            billsID += 3;

            pst.setInt(1, billsID);
            pst.setDate(2, Date.valueOf(date));
            pst.setInt(3, billNo);
            pst.setString(4, product);
            pst.setInt(5, totalPrice);

            pst.executeUpdate();
            pst.close();

            //ekelenenlere göre billtable a ekeleme yapma
            query = "INSERT INTO billtable (ID, companyID, billsID) values (?, ?)";
            pst = connection.prepareStatement(query);

            billtable += 3;

            pst.setInt(1, billtable);
            pst.setInt(2, companyID);
            pst.setInt(3, billsID);

            pst.executeUpdate();
            pst.close();

        }else {

            query = "SELECT `billsID` FROM `billtable` WHERE companyID = " + companyID;
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                billsID = rs.getInt(1);
            }

            statement.close();
            rs.close();


            PreparedStatement pst;

            //fiş tablosna ekleme yap
            query = "INSERT INTO bills (ID, date, billNo, Products, totalPrice) VALUES (?, ?)";
            pst =connection.prepareStatement(query);

            billsID += 3;

            pst.setInt(1, billsID);
            pst.setDate(2, Date.valueOf(date));
            pst.setInt(3, billNo);
            pst.setString(4, product);
            pst.setInt(5, totalPrice);

            pst.executeUpdate();
            pst.close();

            //ekelenenlere göre billtable a ekeleme yapma
            query = "INSERT INTO billtable (ID, companyID, billsID) values (?, ?)";
            pst = connection.prepareStatement(query);

            billtable += 3;

            pst.setInt(1, billtable);
            pst.setInt(2, companyID);
            pst.setInt(3, billsID);

            pst.executeUpdate();
            pst.close();

        }

        //bundan önce tesseractin texti almış olması lazım yani parse işlemlerinen sonra çlışaacak Main classının içindeki

        //bundan sonra show on table çalışması lazım


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
