package statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import constants.Constants;
import constants.IntersportColors;
import contract.Contract;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import utils.Utils;

/** the class ShowStatistics */
public class ShowStatistics extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel panelSouth;
	private JComboBox<String> monthBox;
	List<Contract> lstContractContractedCreatedAt = null;
	List<String> lstCustomerContractedCreatedAt = null;

	private JPanel chartPanel;

	/**
	 * Constructor.
	 * 
	 * @param countOfMonth
	 *            - the count of Month
	 */
	public ShowStatistics(String countOfMonth) {

		initComponent(countOfMonth);

	}

	/**
	 * initializes this component for time period now - countOfMonth
	 * 
	 * @param countOfMonth
	 *            - the count of month
	 */
	private void initComponent(String countOfMonth) {
		int month = Utils.getMonth(countOfMonth);

		try {
			lstCustomerContractedCreatedAt = DatabaseLogic.getCustomersOfLastMonth(month);
			lstContractContractedCreatedAt = DatabaseLogic.getContractsOfLastMonth(month);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		chartPanel = createChartPanel(lstCustomerContractedCreatedAt, lstContractContractedCreatedAt, month);
		this.add(chartPanel, BorderLayout.NORTH);

		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_CORAL);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		monthBox = new JComboBox<String>(
				new String[] { Constants.ONE_MONTH, Constants.TWO_MONTHS, Constants.THREE_MONTHS, Constants.SIX_MONTHS,
						Constants.NINE_MONTHS, Constants.TWELVE_MONTHS, Constants.FIFTEEN_MONTHS,
						Constants.EIGHTEEN_MONTHS, Constants.TWENTY_ONE_MONTHS, Constants.TWENTY_FOUR_MONTHS });
		monthBox.setBackground(Color.GREEN);
		monthBox.setBounds(60, 20, 50, 25);
		monthBox.setBorder(BorderFactory.createRaisedBevelBorder());
		monthBox.setSelectedItem(Constants.SIX_MONTHS);
		monthBox.addActionListener(this);

		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(monthBox);

		this.add(panelSouth, BorderLayout.SOUTH);
	}

	private JPanel createChartPanel(List<String> lstCustomerContractedCreatedAt,
			List<Contract> lstContractContractedCreatedAt, int month) {
		String chartTitle = Constants.STATISTCS + Constants.COLON + Constants.EMPTY_SPACE + Constants.NEW_CUSTOMERS
				+ Constants.PIPE + Constants.CONTRACTED;
		String xAxisLabel = Constants.MONTHS;
		String yAxisLabel = Constants.COUNT_OF_ITEMS;

		Map<Integer, Integer> dayMappedToCountOfContractedCustomers = getOrderedCustomers(
				lstCustomerContractedCreatedAt, month);
		Map<Integer, Integer> dayMappedToCountOfContractedContracts = getOrderedContracts(
				lstContractContractedCreatedAt, month);

		CategoryDataset dataset = createDataset(dayMappedToCountOfContractedCustomers,
				dayMappedToCountOfContractedContracts, month);

		boolean showLegend = Boolean.TRUE;
		boolean createURL = Boolean.TRUE;
		boolean createTooltip = Boolean.FALSE;

		JFreeChart chart = ChartFactory.createBarChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

		// Assign it to the chart
		CategoryPlot plot = chart.getCategoryPlot();
		final NumberAxis xAxis = (NumberAxis) plot.getRangeAxis();
		xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return new ChartPanel(chart) {
			private static final long serialVersionUID = 1L;

			private int width = 1000;
			private int height = 735;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
	}

	private DefaultCategoryDataset createDataset(Map<Integer, Integer> dayMappedToCountOfContractedCustomers,
			Map<Integer, Integer> dayMappedToCountOfContractedContracts, int days) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int day = 0; day <= days; day++) {

			int countOfCustomers = dayMappedToCountOfContractedCustomers.get(day);
			int countOfContracts = dayMappedToCountOfContractedContracts.get(day);

			dataset.addValue(countOfCustomers, Constants.NEW_CUSTOMERS, String.valueOf(-days + day));
			dataset.addValue(countOfContracts, Constants.CONTRACTED, String.valueOf(-days + day));

		}

		return dataset;
	}

	/**
	 * yields the contracted customers mapped to the month of the contracted date
	 * 
	 * @param lstDateAsString
	 *            - the dates
	 * @param month
	 *            - the month
	 * @return - the ordered dates
	 */
	private Map<Integer, Integer> getOrderedCustomers(List<String> lstDateAsString, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -month);

		Map<Integer, Integer> response = new HashMap<Integer, Integer>();

		for (int i = 0; i <= month; i++) {

			if (i > 0) {
				calendar.add(Calendar.MONTH, 1);
			}

			String contractedDate = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());

			String contractedMonth = contractedDate.split("\\.")[1].trim();
			String contractedYear = contractedDate.split("\\.")[2].trim();

			Iterator<String> iterator = lstDateAsString.iterator();

			int countOfContractedinMonth = 0;

			while (iterator.hasNext()) {
				String tmpCreatedAt = iterator.next();
				String contractedAtDateTmp = tmpCreatedAt.split(Constants.EMPTY_SPACE)[0].trim();
				String contractedAtDateMonth = contractedAtDateTmp.split("\\.")[1].trim();
				String contractedAtDateYear = contractedAtDateTmp.split("\\.")[2].trim();

				if ((contractedAtDateMonth.equals(contractedMonth)) && (contractedAtDateYear.equals(contractedYear))) {
					countOfContractedinMonth++;

					iterator.remove();
				}
			}

			response.put(i, countOfContractedinMonth);
		}

		return response;
	}

	/**
	 * yields the contracted contracts mapped to the month of the contracted date
	 * 
	 * @param lstContract
	 *            - the contracts
	 * @param month
	 *            - the month
	 * @return - the ordered contracts
	 */
	private Map<Integer, Integer> getOrderedContracts(List<Contract> lstContract, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -month);

		Map<Integer, Integer> response = new HashMap<Integer, Integer>();

		for (int i = 0; i <= month; i++) {

			if (i > 0) {
				calendar.add(Calendar.MONTH, 1);
			}

			String contractedDate = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());

			String contractedMonth = contractedDate.split("\\.")[1].trim();
			String contractedYear = contractedDate.split("\\.")[2].trim();

			Iterator<Contract> iterator = lstContract.iterator();

			int countOfContractedinMonth = 0;

			while (iterator.hasNext()) {
				Contract contract = iterator.next();

				String tmpCreatedAt = contract.getCreatedAt();

				String contractedAtDateTmp = tmpCreatedAt.split(Constants.EMPTY_SPACE)[0].trim();
				String contractedAtDateMonth = contractedAtDateTmp.split("\\.")[1].trim();
				String contractedAtDateYear = contractedAtDateTmp.split("\\.")[2].trim();

				if ((contractedAtDateMonth.equals(contractedMonth)) && (contractedAtDateYear.equals(contractedYear))) {
					countOfContractedinMonth++;

					iterator.remove();
				}
			}

			response.put(i, countOfContractedinMonth);
		}

		return response;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object o = event.getSource();

		if (o.equals(monthBox)) {

			String value = (String) monthBox.getSelectedItem();

			int month = Utils.getMonth(value);

			try {
				lstCustomerContractedCreatedAt = DatabaseLogic.getCustomersOfLastMonth(month);
				lstContractContractedCreatedAt = DatabaseLogic.getContractsOfLastMonth(month);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			this.remove(chartPanel);

			chartPanel = createChartPanel(lstCustomerContractedCreatedAt, lstContractContractedCreatedAt, month);
			this.add(chartPanel, BorderLayout.NORTH);
			this.validate();

		}

	}

}
