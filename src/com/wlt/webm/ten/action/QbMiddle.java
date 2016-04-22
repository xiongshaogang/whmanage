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
 * Q��ҵ����
 * 
 * @author caiSJ
 * 
 */
public class QbMiddle extends DispatchAction {

//	/**
//	 * �жϷ��ؽ����Action
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
//			request.setAttribute("mess", "���ȵ�¼");
//			return new ActionForward("/rights/wltlogin.jsp");
//		}
//		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
//				"userSession");
//		if (!userForm.getRoleType().equals("3")) {
//			request.setAttribute("mess", "��ǰ�û����Ͳ�֧�ֽ���");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		if (!Tools.validateTime()) {
//			request.setAttribute("mess", "23:50��00:10����������");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
//		
//		BiProd bp = new BiProd();
//		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
//		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
//		{
//			request.setAttribute("mess", "������������,���Ӷ��,����ϵ����Ա!");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//				
//		String in_acct = request.getParameter("in_acct");
//		if ("0".equals(num) || null == num || null == in_acct || num == ""
//				|| in_acct == "") {
//			request.setAttribute("mess", "����������������");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		if (num.indexOf("-") != -1) {
//			request.setAttribute("mess", "���׸������Ϸ�!");
//			return new ActionForward("/business/zshqbpay.jsp");
//		}
//		if (Integer.parseInt(num) > 200) {
//			request.setAttribute("mess", "�������200��Q��");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//		String user_no = userForm.getUserno();
//		String sql0 = "select fundacct from wht_facct where user_no='"
//				+ user_no + "'";
//		DBService dbService = null;
//		// ��ǰʱ�� yyyyMMddHHmmss
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
//				request.setAttribute("mess", "�ظ�����");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String facct = dbService.getString(sql0);
//			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
//					+ facct + "01'";
//			Float money = dbService.getFloat(sql);
//			int rebate = 1000;// qq�ҵ���
//			if (money - Integer.parseInt(num) * 1000 <= 0) {
//				if (null != dbService) {
//					dbService.close();
//				}
//				request.setAttribute("mess", "�˻�����");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
//					: "0";
//			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
//					user_no, Integer.parseInt(userForm.getParent_id()));
//			if (null == employFee) {
//				request.setAttribute("mess", "Ӷ��Ȳ���������ϵ�ͷ�");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//
//			// ����Ӷ��
//			int facctfee = Integer.parseInt(num) * rebate;
//			int parentfee = 0;
//			if ("1".equals(flag)) {
//				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
//						Double.valueOf(employFee[0]), 100));
//			} else {
//				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// ��Ӷ���
//				double b = FloatArith.mul(facctfee, a);// ��Ӷ��
//
//				double c = FloatArith.mul(b, FloatArith.div(Double
//						.valueOf(employFee[1]), 100));// �����Ӧ��Ӷ��
//
//				facctfee = (int) FloatArith.sub(facctfee, c);// Ӧ�۽��
//				parentfee = (int) FloatArith.sub(b, c);// �ϼ��ڵ�Ӧ��Ӷ��
//			}
//			//����ָ��������ˮ��
//			String GuangXinSerialNo=Tools.getGuangXinSerialNo();
//			String interfaceTypes="";
//			//�ж����Ǹ��ӿ�
//			if(WhQb.isUse(dbService,Integer.parseInt(num))){
//				interfaceTypes="16";//����
//			}else{
//				interfaceTypes="10";//����
//			}
//			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
//					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
//					facctfee, facct + "01", currTime, currTime, 4, GuangXinSerialNo,
//					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
//					user_no, userForm.getUsername(), 3, 381, null };// 20
//			String acct1 = Tools.getAccountSerial(currTime, user_no);
//			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
//					Integer.parseInt(num) * rebate, facctfee, 6, "����Q��", 0,
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
//						currTime, parentfee, parentfee, 15, "�¼����׷�Ӷ", 0,
//						currTime, moneyq + parentfee, transaction_id,
//						parentFacct + "02", 2 };
//				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
//								flag, facct, parentFacct, currTime, facctfee,
//								parentfee);
//			}
//			if (rsFlag == false) {
//				request.setAttribute("mess", "����ʧ��");
//				return new ActionForward("/wlttencent/qbpay.jsp");
//			}
//			String ip = Tools.getIpAddr(request);
//			Map<String, String> rs=null;
//			if("16".equals(interfaceTypes)){//����
//				Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//				//����
//				int ss=WhQb.useQB(GuangXinSerialNo, GuangXinSerialNo, in_acct, Integer.parseInt(num)*rebate);
//				if(ss==0){
//					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵ�ɹ�,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//					rs=new HashMap<String, String>();
//					rs.put("state",ss+"".trim());
//					rs.put("orderid", "GuangXing09024");
//					rs.put("InterfaceType","1");//����
//				}else if(ss==-1){
//					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵʧ��,���ù����ӿڳ�ֵ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//					//ʧ�ܣ����ù���
//					rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
//					rs.put("InterfaceType","2");//����
//					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵʧ��,���ù����ӿڳ�ֵ,��ֵ���:"+rs.get("state")+",WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//					dbService.update("UPDATE wht_orderform_"+Tools.getNow4().substring(2,6)+" SET buyid='10' WHERE tradeserial='"+transaction_id+"'");
//				}else{
//					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵ��ʱ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//					//��ʱ
//					rs=new HashMap<String, String>();
//					rs.put("state",ss+"".trim());
//					rs.put("orderid", "GuangXing09024");
//					rs.put("InterfaceType","1");//����
//				}
//			}else {//����
//				Log.info("��ͨ���������Q�ҳ�ֵ,������ֵ,WhQb.isUse=false,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//				//����
//				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
//				rs.put("InterfaceType","2");//����
//				Log.info("��ͨ���������Q�ҳ�ֵ,������ֵ,��ֵ���:"+rs.get("state")+",WhQb.isUse=false,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
//			}
//			
//			String state=rs.get("state");
//			
//			String msg = "";
//			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
//			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
//			if (state.equals("0")) {
//				msg = "���׳ɹ�";
//				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
//						+ "#" + Integer.parseInt(num) * rebate + "#"
//						+ rs.get("orderid"), transaction_id, user_no,rs.get("InterfaceType"));
//			} else if (state.equals("-1")) {
//				msg = "���� ʧ��";
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
//				msg = "���״���������ϵ�ͷ�";
//				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
//			}
//			request.setAttribute("mess", msg);
//			return new ActionForward("/wlttencent/qbpay.jsp");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			request.setAttribute("mess", "ϵͳ�쳣����ϵ�ͷ�");
//			return new ActionForward("/wlttencent/qbpay.jsp");
//		}
//	}
	
	/**
	 * �жϷ��ؽ����Action
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
			request.setAttribute("mess", "���ȵ�¼");
			return new ActionForward("/rights/wltlogin.jsp");
		}
		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
				"userSession");
		if (!userForm.getRoleType().equals("3")) {
			request.setAttribute("mess", "��ǰ�û����Ͳ�֧�ֽ���");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		if (!Tools.validateTime()) {
			request.setAttribute("mess", "23:50��00:10����������");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
		
		BiProd bp = new BiProd();
		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
		{
			request.setAttribute("mess", "������������,���Ӷ��,����ϵ����Ա!");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
				
		String in_acct = request.getParameter("in_acct");
		if ("0".equals(num) || null == num || null == in_acct || num == ""
				|| in_acct == "") {
			request.setAttribute("mess", "����������������");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		if (num.indexOf("-") != -1) {
			request.setAttribute("mess", "���׸������Ϸ�!");
			return new ActionForward("/business/zshqbpay.jsp");
		}
		if (Integer.parseInt(num) > 200) {
			request.setAttribute("mess", "�������200��Q��");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
		String user_no = userForm.getUserno();
		String sql0 = "select fundacct from wht_facct where user_no='"
				+ user_no + "'";
		DBService dbService = null;
		// ��ǰʱ�� yyyyMMddHHmmss
		TenpayXmlPath tenpayxmlpath = new TenpayXmlPath();
		String currTime = TenpayUtil.getCurrTime();
		String transaction_id = TenpayUtil.getQBChars(currTime);
		String account = "";
		
		try {
			dbService = new DBService();
			if (dbService.hasData("select tradeserial from wht_orderform_"
					+ currTime.substring(2, 6) + " where tradeserial='"
					+ transaction_id + "'")) {
				request.setAttribute("mess", "�ظ�����");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String facct = dbService.getString(sql0);
			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
					+ facct + "01'";
			Float money = dbService.getFloat(sql);
			int rebate = 1000;// qq�ҵ���
			if (money - Integer.parseInt(num) * 1000 <= 0) {
				if (null != dbService) {
					dbService.close();
				}
				request.setAttribute("mess", "�˻�����");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
					: "0";
			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
					user_no, Integer.parseInt(userForm.getParent_id()));
			if (null == employFee) {
				request.setAttribute("mess", "Ӷ��Ȳ���������ϵ�ͷ�");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}

			// ����Ӷ��
			int facctfee = Integer.parseInt(num) * rebate;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// ��Ӷ���
				double b = FloatArith.mul(facctfee, a);// ��Ӷ��

				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// �����Ӧ��Ӷ��

				facctfee = (int) FloatArith.sub(facctfee, c);// Ӧ�۽��
				parentfee = (int) FloatArith.sub(b, c);// �ϼ��ڵ�Ӧ��Ӷ��
			}
			//����ָ��������ˮ��
			String GuangXinSerialNo=Tools.getGuangXinSerialNo();
			String interfaceTypes="";
			boolean bool=WhQb.IsMultiChannel();
			if(bool){//�ж����Ǹ��ӿ�
				interfaceTypes="16";//����
			}else{
				interfaceTypes="10";//����
			}
			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
					facctfee, facct + "01", currTime, currTime, 4, GuangXinSerialNo,
					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
					user_no, userForm.getUsername(), 3, 381, null };// 20
			String acct1 = Tools.getAccountSerial(currTime, user_no);
			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
					Integer.parseInt(num) * rebate, facctfee, 6, "����Q��", 0,
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
						currTime, parentfee, parentfee, 15, "�¼����׷�Ӷ", 0,
						currTime, moneyq + parentfee, transaction_id,
						parentFacct + "02", 2 };
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
								flag, facct, parentFacct, currTime, facctfee,
								parentfee);
			}
			if (rsFlag == false) {
				request.setAttribute("mess", "����ʧ��");
				return new ActionForward("/wlttencent/qbpay.jsp");
			}
			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
			String ip = Tools.getIpAddr(request);
			Map<String, String> rs=null;
			//���Žӿڣ����ò�
			if(bool && 
					(Integer.parseInt(num)==1 || 
					Integer.parseInt(num)==5 || 
					Integer.parseInt(num)==10 || 
					Integer.parseInt(num)==20 || 
					Integer.parseInt(num)==30)){
				Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
				//����
				int ss=WhQb.useQB(GuangXinSerialNo, GuangXinSerialNo, in_acct, Integer.parseInt(num)*rebate);
				if(ss==0){
					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵ�ɹ�,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
					rs=new HashMap<String, String>();
					rs.put("state",ss+"".trim());
					rs.put("orderid", "GuangXing09024");
					rs.put("InterfaceType","1");//����
				}else if(ss==-1){
					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵʧ��,���ù����ӿڳ�ֵ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
					//ʧ�ܣ����ù���
					dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
					rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
					rs.put("InterfaceType","2");//����
					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵʧ��,���ù����ӿڳ�ֵ,��ֵ���:"+rs.get("state")+",WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
				}else{
					Log.info("��ͨ���������Q�ҳ�ֵ,���ų�ֵ,��ֵ��ʱ,WhQb.isUse=true,,,transaction_id��"+transaction_id+",writeoff:"+GuangXinSerialNo);
					//��ʱ
					rs=new HashMap<String, String>();
					rs.put("state",ss+"".trim());
					rs.put("orderid", "GuangXing09024");
					rs.put("InterfaceType","1");//����
				}
			}else if(bool && 
					Integer.parseInt(num)!=1 &&
					Integer.parseInt(num)!=5 &&
					Integer.parseInt(num)!=10 &&
					Integer.parseInt(num)!=20 &&
					Integer.parseInt(num)!=30){
				//��
				//�� ���
				int[] strs=com.wlt.webm.util.Tools.decomposeQB1(Integer.parseInt(num));
				String tableName="wht_qbbreakrecord_"+Tools.getNow3().substring(2,6);
				
				int oneState=-1;
				int con=0;
				for(int i=0;i<2;i++){
					if(strs[i]==1 || strs[i]==5 || strs[i]==10 || strs[i]==20 || strs[i]==30){
						con++;
						//������transaction_id  ��Ӧ������wht_qbbreakrecord_1503 �����ֶ�oldorderid  
						String order_One=com.wlt.webm.util.Tools.getGuangXinSerialNo();
						//��Ϣ��ˮ��
						String transaction_id_One=TenpayUtil.getQBChars(currTime);
						Object[] qbbreakrecordOne={null,order_One,in_acct,16,"0005",strs[i] * rebate,
								strs[i] * rebate,currTime,Tools.getNow3(),4,order_One+"#"+strs[i] * rebate,userForm.getUserno(),transaction_id,"","","",""};
						dbService.logData(17, qbbreakrecordOne, tableName);
						oneState=WhQb.useQB(transaction_id_One, order_One, in_acct, Integer.parseInt(strs[i]+"") * rebate);
						if(i==0){//��һ�ʵ� ����
							if(oneState==0){//�𵥺�ĵ�һ���߹��ţ��ɹ�
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=0 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								continue;
							}else if(oneState==-1){//ʧ��
								dbService.update("delete from wht_qbbreakrecord_" + currTime.substring(2, 6)+
										"  where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								break;
							}else{//δ֪״̬,�쳣��������,ת�˹�
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=6 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								num=strs[1]+"";
								break;
							}
						}else{//�ڶ��ʵ� ���� ��һ�ʹ��ų�ֵ�ɹ���
							if(oneState==0){//�𵥺�����ʶ��ߣ����ţ��������ʶ��ɹ�,����
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=0 where oldorderid='"+transaction_id+"'");
								dbService.update("update wht_orderform_" + currTime.substring(2, 6)+
										" set state=0,writecheck='"+transaction_id+ "#" + Integer.parseInt(num) * rebate + "#"+ 
										transaction_id+"' where tradeserial='"+transaction_id+"' and userno='"+userForm.getUserno()+"'");
								request.setAttribute("mess","��ֵ�ɹ�");
								return new ActionForward("/wlttencent/qbpay.jsp");
							}else if(oneState==-1){// �ڶ��ʳ�ֵʧ�ܣ��ڶ���Ǯ �߹���
								dbService.update("delete from  wht_qbbreakrecord_" + currTime.substring(2, 6)+
										"  where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								num=strs[1]+"";
								break;
							}else{//δ֪״̬,�쳣��������,ת�˹�
								dbService.update("update wht_qbbreakrecord_" + currTime.substring(2, 6)+
										" set chgtime='"+Tools.getNow3()+"',state=6 where oldorderid='"+transaction_id+"' and tradeserial='"+
										order_One+"' and userno='"+userForm.getUserno()+"'");
								request.setAttribute("mess","��ֵ�ɹ�");
								return new ActionForward("/wlttencent/qbpay.jsp");
							}
						}
					}else{//�ڶ��ʵ� ����
						num=strs[1]+"";
						break;
					}
				}
				Object[] qbbreakrecordOne={null,transaction_id,in_acct,10,"0005",Integer.parseInt(num) * rebate,
						Integer.parseInt(num) * rebate,currTime,"",4,transaction_id+"#"+Integer.parseInt(num)* rebate,userForm.getUserno(),transaction_id,"","","",""};
				dbService.logData(17, qbbreakrecordOne, tableName);
				dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
				rs.put("InterfaceType","2");//����
			}else {//����
				dbService.update("update "+orderTable+" set buyid='10' where tradeserial='"+transaction_id+"'");
				rs=QueryGuanMingState(in_acct,num,transaction_id,ip);
				rs.put("InterfaceType","2");//����
			}
			
			
			
			String state=rs.get("state");
			String msg = "";
			
			if (state.equals("0")) {
				msg = "���׳ɹ�";
				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
						+ "#" + Integer.parseInt(num) * rebate + "#"
						+ rs.get("orderid"), transaction_id, user_no,rs.get("InterfaceType"));
			} else if (state.equals("-1")) {
				msg = "���� ʧ��";
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
				msg = "���״���������ϵ�ͷ�";
				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
			}
			request.setAttribute("mess", msg);
			return new ActionForward("/wlttencent/qbpay.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("mess", "ϵͳ�쳣����ϵ�ͷ�");
			return new ActionForward("/wlttencent/qbpay.jsp");
		}
	}

	/**
	 * ����Q��
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
	 * ��ȡ��Ʒid��AJAX��
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
				request.setAttribute("mess", "��¼��ʱ,�����µ�¼");
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
				sBuffer.append("[{\"mon\":\"" + "ϵͳ��æ....�ɹ��۸����޷���ʾ" + "\"}]");
			}

			// ����Ӷ��
			int facctfee = Integer.parseInt(num) * 1000;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// ��Ӷ���
				double b = FloatArith.mul(facctfee, a);// ��Ӷ��
				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// �����Ӧ��Ӷ��
				facctfee = (int) FloatArith.sub(facctfee, c);// Ӧ�۽��
			}

			sBuffer.append("[{\"mon\":\""
					+ "�ɹ��۸�:"
					+ FloatArith.div(facctfee, 1000)
					+ "Ԫ , ����:"
					+ FloatArith.div(FloatArith.sub(
							Integer.parseInt(num) * 1000, facctfee), 1000)
					+ "Ԫ\"}]");
			pWriter.print(sBuffer.toString());
			pWriter.flush();
			pWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			sBuffer.delete(0, sBuffer.length());
			sBuffer.append("[{\"mon\":\"" + "ϵͳ��æ..�ɹ��۸����޷���ʾ" + "\"}]");
		} finally {
			if (dbService != null) {
				dbService.close();
			}
		}
		return null;
	}

	/**
	 * ��ѯ�Ƿ����ĳ����¼ �����ĳ����¼
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
					Log.info("�յ��ظ���Ϣ��������");
				} else {
					flag = false;
					Log.info("û�鵽��¼...");
				}
			} else {
				int i = 0;
				i = dbService.update(sql, params);
				if (i != 0)
					flag = true;
			}
		} catch (SQLException e) {
			Log.info("QB�쳣" + e.toString());
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
			message = "��ֵ�ɹ�";
			break;
		case 1:
			message = "����ǩ������";
			break;
		case 2:
			message = "�����ظ��ύ";
			break;
		case 3:
			message = "�û��ʺŲ�����";
			break;
		case 4:
			message = "ϵͳ����";
			break;
		case 5:
			message = "IP����";
			break;
		case 6:
			message = "�û�key����";
			break;
		case 7:
			message = "��������";
			break;
		case 8:
			message = "��治��";
			break;
		case 9:
			message = "�û�״̬�쳣";
			break;
		case 10:
			message = "����������";
			break;
		case 101:
			message = "�˹�����ʱ������";
			break;
		case 102:
			message = "����ҵ��Ȩ�޲���";
			break;
		case 103:
			message = "ϵͳά����";
			break;
		default:
			message = "δ֪����";
			break;
		}
		return message;
	}
	
	/**
	 * ��Ѷֱͨ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward qbTenpayTransfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (null == request.getSession().getAttribute("userSession")) {
			request.setAttribute("mess", "���ȵ�¼");
			return new ActionForward("/rights/wltlogin.jsp");
		}
		SysUserForm userForm = (SysUserForm) request.getSession().getAttribute(
				"userSession");
		if (!userForm.getRoleType().equals("3")) {
			request.setAttribute("mess", "��ǰ�û����Ͳ�֧�ֽ���");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		if (!Tools.validateTime()) {
			request.setAttribute("mess", "23:50��00:10����������");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		String num = (null == request.getParameter("qbnum")) ? request.getParameter("order_price") : request.getParameter("qbnum");
		
		BiProd bp = new BiProd();
		String[] strss=bp.getPlaceMoney("1", userForm.getUserno());
		if((Float.parseFloat(strss[0])-(Float.parseFloat(strss[1])+Float.parseFloat(num)))<0)
		{
			request.setAttribute("mess", "������������,���Ӷ��,����ϵ����Ա!");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
				
		String in_acct = request.getParameter("in_acct");
		if ("0".equals(num) || null == num || null == in_acct || num == ""
				|| in_acct == "") {
			request.setAttribute("mess", "����������������");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		if (num.indexOf("-") != -1) {
			request.setAttribute("mess", "���׸������Ϸ�!");
			return new ActionForward("/business/tenxunqbpay.jsp");
		}
		if (Integer.parseInt(num) > 200) {
			request.setAttribute("mess", "�������200��Q��");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
		String user_no = userForm.getUserno();
		String sql0 = "select fundacct from wht_facct where user_no='"
				+ user_no + "'";
		DBService dbService = null;
		// ��ǰʱ�� yyyyMMddHHmmss
		TenpayXmlPath tenpayxmlpath = new TenpayXmlPath();
		String currTime = TenpayUtil.getCurrTime();
		String transaction_id = TenpayUtil.getQBChars(currTime);
		String account = "";
		
		try {
			dbService = new DBService();
			if (dbService.hasData("select tradeserial from wht_orderform_"
					+ currTime.substring(2, 6) + " where tradeserial='"
					+ transaction_id + "'")) {
				request.setAttribute("mess", "�ظ�����");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String facct = dbService.getString(sql0);
			String sql = "select accountleft from  wht_childfacct where  childfacct ='"
					+ facct + "01'";
			Float money = dbService.getFloat(sql);
			int rebate = 1000;// qq�ҵ���
			if (money - Integer.parseInt(num) * 1000 <= 0) {
				if (null != dbService) {
					dbService.close();
				}
				request.setAttribute("mess", "�˻�����");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String flag = AcctBillBean.getStatus(userForm.getParent_id()) == true ? "1"
					: "0";
			String[] employFee = bp.getZZEmploy(dbService, "0005", flag,
					user_no, Integer.parseInt(userForm.getParent_id()));
			if (null == employFee) {
				request.setAttribute("mess", "Ӷ��Ȳ���������ϵ�ͷ�");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}

			// ����Ӷ��
			int facctfee = Integer.parseInt(num) * rebate;
			int parentfee = 0;
			if ("1".equals(flag)) {
				facctfee = (int) FloatArith.mul(facctfee, 1 - FloatArith.div(
						Double.valueOf(employFee[0]), 100));
			} else {
				double a = FloatArith.div(Double.valueOf(employFee[0]), 100);// ��Ӷ���
				double b = FloatArith.mul(facctfee, a);// ��Ӷ��

				double c = FloatArith.mul(b, FloatArith.div(Double
						.valueOf(employFee[1]), 100));// �����Ӧ��Ӷ��

				facctfee = (int) FloatArith.sub(facctfee, c);// Ӧ�۽��
				parentfee = (int) FloatArith.sub(b, c);// �ϼ��ڵ�Ӧ��Ӷ��
			}
			String interfaceTypes="89";
			Object[] orderObj = { null, userForm.getSa_zone(), transaction_id,
					in_acct, interfaceTypes, "0005", Integer.parseInt(num) * rebate,
					facctfee, facct + "01", currTime, currTime, 4, "",
					transaction_id + "#" + Integer.parseInt(num) * rebate, "0",
					user_no, userForm.getUsername(), 3, 381, null };// 20
			String acct1 = Tools.getAccountSerial(currTime, user_no);
			Object[] acctObj = { null, facct + "01", acct1, in_acct, currTime,
					Integer.parseInt(num) * rebate, facctfee, 6, "����Q��", 0,
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
						currTime, parentfee, parentfee, 15, "�¼����׷�Ӷ", 0,
						currTime, moneyq + parentfee, transaction_id,
						parentFacct + "02", 2 };
				rsFlag = bp.innerDeal(dbService, orderObj, acctObj, acctObj1,
								flag, facct, parentFacct, currTime, facctfee,
								parentfee);
			}
			if (rsFlag == false) {
				request.setAttribute("mess", "����ʧ��");
				return new ActionForward("/wlttencent/tenxunqbpay.jsp");
			}
			String orderTable = "wht_orderform_" + currTime.substring(2, 6);
			String acctTable = "wht_acctbill_" + currTime.substring(2, 6);
			String ip = Tools.getIpAddr(request);
			String state=QbZhiTongChe.fill(currTime.substring(8),transaction_id,in_acct,
					 num,System.currentTimeMillis()/1000);//������ѶQ�ҳ�ֵ;
			if("error".equals(state)){
				state="88";
			}else{
			state=state.substring(state.indexOf("ret=")+4, state.indexOf("&mch_id"));
			}
			String msg = "";
			if (state.equals("0")) {
				msg = "���׳ɹ�";
				bp.innerQQSuccessDeal(dbService, orderTable, transaction_id
						+ "#" + Integer.parseInt(num) * rebate + "#"
						+ transaction_id, transaction_id, user_no,interfaceTypes);
			} else if (state.equals("1")||state.equals("2")||state.equals("3")||
					state.equals("5")||state.equals("6")||state.equals("7")
					||state.equals("8")||state.equals("9")||state.equals("101")
					||state.equals("102")||state.equals("103")) {
				msg = "���� ʧ��";
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
				msg = "���״���������ϵ�ͷ�";
				bp.innerTimeOutDeal(dbService, orderTable, transaction_id,user_no);
			}
			request.setAttribute("mess", msg);
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("mess", "ϵͳ�쳣����ϵ�ͷ�");
			return new ActionForward("/wlttencent/tenxunqbpay.jsp");
		}
	}

}