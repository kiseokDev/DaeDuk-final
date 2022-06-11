<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<link rel="stylesheet" href="${cPath }/resources/css/jkanban.min.css" />
<meta id="_csrf" name="_csrf" content="${_csrf.token}" />
<meta id="_csrf_header" name="_csrf_header"
	content="${_csrf.headerName}" />
	
<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.realUser" var="authEmp" />
	<c:set value="${authEmp.empNo}" var="empNo" />
	<c:set value="${authEmp.comNo}" var="comNo" />
	<c:set value="${authEmp.empName}" var="empName" />
	<c:set value="${authEmp.empSelect}" var="empRole" />
</security:authorize>		
	
<style>
#myKanban {
	overflow-x: auto;
	padding: 20px 0;
}

.success {
	background: #00b961;
}

.info {
	background: #2a92bf;
}

.warning {
	background: #f4ce46;
}

.error {
	background: #fb7d44;
}

.kanban-board .kanban-drag {
	min-height: unset;
}

.kanban-board header .kanban-title-board {
	color: #fff;
	font-size: 1.2rem;
}
.kanban-board-header {
	position:relative;
}

.test { 
	background-color: #ccc;
}

.kanban-board{
	background:#fff;
	box-shadow: 0 0 0.25rem 0.05rem rgb(105 108 255/ 10%);
}
.kanban-item{
	background:#eee;
}
.drag_handler{
	background:#eee;
	margin-right:15px;
}
</style>
<script>
/* left 메뉴 표시 */
   $(function(){
      let coll = $("a[href='#collapse4']");
      let item = $('#collapse4');
      coll.addClass('active');
      coll.attr('aria-expanded','true');
      item.addClass('show');
      let openYn = "${pathVO['openYn']}";
      let menu = $("#worksMenu");
		if (openYn == "public") {
			item.find('.gw_menu_item').eq(0).addClass('active');
			menu.text('공용');
		} else {
			item.find('.gw_menu_item').eq(1).addClass('active');
			menu.text('개인');
		}
   });
</script>
<h4 class="fw-bold py-3 mb-4">
	<span class="text-muted fw-light">웍스 /</span> <span id="worksMenu"></span>
</h4>
<!-- SHS start -->
<section class="d-flex align-items-center gw_box flex-row justify-content-between wList_top">
	<div class="position-relative">
		<h5 class="m-0  board_name" id="board_name"></h5>
		<div class="name_wrap">
			<div class="d-flex">
				<input type="text" id="board_name_edit" class="form-control my-2 name_edit"/>
				<input type="button" class="btn btn-secondary name_edit_btn" value="등록">
			</div>
		</div>
	</div>
	<div>
		<div id="likeBtn"></div>
		<button class="btn btn-secondary" onclick="history.back()">목록</button>
		<a href="#" class="btn btn-primary add_btn"> <i class="fa-solid fa-plus"></i> 새 리스트</a>
		<button class="btn btn-outline-danger" id="delete_board">보드삭제</button>
	</div>
</section>
<!-- SHS end -->


<div class="p-3 w-25 add_board" id="add_board">
	<section>
		<form>
			<input class="form-control list_title" type="text" id="list_title">
			<input type="button" class="btn btn-primary" id="addDefault" value="리스트 추가" />
		</form>
	</section>
</div>
<script>
	let name = $('.employee_name');
	let input = $('.name_input');
	name.on('click', function(){
		let text = $(this).text();
		let input1 = input.eq(0).text()=='';
		let input2 = input.eq(1).text()=='';
		let input3 = input.eq(2).text()=='';
		if(input1){
			input1.attr("value",text);
		} else if(!input1 && input2){
			input2.attr("value",text);
		} 
	});
</script>
<div id="myKanban">
	
</div>







<!-- 모달있었던 부분  -->
<script type="text/javascript" src="${cPath }/resources/js/works/jquery-ui.min.js"></script>
<jsp:include page="/includee/cardmodal.jsp" />

<!-- 추가하는 버튼 -->
<button type="button" class="btn btn-primary modal_btn" data-bs-toggle="modal" data-bs-target="#card_modal">
	Launch demo modal</button>
	
<!-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ -->


<script type="text/javascript" src="${cPath }/resources/js/kanban/jkanban.min.js"></script>
<script type="text/javascript" src="${cPath }/resources/js/kanban/drag_auto_scroll.js"></script>


 
<script>
var boardsWrapper = [];

// var token = $("meta#_csrf").attr("content");
// var header = $("meta#_csrf_header").attr("content"); 
// ============================= list추가 start =============================
	let empRole ="${empRole}";
    let beforePrevSilblingId;
    let beforeNextSiblingId;
    let targetId;
    let targetRefNo;
    let afterPrevSiblingId;
    let afterNextSiblingId;
    let afterNextSiblingRefNo;
    let beforeListId;
    let afterListId;
    
    let afterListOrderNo;
    let beforeListOrderNo;
    let targetListId;
    let wbNo; 
    let boardresp;
    let cardModal;
    let cardUrl = "${cPath}/groupware/card/${pathVO.empNo}/${pathVO.wbNo}";
    let wlistUrl = "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}";
    let listTitleUpdateUrl = "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}/listtitle";
    let KanbanTest;
	let cardList = [];
	let cardMap; 
	
    /* 손효선 추가 { */
    let WbTitle = $('.board_name');
    let edit = $('.name_edit');
    let editWrap = $('.name_wrap');
    let editBtn = $('.name_edit_btn');
    let boardDeleteUrl = "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}";
    let boardListUrl = "${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}";
    /* } 손효선 추가  */
    
        
function storeCardInfos(listList){
	$.each(listList,function(idx,list){
		$.each(list.cardList,function(i,card){
			cardList.push(card);
		});
	});
	cardMap = cardList.reduce(function(map, obj) {
		map[obj.wcNo] = obj;
		return map;
	}, {});
	return cardMap; 
}

function updateListOrder(){
	let data = JSON.stringify({
		afterListOrderNo : afterListOrderNo
		,beforeListOrderNo : beforeListOrderNo
		,targetListId : targetListId
		,wbNo : wbNo
	})
	let url = "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}/listPosition";
	updateAjax(url,data);
}
    
function beforeDragNextSibling(el,source){
	beforePrevSilblingId = $(el.previousSibling).data("eid");
	beforeNextSiblingId = $(el.nextSibling).data("eid");
	targetId = $(el).data("eid");
	targetRefNo = $(el).data("refno");
	beforeListId = source.parentElement.getAttribute('data-id');
	console.log("beforePrevSilblingId",beforePrevSilblingId);
// 	console.log("beforeNextSiblingId",beforeNextSiblingId);
// 	console.log("targetId",targetId);
// 	console.log("beforeListId",beforeListId);
}

function afterDragNextSibling(sibling,target,el){ 
	afterPrevSiblingId = $(el.previousSibling).data("eid");
	afterNextSiblingId = $(sibling).data("eid");
	afterNextSiblingRefNo = $(sibling).data("refno");
	afterListId = target.parentElement.getAttribute('data-id');
// 	console.log("afterPrevSiblingId",afterPrevSiblingId);
// 	console.log("afterNextSiblingId",afterNextSiblingId);
// 	console.log("afterNextSiblingRefNo",afterNextSiblingRefNo);
// 	console.log("afterListId",afterListId);
	
	
	let cardData =  JSON.stringify({
		afterPrevSiblingId : afterPrevSiblingId,
		afterNextSiblingId : afterNextSiblingId,
		
		beforePrevSilblingId : beforePrevSilblingId,
		beforeNextSiblingId : beforeNextSiblingId,
		targetId : targetId,
		targetRefNo : targetRefNo,
		
		beforeListId : beforeListId,
		afterListId : afterListId
	})
		let url = "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}/cardposition";
	updateAjax(url,cardData)
}

function updateAjax(url,data){
	$.ajax({
		url : url,
		data : data,
		method : "put",
		contentType : "application/json; charset=UTF-8",
		processData : false,
		dataType : "text",
		success : function(resp) {
			console.log(resp)
		},
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR);
			console.log(textStatus);
			console.log(errorThrown);
		}
	});
}



function retrieveBoardAjax(){
	
	$.ajax({
		url : "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}",
		method : "get",
		dataType : "json",
		success : function(resp) {
			boardresp = resp;
			listList = boardresp.listList;
			cardMap = storeCardInfos(listList);
			
			
			wbNo = resp.wbNo;
			wbTitle = resp.wbTitle;
			retrieveTitle(wbTitle,wbNo);
			spanToInput(resp);
			$.each(resp.wrapperList,function(idx,list){
				boardsWrapper.push(list); 
			});
			 KanbanTest = new jKanban({
			        boards: boardsWrapper,
			        element: "#myKanban",
			        gutter: "10px",
			        widthBoard: "450px", 
			        responsivePercentage: false,                                    // if it is true I use percentage in the width of the boards and it is not necessary gutter and widthBoard
			        dragItems : true,
			        itemHandleOptions:{
			          enabled: true
			        },
			        itemAddOptions: {
				          enabled: true,
				          content: '+ 카드',
				          "class": "btn btn-primary custom-button",
				          footer: true
				    },
			        click: function(el) {
			        	$('.modal_btn').trigger('click'); 
			        	let cardNo = el.getAttribute('data-eid');
						console.log("cardNo",cardNo);
						
						$('.modal_btn').on('click',getCardViewModal(cardNo,cardMap,KanbanTest));
			        },
			        context: function(el, e) {
			          console.log("Trigger on all items right-click!");
			        },
			        dragEl: function (el, source) {
			        	beforeDragNextSibling(el,source);
			        },
			        dropEl: function(el, target, source, sibling){
			        	afterDragNextSibling(sibling, target, el);
			        },
			        dragBoard: function (el, source) {
			        	beforeListOrderNo = el.getAttribute('data-order');
			        	targetListId = el.getAttribute('data-id');
			        	console.log('before',beforeListOrderNo);
			        	console.log('ListId',targetListId);
			        	
			        },  
			        dragendBoard: function (el) { 
			        	afterListOrderNo = el.getAttribute('data-order');
			        	console.log('after',afterListOrderNo);
			        	updateListOrder();
			        	
			        }, 
			        buttonClick: function(el, boardId) {
			          console.log('el',el);
			          console.log(boardId);
			          
			          let selectedBoard = $('.kanban-board[data-id='+boardId+']'); 
			          let lastItem = selectedBoard.find('.kanban-item:last-child');
			           
			          console.log('selectedBoard',selectedBoard);  
			          console.log('lastItem',lastItem[0]);  
			          
			          let prevNo = lastItem.data('eid');
			          console.log('lastItemId',prevNo);   
			          
			         
			          
			          
			          
			          // create a form to enter element
			          var formItem = document.createElement("form");
			          formItem.setAttribute("class", "itemform");
			          formItem.innerHTML =
			            '<div class="form-group"><textarea class="form-control" rows="2" autofocus placeholder="카드의 제목을 입력해주세요"></textarea></div><div class="form-group text-center"><button type="submit" class="btn btn-secondary btn-xs pull-right">등록</button><button type="button" id="CancelBtn" class="btn btn-default btn-xs pull-right">취소</button></div>';
	
			          KanbanTest.addForm(boardId, formItem);
			          formItem.addEventListener("submit", function(e) {
			            e.preventDefault();
			            var text = e.target[0].value;
			            if (text.trim() == '') {
			    			alert("카드 제목은 필수입니다.");
			    			return false;
			    		}
			          	let url ="${cPath}/groupware/card/${pathVO.empNo}/${pathVO.wbNo}/"+1+"/new"; 
			            insertCard(url,boardId,prevNo,text,KanbanTest);
			            
			            formItem.parentNode.removeChild(formItem);
			          });
			          document.getElementById("CancelBtn").onclick = function() {
			            formItem.parentNode.removeChild(formItem);
			          };
			        }
			      });
			 ///////////////////////////////////////////////////////
			listList.forEach(ele => editListDiv(ele));			
			cardList.forEach(ele => editCardDiv(ele));			
			 
			////////////////////////////////////////////////////////

			var curUrl = new URL(window.location.href);
			const urlParams = curUrl.searchParams;
			if(urlParams.get('md') != null){
				$('[data-eid="'+urlParams.get('md')+'"]').click();
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR",jqXHR); 
			console.log('textStatus',textStatus);
			console.log('errorThrown',errorThrown); 
		}
	});
}
retrieveBoardAjax();


///////////////////////////////onload후 리스트에 X표시///////////////////////
function editListDiv(ele){
	let listId =  ele['wlNo']
	let list = KanbanTest.findBoard(listId);
	let deleteTag;
	$('.kanban-board-header').addClass('d-flex justify-content-between');
	$(list).find(".kanban-title-board").after($("<a class='listDelete'><i class='fa-solid fa-xmark'></i></a>"));  
}

///////////////////////////////onload후 카드에 날짜랑,complete,담당자 ///////////////////////
function editCardDiv(ele){  
	$('.kanban-item').addClass('position-relative');

	let cardDiv = KanbanTest.findElement(ele['wcNo']);
	let remove = $(cardDiv).children(":gt(1)").remove();  
	let card = cardMap[ele['wcNo']];
	let textDIV = $(cardDiv).children(":eq(1)");   
	$(textDIV).text(card['wcTitle']);	 
	
	let workers = [];
	if(card.wcStart && card.wcEnd){
		let dateTag='<div class="badge my-2 bg-secondary"><i class="fa-solid fa-calendar-days"></i> '+card.wcStart+'  &nbsp; ~ &nbsp;  '+card.wcEnd+'</div>';  
		let cardText = $(cardDiv).text();  	  
		$(cardDiv).append(dateTag);   	  
	}
	if(card.wcDone=="Y"){
	 	let completeTag = '<span class="badge my-2 bg-warning mx-1">complete</span>';
		$(cardDiv).append(completeTag);   	  
	}
// 	if((card.empList).length>0 && card.empList!=null){  
	if(card.empList!=null){
		let workerTag=('<div class="position-absolute top-0 end-0 my-1 mx-1">');
		for(let emp of card.empList){ 
			workerTag+=('<span class="badge mx-1 bg-info incharge-emp">'+emp.empName+'</span>');
		}  
		workerTag+=('</div>')
		$(cardDiv).append(workerTag);     	   
	}
	
}




// ==================== list 출력 end ====================
 

      

      
	
//=============================== 리스트 추가 start =============================

$('.add_btn').on('click', function(){
	$('#add_board').addClass('active');
	 
});
function commonSuccess(resp){
	if(resp.result == "OK"){
		$("#list_title").val("");
		$('#add_board').removeClass('active');
		console.log("insert 할 list data", resp.data);
		KanbanTest.addBoards([resp.data]);
		let deleteBtn = ('<a class="listDelete"><i class="fa-solid fa-xmark"></i></a>');
		$('[data-id='+resp.data['id']+']').find('header').addClass('d-flex justify-content-between'); 
		$('[data-id='+resp.data['id']+']').find('.kanban-title-board').after(deleteBtn); 
	}else if(resp.message){ 
		alert(resp.message);
	}
}
var addBoardDefault = document.getElementById("addDefault");

	addBoardDefault.addEventListener("click", function() {
		let updateReservedTitle = $("#list_title").val();
		if (updateReservedTitle.trim() == '') {
			alert("리스트 제목은 필수입니다.");
			return false;
		}
		
		$.ajax({
				url : "${cPath}/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/${wbNo}/new",
				method : "post", 
				data : JSON.stringify({
					wlTitle : updateReservedTitle
				}), 
				contentType : "application/json; charset=UTF-8",
				dataType : "json",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				success : commonSuccess,
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(jqXHR);
					console.log(textStatus);
					console.log(errorThrown);
				}
			});

		
	});

	//=============================== 리스트 추가 end =============================

	//       $('.wList_top button:not([type="submit"])').on('click', function(){
	$('.wList_top button[id="set_auth"]').on('click', function() {
		var auth = $('#sub_modal_auth');
		var close = auth.find('.btn-close');
		auth.toggleClass('active');
		close.on('click', function() {
			auth.removeClass('active');
		})
	});
	var ulTag = $("#listBody > ul");
	var favoriteBody = $("#favorite_listBody > ul");
	const liked = "${cPath}/resources/img/favorite_filled.png";
	const nliked = "${cPath}/resources/img/favorite_empty.png";
	var collection = $("select[name=collectionName]");
	var YImgTag = $("<img>").attr("src",
			"${cPath}/resources/img/favorite_filled.png");
	var NImgTag = $("<img>").attr("src",
			"${cPath}/resources/img/favorite_empty.png");
	var ImageAtag;
	var favorite;

	//=========================== 전체조회 start =========================== 
	//=========================== SHS start =========================== 
	//       function retrieveBoard(resp) {
	//       	$.each(resp, function(idx, wbVO) {
	//       						if (wbVO.boardLike == "Y") {
	//       							ImageAtag = $("<a>")
	//       									.append(
	//       											$("<img>")
	//       													.attr("src",
	//       															"${cPath}/resources/img/favorite_filled.png"));
	//       						} else {
	//       							ImageAtag = $("<a>")
	//       									.append(
	//       											$("<img>")
	//       													.attr("src",
	//       															"${cPath}/resources/img/favorite_empty.png"));
	//       						}
	//       						ImageAtag
	//       								.data(
	//       										"href",
	//       										"${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/like")
	//       								.data("boardLike", wbVO.boardLike)
	//       								.addClass("ajaxBtn favorite_btn");

	//       						let boardTitle = $("<a>").attr(
	//       								"href",
	//       								"${cPath }/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/"
	//       										+ wbVO.wbNo).html(wbVO.wbTitle);
	//       						let titleWrap = $("<div>").addClass(
	//       								"d-flex justify-content-between fs-4").css(
	//       								"height", "80px").append(boardTitle).append(ImageAtag);
	//       						let divCollection = $("<div>").addClass("badge bg-label-primary").html(wbVO.wbClass);
	//       						let gwBox = $("<div>").addClass("gw_box").append(titleWrap).append(divCollection);

	//       						let board = $("<li>").addClass("col p-2").append(gwBox).prop("id", wbVO.wbNo);

	//       						if (wbVO.boardLike == "Y") {
	//       							$('#favorite_listBody ul').append(board);								
	//       						}else{
	//       							ulTag.append(board);

	//       						}
	//       					});
	//       }
	//       function ajax() {
	//       	$.ajax({
	//       				url : "${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}",
	//       				dataType : "json",
	//       				success : function(resp) {
	//       					retrieveBoard(resp);
	//       				},
	//       				error : function(jqXHR, textStatus, errorThrown) {
	//       					console.log(jqXHR);
	//       					console.log(textStatus);
	//       					console.log(errorThrown);
	//       				}
	//       			});
	//       }

	//       ajax();
	//       //=========================== 전체조회 end =========================== 

	//       //=========================== 즐겨찾기 상태수정 start =========================== 
	//       $(document).on("click", ".ajaxBtn", function() {
	//       	let href = $(this).data("href");
	//       	let id = $(this).parents('li').attr("id");
	//       	let boardLike = $(this).data("boardLike");
	//       	ajaxAtag = $(this);
	//       	console.log(nliked);

	//       	var likeBody = {
	//       		ajaxAtag : ajaxAtag,
	//       		boardLike : boardLike,
	//       		nliked : nliked,
	//       		liked : liked
	//       	}

	//       	if (boardLike == 'Y') {
	//       		ajaxAtag.find('img').attr('src', nliked);
	//       		ajaxAtag.data("boardLike", 'N')
	//       	} else if (boardLike == 'N') {
	//       		ajaxAtag.find('img').attr('src', liked);
	//       		ajaxAtag.data("boardLike", 'Y');
	//       	}

	//       	console.log(boardLike);
	//       	let data = JSON.stringify({
	//       		"wbNo" : id,
	//       		"boardLike" : boardLike
	//       	})

	//       	$.ajax({
	//       		url : href,
	//       		method : "put",
	//       		contentType : "application/json; charset=UTF-8",
	//       		processData : false,
	//       		data : data,
	//       		dataType : "text",
	//       		success : function(resp, likeBody) {
	//       			let boardLike = likeBody.boardLike;
	//       			let ajaxAtag = likeBody.ajaxAtag;

	//       			if (boardLike == 'Y') { 
	//       				ajaxAtag.find('img').attr('src', likeBody.nliked);
	//       				ajaxAtag.data("boardLike", 'N')
	//       			} else if (boardLike == 'N') {
	//       				ajaxAtag.find('img').attr('src', likeBody.liked);
	//       				ajaxAtag.data("boardLike", 'Y');
	//       			}
	//       		},
	//       		beforeSend : function(xhr) {
	//       			xhr.setRequestHeader(header, token);
	//       		},
	//       		error : function(jqXHR, textStatus, errorThrown) {
	//       			console.log(jqXHR);
	//       			console.log(textStatus);
	//       			console.log(errorThrown);
	//       		}
	//       	});

	//       }).css("cursor", "pointer");
	///////////////////////////////////////////SHS end
	/*  function enterKey(){
	 if (e.keyCode =='13'){
	 retrieveTitle(editText,wbNo);
	 edit.empty();
	 edit.removeClass('active');
	 }
	 } */

	/* 손효선 코드(토) { */

	
</script>  
 <script type="text/javascript"
	src="${cPath }/resources/js/works/wListLKS.js"></script> 
 <script type="text/javascript"
	src="${cPath }/resources/js/works/wListSHS.js"></script>
 <script type="text/javascript"
	src="${cPath }/resources/js/works/cardmodal.js"></script>
 <script type="text/javascript"
	src="${cPath }/resources/js/works/submodal.js"></script>
 <script type="text/javascript"
	src="${cPath }/resources/js/works/submodalLKS.js"></script>


