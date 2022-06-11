/**
 * 
 */
package kr.or.easybusy.works.list.service;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.ListVO;
import kr.or.easybusy.vo.WorksBoardVO;
import kr.or.easybusy.works.list.dao.WorksListDAO;
import lombok.extern.slf4j.Slf4j;

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
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@Service
@Slf4j
public class WorksListServiceImpl implements WorksListService {
	
	@Inject
	private WorksListDAO dao;

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.list.service.WorksListService#retrieveWorksList(java.lang.Integer)
	 */
	@Override
	public WorksBoardVO retrieveWorksList(WorksBoardVO wbVO) {
		return dao.selectWorksList(wbVO);
	}

	@Override
	public ServiceResult modifyCardPosition(Map<String,Object> cardsinfo) {
		int beforeListId = Integer.valueOf((String) cardsinfo.get("beforeListId"));
		int afterListId = Integer.valueOf((String) cardsinfo.get("afterListId"));
		
		if(beforeListId != afterListId) {
			dao.updateCardListNo(cardsinfo);
		}
		
		Integer rowcnt = dao.updateCardPosition(cardsinfo);
		
		if (rowcnt == null) {
			return ServiceResult.FAIL;
		}else if (rowcnt > 0) {
			return ServiceResult.OK;
		}
		return ServiceResult.OK;
	}

	@Override
	public ServiceResult modifyListPosition(Map<String, Object> listInfo) {
		dao.updateListPosition(listInfo);
		Integer rowcnt = (Integer)listInfo.get("rowcnt");
		log.info(Objects.toString(rowcnt));
		return rowcnt>0 || rowcnt !=null ? ServiceResult.OK : ServiceResult.FAIL;
	}

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.list.service.WorksListService#modifyListTitle(kr.or.easybusy.vo.ListVO)
	 */
	@Override
	public Integer modifyListTitle(ListVO listVO) {
		return dao.updateListTitle(listVO);
		
	}

	@Override
	public ServiceResult createWorksList(ListVO list) {
		dao.insertWorksList(list);
		log.info("==========================after Insert{}:",list);
		ListVO data = dao.selectWorksListAfterInsert(list);
		log.info("==========================after Selectdata{}:",data);
		return data == null ? ServiceResult.FAIL : ServiceResult.OK;
	}

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.list.service.WorksListService#removeBoard(java.lang.Integer)
	 */
	@Override
	public ServiceResult removeBoard(Integer wbNo) {
		int rowcnt = dao.deleteBoard(wbNo);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
	}

	
}
