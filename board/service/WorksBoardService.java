package kr.or.easybusy.works.board.service;

import java.util.List;
import java.util.Map;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.vo.WorksBoardVO;
public interface WorksBoardService {
	public List<WorksBoardVO> retrieveBoardList(PathVO pathVO); //employeeVO 파라미터로 받아야함 지금테스트
	public List<Map<String, Object>> retrieveClassList(Integer comNo);
	public int modifyLikeStatus(WorksBoardVO wbVO);
	/**
	 * 새로운 프로젝트를 등록하는 메서드
	 * @param wbVO
	 * @return
	 * @throws Exception 
	 */
	public void createWorksBoard(WorksBoardVO wbVO) throws Exception;
	public int modifyWorksBoard(WorksBoardVO wbVO) throws Exception;
}
