$(document).ready(function() {
	$('#login_register').click(function() {
		$('.login').removeClass('show').addClass('hidden');
		$('.register').removeClass('hidden').addClass('show');
	});
	$('#login_reset').click(function() {
		$('.login').removeClass('show').addClass('hidden');
		$('.reset').removeClass('hidden').addClass('show');
	});
	$('#login_login').click(function() {
		$('.login').removeClass('show').addClass('hidden');
		$('.logout').removeClass('hidden').addClass('show');
	});	
	$('#register_register').click(function() {
		$('.register').removeClass('show').addClass('hidden');
		$('.login').removeClass('hidden').addClass('show');
	});
	$('#register_cancel').click(function() {
		$('.register').removeClass('show').addClass('hidden');
		$('.login').removeClass('hidden').addClass('show');
	});
	$('#logout_profile').click(function() {
		displayDummyText();
	});
	$('#logout_add').click(function() {
		//TODO show modal with dropdown of all types of content, defaulting to text
		addText();
	});
	$('#logout_logout').click(function() {
		$('.logout').removeClass('show').addClass('hidden');
		$('.login').removeClass('hidden').addClass('show');
	});
	$('#reset_reset').click(function() {
		$('.reset').removeClass('show').addClass('hidden');
		$('.login').removeClass('hidden').addClass('show');
	});
	$('#reset_cancel').click(function() {
		$('.reset').removeClass('show').addClass('hidden');
		$('.login').removeClass('hidden').addClass('show');
	});
});