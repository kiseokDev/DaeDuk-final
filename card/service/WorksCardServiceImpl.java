package kr.or.easybusy.works.card.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import kr.or.easybusy.enump.ServiceResult;
import kr.or.easybusy.vo.CardAttatchVO;
import kr.or.easybusy.vo.CardCheckList;
import kr.or.easybusy.vo.CardReplyVO;
import kr.or.easybusy.vo.CardVO;
import kr.or.easybusy.vo.CheckItemVO;
import kr.or.easybusy.vo.PathVO;
import kr.or.easybusy.works.card.dao.AttatchDAO;
import kr.or.easybusy.works.card.dao.WorksCardDAO;
import lombok.extern.slf4j.Slf4j;
import oracle.security.crypto.core.SREntropySource;
@Service
@Slf4j
public class WorksCardServiceImpl implements WorksCardService {
	
	@Inject
	private WorksCardDAO dao;
	
	@Inject
	private AttatchDAO attatchDAO;
	
	
	private FTPClient ftpClient = new FTPClient();
	
	
	private String serverIp = "192.168.35.19";
	private int serverPort = 21;
	private String user = "JHR";
	private String password = "java";

	
	private String LAttFname;
	private String SAttFname;
	
	
	
	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#retrieveCard(kr.or.easybusy.vo.CardVO)
	 */
	@Override
	public CardVO retrieveCard(CardVO card) {
		return dao.selectCard(card);
	}
	@Override
	public ServiceResult createCard(CardVO card) {
			dao.insertCard(card);
			card = dao.insertedCard(card);
		return card == null ? ServiceResult.FAIL : ServiceResult.OK;
	}
	@Override
	public ServiceResult modifyReply(CardReplyVO reply) {
		int rowcnt = dao.updateReply(reply);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	@Override
	public ServiceResult removeReply(CardReplyVO reply) {
		int rowcnt = dao.deleteReply(reply);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	@Override
	public CardReplyVO createReply(CardReplyVO reply) {
		dao.insertReply(reply);
		return dao.selectInsertedReply(reply);
	}
	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#modifyCheckItem(kr.or.easybusy.vo.CardCheckContentVO)
	 */
	@Override
	public ServiceResult modifyCheckItem(CheckItemVO item) {
		log.info("????????? ???????????? ????????????@@@@@@@@@@@@@");
		log.info("item===={}",item);
		
		int rowcnt = dao.updateCheckDone(item);
		
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	@Override
	public ServiceResult removeItem(CheckItemVO item) {
		int rowcnt = dao.deleteCheckItem(item);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#createCheckItem(kr.or.easybusy.vo.CheckItemVO)
	 */
	@Override
	public CheckItemVO createCheckItem(CheckItemVO item) {
		int rowcnt = dao.insertCheckItem(item);
		if(rowcnt == 0) {
			return null;
		}
		return dao.insertedCheckItem(item);
	
	}
	@Override
	public ServiceResult modifyCardDesc(CardVO card) {
		int rowcnt = dao.updateCardDesc(card);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	
	
	
	
	
	//??????????????? ?????? ?????? ??????         START ================ 

	@Override
	public ServiceResult modifyCheckList(CardCheckList list) {
		int rowcnt = dao.updateCheckList(list);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	@Override
	public CardCheckList createCheckList(CardCheckList list) {
		int rowcnt = dao.insertCheckList(list);
		if(rowcnt == 0) {
			return null;
		}
		return dao.selectInsertedCheckList(list);
	}
	@Override
	public ServiceResult removeCheckList(CardCheckList list) {
		int rowcnt = dao.deleteItemsUnderList(list);
		rowcnt = dao.deleteCheckList(list);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL; 
	}
	//??????????????? ?????? ?????? ?????? 	  END   ================
	
		


	@Override
	public void uploadAttatch(CardVO card) throws Exception {
//		List<CardAttatchVO> attaches = card.getAttatchList();
		MultipartFile[] files = card.getCardFiles();
//		log.info("upload????????? ======================!======={}",files.length);  
		if(files == null || files.length==0) return;
//		log.info("upload????????????????????????????????"); 
		InputStream fis = null;
		this.ftpClient.setAutodetectUTF8(true);
		try {
			
			ftpClient.connect(serverIp, serverPort); // ftp ??????
			ftpClient.setControlEncoding("utf-8"); // ftp ???????????????
			int reply = ftpClient.getReplyCode(); // ??????????????????

			if (!FTPReply.isPositiveCompletion(reply)) { // ????????? false ?????? ?????? ?????? exception ??????
				ftpClient.disconnect();
				throw new Exception(serverIp + " FTP ?????? ?????? ??????");
			}
			
			ftpClient.setSoTimeout(1000 * 10); // timeout ??????
			ftpClient.login(user, password); // ftp ?????????
			//LibraryVO(rnum=0, libNo=69, empNo=1, comNo=1, libTitle=asd, libDate=null, libType=1, libDel=null, startLAttNo=0, delAttNos=null, libFiles=null)
			//attatch : null
			
			
			log.info("??????????????? ?????????");

			// ???????????? ??????
			// ????????????
			LAttFname = "/"+card.getWlNo();
			
			// ????????? ??????
			SAttFname = "/"+card.getWcNo();
				
				try {
					ftpClient.makeDirectory(LAttFname);
					ftpClient.makeDirectory(LAttFname+SAttFname);
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			String workingDirectory = ftpClient.printWorkingDirectory();
			ftpClient.changeWorkingDirectory(workingDirectory+LAttFname+SAttFname);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // ??????????????????
//				ftpClient.enterLocalActiveMode(); //active ?????? ??????
			ftpClient.enterLocalPassiveMode();
//			FTPFile[] ftpfiles = ftpClient.mlistDir();
//			for (FTPFile file : ftpfiles) {				
//				if(saveName.equals(file.getName())) {
//					log.info("?????? ????????? ???????????????.");
//					return ServiceResult.EQFILE;
//				}
//				log.info("?????? ?????? ?????? : {}", file.getName());
//			}
//			log.info("?????????");

			attatchDAO.insertAttaches(card);
			
			MultipartFile[] multipartFiles = card.getCardFiles();
			fis = multipartFiles[0].getInputStream();
//			
//			attaches.forEach((file)->{
//				try {
//					ftpClient.storeFile(file.getWaFilename(), fis);
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//			});
//			
			for(MultipartFile eachMultipart :multipartFiles) { 
				ftpClient.storeFile(eachMultipart.getOriginalFilename(), fis);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
			if (fis != null) {
				fis.close();
			}
		}
	
		
	}
	
	

	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#download(kr.or.easybusy.vo.PathVO)
	 */
	@Override
	public void download(HttpServletResponse resp, OutputStream os, PathVO path) {
		String fileName = path.getWaFilename(); 
		ftpClient.setAutodetectUTF8(true);
		
		InputStream in = null;
		  try {
		        ftpClient.setControlEncoding("UTF-8");
		        
		        ftpClient.connect(serverIp, serverPort);
		        ftpClient.login(user, password);
		        String workingDirectory = ftpClient.printWorkingDirectory();
		        ftpClient.changeWorkingDirectory(workingDirectory);
	
		        // db??? ????????? ????????? ??????????????? ???????????????
     
		        String SaveName = path.getWlNo()+"/"+path.getWcNo()+"/"+fileName;
		        log.info("waFilename===:{}",path.getWaFilename());
		        log.info("fileName==:{}",fileName); 
		        log.info("SaveName===:{}",SaveName);
//		        System.out.println(fileName);
//		        System.out.println(SaveName);
		      
		        File pathFile = new File("D:\\A_TeachingMaterial\\files",SaveName);
//		        System.out.println(ext);
		        try {
//		            boolean isSuccess = ftpClient.retrieveFile(fileName, os);
//		            if (isSuccess) { 
		        	
		        		fileName = URLEncoder.encode(fileName,"utf-8");
		        		fileName = fileName.replace("+", " ");
		            	resp.setHeader("Content-Disposition", "attatchment;filename=\""+fileName+"\"");
		            	resp.setHeader("Content-Length", pathFile.length()+"");
		            	os = resp.getOutputStream();  
		               
//		    			int i;
//		    			while ((i = in.read()) != -1) {
//		    				os.write(i);
//		    			}
		            	
		            	FileUtils.copyFile(pathFile, os);
		                
		                System.out.println(pathFile);
		                System.out.println("??????");
		    		
		        } catch(IOException ex) {
		            System.out.println(ex.getMessage());
		        } finally {
		            if (os != null) try { os.close(); } catch(IOException ex) {}
		        }
		        ftpClient.logout();
		    } catch (SocketException e) {
		        System.out.println("Socket:"+e.getMessage());
		    } catch (IOException e) {
		        System.out.println("IO:"+e.getMessage());
		    } finally {
		        if (ftpClient != null && ftpClient.isConnected()) {
		            try { ftpClient.disconnect(); } catch (IOException e) {}
		        }
		    }
		
	}
	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#deleteAttatches(kr.or.easybusy.vo.CardVO)
	 */
	@Override
	public void deleteAttatches(CardVO card) throws Exception {
		int[] delAttNos = card.getDelAttNos();
		if(delAttNos==null || delAttNos.length==0) return;
		List<String> saveNames = Arrays.stream(delAttNos).mapToObj((delAttno)->{
						return attatchDAO.selectAttach(delAttno).getWaFilename();
					}).collect(Collectors.toList());
					
		attatchDAO.deleteAttaches(card);
		
		saveNames.forEach((saveName)->{
			String savePath =card.getWlNo()+"/"+card.getWcNo()+"/"+saveName;
			FileUtils.deleteQuietly(new File("D:\\A_TeachingMaterial\\files",savePath));
		});
		
	}
	/* (non-Javadoc)
	 * @see kr.or.easybusy.works.card.service.WorksCardService#modifyCard(kr.or.easybusy.vo.CardVO)
	 */
	@Override
	public ServiceResult modifyCard(CardVO card) throws Exception {
		deleteAttatches(card);
		uploadAttatch(card); 
		int rowcnt = dao.updateCard(card);
		return rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
	}
		
	
	
}
