package com.wlt.webm.business.bean.liuliangfan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSON;
import com.commsoft.epay.util.logging.Log;

/**
 * �㶫������
 * 
 * @author 1989
 */
public class LlFan {
	/**
	 * �û�ϵͳ���
	 */
	public static String userid = "4283335118";// �û�id
	/**
	 * ���ӵ�ַ
	 */
	private static String createUrL = "http://121.201.27.51:6001/api?";
	static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	static HttpClient client = new HttpClient(connectionManager);
	static {
		client.getHttpConnectionManager().getParams().setConnectionTimeout(
				60 * 1000);// ����ʱ�� 60�룬��λ�Ǻ���
		client.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);// ��ȡʱ��
																				// ,
																				// ��λ�Ǻ���
	}

	/**
	 * http����
	 * @param url  ��ַ
	 * @param data ����
	 * @param id   ������
	 * @param ops  ��������
	 * @return
	 */
	public static String OP(String url, String data, String id, String ops) {
		GetMethod postMt = null;
		try {
			
			postMt = new GetMethod(url);
			NameValuePair params = new NameValuePair("cmd", data);
			postMt.setQueryString(new NameValuePair[] { params });
			System.out.println("1212");
			int status = client.executeMethod(postMt);
			System.out.println("22222222");
			Log.info(id + " ����" + ops + "����Ӧ����:" + status);
			System.out.println(id + " ����" + ops + "����Ӧ����:" + status);
			if (200 == status) {
				String result = convertStreamToString(postMt.getResponseBodyAsStream());
				Log.info(id + " ����" + ops + "��Ӧ���:" + result);
				return result;
			}
		} catch (Exception e) {
			Log.error(id + " ����" + ops + " ϵͳ�쳣" + e.toString());
			System.out.println("3333"+e.toString());
		} finally {
			postMt.releaseConnection();
		}
		return null;
	}

	public static String convertStreamToString(InputStream is)
			throws IOException {
		StringBuilder sf = new StringBuilder();
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
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
	 * ������ֵ
	 * @param request_no
	 *            ������
	 * @param phone
	 *            �绰����
	 * @param face
	 *            ��ֵ
	 * @param type
	 *            ��Ӫ������
	 * @param rage
	 *            ����
	 * @return  0 �ɹ�  -1  ʧ��  2 ������
	 */
	public static int llFanFill(String request_no, String phone, String face,
			String type, String rage) {
		face=face.replace("mb","").replace("MB","");
		int rs = 2;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cmd", "recharge");
		params.put("userid", userid);
		params.put("faceval", face);
		params.put("outor", request_no);
		params.put("phone", phone);
		params.put("rage",rage);
		String op = OP(createUrL, JSON.toJSONString(params), request_no, "FILL");
		System.out.println("1111111111111111"+op);
		if(null!=op){
			System.out.println("1212121212"+op);
			String[] str=op.split("\\|");
			if("1".equals(str[0])){
				rs=0;
			}else if("-1".equals(str[0])){
				rs=-1;
			}
		}
		return rs;
	}

	/**
	 * ������ѯ
	 * @param request_no
	 * @return 0 �ɹ� -1ʧ�� 2�����У��쳣
	 */
	public static int queryflow(String request_no) {
		int rs = 2;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cmd", "orderinfo");
		params.put("userid", userid);
		params.put("outor", request_no);
		String op = OP(createUrL, JSON.toJSONString(params), request_no,
				"QUERY");
		System.out.println("�������ѯ���:" + op.toString());
		if(null!=op){
			String[] str=op.split("\\|");
			if("1".equals(str[0])){//����0��ʾʧ��
				if("1".equals(str[1])){//str[1]==1�ɹ���0��Ҫ�ص���-1��ֵʧ��
					rs=0;
				}else if("-1".equals(str[1])){
					rs=-1;
				}
			}else{
				rs=-1;
			}
		}
		return rs;
	}

	class Test extends Thread{
		@Override
		public void run() {
			queryflow("00123895642");
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// List<BasicNameValuePair> pairList = new
		// ArrayList<BasicNameValuePair>();
//        LlFan an =new LlFan();
//        for(int i=0;i<1000;i++){
//        	Test test=an.new Test();
//        	test.start();
//        }
//		System.out.println("��ѯ���:"+queryflow("00123892128"));
		System.out.println("��ֵ���:"+llFanFill("00123892221", "18926512055", "5", null, ""));
	}

}