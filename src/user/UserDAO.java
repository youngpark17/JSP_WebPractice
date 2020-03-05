package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL="jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword="1234";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public int login(String userID, String userPassword) {
		String SQL="select userPassword from user where userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs  = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //�α��� ����
				}
				else {
					return 0; // ��й�ȣ Ʋ��
				}
			}
			return -1; //���̵� ����
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -2; //������ ���̽� ����
		
	}
}
