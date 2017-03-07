package com.tinckay;

import com.tinckay.common.AppContentUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PjtmanageApplication {

	public static void main(String[] args) {
		ApplicationContext act =
		SpringApplication.run(PjtmanageApplication.class, args);
		AppContentUtil.setApplicationContext(act);


//		Project project = new Project();
//		try {
//			System.out.print(project.getId());
//		}
//		catch (Exception e){
//			System.out.print(e.getMessage());
//		}
//		finally {
//			project = null;
//		}

//		for(int i : BeanRefUtil.stateMap.keySet())
//
//			System.out.println(i + " : " + BeanRefUtil.stateMap.get(i));
		//byte[] pjtState = {0,1,2,3,10,11,12,21,22,31,32};
		//StateInfo.


	}
}
