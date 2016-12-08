package uw.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackTraceTools {

	private static final Logger logger = LoggerFactory.getLogger(StackTraceTools.class);

	/**
	 * key=异常名称 value=代码行数
	 */
	private ArrayList<String[]> map = new ArrayList<String[]>();

	/**
	 * 读入文件内容
	 * 
	 * @param filename
	 * @param encode
	 * @return
	 */
	public void analysisFile(String filename, String charset) {
		String exception = "";
		String stacktrace = "";
		try {
			File ff = new File(filename);
			InputStreamReader read = new InputStreamReader(new FileInputStream(ff), charset);
			BufferedReader ins = new BufferedReader(read);
			String dataLine = "";
			while (null != (dataLine = ins.readLine())) {
				if (dataLine.length() < 1)
					continue;
				if (dataLine.charAt(0) != '\t') {
					if (dataLine.indexOf("Exception") == -1 && dataLine.indexOf("Error") == -1) {
						continue;
					}
					
					// 此时说明是异常，放入map中。
					map.add(new String[] { exception, stacktrace });
					exception = dataLine.trim();
					stacktrace = "";
					logger.debug("正在处理异常" + exception);
				} else {
					if (dataLine.indexOf("at oracle.") == -1 && dataLine.indexOf("at com.caucho.") == -1 && dataLine.indexOf("at com.mysql.") == -1 && dataLine.indexOf("at sun.reflect.") == -1
							&& dataLine.indexOf("at com.mysql.") == -1 && dataLine.indexOf("at java.util.") == -1 && dataLine.indexOf("at java.net.") == -1 && dataLine.indexOf("at uw.dm.") == -1 && dataLine.indexOf("at java.lang.Thread") == -1) {
						stacktrace += dataLine + "|";
					}
				}
			}
			ins.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {

		}
	}

	/**
	 * 写回文件
	 * 
	 * @param filename
	 * @param content
	 */
	public void writeFile(String filename, String charset) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filename), charset);
			BufferedWriter br = new BufferedWriter(osw);
			for (String[] data : map) {
				if (data[1].length() > 1) {
					br.write("\"" + data[0] + "\"" + "," + "\"" + data[1] + "\"" + "\n");
				}
			}
			br.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
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
	 * @param accepttype
	 *            可以接受的文件后缀，通过,区分多个
	 * @param isbackup
	 *            是否备份
	 */
	public StackTraceTools(String inputFile, String incharset, String outputFile, String outcharset) {

		long start = System.currentTimeMillis();
		System.out.println("开始处理" + inputFile + "文件！");
		analysisFile(inputFile, incharset);
		System.out.println("读取编码:" + incharset);
		System.out.println("输出" + outputFile + "文件！");
		writeFile(outputFile, outcharset);
		System.out.println("输出编码:" + outcharset);
		System.out.println("全部处理完毕！" + (System.currentTimeMillis() - start));
	}

	public static void main(String[] args) {
		new StackTraceTools("D:/stderr.log", "utf-8", "D:/out.csv", "gbk");
	}

}
