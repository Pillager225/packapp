function menuitem(sel) {
	$('.menu-sel').removeClass("menu-sel")
	$('.menu div').eq(sel).addClass("menu-sel");
}

function openMenu() {
	animate_time = 1000;
	$('#main').animate({
			left: "+=200"
		},animate_time,null);
	$('#menu').animate({
			left: "+=200"
		},animate_time,null);
	$('#mask').animate({height:"100%"},0,null)
//	$('#mask').animate({
//			opacity:0.7
//		},animate_time,null);
}
function closeMenu() {
	animate_time = 1000;
	$('#main').animate({
			left: "-=200"
		},animate_time,null);
	$('#menu').animate({
			left: "-=200"
		},animate_time,null);
	$('#mask').animate({
			opacity:0
		},animate_time,function() {
			$('#mask').height("0");
	});
}


/*
function openMenu() {
	$('#menu').removeClass("not-active");
	$('#main').removeClass("not-active");
	$('#mask').removeClass("not-active");
	$('#opaDiv').removeClass("not-active");
	$('#menu').addClass("is-active");
	$('#main').addClass("is-active");
	$('#mask').addClass("is-active");
	$('#opaDiv').addClass("is-active");
}

function closeMenu() {
	$('#menu').removeClass("is-active");
	$('#main').removeClass("is-active");
	$('#mask').removeClass("is-active");
	$('#opaDiv').removeClass("is-active");
	$('#menu').addClass("not-active");
	$('#main').addClass("not-active");
	$('#mask').addClass("not-active");
	$('#opaDiv').addClass("not-active");
}
*/
