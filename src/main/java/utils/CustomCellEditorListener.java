package utils;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

public class CustomCellEditorListener implements CellEditorListener {

	@Override
	public void editingCanceled(ChangeEvent arg0) {

		System.out.println("editing cancelled");

	}

	@Override
	public void editingStopped(ChangeEvent arg0) {

		System.out.println("editing stopped");

	}

}
