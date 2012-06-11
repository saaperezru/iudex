/* 
 */
function showCommentInput(){
	//If it was hidden from the begining, it was using the hidden class
	// but before removing the class hide it with jquery.
	$("#commentInput").hide();
	$("#commentInput").removeClass("hidden");
	//Now show it
	$("#commentInput").show("slow");
	$("#rate-and-comment-info").hide();
}
function hideCommentBox(){
	$('#commentInput').hide('slow');
	$('.fittext-no-messages').hide('slow');
}
function toggleAnonymous(e){
	if(e){
		$('#comment-username').text("Anónimo");
		$('#comment-programName').text("...");
		$('.comment-photo>img').attr('src',anonymousImagePath);
	}else{
		$('#comment-username').text(userName);
		$('#comment-programName').text(userProgram);
		$('.comment-photo>img').attr('src',userImagePath);

	}	
}
function updatedRating(){
	//Ajax loader hiding for rating
	$("#fittext-rating").spin(false);
	$("#fittext-rating-info").spin(false);
	//Ajax loader hiding for stars
	$('#courseRatingControl').spin(false);
	$('#courseRatingStars').show();
	//Fittext code
	$("#fittext-rating").fitText(0.15);
	$("#fittext-rating-title").fitText(0.4);
	$("#fittext-rating-info").fitText(0.6);
}
function loadingCourseRating(){
	//Ajax loader showing for stars
	$('#courseRatingStars').hide();
	$('#courseRatingControl').spin("small");
	//Ajax loader showing for rating
	$("#fittext-rating-info").spin("small");
	$("#fittext-rating").spin("small");
	//Hide comments box while sending
	hideCommentBox();
}

function initCommentFilterControls(){
	var numItems;
	for (i=5;i>0;i--){
		numItems = $('.comment-star'+i).length;
		if(numItems == 0){
			$('#comment-filter-star'+i).addClass('disabled');
		}
	}
}
function downVote(data) {
	var opts = {
		lines: 10, // The number of lines to draw
		length: 8, // The length of each line
		width: 4, // The line thickness
		radius: 8, // The radius of the inner circle
		rotate: 0, // The rotation offset
		color: '#000', // #rgb or #rrggbb
		speed: 1, // Rounds per second
		trail: 60, // Afterglow percentage
		shadow: false, // Whether to render a shadow
		hwaccel: false, // Whether to use hardware acceleration
		className: 'spinner', // The CSS class to assign to the spinner
		zIndex: 2e9, // The z-index (defaults to 2000000000)
		top: 'auto', // Top position relative to parent in px
		left: '-' // Left position relative to parent in px
	};
	var ajaxstatus = data.status; // Can be "begin", "complete" and "success"
	var source = $(data.source);
	var voteControl = source.parent().parent();

	switch (ajaxstatus) {
		case "begin": // This is called right before ajax request is been sent.
			voteControl.hide();
			voteControl.parent().spin(opts);
			break;

		case "success": // This is called when ajax response is successfully processed.
			var voteCountElement = source.parent().prev();
			var upVoteElement = source.parent().prev().prev();
			var voteCount = parseInt(voteCountElement.children().first().text(),10);
			if(source.parent().hasClass('vote-wrapper')){
				//This was not the vote
				if(upVoteElement.hasClass('vote-wrapper')){
					//No vote at all
					voteCount = voteCount-1;
					voteCountElement.removeClass('vote-wrapper');
				}else{
					upVoteElement.addClass('vote-wrapper');
					voteCount = voteCount -2;
				}
				//Take out the vote-wrapper, this is the new vote
				source.parent().removeClass('vote-wrapper');
			}else{
				//This was the vote
				voteCount = voteCount+1;
				voteCountElement.addClass('vote-wrapper');
				source.parent().addClass('vote-wrapper');
			}
			voteCountElement.children().first().text(voteCount);
			voteControl.show();
			voteControl.parent().spin(false);
			break;

		case "complete": // This is called right after ajax response is received.
			// NOOP.
			break;
	}
}
function upVote(data) {
	var opts = {
		lines: 10, // The number of lines to draw
		length: 8, // The length of each line
		width: 4, // The line thickness
		radius: 8, // The radius of the inner circle
		rotate: 0, // The rotation offset
		color: '#000', // #rgb or #rrggbb
		speed: 1, // Rounds per second
		trail: 60, // Afterglow percentage
		shadow: false, // Whether to render a shadow
		hwaccel: false, // Whether to use hardware acceleration
		className: 'spinner', // The CSS class to assign to the spinner
		zIndex: 2e9, // The z-index (defaults to 2000000000)
		top: 'auto', // Top position relative to parent in px
		left: '-' // Left position relative to parent in px
	};
	var ajaxstatus = data.status; // Can be "begin", "complete" and "success"
	var source = $(data.source);
	var voteControl = source.parent().parent();

	switch (ajaxstatus) {
		case "begin": // This is called right before ajax request is been sent.
			voteControl.hide();
			voteControl.parent().spin(opts);
			break;

		case "success": // This is called when ajax response is successfully processed.
			var voteCountElement = source.parent().next();
			var downVoteElement = source.parent().next().next();
			var voteCount = parseInt(voteCountElement.children().first().text(),10);
			if(source.parent().hasClass('vote-wrapper')){
				//This was not the vote
				if(downVoteElement.hasClass('vote-wrapper')){
					//No vote at all
					voteCount = voteCount+1;
					voteCountElement.removeClass('vote-wrapper');
				}else{
					downVoteElement.addClass('vote-wrapper');
					voteCount = voteCount +2;
				}
				//Take out the vote-wrapper, this is the new vote
				source.parent().removeClass('vote-wrapper');
			}else{
				//This was the vote
				voteCount = voteCount-1;
				voteCountElement.addClass('vote-wrapper');
				source.parent().addClass('vote-wrapper');
			}
			voteCountElement.children().first().text(voteCount);
			voteControl.show();
			voteControl.parent().spin(false);
			break;

		case "complete": // This is called right after ajax response is received.
			// NOOP.
			break;
	}
}
function voteProfessor(data){

	var ajaxstatus = data.status; // Can be "begin", "complete" and "success"
	var source = $(data.source);
	var voteControl = source.parent().parent();
	var voteCount = $('#professor-rating');

	switch (ajaxstatus) {
		case "begin": // This is called right before ajax request is been sent.
			voteControl.hide();
			voteControl.parent().spin("small");
			voteCount.spin("tiny");
			break;

		case "success": // This is called when ajax response is successfully processed.
			voteControl.show();
			voteControl.parent().spin(false);
			//voteCount.spin(false);
			break;

		case "complete": // This is called right after ajax response is received.
			// NOOP.
			break;
	}
}
function moveScroller() {
	var a = function() {
		var distanceToBottomOfPage = 20;
		var bottomOfViewingPage = $(window).scrollTop()+ $(window).height()-distanceToBottomOfPage;
		var referencePoint = $("#footer-top").offset().top;
		var c=$("#fixed-navbar");
		var newHeight;
		if (bottomOfViewingPage>referencePoint) {
			//It's time to resize
			newHeight = $("#footer-top").offset().top-$(window).scrollTop()-$('#navbar-top').height()-40 // 40 just normalizes the size for the rest of blank spaces not taken into account
		} else {
			// Go back to normal
			if (bottomOfViewingPage!=referencePoint) {
				newHeight = $(window).height()-$('#navbar-top').height()-40-distanceToBottomOfPage;// 40 just normalizes the size for the rest of blank spaces not taken into account
			}
		}
		c.css({
			height: ""+newHeight+"px"
		});
	};
	$(window).scroll(a);
	a()
}
$(function() {
	moveScroller();
});
//This snippet allows multiple JSF forms to work at a time in the same page
//window.myfaces = window.myfaces || {};
//myfaces.config = myfaces.config || {};
////set the config part
//myfaces.config.no_portlet_env = true; 
//End of snippet. Source : http://www.irian.at/en/blog/-/blogs/jsf-ajax-and-multiple-forms

jQuery(document).ready(function() {     
	$('.vote-count').popover({
		placement : 'bottom'
	});
	function redirectLogin(e){
		window.location.href = loginPath;
	}
	if (!loggedIn){
		$('.not-registered').popover({
			placement : 'bottom',
			content : 'Debes estar registrado para poder votar por los comentarios.',
			title : 'Da click para registrarte o iniciar sesión' 
		});
		$('.not-registered').click(redirectLogin);
		$('#courseRatingStars>div').children().popover({
			placement : 'bottom',
			content : 'Debes estar registrado para poder calificar.', 
			title : 'Da click para registrarte o iniciar sesión'
		});
		$('#courseRatingStars>div').children().click(redirectLogin);
	}
	updatedRating();
	//Beginning of fittext code
	$(".fittext-subject").fitText(0.7);
	$(".fittext-period").fitText(0.9);
	$(".fittext-rateInput").fitText(1.2);
	$(".fittext-coment-program").fitText(1.1);
	$('.fittext-no-messages').fitText(1.3)
	//End of fittext code
	initCommentFilterControls();
})
