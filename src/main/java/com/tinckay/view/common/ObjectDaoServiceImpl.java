package com.tinckay.view.common;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Service;



@Service
public class ObjectDaoServiceImpl implements ObjectDaoService{
	@PersistenceContext
	private EntityManager entityManager;
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	private static Logger logger = Logger.getLogger(ObjectDaoServiceImpl.class);
	
	
	
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}


	public EntityManager getEntityManager(){
		return this.entityManager;
	}
	

	
	public List<Map<String, Object>> getViewListFromIdAndTime(
			String viewName,
			String condField,
			Long condId,
			String timeField,
			String startTime,
			String endTime,
			String sortField){
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("select a.* from ");
			sql.append(viewName.concat(" a where (1=1)"));
			if(condId > 0) {
				sql.append(" and (a." + condField + "= :id)");
			}
			if(startTime.length() > 0) {
				sql.append(" and (a." + timeField + ">= :startTime)");
			}
			
			if(endTime.length() > 0) {
				sql.append(" and (a." + timeField + "< :endTime)");
			}
			
			if(sortField.length()>1) {
				sql.append(" order by a." + sortField);
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
			try {
				if(condId > 0) {
					nativeQuery.setParameter("id", condId);
				}
				if(startTime.length() > 0) {
					nativeQuery.setParameter("startTime", startTime);
				}
				if(endTime.length() > 0) {
					nativeQuery.setParameter("endTime", endTime);
				}
				nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
				try {
					List<Map<String, Object>> retVal = nativeQuery.list();
					return retVal;					
				} catch (Exception e) {
					return null;
				}
			} finally {
				nativeQuery = null;
				query = null;
			}
		} finally {
			sql = null;
		}		
	}
	
	public List<Map<String, Object>> getViewListById(
			String viewName,
			String condField,
			String condStr,
			String sortField) {
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("select a.* from ");
			sql.append(viewName.concat(" a"));
			if ((!condStr.equals("")) && (!condStr.equals("0")))
				sql.append(" where a." + condField + "= :id");
			if (sortField.length() > 1)
				sql.append(" order by a." + sortField);
			Query query = this.entityManager.createNativeQuery(sql.toString());
			SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
			try {
				if ((!condStr.equals("")) && (!condStr.equals("0")))
					nativeQuery.setParameter("id", condStr);
				nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
				try {
					List<Map<String, Object>> retVal = nativeQuery.list();
					return retVal;
				} catch (Exception e) {
					return null;
				}

			} finally {
				nativeQuery = null;
				query = null;
			}
		} finally {
			sql = null;
		}
	}

	public List<Map<String, Object>> findByParamList(String viewName,List<String> paramList,String sortField){
		StringBuffer sql = new StringBuffer();
		try{
			sql.append("select * from ");
			sql.append(viewName.concat(" where (1 = 1) "));
			if(null != paramList && paramList.size() > 0){
				for(String param:paramList){
					sql.append(" and ".concat(param));
					//Object object = paramMap.get(field);
					//System.out.print(object.getClass().getName());
				}
			}

			if(!"".equals(sortField)) {
				sql.append(" order by " + sortField);
			}
			logger.info(sql.toString());
			List<Map<String, Object>> retVal = null;

			Query query = this.entityManager.createNativeQuery(sql.toString());
			SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
			try {
//				if((!condStr.equals("")) && (!condStr.equals("0")))
//					nativeQuery.setParameter("id", condStr);
				nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
				try {
					retVal = nativeQuery.list();
					return retVal;
				} catch (Exception e) {
					return null;
				}

			} finally {
				nativeQuery = null;
				query = null;
			}
		} finally {
			sql.setLength(0);
		}
	}


	public List<Map<String, Object>> getListUseProc(String sqlProc,List listParam){
		Query query = this.entityManager.createNativeQuery(sqlProc);
		SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
		try {
			if(listParam.size() > 0){
				for(int i=0;i<listParam.size();i++)
					nativeQuery.setParameter("inParam".concat(String.valueOf(i+1)), listParam.get(i));
			}
			nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			try {
				List<Map<String, Object>> retVal = nativeQuery.list();
				return retVal;					
			} catch (Exception e) {
				return null;
			}
			
		} finally {
			nativeQuery = null;
			query = null;
		}

	}



	
	
}
