<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="kr.or.easybusy.works.board.controller.ControllerAdvisor"%>
<meta id="_csrf" name="_csrf" content="${_csrf.token}" />
<meta id="_csrf_header" name="_csrf_header"
	content="${_csrf.headerName}" />

<script>
	/* left 메뉴 표시 */
	$(function() {
		let coll = $("a[href='#collapse4']");
		let item = $('#collapse4');
		coll.addClass('active');
		coll.attr('aria-expanded', 'true');
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
	<span class="text-muted fw-light">웍스 / </span> <span id="worksMenu"></span>

</h4>
<div class="d-flex justify-content-between gw_box flex-row mb-4">
	<div class="btn btn-primary" id="createBoardClick">
		<i class="fa-solid fa-plus"></i> 새 보드 만들기
	</div>
	<div id="searchDIV" class="form-inline d-flex">
		<select id="collectionName" name="collectionName" class="form-control">
			<option value>choose a collection</option>
		</select> <input type="text" name="searchWord"
			class="form-control mr-2 search_input"> <input type="button"
			value="검색" id="searchBtn" class="btn btn-primary mr-2">
	</div>
</div>
<section id="favorite_board" class="mb-4">
	<h5>즐겨찾기 보드</h5>
	<div id="favorite_listBody">
		<ul class="row row-cols-3">
		</ul>
	</div>
</section>
<section id="all_board" class="mb-4">
	<h5>모든 보드</h5>


	<%-- ${requestScope} --%>

	<div id="listBody">

		<ul class="row row-cols-3">
			<!-- <li> -->
			<%-- 				<c:forEach items="${boardList }" var="wboardVO"> --%>
			<!-- 			 	<li class="col gw_box"> -->
			<%-- 			 		<a href="${cPath }/groupware/wlist/${wboardVO.wbNo}"> --%>
			<%-- 			 				<div>${wboardVO.wbTitle}</div> --%>
			<!-- 			 		</a> -->
			<!-- 		 			<div class="bg-secondary"> -->
			<%-- 			 				<c:if test="${wboardVO.boardLike eq 'y'}"> --%>
			<%-- 			 					<span><img src="${cPath}/resources/img/favorite_filled.png"/></span> --%>
			<%-- 			 				</c:if> --%>
			<%-- 			 				<c:if test="${wboardVO.boardLike ne 'y'}"> --%>
			<%-- 			 					<span><img src="${cPath}/resources/img/favorite_empty.png"/></span> --%>
			<%-- 			 				</c:if> --%>
			<!-- 		 				</div> -->
			<%-- 			 		<div>${wboardVO.wbClass }</div> --%>
			<!-- 			 	</li> -->
			<%-- 			 </c:forEach> --%>
			<!-- </li> -->
		</ul>
	</div>
</section>
<%-- test = ${pathVO} //script or vo 11 --%>

<div class="modal fade" id="createModal" tabindex="-1"
	aria-labelledby="createModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="createModalLabel">보드 등록</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>

			<%--       <form:form modelAttribute="wboard" method="post" > --%>
			<!--       	<input type="hidden" name="_method" value="post"> -->
			<%-- 		<input type=hidden name="empNo" required value="${pathVO.empNo }"> --%>
			<%-- 		<input type=hcidden name="comNo" required value="${pathVO.comNo }"> --%>

			<sec:csrfInput />
			<div class="modal-body">
				<table class="table form-inline">
					<tr>
						<th>보드명</th>
						<td><input id="wbTitle" required="true" class="form-control" />
							<!-- 	      				<errors path="wbTitle" cssClass="error" element="span" /> -->
							<!-- 	      				<input type="text" required name=colnoNo class="form-control" placeholder="보드명"/> -->

						</td>

						<!-- 	      		<tr> -->
						<!-- 	      			<th>공유여부</th> -->
						<!-- 	      			<td> -->
						<!-- 	      				<input id="openYn" required="true" class="form-control"/> -->
						<!-- 	      				<errors path="openYn" cssClass="error" element="span" /> -->
						<%-- 	      				<input type="text"  name=openYn class="form-control" placeholder="${pathVO.openYn }"/> --%>
						<!-- 	      			</td> -->
						<!-- 	      		</tr> -->
					<tr>
						<th>보드 컬렉션이름</th>
						<td><input id="wbClass" class="form-control" /> <!-- 	      				<errors path="wbClass" cssClass="error" element="span" /> -->
							<!-- 	      				<input type="text"  name=wbClass class="form-control" placeholder="보드 컬렉션이름"/> -->
						</td>
					</tr>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					onclick="newWorksBoard();">등록</button>
				<button type="button" class="btn btn-secondary"
					data-bs-dismiss="modal">닫기</button>
			</div>
			<%--       </form:form> --%>
		</div>
	</div>
</div>
<script type="text/javascript">
	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// 	var token = $("meta#_csrf").attr("content");
	// 	var header = $("meta#_csrf_header").attr("content");
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

	function getList() {
		let selectedCollection = $('#collectionName').val();
		console.log(selectedCollection);
		$
				.ajax({
					url : "${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/collection",
					dataType : "json",
					data : {
						"selectedCollection" : selectedCollection
					},
					success : function(resp) {
						let boards = $("#listBody li");
						let favorites = $("#favorite_listBody li");

						boards.remove();
						favorites.remove();

						retrieveBoard(resp);
						// 					favoriteBoard(resp);

						//보드 뿌려주는 script 코드
					},
					cache : true,
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
						console.log(textStatus);
						console.log(errorThrown);
					}
				});
	}

	function newWorksBoard() {
		var wbTitle = $('#wbTitle').val();
		if (wbTitle.trim() == '') {
			alert('제목은 필수입니다.');
			return false;
		}

		var openYn = $('#openYn').val();
		var wbClass = $('#wbClass').val();
		$
				.ajax({
					url : "${cPath }/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/new",
					data : {
						wbTitle : wbTitle,
						wbClass : wbClass
					},
					type : 'post',
					dataType : "text",
					success : function(resp) {
						$('#createModal .btn-close').click();
						getList();
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

	//=========================== 전체조회 start =========================== 
	function retrieveBoard(resp) {
		$
				.each(
						resp,
						function(idx, wbVO) {
							if (wbVO.boardLike == "Y") {
								ImageAtag = $("<a>")
										.append(
												$("<img>")
														.attr("src",
																"${cPath}/resources/img/favorite_filled.png"));
							} else {
								ImageAtag = $("<a>")
										.append(
												$("<img>")
														.attr("src",
																"${cPath}/resources/img/favorite_empty.png"));
							}
							ImageAtag
									.data(
											"href",
											"${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/like")
									.data("boardLike", wbVO.boardLike)
									.addClass("ajaxBtn favorite_btn");

							let boardTitle = $("<a>").attr(
									"href",
									"${cPath }/groupware/wlist/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/"
											+ wbVO.wbNo).html(wbVO.wbTitle);
							let titleWrap = $("<div>").addClass(
									"d-flex justify-content-between fs-4").css(
									"height", "80px").append(boardTitle)
									.append(ImageAtag);
							let divCollection = $("<div>").addClass(
									"badge bg-label-primary")
									.html(wbVO.wbClass);
							let gwBox = $("<div>").addClass("gw_box").append(
									titleWrap).append(divCollection);

							let board = $("<li>").addClass("col p-2").append(
									gwBox).prop("id", wbVO.wbNo);

							if (wbVO.boardLike == "Y") {
								$('#favorite_listBody ul').append(board);
							} else {
								ulTag.append(board);

							}
						});
	}

	function ajax() {
		$
				.ajax({
					url : "${cPath}/groupware/works/${pathVO.comNo}/${pathVO.empNo}/${pathVO.openYn}/retreive",
					dataType : "json",
					success : function(resp) {
						retrieveBoard(resp);

					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
						console.log(textStatus);
						console.log(errorThrown);
					}
				});
	}

	ajax();
	//=========================== 전체조회 end =========================== 

	//=========================== 컬렉션 필터링 ajax start =========================== 
	$
			.ajax({
				url : "${cPath}/groupware/works/${pathVO.comNo}/${pathVO.comNo}/${pathVO.openYn}/filter",
				dataType : "json",
				success : function(list) {
					let options = []
					$.each(list, function(idx, i) {

						// 				if(!i.COLLECTION) return true;
						if (i == null)
							return true;
						//if(isNaN(i.COLLECTION)) return true;

						let option = $("<option>").attr("value", i.COLLECTION)
								.text(i.COLLECTION);
						options.push(option);
					});
					options.push($("<option>").attr("value", "none").text(
							"none"));

					collection.append(options);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log(jqXHR);
					console.log(textStatus);
					console.log(errorThrown);
				}
			});

	//=========================== 컬렉션 필터링 ajax end ===========================  

	//=========================== 컬렉션 필터링 조회 start =========================== 

	let prodLguTag = collection.on("change", function() {
		getList();
	});
	//=========================== 컬렉션 필터링 조회 end =========================== 

	// =========================== 모달창 띄어주기 start ============================
	let createBoardModal = $("#createModal").on("hidden.bs.modal", function() {
		// 		$(this).find("form").get(0).reset();
		$(this).find("table input").val("");
	}).on("shown.bs.modal", function() {
		$(this).find(":text:first").focus().select();
	});

	$("#createBoardClick").on("click", function() {
		createBoardModal.modal('show');
	});

	function commonSuccess(resp) {
		if (resp.result == "OK") {
			createBoardModal.modal("hide");
			alert(resp.message);
			// 			recursivePage(resp.page);
		} else if (resp.message) {
			alert(resp.message);
		}
	}

	let options = {
		dataType : "json",
		success : commonSuccess,
		beforeSubmit : function() {
			createBoardModal.modal("hide");
		}
	}

	let wBoardCreateForm = createBoardModal.find("form").ajaxForm(options);

	// =========================== 모달창 띄어주기 end ============================

	//=========================== 즐겨찾기 상태수정 start =========================== 
	$(document).on("click", ".ajaxBtn", function() {
		let href = $(this).data("href");
		let id = $(this).parents('li').attr("id");
		let boardLike = $(this).data("boardLike");
		ajaxAtag = $(this);
		console.log(nliked);

		var likeBody = {
			ajaxAtag : ajaxAtag,
			boardLike : boardLike,
			nliked : nliked,
			liked : liked
		}

		if (boardLike == 'Y') {
			ajaxAtag.find('img').attr('src', nliked);
			ajaxAtag.data("boardLike", 'N')
		} else if (boardLike == 'N') {
			ajaxAtag.find('img').attr('src', liked);
			ajaxAtag.data("boardLike", 'Y');
		}

		console.log(boardLike);
		let data = JSON.stringify({
			"wbNo" : id,
			"boardLike" : boardLike
		})

		$.ajax({
			url : href,
			method : "put",
			contentType : "application/json; charset=UTF-8",
			processData : false,
			data : data,
			dataType : "text",
			success : function(resp, likeBody) {
				let boardLike = likeBody.boardLike;
				let ajaxAtag = likeBody.ajaxAtag;

				if (boardLike == 'Y') {
					ajaxAtag.find('img').attr('src', likeBody.nliked);
					ajaxAtag.data("boardLike", 'N')
				} else if (boardLike == 'N') {
					ajaxAtag.find('img').attr('src', likeBody.liked);
					ajaxAtag.data("boardLike", 'Y');
				}
				location.reload();
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

	}).css("cursor", "pointer");
	//=========================== 즐겨찾기 상태수정 end ===========================
</script>