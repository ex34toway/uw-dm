package uw.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载appInfo.xml,读取其中的应用信息
 * 
 * @author boreas
 * 
 */
public class AppInfoUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(AppInfoUtils.class);
	
	private static XMLConfiguration config;

	static {
		InputStream in = null;
		try {
			in = AppInfoUtils.class.getResourceAsStream("/appInfo.xml");
			config = new XMLConfiguration();
			config.load(in, "utf-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 获取应用名称
	 * 
	 * @return
	 */
	public static String getAppName() {
		return config.getString("app.appName");
	}

	/**
	 * 获取应用版本
	 * 
	 * @return
	 */
	public static String getAppVersion() {
		return config.getString("app.appVersion");
	}

	/**
	 * 获取应用部署时间
	 * 
	 * @return
	 */
	public static String getDeploymentDate() {
		return config.getString("app.deploymentDate");
	}

	/**
	 * 获取应用部署路径
	 * 
	 * @return
	 */
	public static String getDeploymentPath() {
		return config.getString("app.deploymentPath");
	}
}
