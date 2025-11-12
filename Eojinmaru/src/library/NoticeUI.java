package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DBUtil.DBConn;

public class NoticeUI {
	
	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	// 공지사항 목록 보기
    public void noticeList() {
    	final int MaxNumInPage = 5;
    	int pages = 1;
    	
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql;
    	
    	try {
			while(true) {
				// 게시글 갯수 확인하고 최대 페이지 계산
				sql = "SELECT COUNT(*) cnt FROM notice";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				rs.next();
				int noticeNum = rs.getInt("cnt");
				int maxPage = (int)Math.ceil(noticeNum / (double)MaxNumInPage);
				
				// 해당하는 페이지의 공지글 목록 가져오기
	    		sql = "SELECT notice_id, notice_title, TO_CHAR(notice_date, 'YY-MM-DD') notice_date FROM notice ORDER BY notice_id DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (pages-1)*MaxNumInPage);
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
					while(rs.next()) {
						System.out.println(String.format("| %-3s| %-45s\t| %-4s |", rs.getInt("notice_id"),
								truncateString(rs.getString("notice_title"), 25), rs.getString("notice_date")));
					}
				}
				System.out.println(LINE);
				System.out.println("📔 메뉴: [<]이전장, [>]다음장, [등록]공지 등록, [공지번호]보기 및 수정/삭제, [0]이전 메뉴");
				System.out.print("선택 입력 >> ");
				
				String ch = br.readLine();
				
				switch(ch) {
					case "<": if(pages > 1) pages -= 1; break; // 이전장, 첫 장이면 움직이지 않음
					case ">": if(pages < maxPage) pages += 1; break; // 다음장, 마지마 장이면 움직이지 않음
					case "등록": break;
					case "0": return;
					default: 
						// 없는 공지글 번호가 입력되면 부적절한 입력 에러
						showNotice(ch);
					
							
	
				}
				
		
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
    	
    	
    	///////////////////////////////////////////////////////////////////////////
    
    	

//		
//		while (true) {
//			
//			
//
//			if (input.equalsIgnoreCase("등록")) {
//				System.out.println("\n📢 공지사항 등록 화면으로 이동합니다.");
//				this.noticeinsert();
//				break;
//
//			} else if (input.equals("0")) {
//				System.out.println("\n⬅️ 이전 메뉴로 돌아갑니다.");
//				System.out.println();
//				return;
//
//			} else {
//				try {
//					int noticeId = Integer.parseInt(input);
//
//					boolean isValidId = false;
//
//					for (AdminDTO2 dto : list) {
//						if (dto.getNoticeId() == noticeId) {
//							isValidId = true;
//							break;
//						}
//					}
//					if (isValidId) {
//						System.out.println("\n✏️ 공지 번호 " + noticeId + "번 수정/삭제 화면으로 이동합니다.");
//						this.noticeUpdate(noticeId);
//						break;
//					} else {
//						System.out.println("🚨 유효하지 않은 공지 번호입니다. 다시 입력해주세요.");
//					}
//				} catch (NumberFormatException e) {
//					System.out.println("🚨 잘못된 입력입니다. '등록', 공지번호, 또는 '0'을 입력해주세요.");
//				}
//			}
//		}
//		this.noticeadmin();
//    	
    	
    	
    }
    
    protected void showNotice(String noticeId) throws Exception {
    	
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql;
   
    	try {
    		sql = "SELECT notice_id, notice_title, TO_CHAR(notice_date, 'YY-MM-DD') notice_date, notice_content FROM notice WHERE notice_id = ?";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, Integer.parseInt(noticeId));
    		rs = pstmt.executeQuery();
    		if(rs.next()) {
    			System.out.println("\n=======================================================");
    			System.out.printf("\t\t📢 공지사항 상세 정보 (No. %d)\n", rs.getInt("notice_id"));
    			System.out.println("=======================================================");
    			System.out.printf("\t\t\t\t       작성일: %s\n", rs.getString("notice_date"));
    			System.out.println("-------------------------------------------------------");
    			System.out.printf("제목: %s\n", rs.getString("notice_title"));
    			System.out.println("-------------------------------------------------------");
    			System.out.println("내용:");
    			System.out.println(rs.getString("notice_content"));
    			System.out.println("=======================================================");
    			
    			
    		} else {
    			throw new Exception("🚨 존재하지 않은 공지 번호입니다. 다시 입력해주세요.");
    		}
		} catch (Exception e) {
			throw new Exception("🚨 유효하지 않은 공지 번호입니다. 다시 입력해주세요.");
		}  
    }
    
    
    private String truncateString(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

}
