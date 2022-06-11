/**
 * 
 */
package kr.or.easybusy.works.list.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


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
 * 2022. 5. 6.      작성자명       최초작성
 * 2022. 5. 11 	이기석		카드 드래그앤드롭 수정 메서드 추가(updatePrevCardPosition,updateAfterCardPosition)
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@Mapper
public interface WorksListDAO {
	public WorksBoardVO selectWorksList(WorksBoardVO wbVO);
	public List<ListVO> selectWorksOnlyList(Integer wbNo);
	public Integer updateCardPosition(Map<String, Object> cardsinfo);
	public Integer updateCardListNo(Map<String, Object> cardsinfo);
	public Integer updateListPosition(Map<String, Object> cardsinfo);
	/**
	 * @param listVO
	 * @return
	 */
	public Integer updateListTitle(ListVO listVO);
	
	
	public Integer insertWorksList(ListVO list);
	public ListVO selectWorksListAfterInsert(ListVO list);
	/**
	 * @param wbNo
	 * @return
	 */
	public int deleteBoard(Integer wbNo);
}
