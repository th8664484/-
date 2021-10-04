package com.AuthorityManagement.util;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import com.AuthorityManagement.domain.Token;

public class TokenScanThread extends Thread implements Runnable {

	private ServletContext application ;
    public TokenScanThread(ServletContext application){
        this.application = application;
    }
    
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000*60);
				Enumeration<String> enums = application.getAttributeNames();
				while(enums.hasMoreElements()) {
					String name = enums.nextElement();
					Object value = application.getAttribute(name);
					if(value instanceof Token) {
						Token token = (Token) value;
						if(token.getEnd() < System.currentTimeMillis()) {
							application.removeAttribute(name);
						}
					}
				}
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}

}
