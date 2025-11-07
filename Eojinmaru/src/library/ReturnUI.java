package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DBUtil.DBConn;

public class ReturnUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Connection conn = DBConn.getConnection();

	private LoginInfo login = null;
	private UserUI userUI;

	

	// UI
	public ReturnUI(LoginInfo login) {
		this.login = login;
		userUI = new UserUI(login);
	}

	public void start() {
		System.out.println("/n[도서반납]");
		String bookcode;
		
		
		try {
			System.out.println("반납할 책번호를 입력하세요.");
			bookcode=br.readLine();
			
			
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// DAO
	/*
	public void returnbook(loan_code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
	try {
		
		conn.setAutoCommit(false);
		
		sql ="DELETE FROM loan WHERE loan_code=? ";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1,loan_code);
	} catch (Exception e) {
		
	}
	}
	*/
}
