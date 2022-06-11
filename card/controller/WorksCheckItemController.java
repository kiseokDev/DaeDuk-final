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
import kr.or.easybusy.vo.CardReplyVO;
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
@RequestMapping("/groupware/card/checkItem")
public class WorksCheckItemController {

	@Inject
	private WorksCardService service;

	@PutMapping
	public Map<String, Object> updateCheckItem(
			@Validated(UpdateGroup.class) CheckItemVO item
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap

	) {
		log.info("akjsfdkjasdjkfhakjlsdhfkjlashdlkjfhdasjkf====={}:",item);
		
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		if(!errors.hasErrors()) {
			resultMap.put("message", "성공"); 
			result = service.modifyCheckItem(item);
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
	public Map<String, Object> insertCheckItem(
			@Validated(InsertGroup.class) CheckItemVO item
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		log.info("아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록아이템등록====={}:",item);
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		CheckItemVO data = null;
		log.info("item insert=========={}",item); 
		if(!errors.hasErrors()) {
			
//			data = service.createReply(reply);
			data = service.createCheckItem(item);
			if(data !=null) {
				result = ServiceResult.OK;
			}else {
				resultMap.put("message", "서버 오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "검증에 걸림");
		}
		resultMap.put("result", result);
		resultMap.put("insert", "등록성공");
		resultMap.put("data",data);
		resultMap.putAll(paramMap);
		return resultMap;
	}
	
	
	 
	
	
	@DeleteMapping
	public Map<String, Object> deleteCheckItem(
			@Validated(DeleteGroup.class) CheckItemVO item
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		log.info("item delete=========={}",item); 
		if(!errors.hasErrors()) {
			result = service.removeItem(item);
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
