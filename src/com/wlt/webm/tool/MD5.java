package com.wlt.webm.tool;

import java.security.MessageDigest;

/**
 * MD5������<br>
 */
public class MD5 {

    /**
     * 16��������
     */
    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * ת���ֽ�����Ϊ16�����ִ�
     * @param b �ֽ�����
     * @return 16�����ִ�
     */
    public static String byteArrayToHexString(byte[] b)
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * ת���ֽ�Ϊ16�����ַ���
     * @param b �ֽ�
     * @return 16�����ַ���
     */
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
        {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * �����ַ���
     * @param src �����ܵ��ַ���
     * @return ���ܺ��32λ�ַ���
     */
    public static String encode(String src)
    {
        String resultString = null;
        try
        {
            resultString = new String(src);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        }
        catch (Exception ex)
        {

        }
        return resultString;
    }
}