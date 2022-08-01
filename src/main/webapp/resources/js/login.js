/**
 * login.js
 */
let fnObj = {};

// Event
fnObj.initEvent = function() {
	
	$('#btn-login').on('click', function(){
		console.log('click login');
		$('#login-form').trigger('submit');
	});
}

$(function() {
	fnObj.initEvent();
});