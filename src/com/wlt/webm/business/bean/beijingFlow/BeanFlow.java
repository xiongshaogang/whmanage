package com.wlt.webm.business.bean.beijingFlow;

import java.util.HashMap;
import java.util.Map;

import whmessgae.TradeMsg;

import com.commsoft.epay.util.logging.Log;
import com.wlt.webm.business.bean.BiProd;
import com.wlt.webm.util.Tools;

/**
 * @author 施建桥
 */
public class BeanFlow {
	
	/**
	 * 北京联通流量充值
	 * 
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 *            分
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups   组
	 * @param interfaceID
	 * @param flag
	 * @param name          
	 * @return Map<String,String>
	 */
	public static HashMap<String,String> fillFlow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", "2");
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID);
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10005", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 思空接口
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID 接口id 
	 * @param flag
	 * @param name  50  50M
	 * @param phoneType 0电信  1移动  2联通
	 * @return map
	 */
	public static HashMap<String,String> sikong_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name); 
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10006", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 北京移动流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @return map
	 */
	public static HashMap<String,String> WebServices_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", "1");
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID);
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10007", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 大汉三通 流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType
	 * @return
	 */
	public static HashMap<String,String> Dhst_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice  + "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name); 
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10008", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 乐流流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType
	 * @return
	 */
	public static HashMap<String,String> LeLiu_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10009", orderNo);
		Log.info("乐流流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 连连流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType
	 * @return
	 */
	public static HashMap<String,String> Lianlian_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice+ "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name); 
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10010", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	
	/**
	 * 正邦移动流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @return map
	 */
	public static HashMap<String,String> zb_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", "1");
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID);
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10011", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 自由移动流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @return map
	 */
	public static HashMap<String,String> zy_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", "1");
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID);
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10012", orderNo);
		Log.info("流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 广东流量贩
	 * 
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 *            分
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups   组
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType            
	 * @return Map<String,String>
	 */
	public static HashMap<String,String> LiuLiangFan(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID);
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10013", orderNo);
		Log.info("昊峰流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	/**
	 * 鼎信流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType
	 * @return
	 */
	public static HashMap<String,String> DingXin_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10014", orderNo);
		Log.info("鼎信流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
	
	/**
	 * 智信流量充值
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups
	 * @param interfaceID
	 * @param flag
	 * @param name
	 * @param phoneType
	 * @return
	 */
	public static HashMap<String,String> ZhiXin_flow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name,String phoneType) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// 是否直营标示 0标示非直营 1标示直营
		content.put("userPid", userPid);
		content.put("phonePid", phonePid);
		content.put("areaCode", areaCode);
		content.put("phoneType", phoneType);// 0电信  1移动 2联通
		content.put("tradeFee", realPrice + "");
		content.put("interfaceID", interfaceID); //接口  
		content.put("login", login);
		content.put("time", Tools.getNow3());
		content.put("termType", "3");
		content.put("productId", productId);
		content.put("realPrice", realPrice + "");
		content.put("aCTIVITYID", aCTIVITYID);
		content.put("groups", groups);
		content.put("name", name);
		reveMess.setContent(content);
		BiProd bp = new BiProd();
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10015", orderNo);
		Log.info("智信流量充值:"+orderNo+"  N:"+n);
		String str="";
		if (n == -5) {
			str = "{\"desc\":\"余额不足\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str = "{\"desc\":\"订购成功\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str = "{\"desc\":\"订购失败\",\"status\":\"2\"}";
		}else if (n == 8) {
			str = "{\"desc\":\"虚假面额\",\"status\":\"9103\"}";
		}else if(n==6){
			str = "{\"desc\":\"流水号重复\",\"status\":\"1007\"}";
		}else if(n==3){
			str = "{\"desc\":\"系统异常\",\"status\":\"8\"}";
		}else{
			str = "{\"desc\":\"订购中\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}
}
