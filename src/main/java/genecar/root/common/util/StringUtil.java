package genecar.root.common.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {

	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	/*
     * URL 인코딩 된 PATH 반환 ('/' 제외)
     * ex) /새폴더/매출.xls => /%EC%83%88%ED%8F%B4%EB%8D%94/%EB%A7%A4%EC%B6%9C.xls
     */
    public static String getURLEncodedFilePath(String input){
    	String [] elements = input.split("/");
    	StringBuilder  sb = new StringBuilder();
    	boolean isFirst = true;
    	for(String elm:elements){
    		try {
    			if(isFirst){
    				isFirst = false;
    			}else{
    				sb.append("/");
    			}
    			sb.append(URLEncoder.encode(elm, "UTF-8"));
    		} catch (UnsupportedEncodingException e) {
    			logger.debug(e.getMessage());
                return input;
    		}
    	}
    	return sb.toString();
    }

    /**
     *
     * @param strNm
     * @param limitLen
     * @return
     * @throws UnsupportedEncodingException
     */
	public static String parseStringByBytesUTF8(String strNm, int limitLen)
			throws UnsupportedEncodingException {

		if (strNm == null) return null;

		int extLastIndex = strNm.lastIndexOf('.') == -1 ? strNm.length() : strNm.lastIndexOf('.');

		String fileNm = strNm.substring(0, extLastIndex);
		String fileExt = strNm.substring(extLastIndex);

		String[] rtnStrArry = null;

		byte[] rawBytes = fileNm.getBytes("UTF-8");
		int rawLength = rawBytes.length;

		int index = 0;
		int minusByteNum = 0;
		int offset = 0;

		int hangulByteNum = 3;

		if (rawLength > limitLen) {

			int aryLength = (rawLength / limitLen) + (rawLength % limitLen != 0 ? 1 : 0);
			rtnStrArry = new String[aryLength];

			for (int i = 0; i < aryLength; i++) {
				minusByteNum = 0;
				offset = limitLen;

				if (index + offset > rawBytes.length) {
					offset = offset - index;
				}
				for (int j = 0; j < offset; j++) {
					if (((int) rawBytes[index + j] & 0x80) != 0) {
						minusByteNum++;
					}
				}
				if (minusByteNum % hangulByteNum != 0) {
					offset -= minusByteNum % hangulByteNum;
				}
				rtnStrArry[i] = new String(rawBytes, index, offset, "UTF-8");
				index += offset;
			}

		} else {
			rtnStrArry = new String[] { fileNm };
		}

		String strRet="";
        for(String s : rtnStrArry) {
            strRet+=s;
        }

		return strRet+fileExt;
	}

    /**
     * 문자 Byte 길이 체크
     * @param strNm
     * @param limitLen
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String stringByteChk(String strNm, int limitLen) throws UnsupportedEncodingException{
        String rtnStrNm;

    	if(strNm.getBytes("UTF-8").length < limitLen){

    		rtnStrNm = strNm;

		}else{

			int fileExtIdx = strNm.lastIndexOf('.');
	    	int strLength = strNm.length();

	    	String fileExt = strNm.substring(fileExtIdx, strLength);
	    	String fileNm  = strNm.substring(0,fileExtIdx);

	    	byte[] strByte = fileNm.getBytes("UTF-8");

	    	int endPos = 0;
	    	int	currByte = 0;

	    	for(int i = 0 ; i < fileNm.length() ; i++){
	    		char ch = fileNm.charAt(i);

	    		currByte = currByte + availibleByteNum(ch);

	    		if(currByte > limitLen){
	    			endPos = currByte - availibleByteNum(ch);
	    			break;
	    		}else{
	    			endPos = currByte - availibleByteNum(ch);
	    		}

	    	}

	    	byte newStrByte [] = new byte[endPos];

	    	System.arraycopy(strByte, 0, newStrByte, 0, endPos);

	    	String newStr = new String(newStrByte,"UTF-8");

	    	rtnStrNm =  newStr+fileExt;

		}

    	return rtnStrNm;
    }

	/**
	 *
	 * @param ch
	 * @return
	 */
	public static int availibleByteNum(char ch){

		int oneByteMin = 0x0000;
		int oneByteMax = 0x007F;

		int twoByteMin = 0x0800;
		int twoByteMax = 0x07FF;

		int threeByteMin = 0x0800;
		int threeByteMax = 0xFFFF;

		int surrogateMin = 0x10000;
		int surrogateMax = 0x10FFFF;

		int digit = (int) ch;

		if(oneByteMin <= digit && digit<= oneByteMax) return 1;
		if(twoByteMin <= digit && digit<= twoByteMax) return 2;
		if(threeByteMin <= digit && digit<= threeByteMax) return 3;
		if(surrogateMin <= digit && digit<= surrogateMax) return 4;

		return -1;
	}


    public static boolean isEmpty(String str) {

    	boolean result = false;
    	if(str ==null || str.length() == 0 ){
    	  result = true;
    	}else{
    	  result = false;
    	}
    	return result;
    }


    /**
     * 문자열이 null일때 ""를 리턴한다.
     *
     * @param obj
     *            the obj
     * @return String
     */
    public static String nvl(Object obj) {
        return nvl(obj, "");
    }


    /**
     * 문자열이 null일때 ""를 리턴한다.
     *
     * @param obj
     *            the obj
     * @param ifNull
     *            the if null
     * @return String
     */
    public static String nvl(Object obj, String ifNull) {
        return ( obj != null ) ? obj.toString() : ifNull;
    }

    /**
     * 주어진 문장열이 공백(빈칸,개행문자,탭문자)로만 이루어져 있는지 확인한다.
     * @param str:검사할 문자열
     * @return 공백으로만 이루어졌다면 true 아니면 false
     */
    public static boolean hasOnlySpace(String str)
    {
    	boolean result = false;
    	if(str ==null || str.length() == 0 ){
    	  result = true;
    	}else{
    	  result = false;
    	}
    	return result;

    }

    /**
     * if String is null or "".
     *
     * @param str
     *            the str
     * @return true/false
     */
    public static boolean isBlank(String str) {

        return str == null || "".equals(str) ;
    }

    /**
     * return default value for integer if the object is null.
     *
     * @param i
     *            the i
     * @return the int
     */
    public static int nvlInt(int i) {

        return  (Object) i == null ? 0 : i ;
    }

    /**
     * if String is "", return Default Value.
     *
     * @param str1
     *            the str1
     * @param str2
     *            the str2
     * @return the string
     */
    public static String defVal(String str1, String str2) {

        return  "".equals(nvl(str1)) ? str2 : str1 ;
    }
}
