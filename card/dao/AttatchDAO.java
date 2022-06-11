/**
 * 
 */
package kr.or.easybusy.works.card.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import kr.or.easybusy.vo.CardAttatchVO;
import kr.or.easybusy.vo.CardVO;

/**
 * @author 이기석
 * @since 2022. 5. 25.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                          수정자               수정내용
 * --------     --------    ----------------------
 * 2022. 5. 25.      작성자명       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@Mapper
public interface AttatchDAO {
	public int insertAttaches(CardVO card);
	
	public CardAttatchVO selectAttach(Integer attNo);
	public void incrementDowncount(Integer attNo);
	
	public int deleteAttaches(CardVO card);
}
