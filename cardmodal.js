/**ponputun
 * <pre>
 * 
 * </pre>
 * @author 이기석
 * @since 2022. 5. 13.
 * @version 1.0
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일        수정자       수정내용
 * --------     --------    ----------------------
 * 2022. 5. 13.      이기석       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */ 

/*
1.form 태그를 만들어서 html 시킨다
 1) 주소랑 formAjax할 수있게 새팅해야함
 2) x표시를 눌수 있게하고 누르면 원래 상태(pTag)로 되돌려놔야함
 	2-1) 따로 eventHandler를 줘야하는데 
 	2-2) pTag를 수정 클릭시 전역변수가 리셋되야함
 3) submit을 누르면 ajax보내는 펑션 있어야함
 	1) url이 cardRetrieve.jsp 에 전역변수로 넣어놓기
 	2) _method put으로 해놓기 ※reply 수정 참고
    3) beforeSend 할때 채킹해야함 ???? submit 버튼 누를때 e.preventDefault 하고 체킹하고 넘겨야할듯.
 *
 */
var textTag;
var textChange = false;
var tempWcContent;
var tempdelete;
let replyUpdateForm = $("#replyForm")



//보드 삭제 Start================================

//보드 삭제 End==================================




//리스트 삭제 Start===============================
 $(document).on('click','.listDelete',function(){
//	 if(!(empRole=="ROLE_ADMIN" || empRole=="ROLE_MANAGER")) {
//		 alert("삭제권한은 관리자에게만 있습니다.");
//		 return false;
//	 } 
	 if(!confirm("하위카드들이 모두 삭제됩니다. 삭제하시겠습니까?")) return false;
	 let wlNo = $(this).parents('.kanban-board').data('id');  
	 console.log('KanbanTest',KanbanTest);
	  
	 
	 
	 $.ajax({
			url : wlistUrl+'/'+wlNo, 
			method : "delete",  
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success : function(resp){
				if(resp.result=='FAIL'){
					alert("서버오류 잠시후 다시 시도해주세요");
				}
				KanbanTest.removeBoard(wlNo+'');   
				
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
				console.log(textStatus); 
				console.log(errorThrown);
			}
		});
	 
	 
 });


//리스트 삭제 End===============================


//카드삭제 start=============================
$(document).on('click','#card_delete',function(){ 
	if(!confirm('삭제하시겠습니까?')) return false; 
	$('#cardForm input[name=_method]').val('delete'); 
	$('#cardForm').ajaxForm(cardFormOption).submit();  
});
let cardFormOption = {
		dataType : "json",
		success : cardFormSucess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}
function cardFormSucess(resp){ 
	
	if(resp.result=="INVALID"){
		alert("삭제권한은 관리자에게만 있습니다.");
		swal({ 
            title: "Warning",
            text: "삭제권한은 관리자에게만 있습니다.",                    
            className : 'swal-wide', //커스텀 사이즈
            icon: "warning", //success, warning, error
            button: "확인"
        });
		return false; 
	} 
	$('#card_modal').modal('hide'); 
	KanbanTest.removeElement(resp.wcNo);    
}
//카드삭제 end=============================

///////////////////////////////////////////////댓글 crud Start ///////////////////////////////////////////////////
function appendInsertedReply(reply){
	let InsertedReply;
	InsertedReply = ('<div class="d-flex row py-2 m-1 border-bottom">');
	InsertedReply +=('<span class="col-2">'+reply.empName+'</span>');
	InsertedReply +=('<div class="col-10 d-flex justify-content-between" id="replyTextArea">');
	InsertedReply +=('<p style="width:70%">'+reply.wrContent+'</p>');
	InsertedReply +=('<div class="text-secondary text-end" style="font-size:13px;">');
	InsertedReply +=(''+reply.wrDate+'<br>');
	InsertedReply +=('<a data-reply-id="'+reply.wrNo+'" onclick="updateReply(this)" href="#">수정</a> |');
	InsertedReply +=('<a href="#" onclick="deleteReply(this)">삭제</a>');
	InsertedReply +=('</div>');		
	InsertedReply +=('</div>');
	InsertedReply +=('</div>');	
	 
	console.log("InsertedReply",InsertedReply);
	$("#replyDIV").prepend(InsertedReply); 
	$("#replyTextArea").val('');
}

function insertReply(target){
	let empNo = $('#replyTextArea').data('empId');
	let wrContent = $('#replyTextArea').val();
	if(wrContent.trim()==''){
/*		alert("내용없이 댓글을 달 수 없습니다.");*/
		swal({ 
            title: "Warning",
            text: "내용없이 댓글을 달 수 없습니다.",                    
            className : 'swal-wide', //커스텀 사이즈
            icon: "warning", //success, warning, error
            button: "확인"
        });
		return false;
	}
	$("#replyForm input[name=empNo]").val(empNo);
	$("#replyForm input[name=wrContent]").val(wrContent);
	let replyUpdateForm = $("#replyForm").ajaxForm(formOption).submit();
	
}

function deleteReply(target){
	tempdelete = $(target).parents('.border-bottom');
	if(!confirm("댓글을 삭제합니다")) return false;
	let wrNo = $(target).prev().data('replyId');
	$("#replyForm input[name=wrNo]").val(wrNo)
	$("#replyForm input[name=_method]").val("delete");
	let replyUpdateForm = $("#replyForm").ajaxForm(formOption).submit();
	
}


$(document).on('click',".reply_delete_btn",function(){ //댓글 x 누르면 사라지게 하는 핸들러 ===start===
	$(this).parents("#replyTextArea").html(textTag);
	textChange=false;
})   													//댓글 x 누르면 사라지게 하는 핸들러 ====end====


let formOption ={
		dataType : "json",
		success :replySuccess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
	}

function replySuccess(resp){
	if(resp.result == "OK"){
		if(resp['delete']){
			tempdelete.remove();
		}else if(resp['insert']){
			appendInsertedReply(resp.data);
		}
		$("#replyForm").get(0).reset();
		let replyTextArea = $("#replyDeleteBtn").parent();
		$("#replyDeleteBtn").trigger("click");
		replyTextArea.find("p").text(tempWcContent);
	}else if(resp.message){ 
		alert(resp.message);
	}
}

function replysave(target){
	let input = $(target).prevAll("input[type=text]"); 
	let wrNo = input.data("replyNo");
	let wrContent = input.val();
	if(wrContent.trim()==''){
		alert("아무내용 없이 댓글을 수정할 수 없습니다.");
		return false;
	}

	$("#replyForm input[name=wrNo]").val(wrNo)
	$("#replyForm input[name=wrContent]").val(wrContent);
	$("#replyForm input[name=_method]").val("put");
	tempWcContent=wrContent;
	let replyUpdateForm = $("#replyForm").ajaxForm(formOption).submit();
}  

													 

function createReplyInput(wrContent,wrNo){													// 댓글 수정누를시 생겨나는 UI ===start===
	let replyTag = [];	
	let inputTag = '<input type="text" class="form-control" style="height : 35px" data-reply-no="'+wrNo+'" value="'+wrContent+'"/>';        
	let replyCancleBtn = '<span class="reply_delete_btn" id="replyDeleteBtn"><i class="fa-solid fa-xmark"></i></span>';
	let replySaveBtn = '<button type="button" class="btn btn-secondary addBtn float-end"  onclick="replysave(this)">save</button>';  
	replyTag.push(inputTag);
	replyTag.push(replyCancleBtn);
	replyTag.push(replySaveBtn);
	return replyTag;
}																							// 댓글 수정누를시 생겨나는 UI ====end====
	

function updateReply(target){							// 댓글 수정누를시 로직  ===start===
	if(textChange){ 
		$("#replyDeleteBtn").trigger("click"); 
 	}
	textChange = true;
	let wrContent= $(target).parents("#replyTextArea").find("p").text();
	let wrNo = $(target).data('replyId');
	textTag = $(target).parents("#replyTextArea").children();
	let textTagParent = $(target).parents("#replyTextArea");
	let ReplyTag =createReplyInput(wrContent,wrNo);
	textTagParent.html(ReplyTag);
	textTagParent.find("input").focus().select();  
	ReplyTag.length = 0;
}														// 댓글 수정누를시 로직  ====end====

///////////////////////////////////////////////댓글 end 수정///////////////////////////////////////////////////

function dateCheck(target){ 
	//날짜 클릭했을때 complete 나오는 ajtem
	var checked = $(target).prop("checked");  
	console.log('checked',checked);
	   if(!checked){ 
		   $('.card_date_complete').removeClass('active');  
	   }else{
		   $('.card_date_complete').addClass('active');         
	   }
	   
	let done = $(".checkDone");
	let doneval = done.val().trim();    
	console.log("doneval",doneval);
	if(doneval=="Y"){
		done.val("N");  
	}else if(doneval=="N"){
		done.val("Y"); 
	} 
	
	$('#dateForm input[name="wcDone"]').val(done.val());  
	$("#dateForm").ajaxForm({
								dataType : "json",
								success : function(resp){
								},
								beforeSend : function(xhr) {
									xhr.setRequestHeader(header, token);
								},
							}).submit();    

}
 
 


function updateSuccess(resp){
		if(resp.result=='FAIL'){
			alert(resp.message);
		}
		$("#card_modal").modal('hide');  
		
	}

$(document).on('click',"#cardUpdateBtn",function(){
	 
	var cardForm = document.getElementById('cardForm');  
	let formData = new FormData(cardForm);  
	console.log('cardForm',formData);  
	$.ajax({
		url : updateCardUrl,
		method : "put",
		data : formData,
		contentType: false,
		success : updateSuccess,
		processData : false,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR);
			console.log(textStatus);
			console.log(errorThrown);
		}
	}); 
	
}); 



function getCardViewModal(cardNo,cardMap,KanbanTest){
	console.log("test",cardNo);
	card = cardMap[cardNo];
	let url = cardUrl+"/"+cardNo
//	let element = KanbanTest.findElement(cardNo);
	$.ajax({
		url : url,
		method : "get",
		dataType : "html",
		success : function(resp) {
			$(".modal-content.card").html(resp);  
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR);
			console.log(textStatus);
			console.log(errorThrown);
		}
	});
}
 
$("#card_modal").on("hidden.bs.modal", function(){
	let cardNo = $('#card').data('cardId');
	let url = cardUrl+"/"+cardNo+"/afterClose"; 
	$.ajax({
		url : url,
		method : "get",
		dataType : "json",
		success : function(resp) {
			
			cardMap[resp.wcNo]= resp;    
			editCardDiv(resp);
		}
		
	});
});

//=============================== 카드 상세조회 start =============================

///////////////////////////////////////////////////////////
//let modalClass;
//let closeBtn;
//let submitBtn;  
//$(document).on('click', ".addBtn", function(){
//		let addBtn = $(this).attr("id").replace("add_","");
//		modalClass = $('.sub_modal_'+addBtn);
//		closeBtn = modalClass.find('.btn-close');
//		submitBtn = modalClass.find('.modal_submit');
//
//		switch(addBtn){
//		case "charge":
//			$('.sub_modal_charge').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "date":
//			$('.sub_modal_date').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "check": 
//			$('.sub_modal_check').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "location":
//			$('.sub_modal_location').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "label":
//			$('.sub_modal_label').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "subscribe":
//			$('.sub_modal_subscribe').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		case "share":
//			$('.sub_modal_share').toggleClass('active');
//			console.log(addBtn+"을(를) 등록하였습니다");
//			break;
//		}
//		
//});
//
//$(document).on('click',submitBtn, function(){
//	closeBtn.trigger('click');
//})
//$('.modal_cancel').on('click', function(){
//	closeBtn.trigger('click');
//});
//closeBtn.on('click', function(){
//	modalClass.removeClass('active');
//});

//=============================== 카드 상세조회 end =============================



