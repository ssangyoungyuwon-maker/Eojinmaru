package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MainUI {

	public static final String ANSI_RESET = "\u001B[0m"; // 스타일 초기화

	// 텍스트 색상
	public static final String ANSI_BLUE = "\u001B[34m"; // 파란색
	public static final String ANSI_YELLOW = "\u001B[33m"; // 노란색

	// 스타일
	public static final String ANSI_BOLD = "\u001B[1m"; // 굵게
	public static final String ANSI_ITALIC = "\u001B[3m";  // 이탤릭체
	public static final String ANSI_UNDERLINE = "\u001B[4m"; // 밑줄

	// 배경색
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m"; // 노란색 배경

	private static final String ADMIN_ID = "admin";
	private static final String ADMIN_PW = "admin";

	// private Connection conn = DBConn.getConnection(); 불필요해보임

	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private MemberDAO memberDAO = new MemberDAOImpl();
	private BookDAO bookDAO = new BookDAOImpl();
	private LoginInfo login = new LoginInfo(); // 로그인 정보 저장
	private AdminUI adminUI = new AdminUI();
	private UserUI userUI = new UserUI(login);
	private NoticeUI noticeUI = new NoticeUI(false);
	private BookRequestUI bookRequestUI = new BookRequestUI();

	public void menu() {
		while (true) {
			try {
				noticeUI.Printrecomendbook();
				System.out.println();
				noticeUI.PrintlastestNoticeTitle();
				System.out.println();

				String welcomeMessage = "🏠 어진 마루 도서관 📚";
				System.out.println(ANSI_BOLD + "\t\t\t\t\t       " + ANSI_UNDERLINE + welcomeMessage + ANSI_RESET);

				System.out.println(
						"\n========================================"+ANSI_BOLD +"  [ M A I N ] " +ANSI_RESET +"========================================");
				System.out.println(ANSI_ITALIC + "\t\t\t\t\t\t 1. Login");
				System.out.println("\t\t\t\t\t 2. Sign up");
				System.out.println("\t\t\t\t\t 3. Book Search");
				System.out.println("\t\t\t\t\t 4. Book Request");
				System.out.println("\t\t\t\t\t 5. Notice");
				System.out.println(
						"==============================================================================================");
				System.out.print("\t\t\t   ➡️ Select menu(num) : " + ANSI_RESET);

				int mainChoice = Integer.parseInt(br.readLine());
				
				switch (mainChoice) {
				// 1. 로그인
				case 1:
					signin();
					break;
				// 2. 회원가입
				case 2:
					signup();
					break;
				// 3. 도서 검색
				case 3:
					bookSearch();
					break;
				// 4. 도서 신청
				case 4:
					bookRequestUI.request();
					break;
				// 5. 공지사항 조회
				case 5:
					noticeUI.noticeList();
					break;
				default:
					System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println(">> 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	// 로그인
	private void signin() {
		String LINE = "=============================================================================================";
		try {

			System.out.println(
					"\n===========================================" +ANSI_ITALIC + ANSI_BOLD + "[Log IN]" +ANSI_RESET + "===========================================");
			System.out.print("                                        " +ANSI_ITALIC + ANSI_BOLD + "🆔 ID : "+ ANSI_RESET);
			String inputId = br.readLine();
			System.out.print("                                       " +ANSI_ITALIC + ANSI_BOLD +  "🗝️ PWD: " + ANSI_RESET);
			String inputPw = br.readLine();
			System.out.println(LINE);

			// 1. 관리자 로그인 확인 및 관리자 메뉴 호출
			if (ADMIN_ID.equals(inputId) && ADMIN_PW.equals(inputPw)) {
				System.out.println("\t\t\t     >> 관리자님, 환영합니다. [관리자 화면]으로 이동합니다.");
				adminUI.showMenu();
			}
			// 2. 관리자가 아니면, 사용자 로그인 시도
			else {
				// MemberDAO를 통해 사용자 로그인 시도
				MemberDTO user = memberDAO.login(inputId, inputPw);

				if (user != null) {
					// 로그인 성공 -> LoginInfo에 정보 저장
					login.login(user);
					System.out.println(">> " + user.getUser_name() + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
					// 사용자 메뉴 호출
					userUI.menu();
				} else {
					// 로그인 실패
					System.out.println(">> 아이디 또는 비밀번호가 일치하지 않습니다.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 회원 가입
	private void signup() {
		System.out.println(
				"\n========================================"+ ANSI_BOLD + ANSI_ITALIC +"📋[SIGN UP]"+ ANSI_RESET+"========================================");

		try {
			MemberDTO newUser = new MemberDTO();
			System.out.print(ANSI_ITALIC+"\t\t\t\t\t🆔 ID : ");
			newUser.setUser_Id(br.readLine());
			System.out.print("\t\t\t\t🗝️ PWD : ");
			newUser.setUser_pwd(br.readLine());
			System.out.print("\t\t\t\t🤖 NAME : ");
			newUser.setUser_name(br.readLine());
			System.out.print("\t\t\t\t🎉 BIRTH (YYYY-MM-DD): ");
			newUser.setUser_birth(br.readLine());
			System.out.print("\t\t\t\t📱 TEL (010-XXXX-XXXX) : ");
			newUser.setUser_tel(br.readLine());
			System.out.print("\t\t\t\t📨 E-MAIL : ");
			newUser.setUser_email(br.readLine());
			System.out.print("\t\t\t\t🏠 ADDRESS : "+ANSI_RESET);
			newUser.setUser_address(br.readLine());
			boolean isSuccess = memberDAO.signUpUser(newUser);
			if (isSuccess) {
				System.out.println(">> 회원가입이 성공적으로 완료되었습니다.");
				// 회원가입 성공시 바로 로그인까지
				// 로그인 성공 -> LoginInfo에 정보 저장
				login.login(newUser);
				System.out.println(">> " + newUser.getUser_name() + "님, 환영합니다. [사용자 화면]으로 이동합니다.");
				// 사용자 메뉴 호출
				userUI.menu();
			} else {
				System.out.println(">> 회원가입에 실패하였습니다. ");
				System.out.println("메인 화면으로 돌아갑니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 도서 검색(책 제목 or 저자 이름)
	private void bookSearch() {
		String LINE = "=====================================================================================";
		System.out.print(LINE);
		System.out.println("\n\t\t\t\t" +ANSI_BOLD + ANSI_ITALIC + "🔎 [  book search  ] 🔎" + ANSI_RESET);
		System.out.println(LINE);

		String search;
		List<BookInfoDTO> list = null;

		while (true) {
			try {
				System.out.print("📋 도서명 또는 저자를 입력해주세요.\n");
				System.out.print(" 입력 [뒤로가기 : 공백] =>");
				search = br.readLine().trim();
				System.out.println();

				// 공백 입력시 뒤로가기
				if (search.isBlank())
					return;

				list = bookDAO.listBook(search);

				// 결과 출력
				System.out.println();
				System.out.println(LINE);
				System.out.println("\t\t\t\t" +ANSI_BOLD + ANSI_ITALIC + "😎 [  Search Result  ] 😎"+ ANSI_RESET);
				System.out.println(LINE);
				System.out.println(String.format("| %-4s|%-23s\t| %-10s\t| %-6s\t| %-6s|", "번호", "              책 제목",
						"저자", "출판사", "대출여부"));
				System.out.println(LINE);
				if (list.size() == 0) {
					System.out.println("🚨 해당 도서는 📚어진마루📚에 등록된 도서가 아닙니다.");
				} else {
					for (BookInfoDTO dto : list) {
						System.out.println(String.format("| %-4s | %-23s\t| %-10s\t| %-6s\t| %-6s|", dto.getBook_code(),
								truncateString(dto.getBookName(), 20), truncateString(dto.getAuthor_name(), 10),
								truncateString(dto.getPublisher_name(), 6),
								(dto.getBook_condition() == "대출가능" ? "대출가능" : "대출불가")));
					}
				}
				System.out.println(LINE);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
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