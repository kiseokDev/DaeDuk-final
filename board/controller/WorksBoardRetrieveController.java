package kr.or.easybusy.works.board.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.EmployeeVO;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.vo.WorksBoardVO;
import kr.or.easybusy.vo.security.EmployeeVOWrapper;
import kr.or.easybusy.works.board.service.WorksBoardService;
import lombok.extern.slf4j.Slf4j;

@Controller					
@RequestMapping("/groupware/works/{comNo}/{empNo}/{openYn}")
@Slf4j
public class WorksBoardRetrieveController {
	private final WorksBoardService service;
	public WorksBoardRetrieveController(WorksBoardService service) {
		super();
		this.service = service;
	}
	
	
	@ModelAttribute("wboard")
	public WorksBoardVO worksBoard() {
		return new WorksBoardVO();
	}
	
	@GetMapping
	public String form() {
		return "works/BoardList";
	}
	
	@GetMapping(value="retreive",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<WorksBoardVO> publicBoard(
			@ModelAttribute("pathVO") PathVO pathVO
			) {
		
		List<WorksBoardVO> list = service.retrieveBoardList(pathVO);
		return list;
	}

	
	@PostMapping("new")
	@ResponseBody
	public String newWorksBoard(@ModelAttribute WorksBoardVO wbVO) {
		try {
			service.createWorksBoard(wbVO);
		} catch (Exception e) {
			return "fail";
		}
		return "success"; 
		
	}

	
	@GetMapping(value="filter",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Map<String, Object>> collections(@PathVariable Integer comNo) {
		return service.retrieveClassList(comNo);
	}

	@PutMapping(value="like")
	@ResponseBody
	public String updateLike(
			@RequestBody Map<String,Object> paramMap
			,@ModelAttribute("pathVO") PathVO pathVO
			) throws IllegalAccessException, InvocationTargetException {
			
		WorksBoardVO wbVO = new WorksBoardVO();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.convertValue(pathVO, Map.class);
		BeanUtils.populate(wbVO, map);
		BeanUtils.populate(wbVO, paramMap);
		int cnt = service.modifyLikeStatus(wbVO);
		String text = "fail!!";
		if(cnt>=1) text = "sucess!!";
		
		return text;
	}
	
	
	
	@GetMapping(value="collection",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<WorksBoardVO> selectedCollectionBoard(
			@ModelAttribute("PathVO") PathVO pathVO
			,@RequestParam String selectedCollection
			){
		 	pathVO.setWbClass(selectedCollection);
		 	log.info("@@@@@@@@@@retrieve normal vs filter:{}",selectedCollection);
		 	List<WorksBoardVO> list = service.retrieveBoardList(pathVO);
		return list;
	}
	

}
