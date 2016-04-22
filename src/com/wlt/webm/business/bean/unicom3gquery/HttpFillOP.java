package com.wlt.webm.business.bean.unicom3gquery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sf.json.test.JSONAssert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.text.StrSubstitutor;

import whmessgae.TradeMsg;

import bsh.This;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.wo_key.WoMd5;
import com.commsoft.epay.util.logging.Log;
import com.wlt.webm.business.bean.BiProd;
import com.wlt.webm.db.DBService;
import com.wlt.webm.ten.service.MD5Util;
import com.wlt.webm.tool.FloatArith;
import com.wlt.webm.util.Tools;

/**
 * ��ͨ������ֵ
 * 
 * @author 1989
 * 
 */
public class HttpFillOP {
	public static final String tencent_code="1000";//��Ѷ
	public static final String jd_code="1001"; //����
	
    public static String FLOw1="21";//ʡ��˾���� ��Ҫͬ����Ʒ
    public static String FLOw2="23";//������ͨ
    public static String FLOW3="25";//˼���ƶ�����
    public static String FLOW4="26";//˼����ͨ����
    public static String FLOW5="27";//˼�յ�������
    public static String FLOW6="29";//�����ƶ�����
    
    public static String FLOW7="31";//����ͨ����
    public static String FLOW8="32";//����ͨ�ƶ�
    public static String FLOW9="33";//����ͨ��ͨ
    
    public static String FLOW10="34";//��������
    public static String FLOW11="35";//�����ƶ�
    public static String FLOW12="36";//������ͨ
    
    public static String FLOW13="37";//��������
    public static String FLOW14="38";//�����ƶ�
    public static String FLOW15="39";//������ͨ
    
    public static String flow16="41";//��������
    public static String flow17="42";//�����ƶ�����
    public static String flow18="43";//껷� ��ͨ
    
    public static String FLOW19="44";//���ŵ���
    public static String FLOW20="45";//�����ƶ�
    public static String FLOW21="46";//������ͨ
    public static String FLOW22="47";//������ͨ
    public static String FLOW23="48";//���ŵ���
    
    public static String FLOW24="49";//껷����
    public static String FLOW25="50";//껷��ƶ�
    
	public static String ACTIVITYID = "1278";//"2808";// ȫ����ƷĿ¼ ��Ӧ 21 ʡ��˾����
	public static String ZDY="1234";//��ƷĿ¼�����ⶨ��
//	public static String SHANGHAI = "2849";// �Ϻ�
//	public static String HEILONGJIAN = "2848";// ������
	
	public static String BEIFENUNICOM="3366";//������ͨ��ƷĿ¼

	//��ʽ
	public static String SIGNKEY ="womail201409231504"; //"test123";// ��Կ
	
	public static String QUERYURL = "http://e.wo.com.cn/ewo/server/product/getBackProList.jsonp?";// ��Ʒ����rl
	public static String FILLURL = "http://e.wo.com.cn/ewo/server/product/backOrderInterfaceNew.jsonp?";// ��Ʒ����
	public static String RESULTURL = "http://e.wo.com.cn/ewo/server/product/queryBackOrderResultNewSec.jsonp?";// ��Ʒ���������ѯ
	public static String CHECKURL="http://e.wo.com.cn/ewo/server/product/checkBackProduct.jsonp?";//��Ʒ��Ȩ�ӿ�

	static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	static HttpClient client = new HttpClient(connectionManager);
	static{
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60*1000);//����ʱ�� 60�룬��λ�Ǻ���
		client.getHttpConnectionManager().getParams().setSoTimeout(60*1000);//��ȡʱ�� 20�룬��λ�Ǻ���
	}
	public static Map<String,String> phone_check=new HashMap<String, String>();//��Ȩ��ֵ��Ʒid��Ӧ
	public static Map<String, String> phoneAreas = new HashMap<String, String>();// ʡ�ݶ�Ӧ����  ������˾--������
	public static Map<String, String> phoneAreas1 = new HashMap<String, String>();// ʡ�ݶ�Ӧ����  ����--��������˾
	static {
		phoneAreas.put("71", "2");
		phoneAreas.put("19", "46");
		phoneAreas.put("51", "35");
		phoneAreas.put("11", "43");
		phoneAreas.put("13", "44");
		phoneAreas.put("18", "45");
		phoneAreas.put("10", "48");
		phoneAreas.put("91", "49");
		phoneAreas.put("90", "50");
		phoneAreas.put("97", "51");
		phoneAreas.put("31", "52");
		phoneAreas.put("34", "53");
		phoneAreas.put("36", "54");
		phoneAreas.put("30", "55");
		phoneAreas.put("38", "56");
		phoneAreas.put("75", "57");
		phoneAreas.put("17", "58");
		phoneAreas.put("76", "59");
		phoneAreas.put("74", "60");
		phoneAreas.put("59", "61");
		phoneAreas.put("50", "62");
		phoneAreas.put("83", "63");
		phoneAreas.put("81", "64");
		phoneAreas.put("85", "65");
		phoneAreas.put("86", "66");
		phoneAreas.put("79", "67");
		phoneAreas.put("84", "68");
		phoneAreas.put("87", "69");
		phoneAreas.put("70", "70");
		phoneAreas.put("88", "71");
		phoneAreas.put("89", "72");
		//ʡ�ݶ�Ӧ����  ����--��������˾
		phoneAreas1.put("2","71");
		phoneAreas1.put("46","19");
		phoneAreas1.put("35","51");
		phoneAreas1.put("43","11");
		phoneAreas1.put("44","13");
		phoneAreas1.put("45","18");
		phoneAreas1.put("48","10");
		phoneAreas1.put("49","91");
		phoneAreas1.put("50","90");
		phoneAreas1.put("51","97");
		phoneAreas1.put("52","31");
		phoneAreas1.put("53","34");
		phoneAreas1.put("54","36");
		phoneAreas1.put("55","30");
		phoneAreas1.put("56","38");
		phoneAreas1.put("57","75");
		phoneAreas1.put("58","17");
		phoneAreas1.put("59","76");
		phoneAreas1.put("60","74");
		phoneAreas1.put("61","59");
		phoneAreas1.put("62","50");
		phoneAreas1.put("63","83");
		phoneAreas1.put("64","81");
		phoneAreas1.put("65","85");
		phoneAreas1.put("66","86");
		phoneAreas1.put("67","79");
		phoneAreas1.put("68","84");
		phoneAreas1.put("69","87");
		phoneAreas1.put("70","70");
		phoneAreas1.put("71","88");
		phoneAreas1.put("72","89");
		
		//��ֵ��Ӧ��Ʒid
		phone_check.put("50","11_000501");
		phone_check.put("200","11_002001");
		phone_check.put("100","11_001000");
		phone_check.put("20","11_002000");
		phone_check.put("500","11_005000");
	}

	/**
	 * 
	 * @param url
	 *            �ʺŽ�β
	 * @param data
	 *            ����
	 * @param method
	 *            GET����POST
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String OP(String url, HashMap data, String method) {
		Set<String> set = data.keySet();
		for (String key : set) {
			url += (key + "=" + data.get(key) + "&");
		}
		url = url.substring(0, url.length() - 1);
		Log.info("��ͨ������������,�������:"+data.get("c_phone")+",,,url:"+url);
		HttpMethod obj = null;
		if ("GET".equals(method)) {
			obj = new GetMethod(url);
		} else {
			obj = new PostMethod(url);
		}
		try {
			int status = client.executeMethod(obj);
			if (200 == status) {
				String result = convertStreamToString(obj
						.getResponseBodyAsStream());
				Log.info("��ͨ��Ʒ�������:"+data.get("c_phone")+",,,��Ӧ���" + result);
				if (null == result) {
					Log.info("������ͨ����:"+data.get("c_phone")+",,,������Ӧ���ݴ���");
					return "{\"desc\":\"ʡ������æ\",\"status\":\"8\"}";
				} else {
					FillProductInfo info = JSON.parseObject(result.toLowerCase(),
							FillProductInfo.class);
					if (info!=null){
						return result;
					}
				}
			}
		} catch (Exception e) {
           Log.error("������ͨ����:"+data.get("c_phone")+",,,ϵͳ�쳣"+e.toString());
           return "{\"desc\":\"ʡ������æ\",\"status\":\"8\"}";
		} finally {
			 obj.releaseConnection();
		}
		Log.info("������ͨ����:"+data.get("c_phone")+",,,��Ӧ��ʱ,,����������");
		return "{\"desc\":\"ʡ������æ\",\"status\":\"8\"}";
	}

	/**
	 * ������ת���ַ���
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream is)
			throws IOException {
		StringBuilder sf = new StringBuilder();
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sf.append(line);
			}
		} finally {
			is.close();
			if (null != reader) {
				reader.close();
			}
		}
		return sf.toString();
	}

	/**
	 * ���ݺ��� ʡ��id ���˲�Ʒ
	 * 
	 * @param phone
	 * @param areaId
	 * @return FillProductInfo
	 */
	public static FillProductInfo filterProduct(String phone, String areaId,
			String ACTIVITYID) {
		FillProductInfo info = null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("activityId", ACTIVITYID);
		map.put("phoneNum", "");
		map.put("areaId", areaId);
		map.put("c_phone", phone);
		String rs = OP(QUERYURL, map, "GET");
		System.out.println("��ͨ������ֵ,��Ʒ��ѯ����json,����:"+phone+",,,resutl:"+rs);
		if (null != rs) {
			info = JSON.parseObject(rs.toLowerCase(), FillProductInfo.class);
		}
		return info;
	}

	/**
	 * ���ݺ��� ʡ��id ���˲�Ʒ
	 * @param phone
	 * @param areaId  ����ʡ��id
	 * @param areacode   0����  1�ƶ�  2��ͨ
	 * @param rtype json or bean
	 * @param tp_groups �ӿ��� Ӷ��ħ����
	 * @return FillProductInfo
	 */
	@SuppressWarnings("unchecked")
	public static Object filterProductJSON(String phone, String areaId,String areacode,String rtype,String tp_groups)  {
		List<String[]> arrs=BiProd.getFaceBytype(areacode,HttpFillOP.phoneAreas.get(areaId)); //[0]�ӿ�id   [1]������
		if(arrs==null || arrs.size()<=0){
			Log.info("����ͬ����Ʒ,����:"+phone+",,��ȡ�������ݿ���ֵ,�ӿ�ʧ��");
			return null;
		}
		DBService db=null;
		try{
			String aa="";
			String ff="";
			String f2="";
			for(String[] str:arrs){
				if(!FLOw1.equals(str[0])){
					aa=aa+str[1]+",";
				}else{//������˾
					ff=ff+str[1]+"mb";
					f2=f2+str[1]+",";
				}
			}
			FillProductInfo info=new FillProductInfo();
			ArrayList<FillProductDetail> pList=new ArrayList<FillProductDetail>();
			
			db=new DBService();
			if(!"".equals(aa)){
				String sql="SELECT fl.name,fl.price,(SELECT tp.value FROM sys_tpemploy_Flow tp WHERE tp.type='"+areacode+"' AND tp.cm_addr=0 AND tp.groups='"+tp_groups+"' LIMIT 0,1) AS yj FROM wht_flowprice fl  WHERE fl.type='"+areacode+"' AND fl.name in("+aa.substring(0,aa.length()-1)+")";
				if("1".equals(areacode)){
					sql="SELECT fl.name,fl.price,(SELECT tp.value FROM sys_tpemploy_Flow tp WHERE tp.type='"+areacode+"' AND tp.cm_addr='"+areaId+"' AND tp.groups='"+tp_groups+"' LIMIT 0,1) AS yj FROM wht_flowprice fl  WHERE fl.type='"+areacode+"' AND fl.name in("+aa.substring(0,aa.length()-1)+")";
				}
				List<String[]> ar=db.getList(sql);
				for(String[] s:ar){
					FillProductDetail d=new FillProductDetail();
					d.setProduct_id(ZDY);
					d.setProduct_name(s[0]+"mb");
					double facctFee=0.0;
					try {
						facctFee=FloatArith.mul(Double.parseDouble(s[1])*1000,1-FloatArith.div(Float.parseFloat(s[2]), 100));
					} catch (Exception e) {
						Log.info("����ͬ����Ʒ,����:"+phone+",,����Ӷ�����"+e.toString());
						return null;
					}
					d.setProduct_price(s[1]+"");
					d.setPrice_num((int)facctFee);
					d.setPro_num(1);
					pList.add(d);
				}
			}
			
			if(!"".equals(ff)){//������˾
				String sql2="SELECT fl.name,fl.price,(SELECT tp.value FROM sys_tpemploy_Flow tp WHERE tp.type='"+areacode+"' AND tp.cm_addr=0 AND tp.groups='"+tp_groups+"' LIMIT 0,1) AS yj FROM wht_flowprice fl  WHERE fl.type='"+areacode+"' AND fl.name in("+f2.substring(0,f2.length()-1)+")";
				List<String[]> ars=db.getList(sql2);
				FillProductInfo result=filterProduct(phone, areaId, ACTIVITYID);
				if(result!=null && result.getData()!=null && result.getData().getList()!=null && result.getData().getList().size()>0){
					List<FillProductDetail> fList=result.getData().getList();
					for(FillProductDetail f:fList){
						if(ff.indexOf(f.getProduct_name())!=-1){
							for(String[] s2:ars){
								if(f.getProduct_name().equals(s2[0]+"mb")){
									f.setProduct_price(s2[1]);
									double facctFee=0.0;
									try {
										facctFee=FloatArith.mul(Double.parseDouble(s2[1])*1000,1-FloatArith.div(Float.parseFloat(s2[2]), 100));
									} catch (Exception e) {
										Log.info("����ͬ����Ʒ,����:"+phone+",������˾,����Ӷ�����");
										return null;
									}
									f.setPrice_num((int)facctFee);
									pList.add(f);
									break;
								}
							}
						}
					}
				}
			}
			if(pList.size()>0){
				ProList p=new ProList();
				p.setList(pList);
				info.setStatus("0");
				info.setDesc("���ݶ�ȡ�ɹ�");
				info.setData(p);
				if("json".equals(rtype))
					return JSON.toJSONString(info);
				else 
					return info;
			}else{
				if("json".equals(rtype))
					return "{\"desc\":\"�ӿڲ�ѯ��ʱ,�����»�ȡ\",\"status\":\"3\"}";
				else 
					return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("json".equals(rtype))
				return "{\"desc\":\"ϵͳ�쳣\",\"status\":\"10\"}";
			else 
				return null;
		}finally{
			if(null!=db){
				db.close();
				db=null;
			}
		}
	}

	/**
	 * ��ͨ������ֵ
	 * 
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 *            ��
	 * @param aCTIVITYID
	 * @param userNo
	 * @param login
	 * @param areaCode
	 * @param phonePid
	 * @param userPid
	 * @param parentID
	 * @param groups   ��
	 * @param interfaceID
	 * @param flag
	 * @param name            
	 * @return
	 */
	public static HashMap<String, String> fillFlow(String orderNo, String productId,
			String phone, int realPrice, String aCTIVITYID, String userNo,
			String login, String areaCode, String phonePid, String userPid,
			String parentID, String groups,String interfaceID,String flag,String name) {
		TradeMsg reveMess = new TradeMsg();
		reveMess.setSeqNo(orderNo);
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("phoneNum", phone);
		content.put("userNo", userNo);
		content.put("parentID", parentID);
		content.put("isline", flag);// �Ƿ�ֱӪ��ʾ 0��ʾ��ֱӪ 1��ʾֱӪ
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
		int n = bp.yiKaHuiService(reveMess, null, orderNo, "10001", orderNo);
		String str=null;
		if (n == -5) {
			str= "{\"desc\":\"����\",\"status\":\"2030\"}";
		} else if (n == 0) {
			str ="{\"desc\":\"�����ɹ�\",\"status\":\"0\"}";
		} else if (n == -1 || n == 1) {
			str= "{\"desc\":\"����ʧ��\",\"status\":\"2\"}";
		}else if (n == 8) {
			str= "{\"desc\":\"������\",\"status\":\"9103\"}";
		} else if(n==3){
			str= "{\"desc\":\"�ӿ��쳣\",\"status\":\"8\"}";
		}else{
			str= "{\"desc\":\"������\",\"status\":\"1\"}";
		}
		content.clear();
		content.put("code",n+"");
		content.put("result",str);
		return content;
	}

	/**
	 * ��ͨ����������Ʒ
	 * 
	 * @param orderNo
	 * @param productId
	 * @param phone
	 * @param realPrice
	 * @return 0�ɹ� -1ʧ�� 2�쳣 1δ֪״̬
	 */
	public static int fillProduct(String orderNo, String productId,
			String phone, long realPrice, String aCTIVITYID) {
		int a = 1;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("activityId", aCTIVITYID);
		map.put("phoneNum", phone);
		map.put("orderNo", orderNo);
		map.put("productId", productId);
		map.put("realPrice", realPrice);
		map.put("c_phone", phone);
		String sign = WoMd5.encode(SIGNKEY + orderNo + phone + productId
				+ aCTIVITYID + realPrice);
		map.put("token", sign);
		String rs1 = OP(FILLURL, map, "GET");
		FillRes rs = null;
		if (null != rs1) {
			rs = JSON.parseObject(rs1.toLowerCase(), FillRes.class);
			if (null != rs) {
				a = Integer.parseInt(rs.getStatus());
				if (a == 0 ) {
					a=0;
				} else if (a==8||a==1){
					a=2;
				}else{
					a=-1;
				}
			}
		}
		Log.info("��ͨ������ֵ����,����:"+phone+",,��ֵ���(0�ɹ� -1ʧ�� 2�쳣 1δ֪״̬):"+a);
		return a;
	}

	/**
	 * ���������ѯ
	 * 
	 * @param orderNo
	 * @return 0�������ɹ�����ֵ�ɹ��� 1������ʧ�ܣ���ֵʧ�ܣ� 2����ѯʧ�ܣ��޸ö�����ʧ�ܣ�δ��ֵ�� 3�������У���ֵ�У�
	 *         -1���ӿڵ����쳣��ϵͳ�쳣����ϵ����Ա�����
	 */
	@SuppressWarnings("unchecked")
	public static int queryResult(String orderNo) {
		int a = -1;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orderNo", orderNo);
		map.put("activityId", ACTIVITYID);
		String rs = OP(RESULTURL, map, "GET");
		if (null != rs) {
			HashMap<String, String> result = JSON
					.parseObject(rs, HashMap.class);
			a = Integer.parseInt(result.get("status"));
		}
		return a;
	}

	/**
	 * ���������ѯJSON
	 * 
	 * @param orderNo
	 * @return json
	 */
	@SuppressWarnings("unchecked")
	public static String queryResultJSON(String orderNo) {
		String rs = "";
		String tableName="wht_orderform_"+Tools.getNow3().substring(2,6);
		DBService db=null;
		try {
			db=new DBService();
			String[] strs=db.getStringArray("SELECT state,CONCAT(writeoff,'������ֵ') AS cc FROM "+tableName+" WHERE tradeserial='"+orderNo+"'");
			if(strs==null || strs.length<=0){
				//����������
				rs="{\"desc\":\"��ѯʧ�ܣ��޸ö���\",\"status\":\"2\"}";
			}else if("0".equals(strs[0])){
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"0\"}";
			}else if("1".equals(strs[0])){
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"1\"}";
			}else {
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"3\"}";
			}
		} catch (Exception e) {
			Log.error("������ѯ�ӿ�,������:"+orderNo+",ϵͳ�쳣,ex:"+e);
			rs="{\"desc\":\"ϵͳ�쳣\",\"status\":\"-1\"}";
		}finally{
			if(db!=null){
				db.close();
				db=null;
			}
		}
		return rs;
	}
	
	/**
	 * 
	 * @param url
	 * @param data
	 * @param method
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@SuppressWarnings("unchecked")
	public static void OPReturn(String url, HashMap data, String method) throws HttpException, IOException {
		Set<String> set = data.keySet();
		for (String key : set) {
			url += (key + "=" + data.get(key) + "&");
		}
		url = url.substring(0, url.length() - 1);
		HttpMethod obj = null;
		if ("GET".equals(method)) {
			obj = new GetMethod(url);
		} else {
			obj = new PostMethod(url);
		}
		int status=client.executeMethod(obj);
		if (200 == status) {
			String result = obj.getResponseBodyAsString();
		}
	}

	public static void main(String[] args) {
//		String str="";
//		System.out.println("20150909150408T15474".length());
//		queryResult("1234567890");
		// ���ó�ֵ��ѯ
//		for(int i=0;i<10;i++){
//		 FillProductInfo info=filterProduct("18682033916","51","1278");
//		 List<FillProductDetail> list=info.getData().getList();
//		}
//		 for(FillProductDetail a:list){
//		 System.out.println(a.getProduct_name()+"  "+a.getProduct_id()+"  "+a.
//		 getProduct_price());
//		 }
		// ���ó�ֵ
		/*
		String orderno = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date())
				+ (int) (Math.random() * (1000 - 100) + 100);
		System.out.println(orderno);
		// int map=fillProduct(orderno,"51_000501X","18682033916",6);//��Ǯ������?
		// System.out.println("��ֵ���:"+map);
		System.out
				.println(MD5Util
						.MD5Encode(
								"201505131037080661990000000201236TWX778T8MV9F937GTQVKBB87VT2FY",
								"UTF-8"));
		// System.out.println(queryResult("111111111"));
		 */

	}
	/**
	 * ���������ѯJSON
	 * 
	 * @param orderNo
	 * @return json
	 */
	@SuppressWarnings("unchecked")
	public static String queryResultZDJSON(String orderNo) {
		String rs = "";
		String tableName="wht_orderform_"+Tools.getNow3().substring(2,6);
		DBService db=null;
		String str;
		try {
			db=new DBService();
			String[] strs=db.getStringArray("SELECT state,CONCAT(writeoff,'������ֵ'),writeoff,tradetime,chgtime,tradefee AS cc FROM "+tableName+" WHERE tradeserial='"+orderNo+"'");
			if(strs==null || strs.length<=0){
				//����������
				rs="{\"desc\":\"��ѯʧ�ܣ��޸ö���\",\"status\":\"2\"";
			}else if("0".equals(strs[0])){
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"0\"";
			}else if("1".equals(strs[0])){
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"1\"";
			}else {
				rs="{\"desc\":\""+strs[1]+"\",\"status\":\"3\"";
			}
			rs+=",\"writeoff\":\""+strs[2]+"\""+",\"tradetime\":\""+strs[3]+"\""+",\"chgtime\":\""+strs[4]+"\""+",\"tradefree\":\""+strs[5]+"\"}";
		} catch (Exception e) {
			Log.error("������ѯ�ӿ�,������:"+orderNo+",ϵͳ�쳣,ex:"+e);
			rs="{\"desc\":\"ϵͳ�쳣\",\"status\":\"-1\"}";
		}finally{
			if(db!=null){
				db.close();
				db=null;
			}
		}
		return rs;
	}

}