/**
 * 
 */
package kr.or.easybusy.works.list.service;

import java.util.List;
import java.util.Map;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.ListVO;
import kr.or.easybusy.vo.WorksBoardVO;

/**
 * @author 이기석
 * @since 2022. 5. 6.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                          수정자               수정내용
 * --------     --------    ----------------------
 * 2022. 5. 6. 작성자명 :이기석       최초작성:이기석
 * 2022. 5. 9. 작성자명 :이기석       card제외한 list 출력메소드추가
 * 2022. 5.11. 작성자명 :이기석      카드 드개그앤드롭 update 메소드 추가(prev,after)
 *
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
public interface WorksListService {
	public WorksBoardVO retrieveWorksList(WorksBoardVO wbVO);  
	public ServiceResult modifyCardPosition(Map<String,Object> cardsinfo);
	public ServiceResult modifyListPosition(Map<String,Object> listInfo);
	/**
	 * @param listVO
	 * @return 
	 */
	public Integer modifyListTitle(ListVO listVO);
	public ServiceResult createWorksList(ListVO list) throws Exception;
	/**
	 * @param wbNo
	 * @return
	 */
	public ServiceResult removeBoard(Integer wbNo);
}
