package com.cd2cd.admin.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public class GZIPUtil {

	private static final String compressChart = "ISO-8859-1";
	public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
	        gzip.close();
	        return out.toString(compressChart);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return str;
    }
	
	public static String unCompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        return IOUtils.toString(new GZIPInputStream(new ByteArrayInputStream(str.getBytes(compressChart))), compressChart);
    }
	
	public static boolean isZipChart(String str) {
		try {
			new GZIPInputStream(new ByteArrayInputStream(str.getBytes(compressChart)));
			return true;
		} catch (IOException e) {
			
		}
		return false;
	}
	public static void main(String[] args) throws IOException {
		
		String ss = "899809898";
		String aa = compress(ss);
		
		System.out.println(isZipChart(aa));
	}
	/**
	 * jquery;pako;
	 * 
	 * let reportStr = report;
            try{
              reportStr = pako.ungzip(report, { to: 'string' } );
            }catch(err){
              console.log(`------${err}`);
            }
	 */
}
