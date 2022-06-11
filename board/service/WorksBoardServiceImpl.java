package kr.or.easybusy.works.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.vo.WorksBoardVO;
import kr.or.easybusy.works.board.dao.WorksBoardDAO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorksBoardServiceImpl implements WorksBoardService {
	private final WorksBoardDAO dao;
	
	public WorksBoardServiceImpl(WorksBoardDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public List<WorksBoardVO> retrieveBoardList(PathVO pathVO) {
		return dao.selectWorksBoardList(pathVO);
	}


	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.board.service.WorksBoardService#retrieveClassList()
	 * return : 회사가 프로젝트에 부여한 클래스(collection이라는 용어로 쓰임) 에 대한 정보를 리스트에 담아 리턴시키는 메서드
	 * param : 회사 번호
	 */
	@Override
	public List<Map<String, Object>> retrieveClassList(Integer comNo) {
		return dao.selectClassList(comNo);
	}

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.board.service.WorksBoardService#changeLike(kr.or.easybusy.vo.WorksBoardVO)
	 * 설명 : 프로젝트 즐겨찾기 등록, 수정하는 메서드
	 */
	@Override
	public int modifyLikeStatus(WorksBoardVO wbVO) {
		return dao.modifyLikeStatus(wbVO);
	}

	@Override
	public void createWorksBoard(WorksBoardVO wbVO) throws Exception{
		dao.insertWorksBoard(wbVO);
		dao.insertWorksAdminDefault(wbVO);
		dao.insertWorksAdminCreator(wbVO);
		
	}

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.board.service.WorksBoardService#modifyWorksBoard(kr.or.easybusy.vo.WorksBoardVO)
	 */
	@Override
	public int modifyWorksBoard(WorksBoardVO wbVO) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateWorksBoard(wbVO);
	}
	
	
	
	
}
