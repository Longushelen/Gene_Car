package genecar.root.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CmmnUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static final ObjectMapper OM = new ObjectMapper();
	
	public static String yyyyMMddhhmmssSSS() {
		return sdf.format(new Date());
	}
	public static String yyyyMMddhhmmss() {
		return sdf2.format(new Date());
	}
	public static String yyyyMMddhhmmss(Date date) {
		return sdf2.format(date);
	}
	
	public static String yyyyMMddhhmmss(long time) {
		Date date = new Date();
		date.setTime(time);
		return sdf2.format(date);
	}
	
	public static String subStrBytes(String source, int cutLength) {
		if(!source.isEmpty()) { 
			source = source.trim(); 
			if(source.getBytes().length <= cutLength) { 
				return source; 
			} else { 
				StringBuffer sb = new StringBuffer(cutLength); 
				int cnt = 0; 
				for(char ch : source.toCharArray()) { 
					cnt += String.valueOf(ch).getBytes().length; 
					if(cnt > cutLength) break; 
				} 
				return sb.toString(); 
			} 
		} else { 
			return ""; 
		} 
	}
	
	public static String getUserIp() {
		
        String ip = null;
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
		
		return ip;
	}
	
}
