package com.infopublic.servletlistener;

import com.hunau.controller.TrafficQuartzJob;
import com.infopublic.util.Const;
import com.infopublic.util.QuartzManager;
import com.infopublic.util.Tools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TrafficResetServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init(){  
//		String qztime = "0 * * * * ?";
		String date = Tools.readTxtFile(Const.TRAFFICRESEDATE);
		String qztime = "0 0 0 "+Integer.parseInt(date)+" * ?";
		if(QuartzManager.checkExists(Const.TRAFFICRESEJOB)){
			QuartzManager.modifyJobTime(Const.TRAFFICRESEJOB, qztime);
		}else{
			QuartzManager.addJob(Const.TRAFFICRESEJOB, TrafficQuartzJob.class, qztime ); 
		}
    }  
  
    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException{
    }  
  
}
