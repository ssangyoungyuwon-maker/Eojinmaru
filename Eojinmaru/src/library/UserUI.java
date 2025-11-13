package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class UserUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private BookDAOImpl dao = new BookDAOImpl();

	private LoginInfo login = null;
	private ReturnUI returnUI = null;
	private MypageUI mypageUI = null;

	public UserUI(LoginInfo login) {
		this.login = login;

		returnUI = new ReturnUI(login);
		mypageUI = new MypageUI(login);
	}

	public void menu() {
		
		int ch = 0;

		while (true) {
			System.out.println("\n[사용자 화면]");

			try {
				System.out.print("1.도서검색/대출신청 2.대출 3.반납신청 4.도서신청 5.마이페이지 6.로그아웃  =>  ");
				ch = Integer.parseInt(br.readLine());
				
				if (ch == 6) {
					login.logout();
					System.out.println("로그아웃 되었습니다.");
					return;				
				}
				switch (ch) {
				case 1: findBybook(); break;
				case 2: loan(); break;
				case 3: returnUI.start(); break;
				case 4: sincheong(); break;
				case 5: mypageUI.menu(); break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력해주세요");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 1. 도서 검색[제목], [저자] / (도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력)
	protected void findBybook() {
		System.out.println("\n[도서검색/대출신청]");
		
		String search;
		List<BookInfoDTO> list = null;
	
			while(true) {
				try {
					System.out.print("도서 제목 또는 저자(공백 입력시 이전 메뉴) => ");
					search = br.readLine();
					System.out.println();
					
					// 공백 입력시 뒤로가기
					if(search.isBlank()) return;
					
					list = dao.listBook(search);
					
					// 결과 출력
					String LINE = "==================================================================================";
					System.out.println();
					System.out.println(LINE);				
					System.out.println(String.format("| %-4s|\t\t%-20s\t| %-10s\t| %-6s\t| %-6s|", "번호", "책 제목", "저자", "출판사", "대출가능여부"));
					if (list.size() == 0) {
						System.out.println("우리 도서관에 등록된 도서가 아닙니다.");
					} else {
						for (BookInfoDTO dto : list) {
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println(String.format("| %-3s| %-20s\t\t| %-10s\t| %-6s\t| %-6s|", dto.getBook_code(), truncateString(dto.getBookName(), 20),
									truncateString(dto.getAuthor_name(), 10), truncateString(dto.getPublisher_name(), 6) , (dto.getBook_condition() == "대출가능" ? "대출가능":"대출불가" )));						
						}					
					}
					System.out.println(LINE);
					
					int ch3 = 0;
					
					System.out.print("1.대출신청 2.도서검색 3.사용자화면  =>  ");
					ch3 = Integer.parseInt(br.readLine());
					
					switch(ch3) {
					case 1: insertloan(); return;
					case 2: break;
					case 3: return;
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
	    }


	// 대출 신청 시 연체된 회원이면 대출불가 날짜 출력
	// 대출 연장 시 연장 가능 회기 출력
	protected void insertloan() {
	    System.out.println("\n[대출 신청]");

	    int bookcode;
	    
	    try {
	    	// 패널티 조회
	    	
	    	// 연체 조회
	    	
	    	// 도서 대출 개수(5이하)
	    	
	    	
	    	
	        System.out.print("도서 코드 ? ");
	        bookcode = Integer.parseInt(br.readLine());
	        	        
	        List<BookInfoDTO> list = dao.loanBook(bookcode);
	        
	        if(list.size() == 0) {
	        	System.out.println("도서코드는 잘못되었습니다. 다시 입력하세요..");
	        	return;
           } 
	        
	        LoanDTO dto = new LoanDTO();
	        dto.setBook_code(bookcode);
	        dto.setUser_code(login.loginUser().getUser_code());
	        
	        dao.insertloan(dto);

	        System.out.println("대출이 완료 되었습니다.");

	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    System.out.println();
	}

	
	// 2. 대출
	public void loan() {
		System.out.println("\n[대출 리스트]");
		
		int usercode;

		try {
			System.out.print("회원 번호 ? ");
			usercode = Integer.parseInt(br.readLine());
			
			List<LoanDTO> list = dao.listloan(usercode);

			if (list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
			}
			
			System.out.println("대출번호     도서제목     도서코드     대출일자    반납예정일자    실제반납일자");
			for (LoanDTO dto : list) {
				System.out.print(dto.getLoan_code() + "\t");
				System.out.print(dto.getBookname() + "\t");
				System.out.print(dto.getBook_code() + "\t");
				System.out.print(dto.getCheckout_date() + "\t");
				System.out.print(dto.getDue_date() + "\t");
				System.out.println(dto.getDue_date() + "\t");
			}
			System.out.println();
		    System.out.println("[회원님이 대출 중인 도서 목록입니다.]");
		    System.out.println();

		    int ch2 = 0;

		       System.out.println("1.대출연장 2.대출예약");
		       ch2 = Integer.parseInt(br.readLine());
		       
		       /*
		       if(ch2 == 1) {
		    	   System.out.print("연장할 대출 번호 ? ");
		    	   int loan_code = Integer.parseInt(br.readLine());
		    	   boolean b = false;
		    	   
		    	   for (LoanDTO dto : list) {
		    		   if(dto.getLoan_code() == loan_code && dto.getIsExtended() == 0) {
		    			   b = true;
		    			   break;
		    		   }
		    	   }
		    	
		    	   if(b) {
		    		   
		    	   } else {
		    		   System.out.println("연장신청을 1회 사용하였기에 연장 신청이 불가합니다.");
		    	   }
		    	   
		       } else {
		    	   renewloan(); 
		       }
		       */
		       
		      switch (ch2) {
		      case 1: renewloan(); break;
		      case 2: loanreservation(); break;
		      }
		   
		      } catch (Exception e) {
			  e.printStackTrace();
     	   }
	      }
	
	protected void renewloan() {
		System.out.println("\n[대출연장 신청]");
		
		int loancode;

		try {
			System.out.print("대출 번호 ? ");
			loancode = Integer.parseInt(br.readLine());
			
			List<LoanDTO> list = dao.listloaning(loancode);

			if (list.size() == 0) {
				System.out.println("대출신청한 도서가 없습니다.");
			} 
			
			LoanDTO dto = new LoanDTO();
			dto.setLoan_code(loancode);
			dto.setUser_code(login.loginUser().getUser_code());
			
			System.out.println("대출 연장 신청이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("");
	}

	// 대출 예약(대출 중인 도서)
	protected void loanreservation() {
		System.out.println("\n[대출 예약 신청]");
		
		
	}
	
	// 4. 도서신청
	protected void sincheong() {
		System.out.println("\n[도서신청]");

	}
	
	   // 말줄임 함수(책제목, 저자 이름 자르는데 사용)
    private String truncateString(String text, int maxLength) {
    	if (text == null) {
			text = "";
		}
		if (text.length() > maxLength) {
			if (maxLength < 3) {
				return text.substring(0, maxLength);
			}
			return text.substring(0, maxLength - 3) + "...";
		}
		if (text.length() < maxLength) {
			StringBuilder sb = new StringBuilder(text);
			int paddingLength = maxLength - text.length();
			for (int i = 0; i < paddingLength; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
		return text;
    }
}
