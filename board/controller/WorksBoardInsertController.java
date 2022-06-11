/**
 * 
 */
package kr.or.easybusy.works.board.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.works.board.service.WorksBoardService;

/**
 * @author 이기석
 * @since 2022. 4. 29.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                          수정자               수정내용
 * --------     --------    ----------------------
 * 2022. 4. 29.      작성자명       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@RestController
@RequestMapping("/groupware/works/new")
public class WorksBoardInsertController {
	@Inject
	private WorksBoardService service;
	
	@PostMapping
	public Map<String,Object> insert(
			@RequestParam Map<String,Object> paramMap){ 
														//0.VO에 validator 추가하고
														//1.js에서 실패시 resultMap에서 데이터 input 태그에 넣어주는 코드 넣고
														//2.js성공시 모달창 닫는코드 넣고
														//3. 검증결과 resultMap에 넣는 코드 여기서 작성하기
		Map<String,Object> resultMap = new HashMap<>();
		ServiceResult result = ServiceResult.OK;
		
		resultMap.put("result", result);
		resultMap.put("message", "Model-View Test성공");
		resultMap.putAll(paramMap);
		return resultMap;
	}
}
