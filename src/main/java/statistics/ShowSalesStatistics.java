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

/** the class ShowSalesStatistics */
public class ShowSalesStatistics extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel panelSouth;
	private JComboBox<String> monthBox;
	List<Contract> lstContract = null;

	/** the inner class SumsOfContracts */
	private class SumsOfContracts {

		private Double uvp = .0;

		private Double houseprice = .0;

		public void addUvp(Double uvp) {
			this.uvp += uvp;
		}

		public void addHouseprice(Double houseprice) {
			this.houseprice += houseprice;
		}

		// get & set will follow below here
		public Double getUvp() {
			return uvp;
		}

		public Double getHouseprice() {
			return houseprice;
		}

	}

	private JPanel chartPanel;

	/**
	 * Constructor.
	 * 
	 * @param countOfMonth
	 *            - the count of Month
	 */
	public ShowSalesStatistics(String countOfMonth) {

		initComponent(countOfMonth);
	}

	/**
	 * initializes this component for time period now - countOfMonth
	 * 
	 * @param countOfMonths
	 *            - describes the time period: start=now- countOfMonth;end=now
	 */
	private void initComponent(String countOfMonth) {
		int month = Utils.getMonth(countOfMonth);

		try {
			lstContract = DatabaseLogic.getContractsOfLastMonth(month);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		lstContract.forEach(contract -> {
			try {
				contract.setSellPositions(DatabaseLogic.getSellPositions(contract.getIdOfContract()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		chartPanel = createChartPanel(lstContract, month);
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

	/**
	 * creates the chartpanel for the contracts that should be analysed
	 * 
	 * @param lstContract
	 *            the contracts that should be analysed
	 * @param countOfMonths
	 *            - describes the time period: start=now- countOfMonth;end=now
	 * @return
	 */
	private JPanel createChartPanel(List<Contract> lstContract, int countOfMonths) {
		String chartTitle = Constants.SALES_STATISTCS + Constants.COLON + Constants.EMPTY_SPACE + Constants.HOUSE_PRICE
				+ Constants.PIPE + Constants.UVP;
		String xAxisLabel = Constants.MONTHS;
		String yAxisLabel = Constants.SUMS_OF_CONTRACTS;

		Map<Integer, ShowSalesStatistics.SumsOfContracts> monthMappedToSumsOfContractedContracts = getOrderedSumsOfContracts(
				lstContract, countOfMonths);

		CategoryDataset dataset = createDataset(monthMappedToSumsOfContractedContracts, countOfMonths);

		boolean showLegend = Boolean.TRUE;
		boolean createURL = Boolean.TRUE;
		boolean createTooltip = Boolean.FALSE;

		JFreeChart chart = ChartFactory.createBarChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

		// Assign it to the chart
		CategoryPlot plot = chart.getCategoryPlot();
		final NumberAxis xAxis = (NumberAxis) plot.getRangeAxis();
		xAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

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

	/**
	 * creates the dataset: month to corresponding houseprice and uvp
	 * 
	 * @param monthMappedToSumsOfContractedContracts
	 *            - months to corresponding houseprices and uvps
	 * @param months
	 *            - the count of months
	 * @return
	 */
	private DefaultCategoryDataset createDataset(
			Map<Integer, ShowSalesStatistics.SumsOfContracts> monthMappedToSumsOfContractedContracts, int months) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int month = 0; month <= months; month++) {

			SumsOfContracts sumsOfContracts = monthMappedToSumsOfContractedContracts.get(month);

			dataset.addValue(sumsOfContracts.getHouseprice(), Constants.HOUSE_PRICE, String.valueOf(-months + month));
			dataset.addValue(sumsOfContracts.getUvp(), Constants.UVP, String.valueOf(-months + month));

		}

		return dataset;
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
	private Map<Integer, ShowSalesStatistics.SumsOfContracts> getOrderedSumsOfContracts(List<Contract> lstContract,
			int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -month);

		Map<Integer, ShowSalesStatistics.SumsOfContracts> response = new HashMap<Integer, ShowSalesStatistics.SumsOfContracts>();

		for (int i = 0; i <= month; i++) {

			if (i > 0) {
				calendar.add(Calendar.MONTH, 1);
			}

			String contractedDate = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());
			String contractedMonth = contractedDate.split("\\.")[1].trim();
			String contractedYear = contractedDate.split("\\.")[2].trim();

			Iterator<Contract> iterator = lstContract.iterator();

			ShowSalesStatistics.SumsOfContracts sumsOfContracts = new ShowSalesStatistics.SumsOfContracts();

			while (iterator.hasNext()) {
				Contract contract = iterator.next();

				String tmpCreatedAt = contract.getCreatedAt();

				String contractedAtDateTmp = tmpCreatedAt.split(Constants.EMPTY_SPACE)[0].trim();
				String contractedAtDateMonth = contractedAtDateTmp.split("\\.")[1].trim();
				String contractedAtDateYear = contractedAtDateTmp.split("\\.")[2].trim();

				if ((contractedAtDateMonth.equals(contractedMonth)) && (contractedAtDateYear.equals(contractedYear))) {

					Double uvp = Utils.getTotalUvpOfContract(contract);
					Double housePrice = Utils.getTotalHousepriceOfContract(contract);

					sumsOfContracts.addUvp(uvp);
					sumsOfContracts.addHouseprice(housePrice);

					iterator.remove();
				}
			}

			response.put(i, sumsOfContracts);
		}

		return response;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object o = event.getSource();

		if (o.equals(monthBox)) {

			String value = (String) monthBox.getSelectedItem();

			processContracts(value);
		}

	}

	private void processContracts(String value) {

		int month = Utils.getMonth(value);

		try {
			lstContract = DatabaseLogic.getContractsOfLastMonth(month);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lstContract.forEach(contract -> {
			try {
				contract.setSellPositions(DatabaseLogic.getSellPositions(contract.getIdOfContract()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		this.remove(chartPanel);
		chartPanel = createChartPanel(lstContract, month);
		this.add(chartPanel, BorderLayout.NORTH);
		this.validate();

	}

}
