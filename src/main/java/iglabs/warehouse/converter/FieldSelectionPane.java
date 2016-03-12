package iglabs.warehouse.converter;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class FieldSelectionPane extends JPanel {
	private static final long serialVersionUID = 1L;

	private static class DropDownInfo {
		public DropDownInfo(FieldSelectionPane pane, String id, String title, String[] matches) {
			this.pane = pane;
			this.id = id;
			this.title = title;
			this.matches = matches;
		
			comboBox = null;
			model = new DefaultComboBoxModel<String>();			
		}
		
		private FieldSelectionPane pane;
		private String id;
		private String title;
		private String[] matches;
		private JComboBox<String> comboBox;
		private DefaultComboBoxModel<String> model;
		
		public void addToContainer(Container container) {
			JLabel label = new JLabel(title + ":");
			container.add(label);
			
			JComboBox<String> comboBox = new JComboBox<>(model);
			comboBox.setPreferredSize(new Dimension(150, comboBox.getPreferredSize().height));
			container.add(comboBox);
			
			comboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pane.fireSelectionEvent();
				}
			});
			
			this.comboBox = comboBox;
		}
		
		public void setEnabled(boolean enabled) {
			comboBox.setEnabled(enabled);
		}
		
		public void setColumnNames(String[] columns) {
			model.removeAllElements();
			
			String selection = null;
			for (String column: columns) {
				model.addElement(column);
				
				if (selection == null) {
					for (String match: matches) {
						if (column.toUpperCase().contains(match.toUpperCase())) {
							selection = column;
							break;
						}
					}
				}
			}
			
			if (selection != null) {
				model.setSelectedItem(selection);
			}
			else {
				if (columns.length > 0) {
					model.setSelectedItem(columns[0]);
				}
			}
		}
		
		public Selection getSelection() {
			int columnIndex = comboBox.getSelectedIndex();
			return new Selection(id, columnIndex >= 0 ? Integer.valueOf(columnIndex) : null);
		}
	}
	
	public static class Selection {
		public Selection(String id, Integer columnIndex) {
			this.id = id;
			this.columnIndex = columnIndex;
		}
		
		private String id;
		private Integer columnIndex;
		
		public String getId() {
			return id;
		}
		
		public Integer getColumnIndex() {
			return columnIndex;
		}
	}

	
	public static final int ACTION_SELECTION	= 1;
	
	public static final String ID_CODE 			= "CODE";
	public static final String ID_NAME 			= "NAME";
	public static final String ID_DESCRIPTION 	= "DESCRIPTION";
	public static final String ID_UNIT			= "UNIT";
	public static final String ID_PRICE			= "PRICE";
	public static final String ID_QUANTITY		= "QUANTITY";
	
	
	private final DropDownInfo[] infos = new DropDownInfo[] {
		new DropDownInfo(this, ID_CODE, "Код", new String[] { "код", "code" }),
		new DropDownInfo(this, ID_NAME, "Наименование", new String[] { "название", "наименование", "name", "title" }),
		new DropDownInfo(this, ID_DESCRIPTION, "Описание", new String[] { "описание", "примечание", "комментарий", "desc" }),
		new DropDownInfo(this, ID_UNIT, "Ед.изм.", new String[] { "изм", "единица", "unit", "measure" }),
		new DropDownInfo(this, ID_PRICE, "Цена", new String[] { "цена", "стоимость", "price", "cost" }),
		new DropDownInfo(this, ID_QUANTITY, "Количество", new String[] { "колич", "кол-во", "quantity", "total", "stock" })
	};

	private ArrayList<ActionListener> selectionListeners = new ArrayList<ActionListener>();
	
	
	public FieldSelectionPane() {
		initView();
	}
	
	private void initView() {			
		GridLayout layout = new GridLayout(infos.length, 2);
		layout.setHgap(UI.CONTROLS_GAP);
		layout.setVgap(UI.CONTROLS_GAP);
		setLayout(layout);
		
		for (DropDownInfo info: infos) {
			info.addToContainer(this);
		}
	}
		
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		for (DropDownInfo info: infos) {
			info.setEnabled(enabled);
		}
	}
	
	public void setColumnNames(String[] columns) {
		for (DropDownInfo info: infos) {
			info.setColumnNames(columns);
		}
	}
	
	public List<Selection> getSelection() {
		ArrayList<Selection> result = new ArrayList<>();
		for (DropDownInfo info: infos) {
			result.add(info.getSelection());
		}
		
		return result;
	}
	
	public void addSelectionListener(ActionListener listener) {
		selectionListeners.add(listener);
	}
	
	private void fireSelectionEvent() {
		ActionEvent event = new ActionEvent(this, ACTION_SELECTION, null);
		
		for (ActionListener listener: selectionListeners) {
			listener.actionPerformed(event);
		}
	}
}
