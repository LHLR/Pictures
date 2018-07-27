package org.r.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GetWeb {
	private int webDepth = 5; // �������
	private int intThreadNum = 1; // �߳���
	private String strHomePage = ""; // ��ҳ��ַ
	private String myDomain; // ����
	private String fPath = "C:/Baidu"; // ������ҳ�ļ���Ŀ¼��
	private ArrayList<String> arrUrls = new ArrayList<String>(); // �洢δ����URL
	private ArrayList<String> arrUrl = new ArrayList<String>(); // �洢����URL����������
	private Hashtable<String, Integer> allUrls = new Hashtable<String, Integer>(); // �洢����URL����ҳ��
	private Hashtable<String, Integer> deepUrls = new Hashtable<String, Integer>(); // �洢����URL���
	private int intWebIndex = 0; // ��ҳ��Ӧ�ļ��±꣬��0��ʼ
	private long startTime;
	private int webSuccessed = 0;
	private int webFailed = 0;
	
	public static void main(String[] args) {
		GetWeb gw = new GetWeb("http://www.baidu.com/");
		gw.getWebByHomePage();
	}
	
	public GetWeb(String s) {
		this.strHomePage = s;
	}
 
	public GetWeb(String s, int i) {
		this.strHomePage = s;
		this.webDepth = i;
	}
 
	public synchronized void addWebSuccessed() {
		webSuccessed++;
	}
 
	public synchronized void addWebFailed() {
		webFailed++;
	}
 
	public synchronized String getAUrl() {
		String tmpAUrl = arrUrls.get(0);
		arrUrls.remove(0);
		return tmpAUrl;
	}
 
	public synchronized String getUrl() {
		String tmpUrl = arrUrl.get(0);
		arrUrl.remove(0);
		return tmpUrl;
	}
 
	public synchronized Integer getIntWebIndex() {
		intWebIndex++;
		return intWebIndex;
	}
 
	
 
	/**
	 * ���û��ṩ������վ�㿪ʼ������������ҳ�����ץȡ
	 */
	public void getWebByHomePage() {
		startTime = System.currentTimeMillis();
		this.myDomain = getDomain();
		if (myDomain == null) {
			System.out.println("Wrong input!");
			return;
		}
 
		System.out.println("Homepage����ҳ��ַ�� = " + strHomePage);
		System.out.println("Domain �������� = " + myDomain);
		arrUrls.add(strHomePage);
		arrUrl.add(strHomePage);
		allUrls.put(strHomePage, 0);
		deepUrls.put(strHomePage, 1);
 
		File fDir = new File(fPath);
		if (!fDir.exists()) {
			fDir.mkdir();
		}
 
		System.out.println("��ʼ����");
		String tmp = getAUrl(); // ȡ���µ�URL
		this.getWebByUrl(tmp, allUrls.get(tmp) + ""); // ����URL����Ӧ����ҳ����ץȡ
		int i = 0;
		for (i = 0; i < intThreadNum; i++) {
			new Thread(new Processer(this)).start();
		}
		while (true) {
			if (arrUrls.isEmpty() && Thread.activeCount() == 1) {
				long finishTime = System.currentTimeMillis();
				long costTime = finishTime - startTime;
				System.out.println("\n\n\n\n\n���");
				System.out.println(
						"��ʼʱ�� = " + startTime + "   " + "����ʱ�� = " + finishTime + "   " + "��ȡ��ʱ��= " + costTime + "ms");
				System.out.println("��ȡ��URL���� = " + (webSuccessed + webFailed) + "   �ɹ���URL����: " + webSuccessed
						+ "   ʧ�ܵ�URL����: " + webFailed);
				String strIndex = "";
				String tmpUrl = "";
				while (!arrUrl.isEmpty()) {
					tmpUrl = getUrl();
					strIndex += "Web depth:" + deepUrls.get(tmpUrl) + "   Filepath: " + fPath + "/web"
							+ allUrls.get(tmpUrl) + ".htm" + "url:" + tmpUrl + "\n\n";
				}
				System.out.println(strIndex);
				try {
					PrintWriter pwIndex = new PrintWriter(new FileOutputStream("fileindex.txt"));
					pwIndex.println(strIndex);
					pwIndex.close();
				} catch (Exception e) {
					System.out.println("���������ļ�ʧ��!");
				}
				break;
			}
		}
	}
 
	/**
	 * �Ժ�����������վ������ȡ
	 * 
	 * @param strUrl
	 * @param fileIndex
	 */
	public void getWebByUrl(String strUrl, String fileIndex) {
		try {
			System.out.println("ͨ��URL�õ���վ: " + strUrl);
 
			URL url = new URL(strUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			InputStream is = null;
			is = url.openStream();
			String filename = strUrl.replaceAll("/", "_");
			filename = filename.replace(":", ".");
			if (filename.indexOf("*") > 0) {
				filename = filename.replaceAll("*", ".");
			}
			if (filename.indexOf("?") > 0) {
				filename = filename.replaceAll("?", ".");
			}
			if (filename.indexOf("\"") > 0) {
				filename = filename.replaceAll("\"", ".");
			}
			if (filename.indexOf(">") > 0) {
				filename = filename.replaceAll(">", ".");
			}
			if (filename.indexOf("<") > 0) {
				filename = filename.replaceAll("<", ".");
			}
			if (filename.indexOf("|") > 0) {
				filename = filename.replaceAll("|", ".");
			}
			String filePath = fPath + "\\" + filename;
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		/*	JDBCHelper helper = new JDBCHelper();
			helper.insertFilePath(filename, filePath, strUrl);*/
			GetPicture getp = new GetPicture();
			getp.get(strUrl, filePath);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String rLine = null;
			String tmp_rLine = null;
			while ((rLine = bReader.readLine()) != null) {
				tmp_rLine = rLine;
				int str_len = tmp_rLine.length();
				if (str_len > 0) {
					sb.append("\n" + tmp_rLine);
					if (deepUrls.get(strUrl) < webDepth)
						getUrlByString(tmp_rLine, strUrl);
				}
				tmp_rLine = null;
			}
			is.close();
			System.out.println("��ȡ��վ�ɹ� " + strUrl);
			addWebSuccessed();
		} catch (Exception e) {
			System.out.println("��ȡ��վʧ�ܣ�����URL�Ƿ���� " + strUrl);
			addWebFailed();
		}
	}
 
	/**
	 * �ж��û����ṩURL�Ƿ�Ϊ������ַ
	 * 
	 * @return
	 */
	public String getDomain() {
		String reg = "(?<=http\\://[a-zA-Z0-9]{0,100}[.]{0,1})[^.\\s]*?\\.(com|cn|net|org|biz|info|cc|tv|edu)";
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(strHomePage);
		boolean blnp = m.find();
		if (blnp == true) {
			return m.group(0);
		}
		return null;
	}
 
	/**
	 * �����µ���ҳ����ȡ���к��е�������Ϣ
	 * 
	 * @param inputArgs
	 * @param strUrl
	 */
	public void getUrlByString(String inputArgs, String strUrl) {
		String tmpStr = inputArgs;
		String regUrl = "(?<=(href=)[\"]?[\']?)[http://][^\\s\"\'\\?]*(" + myDomain + ")[^\\s\"\'>]*";
		Pattern p = Pattern.compile(regUrl, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(tmpStr);
		boolean blnp = m.find();
		while (blnp == true) {
			if (!allUrls.containsKey(m.group(0))) {
				System.out.println("Find a new url,depth:" + (deepUrls.get(strUrl) + 1) + " " + m.group(0));
				arrUrls.add(m.group(0));
				arrUrl.add(m.group(0));
				allUrls.put(m.group(0), getIntWebIndex());
				deepUrls.put(m.group(0), (deepUrls.get(strUrl) + 1));
			}
			tmpStr = tmpStr.substring(m.end(), tmpStr.length());
			m = p.matcher(tmpStr);
			blnp = m.find();
		}
	}
 
	/**
	 * @author amuxia ��һ����������ȡ�߳�
	 */
	class Processer implements Runnable {
		GetWeb gw;
 
		public Processer(GetWeb g) {
			this.gw = g;
		}
 
		public void run() {
			while (!arrUrls.isEmpty()) {
				String tmp = getAUrl();
				getWebByUrl(tmp, allUrls.get(tmp) + "");
			}
		}
	}
}
