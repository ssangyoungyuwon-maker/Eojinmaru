package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MypageUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private LoginInfo login = null;
	private UserDAO dao = new UserDAO();

	public MypageUI(LoginInfo login) {
		this.login = login;
	}

	public void menu() {
		int ch;
		while (true) {
			try {
				MemberDTO loginUser = login.loginUser();
				System.out.println(loginUser.getUser_name() + "님 아래 메뉴를 선택하세요.");
				System.out.println("1.내정보확인 2.내정보수정 3.탈퇴 4.로그아웃 5.홈으로");
				ch = Integer.parseInt(br.readLine());

				switch (ch) {
				case 1: chkmyinfo(); break;
				case 2: update(); break;
				case 3: delete(); break;
				case 4: login.logout();
					System.out.println("로그아웃되었습니다.");
					return;
				case 5:System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void chkmyinfo() {
		System.out.println("\n[내정보확인]");
		try {
			MemberDTO dto = login.loginUser();
			String pwd;

			for (int i = 1; i <= 3; i++) {
				System.out.println("내 정보 확인을 위해 기존 비밀번호를 입력하세요.");
				pwd = br.readLine();

				if (dto.getUser_pwd().equals(pwd)) {
					System.out.println("아이디\t비밀번호\t이름\t생년월일\t전화번호\t이메일\t주소");
					System.out.println("------------------------------------------");

					System.out.print(dto.getUser_Id() + "\t");
					System.out.print(dto.getUser_pwd() + "\t");
					System.out.print(dto.getUser_name() + "\t");
					System.out.print(dto.getUser_birth() + "\t");
					System.out.print(dto.getUser_tel() + "\t");
					System.out.print(dto.getUser_email() + "\t");
					System.out.println(dto.getUser_address());

					return;
				} else {
					System.out.println("비밀번호가 틀렸습니다. 남은 로그인 횟수: " + (3 - i));
				}
			}
			System.out.println("비밀번호 3회 초과! 마이페이지로 돌아갑니다.");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void update() {
		System.out.println("\n[내정보수정]");
		try {
			MemberDTO dto = login.loginUser();

			System.out.println("정보 수정을 위해 기존 비밀번호를 입력하세요.");
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

				System.out.println("회원정보가 수정되었습니다.");
			} else {
				System.out.println("비밀번호가 틀립니다.");
				return;
			}
		} catch (Exception e) {
		}
	}

	public void delete() {
		System.out.println("\n[탈퇴]");

		try {
			MemberDTO dto = login.loginUser();

			System.out.println("비밀번호를 입력하세요.");
			String pwd = br.readLine();
			if (!dto.getUser_pwd().equals(pwd)) {
				System.out.println("비밀번호가 틀렸습니다. 마이페이지로 돌아갑니다.");
				return;
			}

			while (true) {
				System.out.println("정말로 탈퇴하시겠습니까? [Y/N]");
				String rs = br.readLine();

				if (rs == null || rs.isBlank()) {
					System.out.println("Y나 N중에 하나만 입력하세요.");
					continue;
				}

				char input = rs.toUpperCase().charAt(0);

				if (input == 'Y') {
					dao.deleteUser(login.loginUser().getUser_Id());
					System.out.println("탈퇴가 완료되었습니다.");
					login.logout();
					return;
				} else if (input == 'N') {
					System.out.println("취소되었습니다. 마이페이지로 이동합니다.");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
