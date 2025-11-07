package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private UserDAOImpl1 dao = new UserDAOImpl1();
	private LoginInfo login = null;
	
	public UserUI(LoginInfo login) {
		this.login = login;
	}
	
	public void menu() {
	
		int ch = 0;
		
		while(true) {
			System.out.println("\n[사용자 화면]");
			
			try {
				System.out.print("1.도서검색 2.대출 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 6) {
					login.logout();
					System.out.println("로그아웃 되었습니다.");
					
					return;
				}
					switch(ch) {
					case 1 : findBybook(); break;
					case 2 : loan(); break;
					case 3 : returnbook(); break;
					case 4 : sincheong(); break;
					case 5 : mypage(); break;					
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
	}
	}
	
	
	// 1. 도서 검색[제목], [저자] / (도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력)
	protected void findBybook() {
		System.out.println("\n[도서검색]");
		
		String bookname;
		String authorname;
		
		try {
			System.out.print("도서 제목 ? ");
			bookname = br.readLine();
			authorname = br.readLine();
			
			List<BookInfoDTO1> list = dao.listBook(bookname, authorname);
			
			if(list.size() == 0) {
				System.out.println("우리 도서관에 등록된 도서가 아닙니다.");
				return;
			}
			for(BookInfoDTO1 dto : list) {
				System.out.print(dto.getBook_code() + "\t");
				System.out.print(dto.getIsbn() + "\t");
				System.out.print(dto.getBookName() + "\t");
				System.out.print(dto.getAuthor_name() + "\t");
				System.out.print(dto.getPublisher_name() + "\t");
				System.out.println(dto.getPublish_date());
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
	// 2. 대출신청/연장
	protected void loan () {

		int ch = 0;
		
		while(true) {
			System.out.println("\n[대출]");
			
			try {
				System.out.print("1.대출신청 2.대출연장");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 1) {
					System.out.println("대출이 신청되었습니다.");
					return;
				} else {
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	// 3. 반납신청
	protected void returnbook() {
		System.out.println("\n[반납신청]");
		
	}
	// 4. 도서신청
	protected void sincheong() {
		System.out.println("\n[도서신청]");
		
	}
	// 5. 마이페이지
	protected void mypage() {
		System.out.println("\n[마이페이지]");
		
	}
}
