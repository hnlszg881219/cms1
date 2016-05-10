package com.banhui.cms.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CaptchaCodeService {
	Logger logger = Logger.getLogger(CaptchaCodeService.class);
	
	 // set width
	 private int width = 70;
	 // set height
	 private int height = 20;
	 // count
	 private int codeCount = 5;
	 private int xx = 10;
	 private int fontHeight = 18;
	 private int codeY = 16;
	/* char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
	      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
	      'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };*/
	 char[] codeSequence = {'1', '2', '3', '4', '5', '6', '7', '8', '9' };

	  @RequestMapping("/code")
	  public void getCode(HttpServletRequest req, HttpServletResponse resp)
	      throws IOException {

	    // 
	    BufferedImage buffImg = new BufferedImage(width, height,
	        BufferedImage.TYPE_INT_RGB);
	    Graphics gd = buffImg.getGraphics();
	    // 
	    Random random = new Random();
	    // 
	    gd.setColor(Color.WHITE);
	    gd.fillRect(0, 0, width, height);

	    // 
	    Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
	    // 
	    gd.setFont(font);

	    // 
	    gd.setColor(Color.BLACK);
	    gd.drawRect(0, 0, width - 1, height - 1);

	    // 
	    gd.setColor(Color.BLACK);
	    /*for (int i = 0; i < 40; i++) {
	      int x = random.nextInt(width);
	      int y = random.nextInt(height);
	      int xl = random.nextInt(12);
	      int yl = random.nextInt(12);
	      gd.drawLine(x, y, x + xl, y + yl);
	    }*/

	    // 
	    StringBuffer randomCode = new StringBuffer();
	    int red = 0, green = 0, blue = 0;
	    // 
	    for (int i = 0; i < codeCount; i++) {
	      // 
	      String code = String.valueOf(codeSequence[random.nextInt(9)]);
	      // 
	      red = random.nextInt(255);
	      green = random.nextInt(255);
	      blue = random.nextInt(255);
          if(i==2){
        	  code = "+";
        	  red = 0;
    	      green = 0;
    	      blue = 0;
          }
	      // 
	      gd.setColor(new Color(red, green, blue));
	      gd.drawString(code, (i + 1) * xx, codeY);

	      // 
	      randomCode.append(code);
	    }
	    int partner1 = Integer.parseInt(randomCode.toString().substring(0, 2));
        int partner2 = Integer.parseInt(randomCode.toString().substring(3, 5));
	    // 
	    HttpSession session = req.getSession();
	    //System.out.print(randomCode);
	    session.setAttribute("code", (partner1+partner2)+"");

	    // 
	    resp.setHeader("Pragma", "no-cache");
	    resp.setHeader("Cache-Control", "no-cache");
	    resp.setDateHeader("Expires", 0);

	    resp.setContentType("image/jpeg");

	    // 
	    ServletOutputStream sos = resp.getOutputStream();
	    ImageIO.write(buffImg, "jpeg", sos);
	    sos.close();
	  }
}
