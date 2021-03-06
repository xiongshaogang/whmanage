<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<jsp:useBean id="userSession" scope="session" class="com.wlt.webm.rights.form.SysUserForm" />
<jsp:useBean id="commission" scope="page" class="com.wlt.webm.business.bean.SysCommission"/>
<%
  List list = commission.listCommissiontcgroup(); 
  System.out.println(list.size());
  request.setAttribute("commissionList",list); 
%>
<html:html>
<jsp:useBean id="area" scope="page" class="com.wlt.webm.rights.bean.SysArea"/>
<head>
<title>万汇通</title>
<link rel="stylesheet" type="text/css" href=../css/common.css>
<style type="text/css">
.tabs_wzdh { width: 100px; float:left;}
.tabs_wzdh li { height:35px; line-height:35px; margin-top:5px; padding-left:20px; font-size:14px; font-weight:bold;}
/*tree_nav*/
.tree_nav { border:1px solid #CCC; background:#f5f5f5; padding:10px; margin-top:20px;}
.field-item_button_m2 { margin:0 auto; text-align:center;}
.field-item_button2 { border:0px;width:136px; height:32px; color:#fff; background:url(../images/button_out2.png) no-repeat; cursor:pointer;margin-right:20px;}
.field-item_button2:hover { border:0px; width:136px; height:32px; color:#fff; background:url(../images/button_on2.png) no-repeat; margin-right:20px;}
</style>
<script type="text/javascript" src="../js/util.js"></script>
</head>
<script language="JavaScript">
	function ha_add(){
	 with(document.forms[0]){
	  action="wltcommissionadd.jsp?stype=2";
	  target="_self";
	  submit();
	  }
	}
	
		function setParentid(areavalue){
	document.forms[0].areacope.value=areavalue;	
	}
	
		function ha_modify(){
	 	with(document.forms[0]){
	 	var count = checkedSize(ids);
	  	if(count == 0){
	    	alert("请选择佣金组");
			return false;
	  	}
	 	 action="wltcommissionmodify.jsp?stype=2&sg_id="+areacope.value;
	  	target="_self";
	  	submit();
	 	 }
	 	 }
	 	 
	 	function ha_checkmx(){
	 	with(document.forms[0]){
	 	var count = checkedSize(ids);
	  	if(count == 0){
	    	alert("请选择佣金组");
			return false;
	  	}
	 	 action="wltcommissionchecktcmx.jsp?sg_id="+areacope.value;
	  	target="_self";
	  	submit();
	 	 }
	 	 }
	 	 
	 	function ha_addmx(){
	 	with(document.forms[0]){
	 	 action="wltcommissionmodify.jsp?sg_id="+areacope.value;
	  	target="_self";
	  	submit();
	 	 }
	 	 }	 	 
	
		function hasRole(){
		var size = <%=list.size()%>;
		if(size<=0){
			alert("请先添加佣金组");
			return false;
		}
		return true;
	}
	
	function isSelectRole(){
		  with(document.forms[0]){
		var count = checkedSize(ids);
	  	if(count == 0){
	    	alert("请选择佣金组");
			return false;
	  	}
	  	return true;
	  	 }
	}
	
	function ha_del(){
	  with(document.forms[0]){
	  		if(hasRole() && isSelectRole()){
			  	action = "syscommission.do?method=del";
			  	target="_self";
			  	submit();
	  		}
	  }
	}
	
</script>
<body rightmargin="0" leftmargin="0"  topmargin="0" class="body_no_bg">
<html:form method="post" action="/business/syscommission.do?method=del">
		<tr class="trcontent">
		<td colspan="4" align="center" class="fontcontitle">&nbsp;代理点提成佣金组信息&nbsp;</td>
		</tr>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" class="tablelist">
  <tr class="trlist">
    <td colspan="9"><div align="center" class="fontlisttitle">代理点提成佣金组</div></td>
  </tr>
  <tr class="fontcoltitle">
      <td nowrap="nowrap"><div align="center" >序号</div></td>
    <td nowrap="nowrap"><div align="center" >佣金组标识</div></td>
    <td nowrap="nowrap"><div align="center" >佣金组名称</div></td>
    <td nowrap="nowrap"><div align="center" >区域名称</div></td>
    <td nowrap="nowrap"><div align="center" >是否默认</div></td>
    <td ><div align="center"><input name="button1" type="button" onClick="setCheckedState(this.form.ids)" value="全选" class="onbutton">
        <input type="hidden" name="check" value="0">
      </div></td>
  </tr>
  <logic:iterate id="commissioninfo" name="commissionList" indexId="i">
  <tr class="tr1">
    <td align="center">${i + 1}</td>
    <logic:iterate id="info" name="commissioninfo">
    <td align="center">${info }</td>
    </logic:iterate>
    <td align="center"> <input name="ids" type="checkbox" value="${commissioninfo[0]}" onclick="setParentid('${commissioninfo[0]}')"></td>
  </tr>
  </logic:iterate>
</table>
<p align="center">
  <input type="hidden" name="areacope" value="${areacope}" />
  <input name="Button" type="button" value="增加" class="onbutton" onClick="ha_add()">
  <input name="Button" type="button" value="修改" class="onbutton" onClick="ha_modify()">
  <input name="Button" type="button" value="查看佣金明细" class="onbutton" onClick="ha_checkmx()">
  <input type="button" name="Submit2" value="删除" class="onbutton" onClick="ha_del()">
</p>
</html:form>
</body>
</html:html>