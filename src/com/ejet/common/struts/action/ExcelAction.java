package com.ejet.common.struts.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ejet.common.struts.bean.Count;
import com.ejet.common.struts.form.CountForm;
import com.ejet.util.Excel;
import com.wlt.webm.rights.form.SysUserForm;

/**
 * ����EXCEL�Ĺ���Action��<hr>
 * Ҫ���û������б�����beanName���ԣ����������ݱ�����ʵ����<code>Count</code>�ӿڵı���ͳ�����ȫ���ơ�<br>
 * Company��������###EJET###���޹�˾
 * Copyright: Copyright (c) 2009
 * @author ###ejet###
 * �޸��ˣ�����
 * �޸����ڣ�2009-9-28
 * @version V2.1.2.0
 */
public class ExcelAction extends Action
{
    /**
     * ִ�е���EXCEL�ķ���
     * @param mapping �ͻ��������Ӧ��ش�����ӳ��
     * @param form ����ͳ�������ı���
     * @param request �ͻ����������
     * @param response �������Ӧ����
     * @return ҳ����ת����
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        SysUserForm user = (SysUserForm) session.getAttribute("userSession");//�û���Ϣ
        CountForm fm = (CountForm) form;//����
        //���õ�ǰҳ��Ϊ���ֵ���򵼳���������
         fm.setPageIndex(Integer.MAX_VALUE);
        Count count = (Count) Class.forName(fm.getBeanName()).newInstance();
        count.setCountForm(fm);
        count.setUserForm(user); 
        count.init();

        String title = count.getTitle();//��������
        String[][] colTitles = (String[][]) count.getColTitles();//��ͷ����
        count.setExcelState("1");
        Object bodyData = count.getBodyData();//��������
        String[] remarks = count.getRemarks();//��ע

        Excel excel = new Excel();
        excel.setCols(colTitles[0].length);
        excel.createCaption(title);
        excel.createColCaption(colTitles);
        if(bodyData!=null){
        	excel.createBody(bodyData);
        }
        
        if (remarks != null)
        {
            excel.createRemarks(remarks);
        }
        String excelFileName = URLEncoder.encode(title + ".xls", "UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=" + excelFileName);
//        response.setContentType("application/vnd.ms-excel");
//        response.reset();
        OutputStream out = null;
        try
        {
            out = response.getOutputStream();
            excel.createFile(out);
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException e)
                {
                }
            }
        }

        return mapping.findForward(null);
    }

}