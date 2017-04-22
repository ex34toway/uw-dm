package uw.dm.test;

import uw.dm.DAOFactory;

public class Test {

	public static void main(String[] args) {
		DAOFactory dao = DAOFactory.getInstance();
		for (int i=0;i<100;i++){
			System.out.println(dao.getSequenceId("test"));
		}
	}

}
