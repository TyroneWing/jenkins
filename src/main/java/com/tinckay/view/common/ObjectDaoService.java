package com.tinckay.view.common;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Service;



@Service
public interface ObjectDaoService {
	
	EntityManager getEntityManager();
	
	EntityManagerFactory getEntityManagerFactory();

	
	public List<Map<String, Object>> getViewListById(
			String viewName,
			String condField,
			String condStr,
			String sortField);
	
	public List<Map<String, Object>> getViewListFromIdAndTime(
			String viewName,
			String condField,
			Long condId,
			String timeField,
			String startTime,
			String endTime,
			String sortField);	
	
	public List<Map<String, Object>> getListUseProc(String sqlProc,List listParam);


	public List<Map<String, Object>> findByParamList(
			String viewName,
			List<String> paramList,
			String sortField);

}
