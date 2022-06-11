/**
 * 
 */
package kr.or.easybusy.works.card.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.validate.DeleteGroup;
import kr.or.easybusy.validate.InsertGroup;
import kr.or.easybusy.validate.UpdateGroup;
import kr.or.easybusy.vo.CardCheckList;
import kr.or.easybusy.vo.CheckItemVO;
import kr.or.easybusy.works.card.service.WorksCardService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 이기석
 * @since 2022. 5. 24.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                          수정자               수정내용
 * --------     --------    ----------------------
 * 2022. 5. 24.      작성자명       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@RestController
@Slf4j
@RequestMapping("/groupware/card/checkList")
public class WorksCheckListController {
	@Inject
	private WorksCardService service;

	@PutMapping
	public Map<String, Object> updateCheckList(
			@Validated(UpdateGroup.class) CardCheckList list
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap

	) {
		log.info("리스트수정=============================={}:",list);
		
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		if(!errors.hasErrors()) {
			resultMap.put("message", "성공"); 
			result = service.modifyCheckList(list);
			resultMap.put("message", "성공");
			if(ServiceResult.FAIL.equals(result)) {
				resultMap.put("message", "서버 오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "제목을 30글자 이하로 적어주세요");
		}
		
		resultMap.put("result", result);
		resultMap.putAll(paramMap);
		return resultMap;
	}
	  
	
	
	@PostMapping  
	public Map<String, Object> insertCheckList(
			@Validated(InsertGroup.class) CardCheckList list
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		CardCheckList data = null;
		log.info("checkList insert=========={}",list); 
		if(!errors.hasErrors()) {
			 
			data = service.createCheckList(list);
			if(data !=null) {
			result = ServiceResult.OK;
			resultMap.put("insert", "등록성공");
			resultMap.put("data",data);
			}else { 
				resultMap.put("message", "서버 오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "검증에 걸림");
		}
		resultMap.put("result", result);
		resultMap.putAll(paramMap);
		return resultMap;
	}
	
	
	 
	
	
	@DeleteMapping
	public Map<String, Object> deleteCheckList(
			@Validated(DeleteGroup.class) CardCheckList list
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		log.info("item delete=========={}",list);  
		if(!errors.hasErrors()) {
			result = service.removeCheckList(list);  
			if(ServiceResult.FAIL.equals(result)) {
				resultMap.put("message", "서버 오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "검증에 걸림");
		}
		resultMap.put("result", result);
		resultMap.put("delete", "삭제성공");
		resultMap.putAll(paramMap);
		return resultMap;
	}
	
	 

}
