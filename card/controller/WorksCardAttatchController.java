/**
 * 
 */
package kr.or.easybusy.works.card.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.easybusy.vo.CardAttatchVO;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.works.card.service.WorksCardService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 이기석
 * @since 2022. 5. 26.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                          수정자               수정내용
 * --------     --------    ----------------------
 * 2022. 5. 26.      작성자명       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */
@RequestMapping("/groupware/cardAttatch/{wlNo}/{wcNo}/{waNo}/{waFilename}")
@Controller
@Slf4j
public class WorksCardAttatchController {
	
	@Inject
	WorksCardService service;
	
	@GetMapping
	public void download(
			@ModelAttribute("pathVO") PathVO path
			,Model model
			,HttpServletResponse resp
	) {

		ServletOutputStream out;
		try {
			out = resp.getOutputStream();
			service.download(resp, out, path);
		} catch (IOException e) {
			e.printStackTrace();
			//뭘해줘야할까요?
		}
		log.info("여기는 다운로드~~~~~~");
		log.info("PathVO ================:{}",path);
//		model.addAttribute("attatch", cardAttatch);
		
	} 
	
}
