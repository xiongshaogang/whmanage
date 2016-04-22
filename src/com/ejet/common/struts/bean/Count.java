package com.ejet.common.struts.bean;

import com.ejet.common.struts.form.CountForm;
import com.ejet.util.Pagination;
import com.wlt.webm.rights.form.SysUserForm;

/**
 * ͳ�Ʊ����Ľӿڡ�<hr>
 * ϵͳ�Զ�����˳��<br>
 * 1������<code>setForm(CountForm)</code>��<code>setUser(UserForm)</code>������<br>
 * 2������<code>init()</code>������<br>
 * 3���������������⣬������������˳���򲻹̶���ʵ����Щ����ʱ��֮�䲻Ӧ���й������ù�ϵ��<br>
 * Company��������###EJET###���޹�˾
 * Copyright: Copyright (c) 2009
 * @author ###ejet###
 * �޸����ڣ�2009-9-28
 * @version V2.1.2.0
 */
public interface Count
{
    /**
     * ���ò�ѯ��������
     * @param form ��ѯ��������
     */
    public void setCountForm(CountForm form);

    /**
     * ���ò�ѯ�û�
     * @param user �û���Ϣ
     */
    public void setUserForm(SysUserForm user);

     /**
      *���õ�������־
      * @param excelState
      */
    public void setExcelState(String excelState) ;
    /**
     * ��ʼ���������÷�������<code>setCountForm()</code>��<code>setUserForm()</code>֮�󱻵��ã�
     * ��������������֮ǰ���á���ˣ������ڴ˷����ڳ�ʼ���û���Ҫ����Դ��
     * @throws Exception 
     */
    public void init() throws Exception;

    /**
     * �����ʾ��Ϣ��
     * @return ��ʾ��Ϣ��û����ʾ��Ϣ�򷵻� null��
     * @throws Exception 
     */
    public String getAlertMessage() throws Exception;

    /**
     * ��ñ�������
     * @return ��������
     * @throws Exception 
     */
    public String getTitle() throws Exception;

    /**
     * ��ñ�����ͷ���⡣<br>
     * <hr>
     * 1�������һ����ͷ���⣬�����ͨ�����´���ʵ�֣�
     * <code><pre>
     * String[][] colTitles = { { "��1", "��2", "��3" } };
     * return colTitles;
     * </pre></code>
     * <hr>
     * 2������Ƕ�����ͷ���⣬�����ͨ�����´���ʵ�֣�
     * <code><pre>
     * String[][] colTitles = { { "��1��1", "��1��2", null, "��1��4" },
     *                          { null, "��2��2", "��2��3", "��2��4" } };
     * return colTitles;
     * </pre></code>
     * ����ʾ���б���ʵ��Ч��Ϊ
     * <table border="1">
     *   <tr align="center"><td rowspan="2">��1��1</td><td colspan="2">��1��2</td><td>��1��4</td></tr>
     *   <tr align="center"><td>��2��2</td><td>��2��3</td><td>��2��4</td></tr>
     * </table>
     * @return ��ͷ����
     * @throws Exception 
     */
    public String[][] getColTitles() throws Exception;

    /**
     * ��ñ���չʾ�����ݡ�
     * @return 1���б���List(String[])��<br>
     *          2���кϲ�һ�е����ݣ�Map(List(String[]))��<br>
     *          3���кϲ����е����ݣ�Map(Map(List(String[])))��
     * @throws Exception 
     */
    public Object getBodyData() throws Exception;

    /**
     * ������ݿ��ҳ��Ϣ
     * @return ���ݿ��ҳ��Ϣ
     * @throws Exception 
     */
    public Pagination getPagination() throws Exception;

    /**
     * ��õ���EXCEL����������
     * @return EXCELÿһ�е�Ԫ�����������
     * @throws Exception 
     */
    public int[] getExcelDataType() throws Exception;

    /**
     * ��ñ�����ע��Ϣ
     * @return ������ע��Ϣ
     */
    public String[] getRemarks();
}