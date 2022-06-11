/**
 * <pre>
 * 
 * </pre>
 * @author 이기석
 * @since 2022. 5. 23.
 * @version 1.0
 * <pre>
 * [[개정이력(Modification Information)]] 
 * 수정일        수정자       수정내용
 * --------     --------    ----------------------
 * 2022. 5. 23.      이기석       최초작성
 * 2022. 5. 27.		 손효선		insertChecklistItem 추가
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */ 
	
//function insertChecklistItem(target){
//	let titleInput = $('.afterDeleteListArea .name_wrap');
////	titleInput.addClass('active');
//	let checkTitle = $(this).parents('.checkListData');
//	checkTitle.append('<div>hyodorie</div>')
//	console.log(checkTitle);
//}

 

$(document).on('click', '.checklist_plus', function(){
	let checkItemInput = ('<div class="d-flex checklist_input">');                                                    
		checkItemInput += ('<input type="text" class="form-control my-2 checklist_input_text" placeholder="체크 아이템 내용을 입력하세요">');              
		checkItemInput += ('<input type="button" class="btn btn-secondary name_edit_btn checklist_input_button" value="등록">'); 
		checkItemInput += ('</div>');
	
	$(this).parents('.checkListData').append(checkItemInput); 
	
	console.log('체크리스트 플러스버튼을 눌렀습니다.');
	
});

function insertCharge(target){
	let nameInput = $('.name_in_charge'); 
	let name = nameInput.val();
	console.log('clicked charge button')
	$('#chargeSection').find('.emp_charge').append('<span class="badge bg-secondary">'+name+'</span>');
}

//<input id="end' type=text wcend>
//<input type=text wcStart>
//<button     onclick="inserDate(this)"><button>

//function insertDate(target){
//	let wcStart = $(target).prevAll('#end')
//	let wcEnd = $(target).prevAll('#end')
//	
//	$('#dateForm input[name=wcStart]').val(wcStart);
//	$('#dateForm input[name=wcEnd]').val(wcEnd);
//	
//	$('#dateForm').ajaxForm().submit();
//	
//	
//}
//
//
//
//
//






/*1수정
 * 1)수정할 데이터 2개(시작날짜, 끝나는날짜)
 * 	1-1) FORM에다가 wcStart , wcEnd 넣는다
 * 	1-2) ajaxForm으로 보낸다
 * 
 * 
 *  1-3) 서버에서 데이터를 받아서 DAO.XML 까지 보내 query 실행
 *  1-4) 변경된 정보데이터를 가져온다.
 *  
 *  2) UI 수정
 *  3) 끝;
 * 
 *  
 *  
 */ 	 


//2삭제

	

//3등록