package uw.dm.test;

import uw.dm.DAOFactory;
import uw.dm.TransactionException;
import uw.dm.entity.MscPerm;
import uw.dm.performance.StatsLogService;

public class Test {

	public static void main(String[] args) throws TransactionException {
		try {
			StatsLogService.start();
			DAOFactory dao = DAOFactory.getInstance();
			MscPerm mp = dao.load(MscPerm.class, 150);
			System.out.println(mp);
			mp.setModifyDate(new java.util.Date());
			dao.save("test", mp);
			System.out.println(mp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mp.setId(1000);
		// mp.setModifyDate(new java.util.Date());
		// dao.save(mp);
		// DataList<MscPerm> list = dao.list(MscPerm.class, "select * from
		// msc_perm");
		// dao.list(MscPerm.class, "select * from msc_perm");
		// dao.list(MscPerm.class, "select * from msc_perm");
		// dao.list(MscPerm.class, "select * from msc_perm");
		// for (MscPerm mp:list){
		// System.out.println(mp);
		// }
		// try {
		// Thread.sleep(1000000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

	}

}
