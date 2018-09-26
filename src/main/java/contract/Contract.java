package contract;

import java.util.List;

import bikeData.BikeData;
import seller.Seller;

/** the class Contract */
public class Contract {

	private int idOfContract;
	private String createdAt;
	private String lastModified;
	private int idOfCustomer;

	/** uvp parts */
	private Double deliveryAssemblyFeeUVP;
	private Double serviceCostGarageUVP;
	private Double redemptionDismantledPartsWheelUVP;
	private Double downPaymentUVP;
	private Double estateUVP;

	/** houseprice parts */
	private Double deliveryAssemblyFeeHousePrice;
	private Double serviceCostGarageHousePrice;
	private Double redemptionDismantledPartsWheelHousePrice;
	private Double downPaymentHousePrice;
	private Double estateHousePrice;

	/** sellpositions */
	private List<SellPosition> lstSellposition;

	/** the bike data */
	private BikeData bikeData;

	private Seller seller;
	private Integer isOnlineShop;

	/**
	 * @param idOfContract
	 *            - the contract's id
	 * 
	 * @param createdAt
	 *            - the date of created
	 * @param lastModified
	 *            - the date of last modified
	 * @param deliveryAssemblyFeeUVP
	 *            - the uvp of delivery assembly fee
	 * @param serviceCostGarageUVP
	 *            - the uvp of service and garage costs
	 * @param redemptionDismantledPartsWheelUVP
	 *            - the uvp of redemption of dismantled parts and wheels
	 * @param downPaymentUVP
	 *            - uvp of the downPayment
	 * @param estateUVP
	 *            - the uvp of estate
	 * @param deliveryAssemblyFeeHousePrice
	 *            - the houseprice of delivery assembly fee
	 * @param serviceCostGarageHousePrice
	 *            - the houseprice of service and garage costs
	 * @param redemptionDismantledPartsWheelHousePrice
	 *            - the houseprice of redemption of dismantled parts and wheels
	 * @param downPaymentHousePrice
	 *            - houseprice of the downPayment
	 * @param estateHousePrice
	 *            - the houseprice of estate
	 * @param Integer
	 *            isOnlineShop - using ONLINESHOP or ADVARICS
	 */
	public Contract(int idOfContract, String createdAt, String lastModified, Double deliveryAssemblyFeeUVP,
			Double serviceCostGarageUVP, Double redemptionDismantledPartsWheelUVP, Double downPaymentUVP,
			Double estateUVP, Double deliveryAssemblyFeeHousePrice, Double serviceCostGarageHousePrice,
			Double redemptionDismantledPartsWheelHousePrice, Double downPaymentHousePrice, Double estateHousePrice,
			Integer isOnlineShop) {
		this.idOfContract = idOfContract;
		this.createdAt = createdAt;
		this.lastModified = lastModified;
		this.deliveryAssemblyFeeUVP = deliveryAssemblyFeeUVP;
		this.serviceCostGarageUVP = serviceCostGarageUVP;
		this.redemptionDismantledPartsWheelUVP = redemptionDismantledPartsWheelUVP;
		this.downPaymentUVP = downPaymentUVP;
		this.estateUVP = estateUVP;
		this.deliveryAssemblyFeeHousePrice = deliveryAssemblyFeeHousePrice;
		this.serviceCostGarageHousePrice = serviceCostGarageHousePrice;
		this.redemptionDismantledPartsWheelHousePrice = redemptionDismantledPartsWheelHousePrice;
		this.downPaymentHousePrice = downPaymentHousePrice;
		this.estateHousePrice = estateHousePrice;
		this.isOnlineShop = isOnlineShop;
	}

	/** default Constructor. */
	public Contract() {
		this(0, "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0);
	}

	// set & get follows below here

	public int getIdOfContract() {
		return idOfContract;
	}

	public void setIdOfContract(int idOfContract) {
		this.idOfContract = idOfContract;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	/** upv */
	public Double getDeliveryAssemblyFeeUVP() {
		return deliveryAssemblyFeeUVP;
	}

	public void setDeliveryAssemblyFeeUVP(Double deliveryAssemblyFee) {
		this.deliveryAssemblyFeeUVP = deliveryAssemblyFee;
	}

	public Double getServiceCostGarageUVP() {
		return serviceCostGarageUVP;
	}

	public void setServiceCostGarageUVP(Double serviceCostGarage) {
		this.serviceCostGarageUVP = serviceCostGarage;
	}

	public Double getRedemptionDismantledPartsWheelUVP() {
		return redemptionDismantledPartsWheelUVP;
	}

	public void setRedemptionDismantledPartsWheelUVP(Double redemptionDismantledPartsWheel) {
		this.redemptionDismantledPartsWheelUVP = redemptionDismantledPartsWheel;
	}

	public Double getDownPaymentUVP() {
		return downPaymentUVP;
	}

	public void setDownPaymentUVP(Double downPaymentUVP) {
		this.downPaymentUVP = downPaymentUVP;
	}

	public Double getEstateUVP() {
		return estateUVP;
	}

	public void setEstateUVP(Double estate) {
		this.estateUVP = estate;
	}

	/** house price */
	public Double getDeliveryAssemblyFeeHousePrice() {
		return deliveryAssemblyFeeHousePrice;
	}

	public void setDeliveryAssemblyFeeHousePrice(Double deliveryAssemblyFeeHousePrice) {
		this.deliveryAssemblyFeeHousePrice = deliveryAssemblyFeeHousePrice;
	}

	public Double getServiceCostGarageHousePrice() {
		return serviceCostGarageHousePrice;
	}

	public void setServiceCostGarageHousePrice(Double serviceCostGarageHousePrice) {
		this.serviceCostGarageHousePrice = serviceCostGarageHousePrice;
	}

	public Double getRedemptionDismantledPartsWheelHousePrice() {
		return redemptionDismantledPartsWheelHousePrice;
	}

	public void setRedemptionDismantledPartsWheelHousePrice(Double redemptionDismantledPartsWheelHousePrice) {
		this.redemptionDismantledPartsWheelHousePrice = redemptionDismantledPartsWheelHousePrice;
	}

	public Double getDownPaymentHousePrice() {
		return downPaymentHousePrice;
	}

	public void setDownPaymentHousePrice(Double downPaymentHousePrice) {
		this.downPaymentHousePrice = downPaymentHousePrice;
	}

	public Double getEstateHousePrice() {
		return estateHousePrice;
	}

	public void setEstateHousePrice(Double estateHousePrice) {
		this.estateHousePrice = estateHousePrice;
	}

	public List<SellPosition> getSellPositions() {
		return lstSellposition;
	}

	public void setSellPositions(List<SellPosition> lstSellposition) {
		this.lstSellposition = lstSellposition;
	}

	public int getIdOfCustomer() {
		return idOfCustomer;
	}

	public void setIdOfCustomer(int idOfCustomer) {
		this.idOfCustomer = idOfCustomer;
	}

	public BikeData getBikeData() {
		return bikeData;
	}

	public void setBikeData(BikeData bikeData) {
		this.bikeData = bikeData;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Integer getIsOnlineShop() {
		return isOnlineShop;
	}

	public void setIsOnlineShop(Integer isOnlineShop) {
		this.isOnlineShop = isOnlineShop;
	}

}
