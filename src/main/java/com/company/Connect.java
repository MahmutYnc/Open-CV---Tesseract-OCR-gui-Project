package main.java.com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;


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
                        " | "+ rs.getString(4) +" | "+ rs.getString(5));
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
                        rs.getString(4), rs.getString(5));
                mList.add(urun);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    int fisID = 0, tfis = 0;

    public void checkDB (String companyName, java.util.Date date, int billNo, String product, String totalPrice) throws SQLException {
        int companyID = 0;
        int billsID = 0, billtable = 0;

        Connection connection = getConnection();

        String query = "Select ID From company where company.companyName = " + "'" + companyName+ "'";
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

            companyID += 1;

            pst.setInt(1, companyID);
            pst.setString(2, companyName);
            pst.executeUpdate();

            pst.close();

            //fiş tablosna ekleme yap
            query = "INSERT INTO bills (ID, date, billNo, Products, totalPrice) VALUES (?, ?, ?, ?, ?)";
            pst =connection.prepareStatement(query);

            billsID = billsID + 1;

            pst.setInt(1, billsID);
            pst.setDate(2, (Date) date);
            pst.setInt(3, billNo);
            pst.setString(4, product);
            pst.setString(5, totalPrice);

            pst.executeUpdate();
            pst.close();

            //ekelenenlere göre billtable a ekeleme yapma
            query = "INSERT INTO billtable (ID, companyID, billsID) values (?, ?, ?)";
            pst = connection.prepareStatement(query);

            billtable += 1;

            pst.setInt(1, billtable);
            pst.setInt(2, companyID);
            pst.setInt(3, billsID);

            pst.executeUpdate();
            pst.close();

        }else {
            //şirket kayıtlı değilse
            //company id cek

            Connection con = getConnection();

            //fişin idsini
            query = "SELECT ID FROM `bills` WHERE 1";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                fisID = rs.getInt(1);
            }
            fisID++;
            System.out.println("buradayızzzz");

            statement.close();
            rs.close();






            PreparedStatement pst;

            //fiş tablosna ekleme yap
            query = "INSERT INTO bills (ID, date, billNo, Products, totalPrice) VALUES (?, ?, ?, ?, ?)";
            pst =connection.prepareStatement(query);

            pst.setInt(1, fisID);
            pst.setDate(2, (Date) date);
            pst.setInt(3, billNo);
            pst.setString(4, product);
            pst.setString(5, totalPrice);

            pst.executeUpdate();
            pst.close();


            query = "SELECT MAX(id) FROM billtable" ;
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                tfis = rs.getInt(1);
            }
            tfis += 1;
            statement.close();
            rs.close();

            System.out.println("*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-* \n" +
                    tfis + "     " + fisID);

            //ekelenenlere göre billtable a ekeleme yapma
            query = "INSERT INTO billtable (ID, companyID, billsID) values (?, ?, ?)";
            pst = connection.prepareStatement(query);



            pst.setInt(1, tfis);
            pst.setInt(2, companyID);
            pst.setInt(3, fisID);

            pst.executeUpdate();
            pst.close();


        }
        connection.close();

        //bundan önce tesseractin texti almış olması lazım yani parse işlemlerinen sonra çlışaacak Main classının içindeki

        //bundan sonra show on table çalışması lazım


    }

    public ArrayList<ForTable> searchBtn(String cname, Date date){
        ArrayList<ForTable> searcList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            String query = "SELECT company.companyName, bills.billNo, bills.date, bills.Products, bills.totalPrice FROM bills, company, billtable " +
                    "WHERE billtable.billsID = bills.ID AND billtable.companyID = company.ID AND bills.date = "+"'"+ date +"'" + " AND company.companyName = " +"'" +cname+ "'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            ForTable urun;
            while (rs.next()) {
                urun = new ForTable(rs.getString(1), rs.getInt(2), rs.getDate(3),
                        rs.getString(4), rs.getString(5));
                searcList.add(urun);
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return searcList;
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
    int billNo;
    String totalPrice;
    private Date date;
    private String products;

    public ForTable(String companyName, int billNo,  Date date, String products, String totalPrice) {
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
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
