package kr.or.easybusy.works.card.service;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;


import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.CardCheckList;
import kr.or.easybusy.vo.CardReplyVO;
import kr.or.easybusy.vo.CardVO;
import kr.or.easybusy.vo.CheckItemVO;
import kr.or.easybusy.vo.PathVO;

public interface WorksCardService {
	public CardVO retrieveCard(CardVO card);
	public ServiceResult createCard(CardVO card) throws Exception;
	public ServiceResult modifyCard(CardVO card) throws Exception;
	
	
	
	public ServiceResult modifyReply(CardReplyVO reply);
	public ServiceResult removeReply(CardReplyVO reply);
	public CardReplyVO createReply(CardReplyVO reply);
	
	
	
	
	public ServiceResult modifyCheckItem(CheckItemVO item);
	/**
	 * @param item
	 * @return
	 */
	public ServiceResult removeItem(CheckItemVO item);
	/**
	 * @param item
	 * @return
	 */
	public CheckItemVO createCheckItem(CheckItemVO item);
	/**
	 * @param card
	 * @return
	 */
	public ServiceResult modifyCardDesc(CardVO card);
	/**
	 * @param list
	 * @return
	 */
	
	
	
	
	//체크리스트 등록 수정 삭제         START ================ 
	public ServiceResult modifyCheckList(CardCheckList list);
	/**
	 * @param list
	 * @return
	 */
	public CardCheckList createCheckList(CardCheckList list);
	/**
	 * @param list
	 * @return
	 */
	public ServiceResult removeCheckList(CardCheckList list);
	//체크리스트 등록 수정 삭제         START ================
	
	
	
	
	//카드 파일 데이터 처리    START ================
	
	public void uploadAttatch(CardVO card) throws Exception;
	
	public void deleteAttatches(CardVO card) throws Exception;
	
	public void download(HttpServletResponse resp, OutputStream os, PathVO path);
	
	//카드 파일 데이터 처리  End ================
	/**
	 * @param card
	 * @return
	 */
	
	
	
	
	
	
	

	

	
	
}