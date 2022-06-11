<%--
* [[개정이력(Modification Information)]]
* 수정일                 수정자      수정내용
* ----------  ---------  -----------------
* 2022. 5. 18.      작성자명      최초작성
* Copyright (c) 2022 by DDIT All right reserved
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>


<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.realUser" var="authEmp" />
	<c:set value="${authEmp.empNo}" var="empNo" />
	<c:set value="${authEmp.comNo}" var="comNo" />
	<c:set value="${authEmp.empName}" var="empName" />
	<c:set value="${authEmp.empSelect}" var="empRole" />
</security:authorize>

${emp }
<div class="modal-header" id="card" data-card-id="${card.wcNo }">
	<h5 class="modal-title" id="card_modal_title">${card.wcTitle}</h5>
	<div class="card_name_wrap">
		<div class="d-flex">
			<input type="text" id="card_name_edit" class="form-control my-2 name_edit" /> 
<!-- 			<input type="button" class="btn btn-secondary name_edit_btn" value="등록"> --> 
		</div>
	</div>
	<button type="button" class="btn-close card-close" data-bs-dismiss="modal" aria-label="Close"></button>
</div>
<div class="modal-body">
	<div class="contents_wrap p-3">
		<div
			class="add_btns d-flex justify-content-between align-items-center">
			<label for="" class="form-label fw-bold"><i class="fa-solid fa-file-circle-plus"></i> 항목 추가</label>
			<div class="text-center">
				<button class="btn addBtn btn-outline-primary" id="add_charge">담당자</button>
				<button class="btn addBtn btn-outline-primary" id="add_date">기간</button>
				<button class="btn addBtn btn-outline-primary" id="add_check">체크리스트</button>
				<!-- <button class="btn addBtn btn-outline-primary" id="add_label">라벨</button> 
					<button class="btn addBtn btn-outline-primary" id="add_subscribe">구독</button>
					<button class="btn addBtn btn-outline-primary" id="add_share">공유</button> -->
<!-- 				<button class="btn addBtn btn-outline-danger" id="add_check_test" onclick="insertCheckList(this)">체크리스트추가테스트</button> -->
			</div>
		</div>
		<form:form modelAttribute="card" id="cardForm" enctype="multipart/form-data">
			<input type="hidden" name="_method" >
			<input type="hidden" name="wcTitle" value="${card.wcTitle }" />
			<input type="hidden" name="wcNo" value="${card.wcNo}" /> 
			<input type="hidden" name="wlNo" value="${card.wlNo}" />   
			<input type="hidden" name="empRole" value="${empRole }"/>     
			<input type="hidden" name="wcStart" value="${card.wcStart}">
			<input type="hidden" name="wcEnd" value="${card.wcEnd}">
			<input type="hidden" class="checkDone" name="wcDone" value="${card.wcDone eq 'Y' ? 'Y':'N'}"> 
			<section id="descSection">
				<label class="form-label fw-bold"><i class="fa-solid fa-align-left"></i> 설명</label>
				<form:textarea path="wcDesc" class="form-control" id="cardDesc" rows="3"></form:textarea>
			</section>
			<section id="dateSection">
				<label class="form-label fw-bold"><i class="fa-solid fa-calendar-days"></i> 기간</label>
				<c:if test="${not empty card.wcEnd  && not empty card.wcStart}">
					<div class="card_date_wrap">
						<input id="card_date_chck" class="form-check-input" type="checkbox" ${card.wcDone eq "N" ? "":"checked"} onclick="dateCheck(this)" />
							 &nbsp;&nbsp;<label id="card_date_label" for="card_date_chck">${card.wcStart}  ~  ${card.wcEnd}</label>
						<!-- ★★★★★★★★★★★★★★★★★★★★1.날짜UI  -->
						<span class="badge bg-warning card_date_complete">complete</span>
						<a href="#" class="dateSection checkItem_delete_btn mx-2"><i class="fa-solid fa-delete-left"></i></a>
					</div>
				</c:if>
			</section>
			<section id="chargeSection">
				<label class="form-label fw-bold"><i class="fa-solid fa-user"></i> 담당자</label>
				<div class="mx-3 emp_charge">
				<c:if test="${not empty card.empList }">
					<c:forEach items="${card.empList }" var="emp" >
						<span class="badge bg-secondary incharge-emp" data-emp-no="${emp.empNo}">
							<c:if test="${emp.empNo == pathVO.empNo }"> 
							<a href="#" onclick="deleteIncharge(this)">   
								${emp.empName }
							</a>
							</c:if>
							<c:if test="${emp.empNo != pathVO.empNo}">
								${emp.empName }
							</c:if>
						</span>
					</c:forEach>				
<!-- 					<span class="badge bg-secondary">이순신</span> -->
<!-- 					<span class="badge bg-secondary">성춘향</span> -->
				</c:if>
				</div>
			</section>
			<section id="checkListSection">
			<label class="form-label fw-bold"><i class="fa-solid fa-list-check"></i> 체크리스트</label>
					<div class="progressbar-container">
						<div class="progressbar-bar" id="progressbar">
							<span class="progressbar-label">50%</span>
						</div>
					</div>
					<div class="checkList-container">
						<c:if test="${not empty card.checkList }">
							<div class="afterDeleteListArea">
								<c:forEach items="${card.checkList }" var="check" varStatus="st">
									<div class="position-relative checkListData"
										data-checklist-id="${check.wckNo }">
										<div class="d-flex justify-content-between ">
											<p>${check.wckTitle }</p>
											<div class="checklist_option">
												<button type="button" class="checklist_plus">
													<i class="fa-solid fa-circle-plus"></i>
												</button>
												<button type="button" class="checklist_delete" onclick="deleteCheckList(this)">
													<i class="fa-solid fa-circle-minus"></i>
												</button>
											</div>
										</div>
										<c:forEach items="${check.checkItemList}" var="checkCont" varStatus="status">
											<c:if test="${not empty checkCont.wckCntNo }">
												<div class="d-flex checkItemData">
													<div class="checklist_content">
														<input class="form-check-input" type="checkbox"
															id="${checkCont.wckCntNo }"
															data-item-id="${checkCont.wckCntNo }"
															data-item-title="${checkCont.wckCntTitle }"
															${ checkCont.wckCntDone eq "N" ? "":"checked" }
															onclick="itemClick(this)"> 
															<label for="${checkCont.wckCntNo }" class="check_items">${checkCont.wckCntTitle }</label>
														<a href="#" class="checkListSection checkItem_delete_btn mx-2"><i class="fa-solid fa-delete-left"></i></a>
													</div>
												</div>
											</c:if>
										</c:forEach>
									</div>
								</c:forEach>
							</div>
						</c:if>
					</div>
			</section>
			<section id="uploadSection" style="margin-bottom: 0">
				<label class="form-label fw-bold"><i class="fa-solid fa-download"></i> 파일 첨부</label> 
				<input type="file" class="form-control" name="cardFiles" multiple="multiple" />
				<c:if test="${not empty card.attatchList }">
					<!-- <label class="form-label">다운로드</label> -->
					<div class="form-control bg-light download_area">
						<small class="d-block">다운로드 파일 목록</small>
						<c:forEach items="${card.attatchList}" var="attatch" varStatus="vs">
							<c:url
								value="/groupware/cardAttatch/${card.wlNo}/${card.wcNo}/${attatch.waNo }/${attatch.waFilename}"
								var="downloadURL" />
							<div class="d-inline-block border-bottom card_file_list">
								<a href="${downloadURL }">${attatch.waFilename }</a> 
								<span class="file_delete_btn" data-wa-no="${attatch.waNo}">
									<i class="fa-solid fa-xmark"></i>
								</span>
								<!-- 7. 파일삭제 x 표시 ★ -->
							</div>
						</c:forEach>
					</div>
				</c:if>
			</section>
			<section class="mt-5">
				<label for="" class="form-label fw-bold">
					<i class="fa-solid fa-comment-dots"></i> 댓글 <span id="comment_count"></span>
				</label>
				<div class="d-flex border-top pt-3 mb-3 row align-items-center">
					<span class="col">${pathVO.empInfos.empName}</span>
					<div class="col-8">
						<textarea class="form-control d-inline-block" id="replyTextArea"
							data-emp-id='${pathVO.empNo }' rows="3"></textarea>
					</div>
					<button type="button" onclick="insertReply(this)"
						class="btn btn-primary col" style="min-width: unset; height: 80px;">등록</button>
					<!-- 8. 댓글입력칸수정 취소 -->
					<!-- <button type="reset" class="btn btn btn-danger col " style="min-width:unset; height: 80px;">취소</button> -->
					<!-- 8. 댓글입력칸수정 취소 -->
				</div>
				<div id="replyDIV" style="width: 100%">
					<c:if test="${not empty card.replyList }">
						<c:forEach items="${card.replyList }" varStatus="vs" var="reply">
							<div class="d-flex row py-2 m-1 border-bottom">
								<span class="col-2">${ reply.empName} </span>
								<!-- 10. 댓글 작성자 ui 위치랑 아이콘? 표시 왼쪽으로 ..  -->
								<div class="col-10 d-flex justify-content-between" id="replyTextArea">
									<p style="width: 70%">${reply.wrContent}</p>
									<div class="text-secondary text-end" style="font-size: 13px;">
										${reply.wrDate }<br>
										<c:if test="${reply.empNo == pathVO.empNo}">
											<a data-reply-id="${reply.wrNo }" onclick="updateReply(this)" href="#">수정</a> |
											<a href="#" onclick="deleteReply(this)">삭제</a>
										</c:if>
									</div>
									<!-- 9. 작성된 댓글 삭제 수정 표시  -->
								</div>
							</div>
						</c:forEach>
					</c:if>
				</div>
			</section>
		</form:form>

	</div>
	<div class="d-flex justify-content-end mt-3">
		<button class="btn btn-outline-danger" id="card_delete">카드 삭제</button>
		<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
		<button type="button" id="cardUpdateBtn" class="btn btn-primary">수정</button>
	</div>
</div>

<%-- 




<div class="sub_modal sub_modal_charge">
		<div class="modal-header">
			<h5 class="modal-title">담당자 추가</h5>
			<button type="button" class="btn-close sub_close"></button>
		</div>
		<form action="">
			<div class="modal-body">
				<small>구성원 검색</small> <input class="form-control name_in_charge" type="text"
					name="name_in_charge" />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary modal_cancel">취소</button>
				<button type="button" class="btn btn-primary modal_submit" onclick="insertCharge(this)">등록</button>
			</div>
		</form>
	</div> --%>

	<div class="sub_modal sub_modal_date">
		<div class="modal-header">
			<h5 class="modal-title">기간 추가</h5>
			<button type="button" class="btn-close sub_close"></button>
		</div>
		<form action="">
			<div class="modal-body">
				<small>시작일</small> <input class="form-control" type="date" /> <small>종료일</small>
				<input class="form-control" type="date" />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary modal_cancel">취소</button>
				<button type="button" class="btn btn-primary modal_submit" onclick="insertDate(this)">등록</button> 
			</div>
		</form>
	</div>


	<!-- ============================== 카드 모달창 end ============================= -->
	<!-- 	날짜입력 모달-->

	<div class="sub_modal sub_modal_check">
		<div class="modal-header">
			<h5 class="modal-title">체크리스트 추가</h5>
			<button type="button" class="btn-close sub_close"></button>
		</div>
		<form action="">
			<div class="modal-body">
				<div class="d-inline-block w-100">
					<input type="text" placeholder="체크리스트 제목을 입력해주세요" class="form-control d-inline-block" id="inputCheckList"/>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary modal_cancel">취소</button>
				<button type="button" class="btn btn-primary modal_submit" id="insertCheckList">등록</button> 
			</div>
		</form>
	</div>


<form id="replyForm" method="post"
	action="${cPath }/groupware/card/reply">
	<input type="hidden" name="_method" /> <input type="hidden"
		name="wrNo" /> <input type="hidden" name="wrContent" /> <input
		type="hidden" name="empNo" /> <input type="hidden" name="wcNo"
		value='${pathVO.wcNo}' />
</form>

<form id="checkItemForm" method="post"
	action="${cPath }/groupware/card/checkItem">
	<input type="hidden" name="_method" /> 
	<input type="hidden" name="wckNo" placeholder="여기"/>
	<input type="hidden" name="wckCntTitle" /> 
	<input type="hidden" name="wckCntDone" /> 
	<input type="hidden" name="wckCntNo" />
</form>

<form id="cardDesc_TitleForm" method="post" action="${cPath }/groupware/card/${pathVO.empNo}/${pathVO.wbNo}/${pathVO.wcNo}/desc_title">
	<input type="hidden" name="_method" value="put"/>
	<input type="hidden" name="wcNo"/>
	<input type="hidden" name="wcDesc"/>
	<input type="hidden" name="wcTitle"/> 
</form>

<form id="checkListForm" method="post"
	action="${cPath }/groupware/card/checkList">
	<input type="hidden" name="_method" />
	 <input type="hidden" name="wcNo" value='${pathVO.wcNo}' /> 
	 <input type="hidden" name="wckNo" /> 
	 <input type="hidden" name="wckTitle" />  	
</form>

<form id="dateForm" method="post" action="${cPath }/groupware/card/date">
	<input type="hidden" name="_method" value="put" />
	 <input	type="hidden" name="wcNo" value='${pathVO.wcNo}' />
	<input type="hidden" name="wcStart" value="${card.wcStart }"/> 
	<input type="hidden" name="wcEnd" value="${card.wcEnd }"/>  
	<input type="hidden" name="wcDone" />   
</form>

<form id="chargeForm" method="post" action="${cPath }/groupware/card/charge">
	<input type="hidden" name="_method">
	<input type="hidden" name="wcNo" value="${card.wcNo }"/> 
	<input type="hidden" name="empNo" value="${pathVO.empNo }"/>
	<input type="hidden" name="empName" value="${pathVO.empInfos.empName }">   
</form>


<script>
//submodal eventHandler 만들기/////////////////////// 

 $('.addBtn').on('click', function(){ 
	$('.sub_modal.active').toggleClass('active'); 
	let addBtn = $(this).attr("id").replace("add_","");
	$('.sub_modal_'+addBtn).toggleClass('active');  
});

$('.sub_close').on('click',function(){      
	$(this).closest(".sub_modal").find('input').val(""); 
	$(this).closest(".sub_modal").toggleClass('active'); 
})  

/* $('.modal_submit').on('click',function(){
	$(this).closest(".sub_modal").find('input').val(""); 
	$(this).closest(".sub_modal").toggleClass('active');
}); */

$('.modal_cancel').on('click',function(){       
	$(this).closest(".sub_modal").find('input').val("");  
	$(this).closest(".sub_modal").toggleClass('active'); 
})   


	
//submodal eventHandler 만들기///////////////////////

	
	
	
	
	
	

	wcStart = "${card.wcStart}";
	wcEnd = "${card.wcEnd}";
	var wcNo = '${card.wcNo}'; 
	var empNo = '${pathVO.empNo}';

	deleteChargeUrl = '${cPath}/groupware/charge';
	updateCardUrl = '${cPath}/groupware/card/${pathVO.empNo}/${pathVO.wbNo}/${pathVO.wcNo}';

	$("#checkListSection").on('click', 'input:checkbox', countBoxes);
	$("#checkListSection").on('click', 'input:checkbox', countChecked);
	//-----------------------------------------------------------------------
	var count = 0;
	var checked = 0;

	function countBoxes() {
		count = $("#checkListSection input[type='checkbox']").length;
		console.log("count", count);
	}
	function countChecked() {
		checked = $("#checkListSection input:checked").length;
		console.log("checked", checked);

		var percentage = parseInt(((checked / count) * 100), 10);
		$(".progressbar-bar").progressbar({
			value : percentage
		});
		$(".progressbar-label").text(percentage + "%");
	}
	countBoxes();
	countChecked();

	function onloadCheckDone() {
		var checked = $('#card_date_chck').attr("checked");
		if (checked) {
			$('.card_date_complete').addClass('active');
		} else {
			$('.card_date_complete').removeClass('active');
		}
	}
	onloadCheckDone();
</script>