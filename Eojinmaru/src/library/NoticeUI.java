package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class NoticeUI {
	private boolean isAdmin = false;

	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public NoticeUI() {
	}

	public NoticeUI(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// 가장 최근 공지사항 제목 출력
	public void PrintlastestNoticeTitle() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		  sql = "SELECT notice_title, TO_CHAR(NOTICE_DATE, 'YYYY-MM-DD') AS NOTICE_DATE_FMT " 
		          + "FROM notice ORDER BY notice_id DESC FETCH FIRST 1 ROWS ONLY";

		    try {
		        pstmt = conn.prepareStatement(sql);
		        rs = pstmt.executeQuery();
		        
		        // 출력 라인 길이를 통일
		        String LINE = "=====================================================================================";
		        System.out.println(LINE);
		        System.out.println("\t\t\t\t    🔔 최신 공지 사항 🔔");
		        System.out.println(LINE);

		        if (rs.next()) {
		            String title = rs.getString("notice_title");
		            String date = rs.getString("NOTICE_DATE_FMT");
		            
		            // 1. 공지 제목 출력 (왼쪽 정렬)
		            System.out.printf("| %-4s| %-40s\t|%-3s| %12s |", "제목", truncateString(title, 35), "날짜", date);
		            System.out.println();
		            
		        } else {
		            // 등록된 공지사항이 없는 경우
		            System.out.println("|                                                                                   |");
		            System.out.println("|          \t\t\t등록된 공지사항이 없습니다.          \t\t\t|");
		            System.out.println("|                                                                                   |");
		        }
		        System.out.println(LINE);
		        
		    } catch (Exception e) {
		        System.out.println("❌ 최신 공지사항을 불러오는 중 오류가 발생했습니다.");
		        e.printStackTrace();
		    } finally {
		        DBUtil.close(pstmt);
		        DBUtil.close(rs);
		    }
		}

	// 추천책 상시노출 3권 (이번달)
	public void Printrecomendbook() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		sql = "SELECT BI.bookname, COUNT(L.loan_code) AS total_loans " + "FROM   Loan L                    "
				+ "JOIN   Book B ON L.book_code = B.book_code " + "JOIN   bookinfo BI ON B.ISBN = BI.ISBN "
				+ "WHERE    L.checkout_date >= TRUNC(SYSDATE, 'MM')"
				+ "     AND L.checkout_date < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)" + "GROUP BY BI.bookname          "
				+ "ORDER BY total_loans DESC     " + "FETCH FIRST 3 ROWS ONLY ";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			String LINE = "=====================================================================================";
			System.out.println(LINE);
			System.out.println("\t\t\t\t🌟 이달의 대출 인기 도서 TOP 3 🌟");
			System.out.println(LINE);

			boolean hasResult = false;
			int rank = 1; // 순위 카운터

			// while 루프를 사용하여 결과 셋의 모든 행(최대 3개)을 반복하며 출력
			while (rs.next()) {
				String bookName = rs.getString("bookname");
				int totalLoans = rs.getInt("total_loans");

				// 포맷팅을 개선하여 순위, 도서명, 대출 건수를 모두 표시
				System.out.printf("| %-4s | %-42s\t  | 대출 건수: %-4d 건 |\n", "TOP " + rank++, truncateString(bookName, 25), // 도서명을
																														// 적절히
																														// 자름
						totalLoans);
				hasResult = true;
			}

			// 결과가 한 건도 없을 경우 처리 (rs.next()가 한 번도 실행되지 않은 경우)
			if (!hasResult) {
				System.out.println(
						"|                                                                                   |");
				System.out.println("|          \t\t\t이번 달의 추천 도서가 존재하지 않습니다.          \t\t\t|");
				System.out.println(
						"|                                                                                   |");
			}

			System.out.println(LINE);

		} catch (Exception e) {
			System.out.println("❌ 추천 도서 정보를 불러오는 중 오류가 발생했습니다.");
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
	}

	// 공지사항 목록 보기
	public void noticeList() {
		final int MaxNumInPage = 5;
		int pages = 1;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		while (true) {
			try {
				// 게시글 갯수 확인하고 최대 페이지 계산
				sql = "SELECT COUNT(*) cnt FROM notice";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				rs.next();
				int noticeNum = rs.getInt("cnt");
				int maxPage = (int) Math.ceil(noticeNum / (double) MaxNumInPage);

				// 해당하는 페이지의 공지글 목록 가져오기
				sql = "SELECT notice_id, notice_title, TO_CHAR(notice_date, 'YY-MM-DD') notice_date FROM notice ORDER BY notice_id DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (pages - 1) * MaxNumInPage);
				pstmt.setInt(2, MaxNumInPage);
				rs = pstmt.executeQuery();

				// 출력
				String LINE = "================================================================================";
				System.out.println();

				System.out.printf("\t\t\t\t💡 공지사항 목록(%d/%d)\n", pages, maxPage);

				System.out.println(LINE);

				System.out.println(String.format("|%-4s|\t\t\t%-25s\t\t| %-4s|", " 번호", "    공지사항", "    일자    "));
				System.out.println("--------------------------------------------------------------------------------");
				if (noticeNum == 0) {
					System.out.println(String.format("|%-26s\t\t|", "\t\t\t등록된 공지사항이 없습니다.\t\t\t"));
				} else {
					while (rs.next()) {
						System.out.println(String.format("| %-3s| %-45s\t| %-4s |", rs.getInt("notice_id"),
								truncateString(rs.getString("notice_title"), 25), rs.getString("notice_date")));
					}
				}
				System.out.println(LINE);

				if (isAdmin) {
					System.out.println("📔 메뉴: [<]이전장, [>]다음장, [등록]공지 등록, [공지번호]보기 및 수정/삭제, [0]이전 메뉴");
					System.out.print("선택 입력 >> ");

					String ch = br.readLine();

					switch (ch) {
					case "<":
						if (pages > 1)
							pages -= 1;
						break; // 이전장, 첫 장이면 움직이지 않음
					case ">":
						if (pages < maxPage)
							pages += 1;
						break; // 다음장, 마지마 장이면 움직이지 않음
					case "등록":
						noticeWrite();
						break;
					case "0":
						return;
					default:
						// 없는 공지글 번호가 입력되면 부적절한 입력 에러
						showNotice(Integer.parseInt(ch));
					}

				} else {
					System.out.println("📔 메뉴: [<]이전장, [>]다음장, [공지번호]보기, [0]이전 메뉴");
					System.out.print("선택 입력 >> ");

					String ch = br.readLine();

					switch (ch) {
					case "<":
						if (pages > 1)
							pages -= 1;
						break; // 이전장, 첫 장이면 움직이지 않음
					case ">":
						if (pages < maxPage)
							pages += 1;
						break; // 다음장, 마지마 장이면 움직이지 않음
					case "0":
						return;
					default:
						// 없는 공지글 번호가 입력되면 부적절한 입력 에러
						showNotice(Integer.parseInt(ch));
					}
				}

			} catch (NumberFormatException e) {
				System.out.println("🚨 유효하지 않은 입력입니다. 다시 입력해주세요.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				DBUtil.close(pstmt);
				DBUtil.close(rs);
			}
		}
	}

	// 선택된 공지글 보여주기
	protected void showNotice(int noticeId) throws Exception {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT notice_id, notice_title, TO_CHAR(notice_date, 'YY-MM-DD') notice_date, notice_content FROM notice WHERE notice_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("\n=======================================================");
				System.out.printf("\t\t📢 공지사항 상세 정보 (No. %d)\n", rs.getInt("notice_id"));
				System.out.println("=======================================================");
				System.out.printf("\t\t\t\t       작성일: %s\n", rs.getString("notice_date"));
				System.out.println("-------------------------------------------------------");
				System.out.printf("제목: %s\n", rs.getString("notice_title"));
				System.out.println("-------------------------------------------------------");
				System.out.println("내용:");
				printWrapped(rs.getString("notice_content"), 40);
				System.out.println("=======================================================");

				if (isAdmin) {
					System.out.println("📔 메뉴: [1]수정, [2]삭제, [0]공지목록");
					System.out.print("선택 입력 >> ");

					int ch = Integer.parseInt(br.readLine());

					switch (ch) {
					case 1:
						noticeUpdate(noticeId);
						break;
					case 2:
						noticeDelete(noticeId);
						break;
					case 0:
						return;
					default:
						throw new Exception("🚨 유효하지 않은 입력입니다. 다시 입력해주세요.");
					}

				} else {
					System.out.println("📔 메뉴: [0]공지목록");
					System.out.print("선택 입력 >> ");

					int ch = Integer.parseInt(br.readLine());

					switch (ch) {
					case 0:
						return;
					default:
						throw new Exception("🚨 유효하지 않은 입력입니다. 다시 입력해주세요.");
					}
				}

			} else {
				throw new Exception("🚨 존재하지 않은 공지 번호입니다. 다시 입력해주세요.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
	}

	// 공지글 쓰기
	protected void noticeWrite() throws Exception {
		PreparedStatement pstmt = null;
		String sql;

		try {
			System.out.print("등록할 공지 제목을 입력해주세요 \n 제목 : ");
			String newTitle = br.readLine().trim();

			System.out.print("등록할 공지 내용을 입력해주세요 \n 내용 : ");
			String newContent = br.readLine().trim();

			if (newTitle.isEmpty() && newContent.isEmpty()) {
				throw new Exception("✅ 입력된 내용이 없어 공지사항 등록이 취소되었습니다.");
			}

			sql = "INSERT INTO NOTICE (notice_title, notice_content, notice_date) VALUES (?, ?, SYSDATE)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newTitle);
			pstmt.setString(2, newContent);

			if (pstmt.executeUpdate() <= 0) {
				throw new Exception("❌ 공지사항 등록에 실패했습니다.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 선택된 공지글 수정
	protected void noticeUpdate(int noticeId) throws Exception {
		PreparedStatement pstmt = null;
		String sql;

		try {
			System.out.println("수정할 공지 제목을 입력해주세요 \n 제목 : ");
			String newTitle = br.readLine().trim();

			System.out.println("수정할 공지 내용을 입력해주세요 \n 내용 : ");
			String newContent = br.readLine().trim();

			if (newTitle.isEmpty() && newContent.isEmpty()) {
				throw new Exception("✅ 입력된 내용이 없어 공지사항 수정이 취소되었습니다.");
			}

			sql = "UPDATE NOTICE SET notice_title = ?, notice_content = ?, notice_date = SYSDATE WHERE notice_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newTitle);
			pstmt.setString(2, newContent);
			pstmt.setInt(3, noticeId);

			if (pstmt.executeUpdate() <= 0) {
				throw new Exception("❌ 공지사항 수정에 실패했습니다.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 선택된 공지글 삭제
	protected void noticeDelete(int noticeId) throws Exception {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM NOTICE WHERE notice_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeId);

			if (pstmt.executeUpdate() <= 0) {
				throw new Exception("❌ 공지사항 삭제에 실패했습니다.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 말줄임 함수(공지사항 제목 자르는데 사용)
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
				sb.append("　");
			}
			return sb.toString();
		}
		return text;
	}

	// 줄바꿈 함수(공지글 내용을 줄바꾸는 데 사용)
	private void printWrapped(String text, int width) {
		int length = text.length();
		for (int i = 0; i < length; i += width) {
			int end = Math.min(i + width, length);
			System.out.println(text.substring(i, end));
		}
	}

}
