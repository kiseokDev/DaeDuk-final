package kr.or.easybusy.works.card.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.or.easybusy.vo.CardCheckList;
import kr.or.easybusy.vo.CardReplyVO;
import kr.or.easybusy.vo.CardVO;
import kr.or.easybusy.vo.CheckItemVO;

@Mapper
public interface WorksCardDAO {
	public CardVO selectCard(CardVO card);

	public void insertCard(CardVO card);
	public CardVO insertedCard(CardVO card);
	public Integer updateReply(CardReplyVO reply);

	public Integer deleteReply(CardReplyVO reply);

	public Integer insertReply(CardReplyVO reply);
	public CardReplyVO selectInsertedReply(CardReplyVO reply);

	/**
	 * @param item
	 * @return
	 */
	public Integer updateCheckcItem(CheckItemVO item);

	/**
	 * @param item
	 * @return
	 */
	public Integer deleteCheckItem(CheckItemVO item);

	/**
	 * @param item
	 * @return
	 */
	public Integer insertCheckItem(CheckItemVO item);

	/**
	 * @param item
	 * @return
	 */
	public CheckItemVO insertedCheckItem(CheckItemVO item);

	/**
	 * @param card
	 * @return
	 */
	public Integer updateCardDesc(CardVO card);

	
	
	
	//체크리스트 등록 수정 삭제         START ===============================
	//
	/**
	 * @param list
	 * @return
	 */
	public int updateCheckList(CardCheckList list);

	/**
	 * @param list
	 * @return
	 */
	public int insertCheckList(CardCheckList list);

	/**
	 * @param list
	 * @return
	 */
	public CardCheckList selectInsertedCheckList(CardCheckList list);

	/**
	 * @param list
	 * @return
	 */
	public int deleteCheckList(CardCheckList list);
	//체크리스트 등록 수정 삭제 	  END   ===============================

	/**
	 * @param card
	 * @return
	 */
	public int deleteCardDate(CardVO card);

	/**
	 * @param list
	 * @return
	 */
	public int deleteItemsUnderList(CardCheckList list);

	/**
	 * @param item
	 * @return 
	 */
	public int updateCheckDone(CheckItemVO item);

	/**
	 * @param card
	 * @return
	 */
	public int updateCard(CardVO card);

	/**
	 * @param card
	 * @return
	 */
}
