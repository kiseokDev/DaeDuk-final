package kr.or.easybusy.works.list.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.validate.InsertGroup;
import kr.or.easybusy.vo.CardReplyVO;
import kr.or.easybusy.vo.KanbanList;
import kr.or.easybusy.vo.KanbanListWrapper;
import kr.or.easybusy.vo.ListVO;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.vo.WorksBoardVO;
import kr.or.easybusy.works.board.service.WorksBoardService;
import kr.or.easybusy.works.list.service.WorksListService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/groupware/wlist/{comNo}/{empNo}/{openYn}/{wbNo}")
@Slf4j
public class WorksListController {
	@Inject
	private WorksListService service;
	
	@Inject 
	private WorksBoardService boardService;
	
	@GetMapping()
	public String worksList(
			@PathVariable Integer wbNo
			,Model model) {
		//보드 like,
		//관리자
		//보드라벨???? 조인해야함...
		log.info("wbNo@@: {}",wbNo.toString());
		return "works/wlist/wListList";
	}
	
	@DeleteMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)  
	@ResponseBody
	public Map<String,Object> deleteBoard(
			@PathVariable Integer wbNo){
		Map<String, Object> resultMap = new HashMap<>();
		ServiceResult result = null;
		log.info("여기는 DELETE BOARD 입니다@@@@@@@@@@@@@@@@@@@@@@@@@@");
		result = service.removeBoard(wbNo); 
		resultMap.put("message","삭제되었습니다 서버용."); 
		if(ServiceResult.FAIL.equals(result)) {
			resultMap.put("message","서버오류"); 
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
//	@DeleteMapping("")
//	public Map<String,Object> deleteBoard(
//			@PathVariable Integer wbNo){
//		Map<String, Object> resultMap = new HashMap<>();
//		log.info("여기는 DELETE BOARD 입니다@@@@@@@@@@@@@@@@@@@@@@@@@@");
//		ServiceResult result = null;
//		result=ServiceResult.OK;
////		result = service.removeBoard(wbNo);
//		if(ServiceResult.FAIL.equals(result)) {
//			resultMap.put("message","서버오류");
//		}
//		resultMap.put("result", result);
//		return resultMap;
//	}
//	
	
	
	
	
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public WorksBoardVO List(
			@ModelAttribute WorksBoardVO worksBoardVO
			){
		//1. 보드정보랑
		//2. List정보랑 
		//3. 카드정보
		WorksBoardVO board = service.retrieveWorksList(worksBoardVO);
		List<ListVO> adapteeList = board.getListList();  // null point error 어떻게 처리 ?
		List<KanbanList<ListVO>> wrapperList = new ArrayList<KanbanList<ListVO>>();
		for(ListVO adaptee : adapteeList) {
			wrapperList.add(new KanbanListWrapper(adaptee));
		}
		board.setWrapperList(wrapperList);
		return board;
	}
	
	@PutMapping("cardposition")
	@ResponseBody
	public String updateCardPosition(
			@RequestBody Map<String,Object> paramMap
			) throws IllegalAccessException, InvocationTargetException {
		log.info("!@!@!@!paramMap : {}",paramMap);

//		ServiceResult result = null;
//		if (when.equals("before")) {
//			result = service.modifyPrevCardPosition(card);
//		}else if (when.equals("after")) {
//			result = service.modifyAfterCardPosition(card);
//		}
//		
//		if(result.equals(ServiceResult.FAIL)) {
//			// 뭘해야하지 ? 
//		}
		service.modifyCardPosition(paramMap);
		String text = "fail";
		text = "success!";
		return text;
	}
	
	@PutMapping("listPosition")
	@ResponseBody
	public String updateListPosition(
			@RequestBody Map<String,Object> paramMap
			) throws IllegalAccessException, InvocationTargetException {
		service.modifyListPosition(paramMap);
		String text = "fail";
		text = "success!";
		return text;
	}

	@PostMapping("new")
	@ResponseBody
	public Map<String, Object> InsertList(
			@Validated(InsertGroup.class) @RequestBody ListVO list
			, BindingResult errors
			,@ModelAttribute("pathVO") PathVO path
			) {
		
		list.setWbNo(path.getWbNo());
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		if(!errors.hasErrors()) {
			try {
				result = service.createWorksList(list);
			} catch (Exception e) {
				resultMap.put("message", "서버오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "검증에 걸렸음");
		}
		KanbanList<ListVO> wrapperListVO = new KanbanListWrapper(list);
		resultMap.put("result", result);
		resultMap.put("data", wrapperListVO);
		return resultMap;
	}
	
	@PutMapping("boardtitle")
	@ResponseBody
	public String updateBoradTitle(
			@RequestBody WorksBoardVO wbVO
			) throws IllegalAccessException, InvocationTargetException {
		
		String text = "success!";
		try {
			boardService.modifyWorksBoard(wbVO);
		} catch (Exception e) {
			 text = "fail";
		}
		return text;
	}
	
	@PutMapping("listtitle")
	@ResponseBody
	public String updateListTitle(
			@RequestBody ListVO listVO
			) throws IllegalAccessException, InvocationTargetException {
		log.info("!@!@!@!@!@@!@!!@@@@@@@@@@@@@@listVO infos : {}", listVO);
		service.modifyListTitle(listVO); 
		String text = "fail";
		text = "success!";
		return text;
	}
	
	
}
