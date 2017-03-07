package com.tinckay.common;


import org.apache.catalina.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.lang.Byte.parseByte;

/**
 * java bean 反射的方法
 * @author 
 */
public class BeanRefUtil {

	/**
	 * pjtState 项目状态值：
	 * 0,新增
	 * 1,完结
	 * 2,取消
	 * 3,暂停
	 * 10,进行中
	 * 11,完结申请
	 * 12,完结未通过
	 * 21,取消申请
	 * 22,取消未通过
	 * 31,暂停申请
	 * 32,暂停未通过
	 */


	private static final byte[]  stateValue =  {0,1,2,3,10,11,12,21,22,31,32};
	private static final String[]  stateInfo =
			{		"新建",
			 		"完结",
					"取消",
					"暂停",
					"进行中",
					"完结申请",
					"完结未通过",
					"取消申请",
					"取消未通过",
					"暂停申请",
					"暂停未通过"
			};

	public static final Map<Integer,String> stateMap = new HashMap(){{
		for(int i = 0; i < stateValue.length; i++ )
			put(Integer.valueOf(stateValue[i]),stateInfo[i]);
	}};

	public static final String getOptFlagStr(byte optFlag){
		String result = "启动";
		switch (optFlag) {
			case 1:
				result = "完结";
				break;
			case 2:
				result = "取消";
				break;
			case 3:
				result = "暂停";
				break;
		}
		return result;
	}



	/**
	 * 通过项目状态判断是否允许项目编辑或删除
	 * param:
	 * 		state :
	 *      optFlag : 1编辑操作，2删除操作
	 * @return
     */
	public static String optPjtInState(byte state,byte optFlag){
		Integer stateValue = Integer.valueOf(state);
		switch (optFlag){
			case 1:
				if((state >= 1 && state <= 3 ) || state == 11 || state == 21 || state == 31 )
					return "项目处于【" + stateMap.get(stateValue) + "】状态，不允许编辑操作。";
				break;
			case 2:
				if(state != 0 && state != 2)
					return "项目处于【" + stateMap.get(stateValue) + "】状态，不允许删除操作。";
				break;
		}

		return "";
	}


	/**
	 * 取Bean的属性和值对应关系的MAP
	 * @param bean
	 * @return Map
	 */
	public static Map<String, String> getFieldValueMap(Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, String> valueMap = new HashMap<String, String>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				String result = null;
				if ("Date".equals(fieldType)) {
					result = fmtDate((Date) fieldVal);
				} else {
					if (null != fieldVal) {
						result = String.valueOf(fieldVal);
					}
				}
				valueMap.put(field.getName(), result);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;

	}

	/**
	 * set属性的值到Bean
	 * @param bean
	 * @param valMap
	 */
	public static void setFieldValue(Object bean, Map<String, String> valMap) {
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {

				String fieldSetName = parSetName(field.getName());
				if (!checkSetMet(methods, fieldSetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
				String value = valMap.get(field.getName());
				if (null != value && !"".equals(value)) {
					String fieldType = field.getType().getSimpleName();
					if ("String".equals(fieldType)) {
						fieldSetMet.invoke(bean, value);
					} else if ("Date".equals(fieldType)) {
						Date temp = parseDate(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Byte".equals(fieldType) || "byte".equals(fieldType)) {
						Byte temp = parseByte(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
						Integer intval = Integer.parseInt(value);
						fieldSetMet.invoke(bean, intval);
					} else if ("Long".equalsIgnoreCase(fieldType)) {
						Long temp = Long.parseLong(value);
						fieldSetMet.invoke(bean, temp);
					} else if("Float".equalsIgnoreCase(fieldType)){
						Float temp = Float.parseFloat(value);
						fieldSetMet.invoke(bean, temp);
					}else if ("Double".equalsIgnoreCase(fieldType)) {
						Double temp = Double.parseDouble(value);
						fieldSetMet.invoke(bean, temp);
					} else if ("Boolean".equalsIgnoreCase(fieldType)) {
						Boolean temp = Boolean.parseBoolean(value);
						fieldSetMet.invoke(bean, temp);
					} else {
						System.out.println("not supper type" + fieldType);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

	}

	public static void setFieldObject(Object bean, Map<String, Object> valMap) {
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {

				String fieldSetName = parSetName(field.getName());
				if (!checkSetMet(methods, fieldSetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
				Object value = valMap.get(field.getName());
				if (null != value ) {
					String fieldType = field.getType().getSimpleName();
					fieldSetMet.invoke(bean,value);
//					if ("String".equals(fieldType)) {
//						fieldSetMet.invoke(bean, value);
//					} else if ("Date".equals(fieldType)) {
//						Date temp = parseDate(value);
//						fieldSetMet.invoke(bean, temp);
//					} else if ("Byte".equals(fieldType) || "byte".equals(fieldType)) {
//						Byte temp = parseByte(value);
//						fieldSetMet.invoke(bean, temp);
//					} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
//						Integer intval = Integer.parseInt(value);
//						fieldSetMet.invoke(bean, intval);
//					} else if ("Long".equalsIgnoreCase(fieldType)) {
//						Long temp = Long.parseLong(value);
//						fieldSetMet.invoke(bean, temp);
//					} else if("Float".equalsIgnoreCase(fieldType)){
//						Float temp = Float.parseFloat(value);
//						fieldSetMet.invoke(bean, temp);
//					}else if ("Double".equalsIgnoreCase(fieldType)) {
//						Double temp = Double.parseDouble(value);
//						fieldSetMet.invoke(bean, temp);
//					} else if ("Boolean".equalsIgnoreCase(fieldType)) {
//						Boolean temp = Boolean.parseBoolean(value);
//						fieldSetMet.invoke(bean, temp);
//					} else {
//						System.out.println("not supper type" + fieldType);
//					}
				}
			} catch (Exception e) {
				continue;
			}
		}

	}
	
	

	

	/**
	 * 格式化string为Date
	 * @param dateStr
	 * @return date
	 */
	public static Date parseDate(String dateStr) {
		if (null == dateStr || "".equals(dateStr.trim())) {
			return null;
		}
		try {
			String fmtstr = "";
			if(dateStr.contains("-")){
				fmtstr = "yyyy-MM-dd";
			}
			if(dateStr.contains("/")){
				fmtstr = "yyyy/MM/dd";
			}
			
			if (dateStr.contains(":")) {
				fmtstr = fmtstr + " HH:mm:ss";
			}
			
			if((fmtstr.equals(""))&&(dateStr.length()==14)){
				fmtstr = "yyyyMMddHHmmss";
			}
			
			if(!fmtstr.equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
				return sdf.parse(dateStr);
			}
			else return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转化为String
	 * @param date
	 * @return dateStr
	 */
	public static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断是否存在某属性的 set方法
	 * 
	 * @param methods
	 * @param fieldSetMet
	 * @return boolean
	 */
	public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
	public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 拼接某属性的 get方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * 拼接在某属性的 set方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public static String getUUID(){ 
        return UUID.randomUUID().toString().replace("-", "");
    }



	@SuppressWarnings("rawtypes")
	public static void copyProperty(Object oldObject,Object newObject) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		//新的class
		Class newClass = newObject.getClass();
		//老的class
		Class oldClass = oldObject.getClass();
		//该类所有的属性
		Field[] newFields = newClass.getDeclaredFields();
		//新的属性
		Field newField = null;
		//老的属性
		Field oldField = null;
		for(Field f : newFields){
			//类中的属性名称
			String fieldName = f.getName();
			if("id".equals(fieldName)){
				continue;
			}
			//通过属性名称获取属性
			newField = newClass.getDeclaredField(fieldName);

			//获取属性的值时需要设置为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
			//值为 false 则指示反射的对象应该实施 Java 语言访问检查。
			newField.setAccessible(true);
			//根据属性获取对象上的值
			Object newFieldObject = newField.get(newObject);

			oldField = oldClass.getDeclaredField(fieldName);
			oldField.setAccessible(true);
			Object oldFieldObject = oldField.get(oldObject);

			//过滤空的属性或者一些默认值
			if (isContinue(newFieldObject,oldFieldObject)) {
				continue;
			}
			oldField.set(oldObject, newFieldObject);
		}
	}

	/**
	 *  是否跳出本次循环
	 * @param newFieldObject //新对象的值
	 * @param oldFieldObject //旧对象的值，需更新的对象
	 * @return true 是 有null或者默认值
	 *         false 否 有默认值
	 */
	private static boolean isContinue(Object newFieldObject,Object oldFieldObject){
		if (null == newFieldObject || "".equals(newFieldObject)) {
			return true;
		}
		String newValue = newFieldObject.toString();
		String oldValue = oldFieldObject.toString();
		if(oldValue.equals(newValue)){
			return true;
		}

//		if ("0".equals(valueStr) || "0.0".equals(valueStr)) {
//			return true;
//		}
		return false;
	}

//	@Bean
//	public FilterRegistrationBean indexFilterRegistration() {
//		FilterRegistrationBean registration = new FilterRegistrationBean(new ResponseExtFilter());
//		registration.addUrlPatterns("/");
//		return registration;
//	}


	public static boolean dirExistsOrCreate(String path){
		File file =new File(path);
		if(!file.exists() && !file.isDirectory()){
			return file.mkdir();
		}
		return true;
	}


	public static String getRemoteUserName(HttpServletRequest request){
		String userName = ((null == request) ? "" : request.getRemoteUser());
		return (null == userName || "".equals(userName)) ? "Admin" : userName;
	}

	public static User getSessionUser(HttpServletRequest request){
		User user = null;
		HttpSession session = request.getSession();
		if(null != session){
			user = (User)session.getAttribute("user");
		}
		return user;
	}


}
