<%@ page contentType="text/html; charset=gb2312" language="java"%>
<%
String path = request.getContextPath();
%>

<html>
<head>
<title>账户激活</title>
<script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> 
<link href="../css/main_style.css" rel="stylesheet" type="text/css" />
<link href="../css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
  			var i = 90;
        function msgShow() {
        document.getElementById("a").href="javascript:return false;";
            document.getElementById("msg").innerHTML ="短信正在飞速发送中...<font color=red>"+i+"</font>秒后未收到短信,请重新获取 ";
            if (i > 0) {
                i--;
            }
            else {
                document.getElementById("a").href="javascript:sendMsg();";
                document.getElementById("msg").innerHTML ="(点击获取短信验证码)";
            }
            setTimeout('msgShow()', 1000);
        }

function sendMsg(a){
i=90;
msgShow();
	$.post(
    	$("#rootPath").val()+"/business/bank.do?method=phsendMsg",
      	{
      	},
      	function (data){
      		if(data == 0){
      		   document.getElementById("a").href="#";
      			alert("短信发送成功");
      		}else{
      			alert("短信发送失败");
      		}
      	}
	)
}
function btnSubmit(){
    var a=$("#msgcode").val();
    var b=document.getElementById("qr");
    if(!b.checked){
    alert("尚未同意服务协议,不允许激活");
    return false;
    }
    if(a.length!=6){
    alert("请输入正确的短信验证码");
    return false;
    }
	$("#form1").submit();
}
$(function(){
	if($("#mess").val() != ""){
		alert($("#mess").val());
	}
})
</script>
</head>
<body>
<input type="hidden" id="rootPath" value="<%=path %>"/>
    <div class="mobile-charge">
<form action="<%=path %>/rights/sysuser.do?method=phoneValidata" id="form1" method="post" class="form">
        <fieldset>
          <div class="form-line">
<br>
<br>
<br>
            <label class="label" for="mobile-num"><font size="3" color="red">第一次登陆请先修改您的登录密码,进行短信验证,激活您的账户：</font></label>
<hr>         
<br>  
 <div style="margin-left: 200;">
				<b>原始登录密码：</b><input height="30px" type="text" name="oldpwd" id="oldpwd"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>新的登录密码：</b><input height="30px" type="text" name="newpwd" id="newpwd"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>确认新密码：</b><input height="30px" type="text" name="newpwd1" id="newpwd1"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>手机验证码：</b><input height="30px" type="text" name="msgcode" id="msgcode"><a id="a" href="javascript:sendMsg();"><span id="msg">(点击获取短信验证码)</span></a>
<br>
<br>
接收短信的手机号码为:<span>${requestScope.phonenum}</span>   
<br>  
<br> 
<input type="checkbox" checked="checked" id="qr"/>我已阅读并同意相关服务条款 <a href="../rights/xieyi.html" target="view_window">服务协议</a>      
</div>
          </div>
<div class="field-item_button_m" id="btnChag" style="margin-left: 200;">
<input type="button" name="Button" value="确认" class="field-item_button" onclick="btnSubmit();">
<input type="button" name="Button" value="返回" class="field-item_button" onclick="history.go(-1)"></div>
<br>
        </fieldset>
      </form>
    </div>
</body>
<input type="hidden" id="mess" value="${requestScope.mess }"/>
</html>
