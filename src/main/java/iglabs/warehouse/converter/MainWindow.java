package iglabs.warehouse.converter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private final AppContext appContext;
	private FieldSelectionPane fieldSelectionPane;
	private JButton createCsvButton;
	private DataTable selectedData = null;

	public MainWindow(AppContext appContext) {
		this.appContext = appContext;
		
		setTitle("Склад-Ассистент конвертер");
		setResizable(false);
		
		initView();
		initEventListeners();
				
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initView() {
		JPanel rootPane = new JPanel();
		rootPane.setBorder(
			new EmptyBorder(
				UI.CONTROLS_GAP,
				UI.CONTROLS_GAP,
				UI.CONTROLS_GAP,
				UI.CONTROLS_GAP
			)
		);
		
		GridBagLayout layout = new GridBagLayout();
		rootPane.setLayout(layout);
		setContentPane(rootPane);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
				
		initSelectFileButton(c);
		initFieldSelectionPanel(c);
		initCreateCsvButton(c);
	}
	
	private void initSelectFileButton(GridBagConstraints c) {
		JButton selectFileButton = new JButton("Выбрать XLSX-файл");
		UI.applyButtonStyle(selectFileButton);
		
		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFileButtonClick();
			}
		});
		
		c.gridy = 0;
		c.weightx = 1;
		
		getContentPane().add(selectFileButton, c);
	}
	
	private void initFieldSelectionPanel(GridBagConstraints c) {
		fieldSelectionPane = new FieldSelectionPane();
		fieldSelectionPane.setEnabled(false);
		
		fieldSelectionPane.addSelectionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fieldsSelected();
			}
		});
		
		c.insets = new Insets(UI.CONTROLS_GAP, 0, 0, 0);
		c.gridy = 1;
		c.weightx = 1;
		
		getContentPane().add(fieldSelectionPane, c);
	}
	
	private void initCreateCsvButton(GridBagConstraints c) {
		createCsvButton = new JButton("Сгенерировать CSV");
		UI.applyButtonStyle(createCsvButton);
		createCsvButton.setEnabled(false);
		
		createCsvButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createCsv();
			}
		});
		
		c.insets = new Insets(UI.CONTROLS_GAP, 0, 0, 0);
		c.gridy = 2;
		c.weightx = 1;
		
		getContentPane().add(createCsvButton, c);
	}
	
	private void initEventListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	private void selectFileButtonClick() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		for (FileFilter filter: filters) {
			fileChooser.removeChoosableFileFilter(filter);
		}
		
		FileFilter filter = new FileNameExtensionFilter("Файлы Microsoft Excel", "xlsx");
		fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				selectedData = appContext.getExcelReader()
						.read(fileChooser.getSelectedFile().getAbsolutePath());
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(
					this,
					"Не удалось загрузить Excel-файл, причина: " + ex.getMessage()
				);
				
				return;
			}
			
			assignHeaders();
		}
	}
	
	private void fieldsSelected() {
		boolean allSelected = true;
		List<FieldSelectionPane.Selection> selection = fieldSelectionPane.getSelection();
		for (FieldSelectionPane.Selection item: selection) {
			if (item.getColumnIndex() == null) {
				allSelected = false;
			}
		}
		
		createCsvButton.setEnabled(allSelected);
	}
	
	private void assignHeaders() {
		fieldSelectionPane.setColumnNames(selectedData.getColumns());
		fieldSelectionPane.setEnabled(true);
	}
	
	private int[] extractColumnMap() {
		List<FieldSelectionPane.Selection> selection = fieldSelectionPane.getSelection();
		int[] result = new int[selection.size()];
		int i = 0;
		
		for (FieldSelectionPane.Selection item: selection) {
			if (item.getColumnIndex() == null) {
				throw new RuntimeException("Не задано соответствие столбцов.");
			}
			
			result[i] = item.getColumnIndex();
			++i;
		}
		
		return result;
	}
	
	private void createCsv() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		for (FileFilter filter: filters) {
			fileChooser.removeChoosableFileFilter(filter);
		}
		
		FileFilter filter = new FileNameExtensionFilter("Файлы CSV", "csv");
		fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				appContext.getCsvWriter().write(
					fileChooser.getSelectedFile().getAbsolutePath(),
					selectedData,
					extractColumnMap()
				);
				
				JOptionPane.showMessageDialog(this, "CSV-файл успешно создан.");
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(
					this,
					"Не записать CSV-файл, причина: " + ex.getMessage()
				);				
			}
		}		
	}
}
