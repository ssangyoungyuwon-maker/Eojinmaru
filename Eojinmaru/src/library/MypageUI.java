package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MypageUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private LoginInfo login = null;
	private UserDAO dao = new UserDAO();
	private ReturnUI returnUI;

	public MypageUI(LoginInfo login) {
		this.login = login;
		this.returnUI = new ReturnUI(login);
	}

	public void menu() {
		int ch;
		while (true) {
			try {
				MemberDTO loginUser = login.loginUser();
				System.out.println("\n===== [마이 페이지] =====");
				System.out.println(loginUser.getUser_name() + "님 아래 메뉴를 선택하세요.");
				System.out.println("1.내정보확인");
				System.out.println("2.내정보수정");
				System.out.println("3.탈퇴");
				System.out.println("4.이전화면");
				System.out.println("--------------------");
				System.out.print("마이페이지 메뉴 선택: ");
				ch = Integer.parseInt(br.readLine());

				switch (ch) {
				case 1: chkmyinfo(); break;
				case 2: update(); break;
				case 3: delete(); break;
				case 4:
					System.out.println("이전 화면으로 이동합니다.");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void chkmyinfo() {
		System.out.println("\n\t\t\t\t\t🔒 [ 내 정보 확인 ] \t\t\t\t\t");

		String LINE = "======================================================================================================================";
		try {
			MemberDTO dto = login.loginUser();
			String pwd;

			for (int i = 1; i <= 3; i++) {
				System.out.println("🔐내 정보 확인을 위해 기존 비밀번호를 입력하세요.");
				pwd = br.readLine();

				if (dto.getUser_pwd().equals(pwd)) {

					System.out.println(LINE);
					System.out.printf("%-15s | %-8s | %-12s\t | %-13s\t | %-20s\t | %-15s\n", "아이디", "이름", "생년월일",
							"전화번호", "이메일", "주소");
					System.out.println(LINE);

					System.out.printf("%-15s\t | %-8s | %-12s\t | %-13s\t | %-20s\t | %-15s\n", dto.getUser_Id(),
							dto.getUser_name(), dto.getUser_birth(), dto.getUser_tel(), dto.getUser_email(),
							dto.getUser_address());
					System.out.println("\n⬅️이전 화면으로 이동합니다.");

					return;
				} else {
					System.out.println(">> ⚠️ 비밀번호가 틀렸습니다. 남은 로그인 횟수: " + (3 - i) + "\n");
				}
			}
			System.out.println("\n❌ 비밀번호 3회 초과! 마이페이지로 돌아갑니다.");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void update() {
		System.out.println("\n\t\t\t\t\t🔒 [ 내 정보 수정 ] \t\t\t\t\t");
		try {
			MemberDTO dto = login.loginUser();

			for (int i = 1; i <= 3; i++) {
				System.out.println("🔐정보 수정을 위해 기존 비밀번호를 입력하세요.");
				String pwd = br.readLine();

				if (dto.getUser_pwd().equals(pwd)) {

					System.out.println("변경할 비밀번호? ");
					dto.setUser_pwd(br.readLine());

					System.out.println("변경할 이름? ");
					dto.setUser_name(br.readLine());

					System.out.println("변경할 전화번호? ");
					dto.setUser_tel(br.readLine());

					System.out.println("변경할 이메일? ");
					dto.setUser_email(br.readLine());

					System.out.println("변경할 주소? ");
					dto.setUser_address(br.readLine());

					dao.updateUser(dto);

					System.out.println("✅ 회원정보가 수정되었습니다.");
				} else {
					System.out.println(">> ⚠️ 비밀번호가 틀렸습니다. 남은 로그인 횟수: " + (3 - i) + "\n");
				}
			}
			System.out.println("\n❌ 비밀번호 3회 초과! 마이페이지로 돌아갑니다.");
			return;
		} catch (Exception e) {
		}
	}

	public void delete() {
		System.out.println("\n\t\t\t\t\t🔒 [ 탈퇴 ] \t\t\t\t\t");

		try {
			MemberDTO dto = login.loginUser();
			List<LoanDTO> list = returnUI.showbooksonloan(dto.getUser_code());

			for (int i = 1; i <= 3; i++) {
				System.out.print("🔐회원 탈퇴를 위해 기존 비밀번호를 입력하세요.");
				String pwd = br.readLine();
				
				if (dto.getUser_pwd().equals(pwd)) {
				System.out.print("🚨 정말로 탈퇴하시겠습니까? [Y/N]");
				String rs = br.readLine();
				
			
				if (rs.equalsIgnoreCase("y")) {
					if (list.size() != 0) {
						System.out.println("⛔ 현재 대출중인 도서가 있어 탈퇴가 불가합니다.");
						return;
					} else {
						dao.deleteUser(login.loginUser().getUser_Id());
						System.out.println("✅ 탈퇴가 완료되었습니다.\n");
						login.logout();
						new MainUI().menu();
					}
			}else if (rs.equalsIgnoreCase("n")) {
				System.out.println("✅ 탈퇴가 취소되었습니다. 이전 화면으로 돌아갑니다.");
				return;
			}else {
				System.out.println("🚫Y나 N만 입력 가능합니다. 이전 화면으로 돌아갑니다."); return;
			}
				} else if(!dto.getUser_pwd().equals(pwd)) {
				System.out.print("\n>>⚠️ 비밀번호가 틀렸습니다. 남은 로그인 횟수: " + (3 - i) + "\n");
			}
				}	
				System.out.println("\n❌ 비밀번호 3회 초과! 마이페이지로 돌아갑니다.");
				return;
				
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
