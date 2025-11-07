package library;

import java.util.List;


public interface AdminDAO2 {
	
	public List<AdminDTO2> sinchoengstatus();					// 신청도서 불러오기
	public String truncateString(String text, int maxLength);	// 신청 글자수가 길면 잘라줌
	public int sujeongsincheongstatus();
	
}
