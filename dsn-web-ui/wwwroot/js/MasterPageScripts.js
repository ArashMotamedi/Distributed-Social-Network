function showPageMessage(title, msg, err) {
	var c = "success";
	if (err === true)
	{
		c = "error";
	}

	$("#pageMessage").stop(false, true).attr("class", c).html(
		'<img src="/images/message-' + c + '.png" class="message-icon" />' +
		'<div class="message"><img src="/images/TextImage.aspx?text=' + title + '&style=message-' + c + '" /><br />' +
		msg + '</div>').fadeIn().click(hidePageMessage);
}

function hidePageMessage() {
	$("#pageMessage").fadeOut();
}

function validateEmail(val) {
	var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	return emailPattern.test(val);
}  