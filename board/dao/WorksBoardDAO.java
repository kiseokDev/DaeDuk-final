package kr.or.easybusy.works.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.vo.WorksBoardVO;

@Mapper
public interface WorksBoardDAO {
	public List<WorksBoardVO> selectWorksBoardList(PathVO pathVO);
	public List<Map<String, Object>> selectClassList(Integer comNo);
	public int modifyLikeStatus(WorksBoardVO wbVO);
	public int updateWorksBoard(WorksBoardVO wbVO);
	/**
	 * @param wbVO
	 */
	public void insertWorksBoard(WorksBoardVO wbVO);
	public void insertWorksAdminDefault(WorksBoardVO wbVO);
	/**
	 * @param wbVO
	 */
	public void insertWorksAdminCreator(WorksBoardVO wbVO);
}
