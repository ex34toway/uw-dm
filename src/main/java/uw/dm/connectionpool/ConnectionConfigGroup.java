package uw.dm.connectionpool;

/**
 * 连接配置组信息。
 * @author axeon
 *
 */
public class ConnectionConfigGroup {

	/**
	 * 组名
	 */
	private String name;

	/**
	 * 使用的算法
	 */
	private String algorithm;

	/**
	 * 连接名组
	 */
	private String[] poolNames;

	/**
	 * 使用roundrobin时使用的。
	 */
	private int pos = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String[] getPoolNames() {
		return poolNames;
	}

	public void setPoolNames(String[] poolNames) {
		this.poolNames = poolNames;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
