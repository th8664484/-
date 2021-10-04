package com.AuthorityManagement.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet( urlPatterns = "/generatcheckcode")
public class GeneratCheckcodeController extends HttpServlet{

	private static final String source = "0123456789ASDFGHJKLQWERTYUIOPZXCVBNM" ;//验证码来源
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = "" ;//装载验证码
		Random r = new Random() ;
               
      //---------使用GUI画一个验证码图片-----------
        BufferedImage image = new BufferedImage(80, 30,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Font font = new Font("Times New Roman", Font.PLAIN, 20); //设置字体
        g.setFont(font);
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, 80, 30);
        g.setColor(Color.black); //设置字体色
        
     // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
        int x = r.nextInt(80);
        int y = r.nextInt(30);
        int xl = r.nextInt(12);
        int yl = r.nextInt(12);
        g.drawLine(x, y, x + xl, y + yl);
        }
        
        for (int i = 0; i < 4; i++) {
        	int index = r.nextInt(source.length());
            String c = source.charAt(index)+"";
            code += c ;
        	 // 将认证码显示到图象中
        	 g.setColor(new Color(20 + r.nextInt(110),
        	 20 + r.nextInt(110),
        	 20 + r.nextInt(110))); //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
        	 g.drawString(c, 15 * i + 6, 22);
        }
        
        request.getSession().setAttribute("code",code);
        
        ImageIO.write(image,"jpg",response.getOutputStream());
	}
	
	static Color getRandColor(int fc, int bc) { //给定范围获得随机颜色
		 Random random = new Random();
		 if (fc > 255)
		 fc = 255;
		 if (bc > 255)
		 bc = 255;
		 int r = fc + random.nextInt(bc - fc);
		 int g = fc + random.nextInt(bc - fc);
		 int b = fc + random.nextInt(bc - fc);
		 return new Color(r, g, b);
		}
}
