import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test { 
    protected static int monitorVoorraad;
    protected static Statement stmt = null;
	protected static Connection conn = null;
	protected static ResultSet rs = null;

	public static void main(String[] args) throws NumberFormatException, SQLException { 

		
		try {
		    conn =DriverManager.getConnection("jdbc:mysql://95.85.1.60:3306/opdracht_twee" ,"ANL", "ANL");
				
				    stmt = conn.createStatement();
				    rs = stmt.executeQuery("SELECT * FROM voorraad");
				
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}finally {
		    if (rs != null) {
		    	while (rs.next()) {
		    	    if(rs.getString(2).equals("Monitor")){
		    	    	monitorVoorraad = Integer.parseInt(rs.getString(3));
		    	    }
		    	}
		    }
		}
		 
        // Maak en start thread 1 
        new Thread(new Runnable() { 
            @Override 
            public void run() { 
            	while (true) { 
                    // Schrijf hier je eigen code 
                		try {
                			int situatie = (int) Math.random() * 11; 
                		
                			if(situatie >= 5){
                				int bestelling = (int) (Math.random() * 11);
                				stmt.executeUpdate("UPDATE voorraad SET amount = amount - " + bestelling + " WHERE name = 'Monitor'");
    							monitorVoorraad = monitorVoorraad - bestelling;
    	                        System.out.println("Bestelling van " + bestelling + " producten, totaal nu op " + monitorVoorraad);
                			}else{
                				int levering = (int) (Math.random() * 11); 
                				stmt.executeUpdate("UPDATE voorraad SET amount = amount + " + levering + " WHERE name = 'Monitor'");
    							monitorVoorraad = monitorVoorraad + levering;
    							//System.out.println(Thread.currentThread().getName() + ": Tijd tot volgende levering:" + wachtTijd2 + " sec"); 
    	                        System.out.println("Levering van " + levering + " producten, totaal nu op " + monitorVoorraad);
                			}
         				    // Slaap wachtTijd seconden 
	                        Thread.sleep((int) (Math.random() * 6) * 1000);
	                        //
	                        
	                        
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                } 
            } 
        }, "Thread 1").start(); 
 
        // Maak en start thread 2. Deze draait tegelijkertijd met thread 1 
        new Thread(new Runnable() { 
            @Override 
            public void run() { 
            	while (true) { 
                    // Schrijf hier je eigen code 
                		try {
                			int situatie = (int) Math.random() * 11;                 			
         				    // Slaap wachtTijd seconden 
                			if(situatie >= 5){
                				int bestelling = (int) (Math.random() * 11);
                				stmt.executeUpdate("UPDATE voorraad SET amount = amount - " + bestelling + " WHERE name = 'Monitor'");
    							monitorVoorraad = monitorVoorraad - bestelling;
    	                        System.out.println("Bestelling van " + bestelling + " producten, totaal nu op " + monitorVoorraad);
                			}else{
                				int levering = (int) (Math.random() * 11); 
                				stmt.executeUpdate("UPDATE voorraad SET amount = amount + " + levering + " WHERE name = 'Monitor'");
    							monitorVoorraad = monitorVoorraad + levering;
    							//System.out.println(Thread.currentThread().getName() + ": Tijd tot volgende levering:" + wachtTijd2 + " sec"); 
    	                        System.out.println("Levering van " + levering + " producten, totaal nu op " + monitorVoorraad);
                			}
                			
	                        Thread.sleep((int) (Math.random() * 6) * 1000);
	                        
	                        
	                        
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                } 
            } 
        }, "Thread 2").start(); 
    } 
}