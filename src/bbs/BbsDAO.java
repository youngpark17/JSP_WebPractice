package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {
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
	
	public String getDate() { //������ ���� �ð� �־���.
		String SQL ="select now()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "";
	}
	
	public int getNext() {
		String SQL ="select bbsID from bbs order by bbsID desc";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next()) { //���� ������ Ŀ�� �̵�. bbs���̵� ������������ �����ϱ� ū ������� ����. ���� ���� �ִٸ�. 
				return rs.getInt(1)+1;
			}
			return 1; // ���簡 ù��° �Խù�
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return -1; //�����ͺ��̽� ����
	}
	
	public int write(String bbsTitle, String userID,String bbsContent) { 
		String SQL ="insert into bbs values (?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return -1; //�����ͺ��̽� ����
	}
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL ="select * from bbs where bbsID<? AND bbsAvailable = 1 order by bbsID desc limit 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setInt(1, getNext()-(pageNumber-1)*10);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return list; //�����ͺ��̽� ����
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL ="select * from bbs where bbsID<? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber-1)*10);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return false; 
	}
}
