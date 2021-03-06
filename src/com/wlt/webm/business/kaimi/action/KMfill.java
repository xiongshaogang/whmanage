package com.wlt.webm.business.kaimi.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wlt.webm.business.InnerProcessDeal;
import com.wlt.webm.business.MobileChargeService;
import com.wlt.webm.business.form.SysPhoneAreaForm;
import com.wlt.webm.business.form.SysUserInterfaceForm;
import com.wlt.webm.business.kaimi.bean.KmInterface;
import com.wlt.webm.db.DBService;
import com.wlt.webm.rights.form.SysUserForm;
import com.wlt.webm.scpcommon.Constant;
import com.wlt.webm.tool.Tools;

/**
 * 卡密
 * @author lenovo
 *
 */
public class KMfill extends DispatchAction{


	/**
	 * 卡密充值
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward kmFill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
		String payNo=request.getParameter("tradeObject").replaceAll(" ", "").trim();
        String paraMoney = request.getParameter("money");
        String pArea =new String(request.getParameter("pArea").getBytes("ISO-8859-1"), "gb2312");
        System.out.println("卡密充值信息  payNo:"+payNo +"  paraMoney:"+paraMoney+"  pArea:"+pArea);
        String mon=Tools.yuanToFen(paraMoney);
        int payFee=Integer.parseInt(mon);
        String nowTime=Tools.getNow();
        String sepNo=payNo.substring(4)+KmInterface.buildRandom(2)+KmInterface.buildRandom(4);
        SysUserForm user = (SysUserForm) request.getSession().getAttribute("userSession");
        MobileChargeService service = new MobileChargeService();
        //用户区域
        String userArea = service.getUserArea(user.getUser_id());
        //号码类型和归属id是否存在
        SysPhoneAreaForm spa = service.getPhoneInfo(payNo.substring(0,7));
        spa.setProvince_code(new Integer(35));
        spa.setPhone_type(3);
        //用户接口id是否存在
        SysUserInterfaceForm sui = new SysUserInterfaceForm();
        sui.setProvince_code(spa.getProvince_code());
        sui.setCharge_type(spa.getPhone_type());
        sui.setUser_id(Integer.parseInt(user.getUser_id()));
        String inId ="0003";
        //用户的佣金组是否存在
        String roleType = service.getUserRoleType(user.getUser_role());
        if(null == roleType || "".equals(roleType)){
        	request.setAttribute("mess", "未知用户类型");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        String sgId = service.getUserCommission(user.getUser_id());
        if(null == sgId || "".equals(sgId)){
        	request.setAttribute("mess", "无对应佣金组");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //佣金子账号和押金账号是否存在
        String fundAcct02 = service.getUserFundAcct("02", user.getUser_id());
        if(null == fundAcct02 || "".equals(fundAcct02)){
        	request.setAttribute("mess", "押金账号不存在");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        String fundAcct03 = service.getUserFundAcct("03", user.getUser_id());
        if(null == fundAcct03 || "".equals(fundAcct03)){
        	request.setAttribute("mess", "佣金子账号不存在");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //佣金子账号和押金账号状态是否正常
        String state = service.getFundAcctState(fundAcct02);
        if(!"1".equals(state)){
        	request.setAttribute("mess", "押金账号不可用");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        String state1 = service.getFundAcctState(fundAcct03);
        if(!"1".equals(state1)){
        	request.setAttribute("mess", "佣金子账号不可用");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //押金子账号的金额是否大于扣费金额
        int acctLeft = Integer.parseInt(service.getFundAcctLeft(fundAcct02));
        if(0 == acctLeft || acctLeft - Integer.parseInt(mon) < 0){
        	request.setAttribute("mess", "押金账号余额不足");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //佣金明细是否存在
        String scId = service.getCommMx(sgId, String.valueOf(spa.getPhone_type()),Integer.parseInt(userArea),spa.getProvince_code(),mon);
        if(null == scId || "".equals(scId)){
        	request.setAttribute("mess", "佣金明细不存在");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //记录订单
        int isSuccess = 0;
        try {
        	isSuccess = InnerProcessDeal.logOrder(String.valueOf(spa.getProvince_code()), sepNo, payNo, "0003", Constant.TELCOM_FILL, Integer.parseInt(mon), fundAcct02.substring(0, fundAcct02.length()-2),
        			nowTime, nowTime, "", "", "0", user.getUser_id(), acctLeft - Integer.parseInt(mon), user.getUsername(), String.valueOf(spa.getPhone_type()));
		} catch (Exception e) {
			request.setAttribute("mess", "生成订单失败");
        	return new ActionForward("/business/kamibusiness.jsp");
		}
		if(0 != isSuccess){
			request.setAttribute("mess", "生成订单失败");
        	return new ActionForward("/business/kamibusiness.jsp");
		}
		Map dealMap = new HashMap();
        if(0 == isSuccess){
        	try {
        		//内部 扣款、返佣、更改订单状态 
        		dealMap = InnerProcessDeal.indeal(fundAcct02.substring(0, fundAcct02.length()-2), payNo, nowTime, Integer.parseInt(mon), String.valueOf(spa.getProvince_code()), service.getTradeType(inId), "", 
        				spa.getPhone_type(), Integer.parseInt(userArea), Integer.parseInt(user.getUser_id()), Integer.parseInt(user.getUser_role()), spa.getProvince_code(), sepNo, sgId);
			} catch (Exception e) {
				request.setAttribute("mess", "数据处理错误");
	        	return new ActionForward("/business/kamibusiness.jsp");
			}
        }
        int dealFlag = (Integer)dealMap.get("flag");
        if(1 == dealFlag){
        	request.setAttribute("mess", "数据处理错误");
        	return new ActionForward("/business/kamibusiness.jsp");
        }
        //调用充值接口
        int status = 0;
        try {
            if(KmInterface.kmFill(sepNo, payNo, paraMoney, "1", "电信", "", "")==0){
            	status = 0;
            }else{
            int count=0;
            while(count<=360){
            	Thread.sleep(1000);
            	count++;
            	status=KmInterface.kmquery(sepNo);
            	if(status==2||status==4||status==6||status==7){
            		break;
            	}
            }
            }
        } catch (Exception e) {
			//更新订单状态
			service.updOrderState("wlt_orderForm_"+nowTime.substring(2, 6), sepNo);
			//返款，更新佣金、账户明细--
			if(null != dealMap){
				//自身返佣金额
				String empFee = String.valueOf(dealMap.get("empFee-self"));
				//账户明细表名
				String tableName = "wlt_acctbill_"+nowTime.substring(2, 6);
				//上一级账户
				String empAcctLevlOne = (String)dealMap.get("acct-levelone");
				String empFeeLevlOne = (String)dealMap.get("empfee-levelone");
				//上上一级账户
				String empAcctLevlTwo = (String)dealMap.get("acct-leveltwo");
				String empFeeLevlTwo = (String)dealMap.get("empfee-leveltwo");
				//自身返款,退回自身佣金并更新账户明细
				service.updAccount(payFee, fundAcct02.substring(0, fundAcct02.length()-2), empFee, tableName,sepNo);
				//上级返回佣金并更新账户明细
				if(null != empAcctLevlOne && !"".equals(empAcctLevlOne)){
					service.updEmpAccount(empAcctLevlOne, empFeeLevlOne, tableName,sepNo);
				}
				//上上级返回佣金并更新账户明细
				if(null != empAcctLevlTwo && !"".equals(empAcctLevlTwo)){
					service.updEmpAccount(empAcctLevlTwo, empFeeLevlTwo, tableName,sepNo);
				}
			}
			request.setAttribute("mess", "充值接口异常");
	        return new ActionForward("/business/kamibusiness.jsp");
			
		}
		if(4 == status){//充值成功,修改订单状态成功
			service.updOrderStatus(sepNo, "0", "wlt_orderForm_"+nowTime.substring(2, 6));
		}else if(status == 6||status == 7||status == 2||status == 0){//充值失败,修改订单状态失败，退钱，退佣金
			//更新订单状态
			service.updOrderState("wlt_orderForm_"+nowTime.substring(2, 6), sepNo);
			//返款，更新佣金、账户明细
			if(null != dealMap){
				//自身返佣金额
				String empFee = String.valueOf(dealMap.get("empFee-self"));
				//账户明细表名
				String tableName = "wlt_acctbill_"+nowTime.substring(2, 6);
				//上一级账户
				String empAcctLevlOne = (String)dealMap.get("acct-levelone");
				String empFeeLevlOne = (String)dealMap.get("empfee-levelone");
				//上上一级账户
				String empAcctLevlTwo = (String)dealMap.get("acct-leveltwo");
				String empFeeLevlTwo = (String)dealMap.get("empfee-leveltwo");
				//自身返款,退回自身佣金并更新账户明细
				service.updAccount(payFee, fundAcct02.substring(0, fundAcct02.length()-2), empFee, tableName,sepNo);
				//上级返回佣金并更新账户明细
				if(null != empAcctLevlOne && !"".equals(empAcctLevlOne)){
					service.updEmpAccount(empAcctLevlOne, empFeeLevlOne, tableName,sepNo);
				}
				//上上级返回佣金并更新账户明细
				if(null != empAcctLevlTwo && !"".equals(empAcctLevlTwo)){
					service.updEmpAccount(empAcctLevlTwo, empFeeLevlTwo, tableName,sepNo);
				}
			}
			request.setAttribute("mess", "充值失败");
	        return new ActionForward("/business/kamibusiness.jsp");
		}else if(9 == status){//无返回结果,修改订单状态异常
			service.updOrderStatus(sepNo, "6", "wlt_orderForm_"+nowTime.substring(2, 6));
			request.setAttribute("mess", "充值异常");
	        return new ActionForward("/business/kamibusiness.jsp");
		}else if(8 == status){//无响应,修改订单状态无响应
			service.updOrderStatus(sepNo, "2", "wlt_orderForm_"+nowTime.substring(2, 6));
			request.setAttribute("mess", "充值无响应");
	        return new ActionForward("/business/kamibusiness.jsp");
		}
        request.setAttribute("mess", "充值成功");
        return new ActionForward("/business/kamibusiness.jsp");
	}

	 /**
	 * 获取用户接口
	 * @return 用户接口
	 * @throws Exception
	 */
	public String getInterfaceId(SysUserInterfaceForm sui) throws Exception {
        DBService db = new DBService();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select in_id")
                    .append(" from sys_user_interface ")
                    .append(" where user_id="+sui.getUser_id()+" and charge_type = "+sui.getCharge_type()+"  and province_code = "+sui.getProvince_code());
            System.out.println("slq:"+sql);
            return db.getString(sql.toString());
        } catch (Exception ex) {
            throw ex;
        } finally {
            db.close();
        }
    }
	

}
