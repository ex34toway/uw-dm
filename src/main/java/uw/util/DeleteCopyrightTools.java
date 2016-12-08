package uw.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除版权标示的工具类。
 * @author axeon
 *
 */
public class DeleteCopyrightTools {

	private static final Logger logger = LoggerFactory.getLogger(DeleteCopyrightTools.class);
	
	/**
	 * 读入文件内容
	 * 
	 * @param filename
	 * @param encode
	 * @return
	 */
	public String readFile(String filename, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			File ff = new File(filename);
			InputStreamReader read = new InputStreamReader(new FileInputStream(ff), charset);
			BufferedReader ins = new BufferedReader(read);
			String dataLine = "";
			while (null != (dataLine = ins.readLine())) {
				sb.append(dataLine);
				sb.append("\r\n");
			}
			ins.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return sb.toString();
	}

	/**
	 * 写回文件
	 * 
	 * @param filename
	 * @param content
	 */
	public void writeFile(String filename, String content, String charset) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filename), charset);
			BufferedWriter br = new BufferedWriter(osw);
			br.write(content);
			br.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 备份文件
	 * 
	 * @param filename
	 */
	public void backupFile(String filename) {
		File file = new File(filename);
		file.renameTo(new File(filename + ".bak"));
	}

	/**
	 * 递归
	 * 
	 * @param path
	 */
	public void recursive(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getAbsolutePath();
			if (files[i].isDirectory()) {
				recursive(filename);
			} else {
				if (this.typeMap.containsKey(getSuffix(filename))) {
					currTimestamp = System.currentTimeMillis();
					System.out.println("正在处理" + filename);
					String content = readFile(filename, charset);
					if (isbackup)
						backupFile(filename);
					content = deleteCopyright(content);
					writeFile(filename, content, charset);
					System.out.println("消耗时间" + (System.currentTimeMillis() - currTimestamp));
				}
			}
		}
	}
	
	public String deleteCopyright(String content){
		int pos = content.indexOf("package ");
		if (pos>-1){
			content = content.substring(pos);
		}
		return content;
	}

	/**
	 * 获得文件后缀
	 * 
	 * @param filename
	 * @return
	 */
	public String getSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	public void initTypeMap(String accepttype) {
		String[] types = accepttype.split(",");
		for (int i = 0; i < types.length; i++) {
			typeMap.put(types[i], "");
		}
	}

	/**
	 * 构造器
	 * 
	 * @param path
	 *            要处理的目录
	 * @param incharset
	 *            读取的编码
	 * @param outcharset
	 *            输出的编码
	 * @param acceptType
	 *            可以接受的文件后缀，通过,区分多个
	 * @param isbackup
	 *            是否备份
	 */
	public DeleteCopyrightTools(String path, String charset , String acceptType, boolean isbackup) {
		this.charset = charset;
		initTypeMap(acceptType);// 初始化文件后缀map
		System.out.println("开始处理" + path + "目录下后缀为" + acceptType + "的文件！");
		System.out.println("======================================================");
		recursive(path);// 开始处理
		System.out.println("======================================================");
		System.out.println("全部处理完毕！");
	}

	private String charset ="utf-8";
	private HashMap<String, String> typeMap = new HashMap<String, String>();
	private boolean isbackup;
	private long currTimestamp;

	public static void main(String[] args) {
		new DeleteCopyrightTools("D:/work_ngn/haokan/src","utf-8", "java", false);
	}

}
