package kr.or.easybusy.works.card.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.validate.InsertGroup;
import kr.or.easybusy.validate.UpdateGroup;
import kr.or.easybusy.vo.CardAttatchVO;
import kr.or.easybusy.vo.CardReplyVO;
import kr.or.easybusy.vo.CardVO;
import kr.or.easybusy.vo.KanbanItem;
import kr.or.easybusy.vo.KanbanItemWrapper;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.works.card.service.WorksCardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/groupware/card/{empNo}/{wbNo}/{wcNo}")
public class WorksCardController {

	@Inject
	private WorksCardService service;
	

	
	
//	@PutMapping("desc")  
//	public Map<String, Object> insertReply(
//			@Validated(InsertGroup.class) CardVO card
//			, BindingResult errors
//			, @RequestParam Map<String, Object> paramMap
//			
//			) {
//		ServiceResult result = null;
//		Map<String, Object> resultMap = new HashMap<>();
//		CardReplyVO data = null;
//		log.info("reply insert=========={}",card); 
//		if(!errors.hasErrors()) {
//			result = service.modifyCardDesc(card);
//			if(data !=null) {
//				result = ServiceResult.OK;
//			}else {
//				resultMap.put("message", "서버 오류");
//			}
//		}else {
//			result = ServiceResult.INVALID;
//			resultMap.put("message", "검증에 걸림");
//		}
//		resultMap.put("result", result);
//		resultMap.putAll(paramMap);
//		return resultMap;
//	}
	
	
	
	
	@GetMapping
	public String selectCard(
			@ModelAttribute CardVO card
			,Model model) {
		card = service.retrieveCard(card);
		log.info("card retrieveCard@@@@@@@@@@@@@@@@@@@@@@: {}",card);
		
		model.addAttribute("card",card);
		return "works/card/cardretrieve"; 
	}

	@PutMapping
	@ResponseBody
	public Map<String,Object> updateCard(
			@Validated(UpdateGroup.class) @ModelAttribute CardVO card
			, BindingResult errors
			, Model model		
			, RedirectAttributes redirectAttributes
			, @RequestParam Map<String,Object> paramMap
			,@RequestPart MultipartFile[] cardFiles  
			) {
		log.info("card############################### : {}",card);
		log.info("paramMap############################### : {}",paramMap);
		ServiceResult result = null;
		card.setCardFiles(cardFiles);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("message", "서버들어오기 성공 modify ");
		
		if(!errors.hasErrors()) {
			try {
				result = service.modifyCard(card);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("message", "서버오류");
			}
		}else{
			result = ServiceResult.INVALID;
			List<ObjectError> list = errors.getAllErrors();
			log.info("errorList======{}",list);
			resultMap.put("message","검증에 걸렸음"); 
		}
		
		resultMap.put("result", result);
		return resultMap;
		
	}
	
	@PostMapping("new") 
	@ResponseBody 
	public Map<String,Object> insertCard(
			@Validated(InsertGroup.class) @RequestBody CardVO card
			, BindingResult errors
			,@ModelAttribute("pathVO") PathVO path){
		log.info("Insert Cardinfo========================={}: ",card);
		
		ServiceResult result = null;
		Map<String,Object> resultMap = new HashMap<>();
		if(!errors.hasErrors()) {
			try {
				result = service.createCard(card);
			} catch (Exception e) {
				resultMap.put("message", "서버오류");
			}
		}else{
			result = ServiceResult.INVALID;
			resultMap.put("message","검증에 걸렸음");
		}
		log.info("after insert========================={}: ",card);
		KanbanItem<CardVO> wrapperCard = new KanbanItemWrapper(card);
		resultMap.put("result", result);
		resultMap.put("data",wrapperCard);
		return resultMap;
		
	}

	   

}
