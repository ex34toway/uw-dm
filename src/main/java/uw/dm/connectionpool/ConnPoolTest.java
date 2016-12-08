package uw.dm.connectionpool;

import uw.dm.DAOFactory;
import uw.dm.TransactionException;
import uw.dm.TransactionManager;

public class ConnPoolTest {

	public static void main(String[] args) {

		// 启动监控进程。
		new Thread() {
			@Override
			public void run() {
				while (true) {
					String sql = "select count(1) from v$session where username='SAAS31'";
					long count = 0;
					DAOFactory dao = DAOFactory.getInstance("saas02");
					TransactionManager tm = null;
					try {
						tm = dao.beginTransaction();
						count = dao.queryForCount(sql);
						tm.commit();
					} catch (TransactionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						try {
							tm.rollback();
						} catch (TransactionException e1) {
							e1.printStackTrace();
						}
					}
					System.err.println("当前连接数：" + count);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		for (int i = 0; i < 300; i++) {
			new Thread() {
				@Override
				public void run() {
					DAOFactory dao = DAOFactory.getInstance("saas31");
					TransactionManager tm = null;

					try {
						tm = dao.beginTransaction();
						long start = System.currentTimeMillis();
						dao.queryForCount("select count(1) from dual");
						System.out.println("执行时间为:" + (System.currentTimeMillis() - start));
						tm.commit();
					} catch (TransactionException e) {
						e.printStackTrace();
						if (tm != null) {
							try {
								tm.rollback();
							} catch (TransactionException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}.start();
			try {
				Thread.sleep((long) (Math.random() * 30));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
