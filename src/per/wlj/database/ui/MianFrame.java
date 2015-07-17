package per.wlj.database.ui;

import java.awt.Menu;
import java.awt.MenuBar;

import javax.swing.JFrame;

public class MianFrame extends JFrame{
	
	private static final long serialVersionUID = 916726339807310198L;
	
	
	public static void main(String[] args) {
		MianFrame mf = new MianFrame();
		MenuBar mb = new MenuBar();
		mb.add(new Menu("file"));
		mf.setMenuBar(mb);
//		JPanel d ;
		mf.setBounds(0, 0, 100, 100);
		mf.doLayout();
		mf.setVisible(true);
	}
}
