package org.r.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
public class JDBCHelper {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String DBurl = "jdbc:mysql://localhost:3306/picture";
	private static final String user = "root";
	private static final String password = "root";
	private PreparedStatement pstmt = null;
	private Connection spiderconn = null;
 
	public void insertFilePath(String fileName, String filepath, String url) {
		try {	
		System.out.println(fileName);
		System.out.println(filepath);
		System.out.println(url);
			Class.forName(driver);

			spiderconn = DriverManager.getConnection(DBurl, user, password);
			String sql = "insert into FilePath (filename,filepath,url) values (?,?,?)";
			pstmt = spiderconn.prepareStatement(sql);
			pstmt.setString(1, fileName);
			pstmt.setString(2, filepath);
			pstmt.setString(3, url);
			pstmt.executeUpdate();
 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				spiderconn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
