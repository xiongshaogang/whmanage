<%@ page contentType="text/html; charset=GBK" language="java"%>

<html>
	<head>
		<title>���ͨ</title>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=GBK">
		<link REL="stylesheet" TYPE="text/css" HREF="../css/common.css">
	</head>

	<body rightmargin="0" leftmargin="0" topmargin="0">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="menu_table">
			<tr>
				<td width="129">
					<img src="../images/lgo_0hm_129x36.on.gif" width="129" height="33">
				</td>
				<td width="125" background="../images/menu0.on.bg.gif" nowrap>
					<div align="center">
						<a href="tepay.jsp" class="fontmenu">�Ƹ�ͨת��</a>
					</div>
				</td>
				<td width="11">
					<img src="../images/menu0.on.end.gif" width="11" height="33">
				</td>
				<td class="menubg">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td height="10" colspan="4" background="../images/menu.bg.gif">
					<div align="center"></div>
				</td>
			</tr>
			<tr>
				<td height="10" colspan="4" background="../images/menu.bg.gif">
					<div align="center"></div>
				</td>
			</tr>
		</table>
		<form action="tenpaytransfer.jsp" method="get" name="directFrm"
			onSubmit="return payFrm();">
			<table width="60%" border="0" align="center" cellpadding="0"
				cellspacing="1" class="tableoutline">
				<tr class="trcontent">
					<td colspan="4" align="center" class="fontcontitle">
						&nbsp;�Ƹ�ͨת��ʧ��&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" align="center" class="tablecontent">
							<tr>
								<td width="50%" align="center">
									&nbsp;&nbsp;<img src="../images/commonTip.gif" width="25" height="25"/>&nbsp;&nbsp;<font color="#FF0000">��ͨ���Ƹ�ͨ��ת��ʧ��!</font>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<p align="center">
				<input type="button" name="continue" value="�ر�" class="onbutton" onclick="window.close();">
			</p>
			<br><br><br>
			<p align="center">
				<font color="#0000CC">��������ϵ�����������Ƽ����޹�˾</font>
			</p>
		</form>
	</body>
</html>