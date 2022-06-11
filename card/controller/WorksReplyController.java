package kr.or.easybusy.works.card.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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
import kr.or.easybusy.works.card.service.WorksCardService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/groupware/card/reply")
public class WorksReplyController {

	@Inject
	private WorksCardService service;


	@PostMapping  
	public Map<String, Object> insertReply(
			@Validated(InsertGroup.class) CardReplyVO reply
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		CardReplyVO data = null;
		log.info("reply insert=========={}",reply); 
		if(!errors.hasErrors()) {
			data = service.createReply(reply);
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
	public Map<String, Object> deleteReply(
			@Validated(DeleteGroup.class) CardReplyVO reply
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap
			
			) {
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		log.info("reply delete=========={}",reply); 
		if(!errors.hasErrors()) {
			result = service.removeReply(reply);
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
	
	
	@PutMapping
	public Map<String, Object> updateReply(
			@Validated(UpdateGroup.class) CardReplyVO reply
			, BindingResult errors
			, @RequestParam Map<String, Object> paramMap

	) {
		
		ServiceResult result = null;
		Map<String, Object> resultMap = new HashMap<>();
		if(!errors.hasErrors()) {
			result = service.modifyReply(reply);
			if(ServiceResult.FAIL.equals(result)) {
				resultMap.put("message", "서버 오류");
			}
		}else {
			result = ServiceResult.INVALID;
			resultMap.put("message", "댓글을 20글자 이하로 적어주세요");
		}
		
		resultMap.put("result", result);
		resultMap.putAll(paramMap);
		return resultMap;
	}
}
