package main.java.com.company;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connect {

    public static Connection c;

    public void connectDatabase() {
        try {
            String query;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bills");
            System.out.println("ID  -ŞirketAdı      -Tarih     -Fiş No    -Ürünler          -Toplam Fiyat");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " | " +
                        rs.getString(4) + " | " + rs.getInt(5) + " | " + rs.getString(6) + " | " +
                        rs.getInt(7) + " | " + rs.getInt(8));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
