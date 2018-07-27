package org.r.demo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class GetPicture {
 
	public void getHtmlPicture(String httpUrl, String filePath,String fromurl) {
		URL url;
		BufferedInputStream in;
		FileOutputStream file;
 
		try {
			System.out.println("爬取网络图片");
			// 获取图片名
			String fileName = httpUrl.substring(httpUrl.lastIndexOf("/")).replace("/", "");
		
			// 初始化url对象
			url = new URL(httpUrl);
			// 初始化in对象，也就是获得url字节流
			in = new BufferedInputStream(url.openStream());
	/*		File file2=null;
			this.inputstreamtofile(in,file2);
			System.out.println("qqqqqqqqqqqqqqqqqqqqqq"+file2);*/
			
			file = new FileOutputStream(new File(filePath + "\\" + fileName));
			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			file.close();
			in.close();
			//保存图片信息到数据库
			JDBCHelper helper = new JDBCHelper();
			helper.insertFilePath(fileName, filePath, fromurl);
			System.out.println("图片爬取成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public String getHtmlCode(String httpUrl) throws IOException {
		String content = "";
		URL url = new URL(httpUrl);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String input;
		// 如果有数据
		while ((input = reader.readLine()) != null) {
			// 将读取数据赋给content
			content += input;
		}
		// 关闭缓冲区
		reader.close();
		// 返回content
		return content;
	}
 
	/**
	 * 图片爬取方法
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void get(String url, String filePath) throws IOException {
 
		// 定义两个获取网页图片的正则表达式
		String searchImgReg = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
		String searchImgReg2 = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
 
		String content = this.getHtmlCode(url);
		Pattern pattern = Pattern.compile(searchImgReg);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			System.out.println(matcher.group(3));
			this.getHtmlPicture(url + "/" + matcher.group(3), filePath,url);
		}
		pattern = Pattern.compile(searchImgReg2);
		matcher = pattern.matcher(content);
		while (matcher.find()) {
			System.out.println(matcher.group(3));
			this.getHtmlPicture(matcher.group(3), filePath,url);
 
		}
 
	}
	
	
/*	public void inputstreamtofile(BufferedInputStream in,File file2) throws IOException{
		OutputStream os = new FileOutputStream(file2);
		   int bytesRead = 0;
		   byte[] buffer = new byte[8192];
		   while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
		      os.write(buffer, 0, bytesRead);
		   }
		   os.close();
		   in.close();
		}*/
}
