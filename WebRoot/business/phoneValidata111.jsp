<%@ page contentType="text/html; charset=gb2312" language="java"%>
<%
String path = request.getContextPath();
%>

<html>
<head>
<title>�˻�����</title>
<script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> 
<link href="../css/main_style.css" rel="stylesheet" type="text/css" />
<link href="../css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
  			var i = 90;
        function msgShow() {
        document.getElementById("a").href="javascript:return false;";
            document.getElementById("msg").innerHTML ="�������ڷ��ٷ�����...<font color=red>"+i+"</font>���δ�յ�����,�����»�ȡ ";
            if (i > 0) {
                i--;
            }
            else {
                document.getElementById("a").href="javascript:sendMsg();";
                document.getElementById("msg").innerHTML ="(�����ȡ������֤��)";
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
      			alert("���ŷ��ͳɹ�");
      		}else{
      			alert("���ŷ���ʧ��");
      		}
      	}
	)
}
function btnSubmit(){
    var a=$("#msgcode").val();
    var b=document.getElementById("qr");
    if(!b.checked){
    alert("��δͬ�����Э��,����������");
    return false;
    }
    if(a.length!=6){
    alert("��������ȷ�Ķ�����֤��");
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
            <label class="label" for="mobile-num"><font size="3" color="red">��һ�ε�½�����޸����ĵ�¼����,���ж�����֤,���������˻���</font></label>
<hr>         
<br>  
 <div style="margin-left: 200;">
				<b>ԭʼ��¼���룺</b><input height="30px" type="text" name="oldpwd" id="oldpwd"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>�µĵ�¼���룺</b><input height="30px" type="text" name="newpwd" id="newpwd"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>ȷ�������룺</b><input height="30px" type="text" name="newpwd1" id="newpwd1"><font color="red" size=3>&nbsp;&nbsp;*</font>
<br>
				<b>�ֻ���֤�룺</b><input height="30px" type="text" name="msgcode" id="msgcode"><a id="a" href="javascript:sendMsg();"><span id="msg">(�����ȡ������֤��)</span></a>
<br>
<br>
���ն��ŵ��ֻ�����Ϊ:<span>${requestScope.phonenum}</span>   
<br>  
<br> 
<input type="checkbox" checked="checked" id="qr"/>�����Ķ���ͬ����ط������� <a href="../rights/xieyi.html" target="view_window">����Э��</a>      
</div>
          </div>
<div class="field-item_button_m" id="btnChag" style="margin-left: 200;">
<input type="button" name="Button" value="ȷ��" class="field-item_button" onclick="btnSubmit();">
<input type="button" name="Button" value="����" class="field-item_button" onclick="history.go(-1)"></div>
<br>
        </fieldset>
      </form>
    </div>
</body>
<input type="hidden" id="mess" value="${requestScope.mess }"/>
</html>