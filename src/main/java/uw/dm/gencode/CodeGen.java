package uw.dm.gencode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import uw.dm.util.DmStringUtils;

public class CodeGen {

	private static final Logger logger = LoggerFactory.getLogger(CodeGen.class);

	public static String SYSTEM_ENCODING = "UTF-8";

	public static String SOURCECODE_PATH = "D:/work_leleku/dm/src/main/java";

	public static String PACKAGE_NAME = "uw.dm.entity";

	public static String CONN_NAME = "";

	public static String TABLE_LIST = "";

	public static void main(String[] args) throws Exception {
		logger.info("开始代码生成...");
		logger.info("CONN_NAME={}", CONN_NAME);
		logger.info("SOURCECODE_PATH={}", SOURCECODE_PATH);
		logger.info("PACKAGE_NAME={}", PACKAGE_NAME);
		logger.info("SYSTEM_ENCODING={}", SYSTEM_ENCODING);
		init();
		process();
		logger.info("代码生成完成...");
	}

	private static Configuration cfg;

	private static void init() throws Exception {
		// 初始化FreeMarker配置
		// 创建一个Configuration实例
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setClassForTemplateLoading(CodeGen.class, "/uw/dm/gencode/");// 类路径
	}

	private static void process() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String author = "axeon";
		map.put("author", author);
		map.put("date", new Date());
		map.put("package", PACKAGE_NAME);
		HashSet<String> set = new HashSet<String>();
		if (TABLE_LIST != null && TABLE_LIST.equals("")) {
			String[] ts = TABLE_LIST.split(",");
			for (String t : ts) {
				set.add(t);
			}
		}
		List<MetaTableInfo> tablelist = DataMetaUtils.getTablesAndViews(set);
		for (MetaTableInfo tmeta : tablelist) {
			map.put("tableMeta", tmeta);

			// 获得主键列表
			List<MetaPrimaryKeyInfo> pklist = DataMetaUtils.getPrimaryKey(tmeta.getTableName());
			map.put("pkList", pklist);

			// 获得列列表
			List<MetaColumnInfo> columnlist = DataMetaUtils.getColumnList(tmeta.getTableName(), pklist);
			map.put("columnList", columnlist);

			String fileName = DmStringUtils.toUpperFirst(tmeta.getEntityName()) + ".java";
			String savePath = SOURCECODE_PATH + "/" + PACKAGE_NAME.replaceAll("\\.", "/") + "/";
			Template template = cfg.getTemplate("pojo.ftl");
			buildTemplate(template, map, savePath, fileName);
			break;
		}
	}

	private static void buildTemplate(Template template, Map root, String savePath, String fileName) {
		String realFileName = savePath + fileName;
		String realSavePath = savePath;
		File newsDir = new File(realSavePath);
		if (!newsDir.exists()) {
			newsDir.mkdirs();
		}

		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(realFileName), SYSTEM_ENCODING);
			template.process(root, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
