<%@ Page Title="Getting Started" Language="C#" MasterPageFile="~/TopLevelMasterPage.master" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
<style>
#email, #password
{
	outline-style: hidden;
	border-width: 0px;
	width: 443px;
	height: 48px;
	background-color: Transparent;
	margin-top: 49px;
	margin-left: 18px;
	font-size: 18pt;
	font-family: Cambria;
	font-weight: bold;
	padding-left: 7px;
	padding-right: 7px;
	color: #111;
}
#password
{
	margin-top: 46px;
}
</style>

<script type="text/javascript">
	$(document).ready(function () {
		$("#email").focus().keypress(function (event) {
			if (event.keyCode == 13) {
				$("#password").focus();
			}
		});

		$("#password").keypress(function (event) {
			if (event.keyCode == 13) {

				hidePageMessage();

				var email, password;
				email = $("#email").val();
				password = $("#password").val();

				if (!validateEmail(email)) {
					showPageMessage("Wrong email address", "Your email address doesn't really seem to be valid!", true);
					$("#email").focus().select();
					return;
				}

				if (password == "") {
					showPageMessage("Password, please!", "Did you forget to specify your password?", true);
					$("#password").focus();
					return;
				}

				// Submit the request
				$.post("/ajax/authenticate.aspx", { email: $("#email").val(), password: $("#password").val() },
					function (data) {
						if (data.substr(0, 6) == "ERROR:") {
							showPageMessage("That didn't work!", data.substr(6), true);
							$("#email").focus().select();
						}
						else if (data.substr(0, 8) == "SUCCESS:") {
							window.location = data.substr(8);
						}
					});
			}
		});
	});
</script>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
	<img src="/images/simplify-onestop.png" class="large-headline" />
	
	<div class="float-right" style="background-position: 0px 60px;">
		<div style="text-align: center"><img src="/images/globe-logos.png" style="margin-bottom: 20px;" /></div>
		<p>
			<img src="/images/TextImage.aspx?text=Welcome to OneStop...&style=h2" /></p>
		<p><b><span style="color: #B01515">One</span><span style="color: #22397B">Stop</span></b> is a social aggregator. With your authorization, we gather data from your social networks
		to provide <b>fabulous</b> services to you.</p>
		<p>We can connect to your <a href="http://www.facebook.com" target="_blank">Facebook</a>, 
			<a href="http://www.twitter.com" target="_blank">Twitter</a>,
			<a href="http://www.linkedin.com" target="_blank">Linked In</a>, and
			<a href="http://www.flickr.com" target="_blank">Flickr</a> accounts and bring them all together in <b>one place</b>,
			so you can stay tuned quicker and easier – basically, in <b><span style="color: #B01515">One</span><span style="color: #22397B">Stop</span></b>!
		</p>
		<p>By analyzing your interestes, and those of other members, we are also able to <b>suggest</b> new social connections to you.
		You will find <b>interesting</b> people that you'd like to meet :)
		</p>
	</div>

	<div style="padding-left: 75px; margin-top: 10px;">
		<img src="/images/text-get-started-we.png" style="margin-left: 3px; margin-bottom: 35px;" /><br />

		<div style="background: url('/images/text-get-started-email.png') no-repeat; width: 550px; height: 130px;">
			<input id="email" name="email" />
		</div>

		<div style="background: url('/images/text-get-started-password.png') no-repeat; width: 550px; height: 130px;">
			<input id="password" type="password" name="password" />
		</div>

	</div>
</asp:Content>