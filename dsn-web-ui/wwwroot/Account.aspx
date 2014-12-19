<%@ Page Title="Manage your accounts" Language="C#" MasterPageFile="~/TopLevelMasterPage.master" AutoEventWireup="true" CodeFile="Account.aspx.cs" Inherits="Account" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
<style type="text/css">
	div#accounts
	{
		background-image: url('/images/accounts-setup-bg.png');
		background-repeat: no-repeat;
		width: 684px;
		height: 282px;
		margin: 40px 0px 25px 0px;
	}
	
	div.account
	{
		padding: 10px 15px;
		margin-bottom: 26px;
		height: 91px;
		cursor: pointer;
	}
	
	div.account p
	{
		/*font-family: Calibri;*/
		font-size: 12pt;
		font-style: italic;
		color: #111;
	}
	
	div.account:hover p
	{
		/*color: #284971;*/
		color: #8E0404;
	}

	
	div.account > img.name
	{
		margin-top: 4px;
		margin-bottom: 6px;
	}
	
	div.account > img.checkmark
	{
		width: 40px;
		height: 40px;
		position: absolute;
		display: none;
	}
</style>

<script type="text/javascript">
	var waitTimer;
	var servicePage;
	var serviceInProgress;

	function waitOnService() {
		if (waitTimer == null) {
			waitTimer = window.setInterval(waitOnService, 100);
			return;
		}

		if (servicePage.closed) {
			// The page is closed, so stop the timer...
			window.clearInterval(waitTimer);
			waitTimer = null;

			// And check the results what just happened!
			$.get("/ajax/updateAuthorizations.aspx", null, function () {
				updateServices();
			});
		}
	}

	function updateServices() {
		// Ok retrieve data for the last updated service
		var lastService; 
	}

	function connectToService(service) {
		serviceInProgress = service;
		openPage("/RequestAuthorization.aspx?serviceProvider=" + service);
		waitOnService();
	}

	function openPage(page, w, h) {
		if (w == null)
			w = 800;
		if (h == null)
			h = 500;
		var l = (screen.width - w) / 2;
		var t = (screen.height - h) / 2;
		servicePage = window.open(page, 'service', 'left=' + l + ',top=' + t + ',height=' + h + ',width=' + w + ',location=no,menubar=no,resieable=no,scrollbars=no,status=no,titlebar=no,toolbar=no', true);
	}

	$(document).ready(function () {
		$("#facebook").click(function () { connectToService("fb"); });
		$("#twitter").click(function () { connectToService("tw"); });
		$("#linkedin").click(function () { connectToService("li"); });
		$("#flickr").click(function () { connectToService("fl"); });
	});
	
</script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
	<div class="float-right" style="margin-top: 30px; width: 270px; background-position: 0px 70px;">
		<center>
		<div style="width: 178px; height: 190px; border: 1px solid #ddd; background: #fafafa url('/images/person.png') no-repeat center center;">
			<img src="/images/picture-frame.png" style="margin-left: -24px; margin-top: -23px">
		</div>
		</center>
	</div>
	<img src="/images/accounts-setup.png" class="large-headline" />
	<p>
	Use the widget below to connect to your <a href="http://www.facebook.com" target="_blank">Facebook</a>,
	<a href="http://www.twitter.com" target="_blank">Twitter</a>,
	<a href="http://www.linkedin.com" target="_blank">Linked In</a>,
	and <a href="http://www.flickr.com" target="_blank">Flickr</a> accounts.</p>
	<div id="accounts">
		<div style="width: 323px; float: left; margin-left: 7px; margin-top: 7px;">
			<div class="account" id="facebook">
			<img class="checkmark" id="img_chk_fb" src="/images/message-success.png" style="margin-top: -8px; margin-left: -43px;" />
			<img class="name" src="/images/TextImage.aspx?text=facebook&style=h2" />
				<p>Best place online for <b>sharing</b> and<br /><b>connecting</b> with your friends</p>
			</div>
			<div class="account" id="linkedin">
			<img class="checkmark" id="img_chk_li" src="/images/message-success.png" style="margin-top: -8px; margin-left: -43px;" />
			<img class="name" src="/images/TextImage.aspx?text=linked in&style=h2" />
				<p>Your <b>professional</b> online life.<br />You gotta keep it, well, professional!</p>
			</div>
		</div>
		<div style="width: 324px; float: left; margin-left: 24px; margin-top: 7px; text-align: right;">
			<div class="account" id="twitter">
			<img class="checkmark" id="img_chk_tw" src="/images/message-success.png" style="float: left; margin-top: -8px; margin-left: 300px;" />
			<img class="name" src="/images/TextImage.aspx?text=twitter&style=h2" />			
				<p>The more <b>to-the-point</b> side of you!<br />Always know what's going on</p>
			</div>
			<div class="account" id="flickr">
			<img class="checkmark" id="img_chk_fl" src="/images/message-success.png" style="float: left; margin-top: -8px; margin-left: 300px;" />
			<img class="name" src="/images/TextImage.aspx?text=flickr&style=h2" />
				<p>If all those others don't let you<br /><b>express</b> yourself, flickr's your buddy</p>
			</div>
		</div>
	</div>

	<p style="width: 690px;">Remember that we don't ask for or store your <b>passwords</b> of these social networking websites.
	We only use your authorization and <b>permission</b> to connect to them on your behalf<br />
	to present you with the <b>latest updates</b> from your social network.
	</p>
</asp:Content>

