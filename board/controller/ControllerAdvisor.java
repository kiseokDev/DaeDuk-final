package kr.or.easybusy.works.board.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;  

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerMapping;

import kr.or.easybusy.employee.service.employeemanagementService;
import kr.or.easybusy.vo.EmployeeVO;
import kr.or.easybusy.vo.PathVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.management.ManagementService;

@ControllerAdvice(basePackages= {"kr.or.easybusy"})
@Slf4j
public class ControllerAdvisor {
	
	public static final String PathVO = "pathVO";
	
	@Inject
	private employeemanagementService service;
	
	@ModelAttribute(PathVO)
	public PathVO aopTest(
			HttpServletRequest req
			) throws IllegalAccessException, InvocationTargetException {
		
		
		//1. Athentication -->sucess Handler 에서 
		
		
		//여기서 BoardPathVO 만들기?
//		Object[] args = jointPoint.getArgs();
		//여기서 BoardPathVO proceed에 넘겨주기
//		Object result = jointPoint.proceed(args);
		
		

		Map<String,Object> map = (Map<String, Object>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		log.info("login info : {}", map);
		PathVO pathVO = new PathVO();
		BeanUtils.populate(pathVO, map);
		
		if(map.get("empNo")!=null && StringUtils.isNumeric((String)map.get("empNo"))){
			EmployeeVO empInfos = service.retrieveEmp(Integer.parseInt((String) map.get("empNo")));
			pathVO.setEmpInfos(empInfos);
		}
//		service.retrieveEmp();
		
		
//		try{
//	        for(Field field : pathVO.getClass().getDeclaredFields()){
//	            field.setAccessible(true);
//	            String name = field.getName();
//	            Class<?> type = field.getType();
//	            String replacedType = type.toString().replace("class java.lang.", "");
//	            if(map.containsKey(name) && replacedType.equals("Integer")) {
//	            	pathVO.setComNo(Integer.parseInt((String) map.get(name)));
//	            }else if(map.containsKey(name) && replacedType.equals("String")){
//	            	pathVO.setOpenYn((String)map.get(name));
//	            }
//	        }    
//	    }catch(Exception e){
//	        log.info("pathVO 변수, 값 추출 에러");
//	    }
		
		return pathVO;
		
		
//		WorksBoardVO tmp = new WorksBoardVO();
//		tmp.setComNo(map.get("comNo"));
		
//		Enumeration<String> enumeration = req.getAttributeNames();
//		while (enumeration.hasMoreElements()) {
//			String string = (String) enumeration.nextElement();
//			log.info("Key:{} |||||| value:{}",string,req.getAttribute(string));
//			
//		} 
		
		
	}
	
}
//1.Model 
//2.exception ==> after weiving

//그러면 저기 