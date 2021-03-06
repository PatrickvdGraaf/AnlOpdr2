/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nick
 */
public class unrep_test {

    protected static int monitorVoorraad;
    protected static Statement stmt = null;
    protected static Connection conn = null;
    protected static ResultSet rs = null;
    protected static int current;
    protected static int repeat = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            conn = DriverManager.getConnection("jdbc:mysql://95.85.1.60:3306/opdracht_twee", "ANL", "ANL");
            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM voorraad");

            while (rs.next()) {
                if (rs.getString(2).equals("Monitor")) {
                    monitorVoorraad = Integer.parseInt(rs.getString(3));
                }
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (repeat > 0) {
                    // Schrijf hier je eigen code 
                    try {
                        Thread.sleep(2000);
                        rs = stmt.executeQuery("SELECT * FROM voorraad");
                        while (rs.next()) {
                            if (rs.getString(2).equals("Monitor")) {
                                monitorVoorraad = Integer.parseInt(rs.getString(3));
                            }
                        }
                        System.out.println("A1)Totaal nu op " + monitorVoorraad);

                        // Slaap wachtTijd seconden 
                        Thread.sleep(2000);
                        rs = stmt.executeQuery("SELECT * FROM voorraad");
                        while (rs.next()) {
                            if (rs.getString(2).equals("Monitor")) {
                                monitorVoorraad = Integer.parseInt(rs.getString(3));
                            }
                        }
                        System.out.println("A2)Totaal nu op " + monitorVoorraad);
                        
                        Thread.sleep(3000);
                        conn.commit();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                repeat = 0;
                }
            }
        }, "Thread 1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (repeat > 0) {

                    // Schrijf hier je eigen code 
                    try {
                        Thread.sleep(3000);
                        int bestelling = 25;
                        rs = stmt.executeQuery("SELECT * FROM voorraad");
                        while (rs.next()) {
                            if (rs.getString(2).equals("Monitor")) {
                                current = Integer.parseInt(rs.getString(3));
                            }
                        }
                        stmt.executeUpdate("UPDATE voorraad SET amount = " + (current + bestelling) + " WHERE name = 'Monitor'");
                        rs = stmt.executeQuery("SELECT * FROM voorraad");
                        while (rs.next()) {
                            if (rs.getString(2).equals("Monitor")) {
                                current = Integer.parseInt(rs.getString(3));
                            }
                        }
                        System.out.println("B1)Totaal nu op " + (current));
                        Thread.sleep(4000);
                        conn.commit();
                        rs = stmt.executeQuery("SELECT * FROM voorraad");
                        while (rs.next()) {
                            if (rs.getString(2).equals("Monitor")) {
                                current = Integer.parseInt(rs.getString(3));
                            }
                        }
                        System.out.println("B2)Totaal nu op " + current);
                        
                        
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                repeat = 0;
                }
            }
        }, "Thread 2").start();
    }
}
