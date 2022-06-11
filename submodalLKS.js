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
 * 2022. 5. 23.      작성자명       최초작성
 * Copyright (c) 2022 by DDIT All right reserved
 * </pre>
 */ 

let afterInsertItemArea;
let afterInsertListArea;
let afterDeleteListArea;
let checkItemInput;
let afterUpdateDateArea;
let afterDeleteIncharge;


// 담당자 추가 Start===================
$(document).on('click','#add_charge',function(){
	console.log('empNo',empNo);
	let chargeSpan = $('.incharge-emp');
	console.log(chargeSpan);
	for(let eachCharge of chargeSpan){
		let chargeEmp = $(eachCharge).data('empNo');
		if(empNo == chargeEmp){
			alert("이미 카드 담당자로 등록되었습니다.");
			return false; 
		} 
	}
	
	
	
	
	$("#chargeForm input[name=_method]").val("put"); 
	$("#chargeForm input[name=empNo]").val(empNo);
	$("#chargeForm").ajaxForm(chargeForm).submit();
	
});
// 담당자 추가 End ===================

// 담당자 삭제 Start================
function deleteIncharge(target){
	let empNo = $(target).closest('.incharge-emp').data("empNo");
	afterDeleteIncharge = $(target).closest('.incharge-emp');
	$("#chargeForm input[name=_method]").val("delete"); 
	$("#chargeForm input[name=empNo]").val(empNo);
	$("#chargeForm").ajaxForm(chargeForm).submit(); 
}
// 담당자 삭제 End================

// 카드 제목 수정 Start==================
$(document).on('change',"#card_name_edit",function(){
	let wcTitle = $(this).val();
	let wcDesc = $('#cardDesc').val();
	$("#cardDesc_TitleForm input[name=wcTitle]").val(wcTitle); 
	$("#cardDesc_TitleForm input[name=wcNo]").val(wcNo);   
	$("#cardDesc_TitleForm input[name=wcDesc]").val(wcDesc);    
	$("#cardDesc_TitleForm").ajaxForm(normalOption).submit(); 
	$('#card_modal_title').text(wcTitle);       
	$(this).parents('.card_name_wrap.active').removeClass("active"); 
});

$(document).on('click',"#card_modal_title",function(){
	let wcTitle = $("#card_modal_title").text();
	$(".card_name_wrap").addClass("active");
	$("#card_name_edit").val(wcTitle); 
});
 

// 카드 제목 수정 End==================


//////////////////////////////날짜 CRUD
function insertDate(target){
	afterUpdateDateArea = $(".card_date_wrap");
	
	let inputDate = $(target).closest('.sub_modal').find('input');
	let wcStart = $(inputDate[0]).val(); 
	let wcEnd = $(inputDate[1]).val(); 
	wcStartTest = wcStart.slice(-5).replace('-','');
	wcEndTest = wcEnd.slice(-5).replace('-','');
	if(wcEndTest < wcStartTest){ 
		alert("종료일은 시작일보다 빠를수 없습니다.");
		return false;
	}
	$('#dateForm').find('input[name=wcStart]').val(wcStart);
	$('#dateForm').find('input[name=wcEnd]').val(wcEnd);
	$('#dateForm').find('input[name=wcDone]').val('N');
	$('#dateForm').ajaxForm(dateformOption).submit();  
	$('#card_date_label').html(wcStart+' ~ '+wcEnd); 
	$('.sub_modal.active').removeClass('active');
	 
} 
//////////////////////////////날짜 CRUD


$(document).on('click','.file_delete_btn',function(){
	let waNo = $(this).data("waNo");
	$(this).parents(".card_file_list").remove();  
	let newInput = $("<input>").attr({
						"type":"hidden"
						, "name":"delAttNos"
						, "value":waNo
					});
	$("#cardForm").append(newInput);
})


 
////////////////////////////체크리스트 CRUD////////////////////////////
function updateCheckList(){

	let wckNo = $("#asfdasdfasd").data("checklistId");
	$('#checkListForm input=[name=wckTitle]').val(wckTitle);
	$('#checkListForm input=[name=wckNo]').val(wckNo);
	$('#checkListForm').ajaxForm(checkListformOption).submit();
	$('.sub_modal.active').removeClass('active');
}

function deleteCheckList(target){   //체크리스트 삭제
	if(!confirm("하위 아이템들이 모두 삭제됩니다. 삭제합니까?")) return false;
	let wckNo = $(target).parents(".checkListData").data("checklistId");
	afterDeleteListArea = $(target).parents('.checkListData'); 
	$('#checkListForm input[name=wckNo]').val(wckNo);
	$('#checkListForm input[name=_method]').val("delete");
	$('#checkListForm').ajaxForm(checkListformOption).submit();  
	$('.sub_modal.active').removeClass('active');
	
}



$(document).on('click','#insertCheckList',function(){
//	let wckTitle = $(this).parents('.sub_modal').find('input').val();
	let wckTitle = $('#inputCheckList').val();
	
	$('#checkListForm input[name=_method]').val("post");
	$('#checkListForm input[name=wckTitle]').val(wckTitle);
	$('#checkListForm input[name=wcNo]').val(wcNo);
	$('#checkListForm').ajaxForm(checkListformOption).submit(); 
	$('#inputCheckList').val('');
	$('.sub_modal.active').removeClass('active'); 
});


$(document).on('change','#cardDesc',function(){    
	let wcDesc = $(this).val(); 
	let wcTitle = $("#card_modal_title").text();
	$("#cardDesc_TitleForm input[name=wcDesc]").val(wcDesc); 
	$("#cardDesc_TitleForm input[name=wcNo]").val(wcNo);
	$("#cardDesc_TitleForm input[name=wcTitle]").val(wcTitle);
	
	$("#cardDesc_TitleForm").ajaxForm(normalOption).submit();  
});
 



//====================================item save 등록하는 버튼에 들어갈꺼...========
$(document).on('click','.checklist_input_button',function(){
	console.log('this',this);    
	
	let wckNo = $(this).parents(".checkListData").data("checklistId");  
	afterInsertItemArea = $(this).closest(".checkListData");
	let checkItemText = $('.checklist_input_text').val();
	checkItemInput = $('.checklist_input');
	$("#checkItemForm input[name=wckCntTitle]").val(checkItemText);
	$("#checkItemForm input[name=_method]").val("post");
	$("#checkItemForm input[name=wckCntDone]").val("N");
	$("#checkItemForm input[name=wckNo]").val(wckNo);
	$("#checkItemForm").ajaxForm(itemformOption).submit();     
	
	$(this).closest('.checklist_input_text').empty();
	$(this).closest('.checklist_input').hide();
	console.log(wckNo+", "+checkItemText);

	
//====================================item save 등록하는 버튼에 들어갈꺼...========
});

$(document).on('click','.checkItem_delete_btn',function(){
	if(!confirm("해당 아이템을 삭제하겠습니까?")) return false;
//	alert(wckCntNo); 
	if($(this).parents('#checkListSection').attr("id")=='checkListSection'){
		let wckCntNo = $(this).prev().attr('for');
		tempdelete = $(this).parents(".checkItemData"); 
		$("#checkItemForm input[name=wckCntNo]").val(wckCntNo); 
		$("#checkItemForm input[name=_method]").val("delete");
	    $("#checkItemForm").ajaxForm(itemformOption).submit();  
	    $(this).parent('.checklist_content').hide(); 
	} else if($(this).parents('#dateSection').attr('id')=="dateSection"){  
		afterUpdateDateArea = $(this).parents(".card_date_wrap");
		$("#dateForm input[name=_method]").val("put");
		$("#dateForm input[name='wcStart']").val(null); 
		$("#dateForm input[name='wcEnd']").val(null);
		$("#dateForm input[name='wcDone']").val("N");  
		$("#dateForm").ajaxForm(dateformOption).submit();   
		
	}
})

function itemClick(target){
	let wckCntNo = $(target).data("itemId");
	let checkedFlag = $(target).prop("checked");
	let wckCntTitle = $(target).next().text();
	
	
	let wckCntDone = "N";
	if(checkedFlag){
		wckCntDone = "Y";
	} 
	 
	console.log("wckCntTitle",wckCntTitle) 
	$("#checkItemForm input[name=wckCntNo]").val(wckCntNo);
	$("#checkItemForm input[name=wckCntDone]").val(wckCntDone);
	$("#checkItemForm input[name=_method]").val("put"); 
	let ItemUpdateForm = $("#checkItemForm").ajaxForm(itemformOption).submit(); 
} 


let normalOption={
		dataType : "json",
		success :function(resp){
			if(resp.result=='FAIL'){ 
				alert(resp.message);
			}
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}


let itemformOption ={
		dataType : "json",
		success :itemSuccess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}

let checkListformOption = {
		dataType : "json",
		success : checklistSuccess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}
let dateformOption = {
		dataType : "json",
		success : dateformSuccess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}
let chargeForm = {
		dataType : "json",
		success : chargeformSuccess,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
}

function chargeformSuccess(resp){
	if(resp.result=="FAIL"){
		alert(resp.message);
	}
	if(resp.type=='delete'){
		afterDeleteIncharge.remove(); 
	}else if(resp.type=="insert"){
		let inCharge = ('<span class="badge bg-secondary incharge-emp" data-emp-no="'+resp.empNo+'">');
		inCharge += ('<a href="#" onclick="deleteIncharge(this)">');   
		inCharge += (resp.empName);
		inCharge += ('</a>');
		inCharge += ('</span>');
		$('.emp_charge').append(inCharge);
	}
	
	
}


function dateformSuccess(resp){
	if(resp['result']=='FAIL'){
		alert(resp.message);
		return false;
	}
	
//	alert(resp['type']);
	
	
	if(resp['type']=="delete"){
		afterUpdateDateArea.remove();
	}else if(resp['type']=='update'){ 
		card = resp.data;
		console.log(card);
		let newDate=('<div class="card_date_wrap">');
		newDate+=('<input id="card_date_chck" class="form-check-input" type="checkbox" onclick="dateCheck(this)">');
		newDate+=(' &nbsp;&nbsp;<label for="card_date_chck">'+card.wcStart+' ~ '+card.wcEnd+'</label>');
		newDate+=('<span class="badge bg-warning card_date_complete">complete</span>');
		newDate+=('<a href="#" class="dateSection checkItem_delete_btn mx-2"><i class="fa-solid fa-delete-left"></i></a>');
		newDate+=('</div>');
		$('#dateSection').append(newDate);
	}
}
 
function checklistSuccess(resp){
	if(resp['delete']){
		afterDeleteListArea.remove();
		countBoxes();
		countChecked();
		
	}else if(resp['insert']){
		let data = resp.data;
		let checkList=('<div class="position-relative checkListData" data-checklist-id="'+data.wckNo+'">');
		checkList+=('<div class="d-flex justify-content-between ">');
		checkList+=('<p>'+data.wckTitle +'</p>');
		checkList+=('<div class="checklist_option">');
		checkList+=('<button type="button" class="checklist_plus">');
		checkList+=('<i class="fa-solid fa-circle-plus"></i>');
		checkList+=('</button>');
		checkList+=('<button type="button" class="checklist_delete" onclick="deleteCheckList(this)">');
		checkList+=('<i class="fa-solid fa-circle-minus"></i>');
		checkList+=('</button>');
		checkList+=('</div>');
		checkList+=('</div>');
		checkList+=('</div>');
		
		$('.checkList-container').append(checkList);
		
	}
}

function itemSuccess(resp){
	if(resp['delete']){
		tempdelete.remove();
		countBoxes();
		countChecked();
	}else if(resp['insert']){
		let item = resp.data;
		checkItemInput.remove();
		
		let checkItem = ('<div class="d-flex checkItemData">');
		checkItem+=('<div class="checklist_content">');
		checkItem+=('<input class="form-check-input" type="checkbox" id="'+item.wckCntNo+'" data-item-id="'+item.wckCntNo+ '" data-item-title="'+item.wckCntTitle+'" >');
		checkItem+=('<label for="'+item.wckCntNo+'" class="check_items">'+item.wckCntTitle +'</label>');
		checkItem+=('<a href="#" class="checkItem_delete_btn mx-2"><i class="fa-solid fa-delete-left"></i></a>');
		checkItem+=('</div>');
		checkItem+=('</div>');
		
		$(afterInsertItemArea).append(checkItem); 
		countBoxes();
		countChecked();
	}
	 
}





 
























