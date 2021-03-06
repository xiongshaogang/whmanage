package com.wlt.webm.ten.action;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.commsoft.epay.util.logging.Log;
import com.wlt.webm.business.bean.AcctBillBean;
import com.wlt.webm.business.bean.BiProd;
import com.wlt.webm.db.DBService;
import com.wlt.webm.gm.bean.GuanMingBean;
import com.wlt.webm.rights.form.SysUserForm;
import com.wlt.webm.ten.bean.QbZhiTongChe;
import com.wlt.webm.ten.bean.WhQb;
import com.wlt.webm.ten.service.TenpayXmlPath;
import com.wlt.webm.tool.FloatArith;
import com.wlt.webm.util.TenpayUtil;
import com.wlt.webm.util.Tools;

/**
 * Q币业务处理
 * 
 * @author caiSJ
 * 
 */
public class QbMiddle extends DispatchAction {

//	/**
//	 * 判断返回结果的Action
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return ActionForward
//	 * @throws Exception
//	 */
//	public ActionForward qbTransfer(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		if (null == request.getSession().getAttribute("userSession")) {
//			request.setAttribute("mess", "请先登录");
//			return new ActionForward("/rights/wltlogin.jsp");
//		}
//		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
//				"userSession");
//		if (!userForm.getRoleType().equals("3")) {
//			request.setAttribute("mess", "当前用户类型不支持交易");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		if (!Tools.validateTime()) {
//			request.setAttribute("mess", "23:50到00:10不允许交易");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
//		
//		BiProd bp = new BiProd();
//		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
//		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
//		{
//			request.setAttribute("mess", "今天额度已用完,添加额度,请联系管理员!");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//				
//		String in_acct = request.getParameter("in_acct");
//		if ("0".equals(num) || null == num || null == in_acct || num == ""
//				|| in_acct == "") {
//			request.setAttribute("mess", "请输入完整的数据");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		if (num.indexOf("-") != -1) {
//			request.setAttribute("mess", "交易个数不合法!");
//			return new ActionForward("/business/zshqbpay.jsp");
//		}
//		if (Integer.parseInt(num) > 200) {
//			request.setAttribute("mess", "单次最多200个Q币");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		String user_no = userForm.getUserno();
//		String sql0 = "select fundacct from wht_facct where user_no='"
//				+ user_no + "'";
//		DBService dbService = null;
//		// 当前时间 yyyyMMddHHmmss
//		TenpayXmlPath tenpayxmlpath = new TenpayXmlPath();
//		String currTime = TenpayUtil.getCurrTime();
//		String transaction_id = TenpayUtil.getQBChars(currTime);
//		String account = "";
//		
//		try {
//			dbService = new DBService();
//			if (dbService.hasData("select tradeserial from wht_orderform_"
//					+ currTime.substring(2, 6) + " where tradeserial='"
//					+ transaction_id + "'")) {
//				request.setAttribute("mess", "重复交易");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String facct = dbService.getString(sql0);
//			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
//					+ facct + "01'";
//			Float money = dbService.getFloat(sql);
//			int rebate = 1000;// qq币单价
//			if (money - Integer.parseInt(num) * 1000 <= 0) {
//				if (null != dbService) {
//					dbService.close();
//				}
//				request.setAttribute("mess", "账户余额不足");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
//					: "0";
//			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
//					user_no, Integer.parseInt(userForm.getParent_id()));
//			if (null == employFee) {
//				request.setAttribute("mess", "佣金比不存在请联系客服");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//
//			// 计算佣金
//			int facctfee = Integer.parseInt(num) * rebate;
//			int parentfee = 0;
//			if ("1".equals(flag)) {
//				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
//						Double.valueOf(employFee[0]), 100));
//			} else {
//				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// 总佣金比
//				double b = FloatArith.mul(facctfee, a);// 总佣金
//
//				double c = FloatArith.mul(b, FloatArith.div(Double
//						.valueOf(employFee[1]), 100));// 代办点应得佣金
//
//				facctfee = (int) FloatArith.sub(facctfee, c);// 应扣金额
//				parentfee = (int) FloatArith.sub(b, c);// 上级节点应得佣金
//			}
//			//广信指定订单流水号
//			String GuangXinSerialNo=Tools.getGuangXinSerialNo();
//			String interfaceTypes="";
//			//判断走那个接口
//			if(WhQb.isUse(dbService,Integer.parseInt(num))){
//				interfaceTypes="16";//广信
//			}else{
//				interfaceTypes="10";//冠名
//			}
//			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
//					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
//					facctfee, facct + "01", currTime, currTime, 4, GuangXinSerialNo,
//					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
//					user_no, userForm.getUsername(), 3, 381, null };// 20
//			String acct1 = Tools.getAccountSerial(currTime, user_no);
//			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
//					Integer.parseInt(num) * rebate, facctfee, 6, "购买Q币", 0,
//					currTime, money - facctfee, transaction_id, facct + "01", 1 };
//			boolean rsFlag = false;
//			String acct2 = Tools.getAccountSerial(currTime, user_no);
//			String parentFacct = "";
//			if ("1".equals(flag)) {
//				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, null, flag,
//						facct, null, currTime, facctfee, 0);
//			} else {
//				parentFacct = bp.getFacctByUserID(dbService, Integer
//						.parseInt(userForm.getParent_id()));
//				String sqlq = "select accountleft from  wht_childfacct where  childfacct ='"
//						+ parentFacct + "02'";
//				int moneyq = dbService.getInt(sqlq);
//				Object[] acctObj1 = { null, parentFacct + "02", acct2, in_acct,
//						currTime, parentfee, parentfee, 15, "下级交易返佣", 0,
//						currTime, moneyq + parentfee, transaction_id,
//						parentFacct + "02", 2 };
//				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
//								flag, facct, parentFacct, currTime, facctfee,
//								parentfee);
//			}
//			if (rsFlag == false) {
//				request.setAttribute("mess", "交易失败");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String ip = Tools.getIpAddr(request);
//			Map<String, String> rs=null;
//			if("16".equals(interfaceTypes)){//广信
//				Log.info("普通代理点万恒Q币充值,广信充值,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//				//广信
//				int ss=WhQb.useQB(GuangXinSerialNo, GuangXinSerialNo, in_acct, Integer.parseInt(num)*rebate);
//				if(ss==0){
//					Log.info("普通代理点万恒Q币充值,广信充值,充值成功,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//					rs=new HashMap<String, String>();
//					rs.put("state",ss+"".trim());
//					rs.put("orderid", "GuangXing09024");
//					rs.put("InterfaceType","1");//广信
//				}else if(ss==-1){
//					Log.info("普通代理点万恒Q币充值,广信充值,充值失败,调用冠名接口充值,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//					//失败，调用冠名
//					rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
//					rs.put("InterfaceType","2");//冠名
//					Log.info("普通代理点万恒Q币充值,广信充值,充值失败,调用冠名接口充值,充值结果:"+rs.get("state")+",WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//					dbService.update("UPDATE wht_orderform_"+Tools.getNow4().substring(2,6)+" SET buyid='10' WHERE tradeserial='"+transaction_id+"'");
//				}else{
//					Log.info("普通代理点万恒Q币充值,广信充值,充值超时,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//					//超时
//					rs=new HashMap<String, String>();
//					rs.put("state",ss+"".trim());
//					rs.put("orderid", "GuangXing09024");
//					rs.put("InterfaceType","1");//广信
//				}
//			}else {//冠名
//				Log.info("普通代理点万恒Q币充值,冠名充值,WhQb.isUse=false,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//				//冠名
//				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
//				rs.put("InterfaceType","2");//冠名
//				Log.info("普通代理点万恒Q币充值,冠名充值,充值结果:"+rs.get("state")+",WhQb.isUse=false,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
//			}
//			
//			String state=rs.get("state");
//			
//			String msg = "";
//			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
//			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
//			if (state.equals("0")) {
//				msg = "交易成功";
//				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
//						+ "#" + Integer.parseInt(num) * rebate + "#"
//						+ rs.get("orderid"), transaction_id, user_no,rs.get("InterfaceType"));
//			} else if (state.equals("-1")) {
//				msg = "交易 失败";
//				if ("1".equals(flag)) {
//					bp.innerFailDeal(dbService, orderTable, acctTable,
//							transaction_id + "#" + Integer.parseInt(num)
//									* rebate + "#" + rs.get("orderid"),
//							transaction_id, user_no, acct1, "", facctfee, 0,
//							facct, "", "1");
//				} else {
//					bp.innerFailDeal(dbService, orderTable, acctTable,
//							transaction_id + "#" + Integer.parseInt(num)
//									* rebate + "#" + rs.get("orderid"),
//							transaction_id, user_no, acct1, acct2, facctfee,
//							parentfee, facct, parentFacct, "0");
//				}
//			} else {
//				msg = "交易处理中请联系客服";
//				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
//			}
//			request.setAttribute("mess", msg);
//			return new ActionForward("/wlttencent/qbpay.jsp");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			request.setAttribute("mess", "系统异常请联系客服");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//	}
	
	/**
	 * 判断返回结果的Action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward qbTransfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (null == request.getSession().getAttribute("userSession")) {
			request.setAttribute("mess", "请先登录");
			return new ActionForward("/rights/wltlogin.jsp");
		}
		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
				"userSession");
		if (!userForm.getRoleType().equals("3")) {
			request.setAttribute("mess", "当前用户类型不支持交易");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		if (!Tools.validateTime()) {
			request.setAttribute("mess", "23:50到00:10不允许交易");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
		
		BiProd bp = new BiProd();
		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
		{
			request.setAttribute("mess", "今天额度已用完,添加额度,请联系管理员!");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
				
		String in_acct = request.getParameter("in_acct");
		if ("0".equals(num) || null == num || null == in_acct || num == ""
				|| in_acct == "") {
			request.setAttribute("mess", "请输入完整的数据");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		if (num.indexOf("-") != -1) {
			request.setAttribute("mess", "交易个数不合法!");
			return new ActionForward("/business/zshqbpay.jsp");
		}
		if (Integer.parseInt(num) > 200) {
			request.setAttribute("mess", "单次最多200个Q币");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		String user_no = userForm.getUserno();
		String sql0 = "select fundacct from wht_facct where user_no='"
				+ user_no + "'";
		DBService dbService = null;
		// 当前时间 yyyyMMddHHmmss
		TenpayXmlPath tenpayxmlpath = new TenpayXmlPath();
		String currTime = TenpayUtil.getCurrTime();
		String transaction_id = TenpayUtil.getQBChars(currTime);
		String account = "";
		
		try {
			dbService = new DBService();
			if (dbService.hasData("select tradeserial from wht_orderform_"
					+ currTime.substring(2, 6) + " where tradeserial='"
					+ transaction_id + "'")) {
				request.setAttribute("mess", "重复交易");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String facct = dbService.getString(sql0);
			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
					+ facct + "01'";
			Float money = dbService.getFloat(sql);
			int rebate = 1000;// qq币单价
			if (money - Integer.parseInt(num) * 1000 <= 0) {
				if (null != dbService) {
					dbService.close();
				}
				request.setAttribute("mess", "账户余额不足");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
					: "0";
			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
					user_no, Integer.parseInt(userForm.getParent_id()));
			if (null == employFee) {
				request.setAttribute("mess", "佣金比不存在请联系客服");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}

			// 计算佣金
			int facctfee = Integer.parseInt(num) * rebate;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// 总佣金比
				double b = FloatArith.mul(facctfee, a);// 总佣金

				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// 代办点应得佣金

				facctfee = (int) FloatArith.sub(facctfee, c);// 应扣金额
				parentfee = (int) FloatArith.sub(b, c);// 上级节点应得佣金
			}
			//广信指定订单流水号
			String GuangXinSerialNo=Tools.getGuangXinSerialNo();
			String interfaceTypes="";
			boolean bool=WhQb.IsMultiChannel();
			if(bool){//判断走那个接口
				interfaceTypes="16";//广信
			}else{
				interfaceTypes="10";//冠名
			}
			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
					facctfee, facct + "01", currTime, currTime, 4, GuangXinSerialNo,
					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
					user_no, userForm.getUsername(), 3, 381, null };// 20
			String acct1 = Tools.getAccountSerial(currTime, user_no);
			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
					Integer.parseInt(num) * rebate, facctfee, 6, "购买Q币", 0,
					currTime, money - facctfee, transaction_id, facct + "01", 1 };
			boolean rsFlag = false;
			String acct2 = Tools.getAccountSerial(currTime, user_no);
			String parentFacct = "";
			if ("1".equals(flag)) {
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, null, flag,
						facct, null, currTime, facctfee, 0);
			} else {
				parentFacct = bp.getFacctByUserID(dbService, Integer
						.parseInt(userForm.getParent_id()));
				String sqlq = "select accountleft from  wht_childfacct where  childfacct ='"
						+ parentFacct + "02'";
				long moneyq = dbService.getLong(sqlq);
				Object[] acctObj1 = { null, parentFacct + "02", acct2, in_acct,
						currTime, parentfee, parentfee, 15, "下级交易返佣", 0,
						currTime, moneyq + parentfee, transaction_id,
						parentFacct + "02", 2 };
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
								flag, facct, parentFacct, currTime, facctfee,
								parentfee);
			}
			if (rsFlag == false) {
				request.setAttribute("mess", "交易失败");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
			String ip = Tools.getIpAddr(request);
			Map<String, String> rs=null;
			//广信接口，面额不用拆单
			if(bool && 
					(Integer.parseInt(num)==1 || 
					Integer.parseInt(num)==5 || 
					Integer.parseInt(num)==10 || 
					Integer.parseInt(num)==20 || 
					Integer.parseInt(num)==30)){
				Log.info("普通代理点万恒Q币充值,广信充值,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
				//广信
				int ss=WhQb.useQB(GuangXinSerialNo, GuangXinSerialNo, in_acct, Integer.parseInt(num)*rebate);
				if(ss==0){
					Log.info("普通代理点万恒Q币充值,广信充值,充值成功,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
					rs=new HashMap<String, String>();
					rs.put("state",ss+"".trim());
					rs.put("orderid", "GuangXing09024");
					rs.put("InterfaceType","1");//广信
				}else if(ss==-1){
					Log.info("普通代理点万恒Q币充值,广信充值,充值失败,调用冠名接口充值,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
					//失败，调用冠名
					dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
					rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
					rs.put("InterfaceType","2");//冠名
					Log.info("普通代理点万恒Q币充值,广信充值,充值失败,调用冠名接口充值,充值结果:"+rs.get("state")+",WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
				}else{
					Log.info("普通代理点万恒Q币充值,广信充值,充值超时,WhQb.isUse=true,,,transaction_id："+transaction_id+",writeoff:"+GuangXinSerialNo);
					//超时
					rs=new HashMap<String, String>();
					rs.put("state",ss+"".trim());
					rs.put("orderid", "GuangXing09024");
					rs.put("InterfaceType","1");//广信
				}
			}else if(bool && 
					Integer.parseInt(num)!=1 &&
					Integer.parseInt(num)!=5 &&
					Integer.parseInt(num)!=10 &&
					Integer.parseInt(num)!=20 &&
					Integer.parseInt(num)!=30){
				//拆单
				//拆单 金额
				int[] strs=com.wlt.webm.util.Tools.decomposeQB1(Integer.parseInt(num));
				String tableName="wht_qbbreakrecord_"+Tools.getNow3().substring(2,6);
				
				int oneState=-1;
				int con=0;
				for(int i=0;i<2;i++){
					if(strs[i]==1 || strs[i]==5 || strs[i]==10 || strs[i]==20 || strs[i]==30){
						con++;
						//订单表transaction_id  对应辅助表wht_qbbreakrecord_1503 关联字段oldorderid  
						String order_One=com.wlt.webm.util.Tools.getGuangXinSerialNo();
						//消息流水号
						String transaction_id_One=TenpayUtil.getQBChars(currTime);
						Object[] qbbreakrecordOne={null,order_One,in_acct,16,"0005",strs[i] * rebate,
								strs[i] * rebate,currTime,Tools.getNow3(),4,order_One+"#"+strs[i] * rebate,userForm.getUserno(),transaction_id,"","","",""};
						dbService.logData(17, qbbreakrecordOne, tableName);
						oneState=WhQb.useQB(transaction_id_One, order_One, in_acct, Integer.parseInt(strs[i]+"") * rebate);
						if(i==0){//第一笔单 广信
							if(oneState==0){//拆单后的第一笔走广信，成功
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=0 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								continue;
							}else if(oneState==-1){//失败
								dbService.update("delete from wht_qbbreakrecord_" + currTime.substring(2, 6)+
										"  where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								break;
							}else{//未知状态,异常订单处理,转人工
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=6 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								num=strs[1]+"";
								break;
							}
						}else{//第二笔单 广信 第一笔广信充值成功，
							if(oneState==0){//拆单后的两笔都走，广信，并且两笔都成功,结束
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=0 where oldorderid='"+transaction_id+"'");
								dbService.update("update wht_orderform_" + currTime.substring(2, 6)+
										" set state=0,writecheck='"+transaction_id+ "#" + Integer.parseInt(num) * rebate + "#"+ 
										transaction_id+"' where tradeserial='"+transaction_id+"' and userno='"+userForm.getUserno()+"'");
								request.setAttribute("mess","充值成功");
								return new ActionForward("/wlttencent/qbpay.jsp");
							}else if(oneState==-1){// 第二笔充值失败，第二笔钱 走冠名
								dbService.update("delete from  wht_qbbreakrecord_" + currTime.substring(2, 6)+
										"  where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								num=strs[1]+"";
								break;
							}else{//未知状态,异常订单处理,转人工
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=6 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								request.setAttribute("mess","充值成功");
								return new ActionForward("/wlttencent/qbpay.jsp");
							}
						}
					}else{//第二笔单 冠名
						num=strs[1]+"";
						break;
					}
				}
				Object[] qbbreakrecordOne={null,transaction_id,in_acct,10,"0005",Integer.parseInt(num) * rebate,
						Integer.parseInt(num) * rebate,currTime,"",4,transaction_id+"#"+Integer.parseInt(num)* rebate,userForm.getUserno(),transaction_id,"","","",""};
				dbService.logData(17, qbbreakrecordOne, tableName);
				dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
				rs.put("InterfaceType","2");//冠名
			}else {//冠名
				dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
				rs.put("InterfaceType","2");//冠名
			}
			
			
			
			String state=rs.get("state");
			String msg = "";
			
			if (state.equals("0")) {
				msg = "交易成功";
				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
						+ "#" + Integer.parseInt(num) * rebate + "#"
						+ rs.get("orderid"), transaction_id, user_no,rs.get("InterfaceType"));
			} else if (state.equals("-1")) {
				msg = "交易 失败";
				if ("1".equals(flag)) {
					bp.innerFailDeal(dbService, orderTable, acctTable,
							transaction_id + "#" + Integer.parseInt(num)
									* rebate + "#" + rs.get("orderid"),
							transaction_id, user_no, acct1, "", facctfee, 0,
							facct, "", "1");
				} else {
					bp.innerFailDeal(dbService, orderTable, acctTable,
							transaction_id + "#" + Integer.parseInt(num)
									* rebate + "#" + rs.get("orderid"),
							transaction_id, user_no, acct1, acct2, facctfee,
							parentfee, facct, parentFacct, "0");
				}
			} else {
				msg = "交易处理中请联系客服";
				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
			}
			request.setAttribute("mess", msg);
			return new ActionForward("/wlttencent/qbpay.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("mess", "系统异常请联系客服");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
	}

	/**
	 * 冠名Q币
	 * @param in_acct
	 * @param num
	 * @param transaction_id
	 * @param ip
	 * @return
	 */
	public static Map<String, String> QueryGuanMingState(String in_acct,String num,String transaction_id,String ip){
		Map map = new HashMap<String, String>();
		map.put("gameapi", "qqes");
		map.put("account", in_acct);
		map.put("buynum", num);
		map.put("sporderid", transaction_id);
		map.put("gamecode", null);
		map.put("gamearea", null);
		map.put("gameserver", null);
		map.put("gamerole", null);
		map.put("clientip", ip);
		String returl = "http://www.wanhuipay.com/business/bank.do?method=GuanMingCallBack";
		// String returl=
		// "http://shijianqiao321.xicp.net:8888/business/bank.do?method=GuanMingCallBack"
		// ;
		map.put("returl", returl);
		GuanMingBean gmBean = new GuanMingBean();
		Map<String, String> rs = gmBean.fillGameOrder(map);
//		String state = rs.get("state");
		//rs.get("orderid")
		return rs;
	}
	
	/**
	 * 获取产品id（AJAX）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward getMoney(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("html/json; charset=UTF-8");

		StringBuffer sBuffer = new StringBuffer();
		DBService dbService = null;
		try {
			PrintWriter pWriter = response.getWriter();
			SysUserForm userForm = (SysUserForm) request.getSession()
					.getAttribute("userSession");
			if (null == userForm) {
				request.setAttribute("mess", "登录超时,请重新登录");
				return new ActionForward("/rights/wltlogin.jsp");
			}
			String num = request.getParameter("price");
			String type = request.getParameter("gametype");
			if (type.equals("qqes4x")) {
				num = (Integer.parseInt(num) * 10) + "";
			}
			dbService = new DBService();
			String flag = AcctBillBean.getStatus(userForm.getParent_id()) ? "1"
					: "0";
			BiProd bp = new BiProd();
			String[] employFee = bp.getZZEmploy(dbService, type, flag, userForm
					.getUserno(), Integer.parseInt(userForm.getParent_id()));
			if (null == employFee) {
				sBuffer.delete(0, sBuffer.length());
				sBuffer.append("[{\"mon\":\"" + "系统繁忙....采购价格暂无法显示" + "\"}]");
			}

			// 计算佣金
			int facctfee = Integer.parseInt(num) * 1000;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// 总佣金比
				double b = FloatArith.mul(facctfee, a);// 总佣金
				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// 代办点应得佣金
				facctfee = (int) FloatArith.sub(facctfee, c);// 应扣金额
			}

			sBuffer.append("[{\"mon\":\""
					+ "采购价格:"
					+ FloatArith.div(facctfee, 1000)
					+ "元 , 利润:"
					+ FloatArith.div(FloatArith.sub(
							Integer.parseInt(num) * 1000, facctfee), 1000)
					+ "元\"}]");
			pWriter.print(sBuffer.toString());
			pWriter.flush();
			pWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			sBuffer.delete(0, sBuffer.length());
			sBuffer.append("[{\"mon\":\"" + "系统繁忙..采购价格暂无法显示" + "\"}]");
		} finally {
			if (dbService != null) {
				dbService.close();
			}
		}
		return null;
	}

	/**
	 * 查询是否存在某条记录 或更新某条记录
	 * 
	 * @param sql
	 * @param params
	 * @param lg
	 * @return flag
	 */
	public static boolean getRecord(String sql, Object[] params, String lg) {
		boolean flag = false;
		DBService dbService = null;
		try {
			dbService = new DBService();
			if (lg.equals("1")) {
				List list = dbService.getStringList(sql, params);
				if (list.size() != 0) {
					flag = true;
					Log.info("收到重复消息不做处理");
				} else {
					flag = false;
					Log.info("没查到记录...");
				}
			} else {
				int i = 0;
				i = dbService.update(sql, params);
				if (i != 0)
					flag = true;
			}
		} catch (SQLException e) {
			Log.info("QB异常" + e.toString());
			e.printStackTrace();
		} finally {
			dbService.close();
		}
		return flag;
	}

	public String doShowMessage(int type) {
		String message = "";
		switch (type) {
		case 0:
			message = "充值成功";
			break;
		case 1:
			message = "数字签名错误";
			break;
		case 2:
			message = "订单重复提交";
			break;
		case 3:
			message = "用户帐号不存在";
			break;
		case 4:
			message = "系统错误";
			break;
		case 5:
			message = "IP错误";
			break;
		case 6:
			message = "用户key错误";
			break;
		case 7:
			message = "参数错误";
			break;
		case 8:
			message = "库存不足";
			break;
		case 9:
			message = "用户状态异常";
			break;
		case 10:
			message = "订单处理中";
			break;
		case 101:
			message = "此功能暂时不可用";
			break;
		case 102:
			message = "该商业号权限不足";
			break;
		case 103:
			message = "系统维护中";
			break;
		default:
			message = "未知错误";
			break;
		}
		return message;
	}
	
	/**
	 * 腾讯直通车
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward qbTenpayTransfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (null == request.getSession().getAttribute("userSession")) {
			request.setAttribute("mess", "请先登录");
			return new ActionForward("/rights/wltlogin.jsp");
		}
		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
				"userSession");
		if (!userForm.getRoleType().equals("3")) {
			request.setAttribute("mess", "当前用户类型不支持交易");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		if (!Tools.validateTime()) {
			request.setAttribute("mess", "23:50到00:10不允许交易");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
		
		BiProd bp = new BiProd();
		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
		{
			request.setAttribute("mess", "今天额度已用完,添加额度,请联系管理员!");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
				
		String in_acct = request.getParameter("in_acct");
		if ("0".equals(num) || null == num || null == in_acct || num == ""
				|| in_acct == "") {
			request.setAttribute("mess", "请输入完整的数据");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		if (num.indexOf("-") != -1) {
			request.setAttribute("mess", "交易个数不合法!");
			return new ActionForward("/business/tenxunqbpay.jsp");
		}
		if (Integer.parseInt(num) > 200) {
			request.setAttribute("mess", "单次最多200个Q币");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		String user_no = userForm.getUserno();
		String sql0 = "select fundacct from wht_facct where user_no='"
				+ user_no + "'";
		DBService dbService = null;
		// 当前时间 yyyyMMddHHmmss
		TenpayXmlPath tenpayxmlpath = new TenpayXmlPath();
		String currTime = TenpayUtil.getCurrTime();
		String transaction_id = TenpayUtil.getQBChars(currTime);
		String account = "";
		
		try {
			dbService = new DBService();
			if (dbService.hasData("select tradeserial from wht_orderform_"
					+ currTime.substring(2, 6) + " where tradeserial='"
					+ transaction_id + "'")) {
				request.setAttribute("mess", "重复交易");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String facct = dbService.getString(sql0);
			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
					+ facct + "01'";
			Float money = dbService.getFloat(sql);
			int rebate = 1000;// qq币单价
			if (money - Integer.parseInt(num) * 1000 <= 0) {
				if (null != dbService) {
					dbService.close();
				}
				request.setAttribute("mess", "账户余额不足");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
					: "0";
			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
					user_no, Integer.parseInt(userForm.getParent_id()));
			if (null == employFee) {
				request.setAttribute("mess", "佣金比不存在请联系客服");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}

			// 计算佣金
			int facctfee = Integer.parseInt(num) * rebate;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// 总佣金比
				double b = FloatArith.mul(facctfee, a);// 总佣金

				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// 代办点应得佣金

				facctfee = (int) FloatArith.sub(facctfee, c);// 应扣金额
				parentfee = (int) FloatArith.sub(b, c);// 上级节点应得佣金
			}
			String interfaceTypes="89";
			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
					facctfee, facct + "01", currTime, currTime, 4, "",
					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
					user_no, userForm.getUsername(), 3, 381, null };// 20
			String acct1 = Tools.getAccountSerial(currTime, user_no);
			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
					Integer.parseInt(num) * rebate, facctfee, 6, "购买Q币", 0,
					currTime, money - facctfee, transaction_id, facct + "01", 1 };
			boolean rsFlag = false;
			String acct2 = Tools.getAccountSerial(currTime, user_no);
			String parentFacct = "";
			if ("1".equals(flag)) {
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, null, flag,
						facct, null, currTime, facctfee, 0);
			} else {
				parentFacct = bp.getFacctByUserID(dbService, Integer
						.parseInt(userForm.getParent_id()));
				String sqlq = "select accountleft from  wht_childfacct where  childfacct ='"
						+ parentFacct + "02'";
				long moneyq = dbService.getLong(sqlq);
				Object[] acctObj1 = { null, parentFacct + "02", acct2, in_acct,
						currTime, parentfee, parentfee, 15, "下级交易返佣", 0,
						currTime, moneyq + parentfee, transaction_id,
						parentFacct + "02", 2 };
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
								flag, facct, parentFacct, currTime, facctfee,
								parentfee);
			}
			if (rsFlag == false) {
				request.setAttribute("mess", "交易失败");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
			String ip = Tools.getIpAddr(request);
			String state=QbZhiTongChe.fill(currTime.substring(8),transaction_id,in_acct,
					 num,System.currentTimeMillis()/1000);//调用腾讯Q币充值;
			if("error".equals(state)){
				state="88";
			}else{
			state=state.substring(state.indexOf("ret=")+4, state.indexOf("&mch_id"));
			}
			String msg = "";
			if (state.equals("0")) {
				msg = "交易成功";
				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
						+ "#" + Integer.parseInt(num) * rebate + "#"
						+ transaction_id, transaction_id, user_no,interfaceTypes);
			} else if (state.equals("1")||state.equals("2")||state.equals("3")||
					state.equals("5")||state.equals("6")||state.equals("7")
					||state.equals("8")||state.equals("9")||state.equals("101")
					||state.equals("102")||state.equals("103")) {
				msg = "交易 失败";
				if ("1".equals(flag)) {
					bp.innerFailDeal(dbService, orderTable, acctTable,
							transaction_id + "#" + Integer.parseInt(num)
									* rebate + "#" + transaction_id,
							transaction_id, user_no, acct1, "", facctfee, 0,
							facct, "", "1");
				} else {
					bp.innerFailDeal(dbService, orderTable, acctTable,
							transaction_id + "#" + Integer.parseInt(num)
									* rebate + "#" + transaction_id,
							transaction_id, user_no, acct1, acct2, facctfee,
							parentfee, facct, parentFacct, "0");
				}
			} else {
				msg = "交易处理中请联系客服";
				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
			}
			request.setAttribute("mess", msg);
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("mess", "系统异常请联系客服");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
	}

}
