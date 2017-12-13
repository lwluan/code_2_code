package com.cd2cd.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.SysUserVo;

/**
 * 对像转换
 */
public class BeanUtil {

	private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	public static <E> List<E> voConvertList(List<?> list, Class<E> target) {
		List<E> retList = new ArrayList<E>();

		for (Object o : list) {
			E targetObj = voConvert(o, target); 
			retList.add(targetObj);
		}
		return retList;
	}

	public static <E> E voConvert(Object o, Class<E> target) {
		E targetObj = null;
		try {
			targetObj = target.newInstance();
			BeanUtils.copyProperties(o, targetObj);
		} catch (Exception e) {
			logger.error("e={}", e);
		}
		return targetObj;
	}
	
	public static BaseRes<DataPageWrapper<SysUserVo>> genDataPageRes(Integer currPage, Integer pageSize) {
		BaseRes<DataPageWrapper<SysUserVo>> res = new BaseRes<DataPageWrapper<SysUserVo>>();
		DataPageWrapper<SysUserVo> dataPageWrapper = new DataPageWrapper<SysUserVo>();
		if( currPage != null) {
			dataPageWrapper.setCurrPage(currPage);
		}
		
		if( pageSize != null ) {
			dataPageWrapper.setPageSize(pageSize);
		}
		
		res.setData(dataPageWrapper);
		return res;
	}
}
