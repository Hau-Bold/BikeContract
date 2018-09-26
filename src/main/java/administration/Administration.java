package administration;

/** the class Administration */
public class Administration {

	private String erpLastReaded;
	private Boolean usePassword;
	private Boolean useHousekeeping;
	private Integer housekeepingPdf;
	private Integer housekeepingContracts;
	private Boolean isOnlineShop;
	private Boolean isAll;

	public Administration(String erpLastReaded, Boolean usePassword, Boolean useHousekeeping, Integer housekeepingPdf,
			Integer housekeepingContracts, Boolean isOnlineShop, Boolean isAll) {

		this.erpLastReaded = erpLastReaded;
		this.usePassword = usePassword;
		this.useHousekeeping = useHousekeeping;
		this.housekeepingPdf = housekeepingPdf;
		this.housekeepingContracts = housekeepingContracts;
		this.isOnlineShop = isOnlineShop;
		this.isAll = isAll;
	}

	public String getErpLastReaded() {
		return erpLastReaded;
	}

	public void setErpLastReaded(String erpLastReaded) {
		this.erpLastReaded = erpLastReaded;
	}

	public Boolean getUsePassword() {
		return usePassword;
	}

	public void setUsePassword(Boolean usePassword) {
		this.usePassword = usePassword;
	}

	public Boolean getUseHousekeeping() {
		return useHousekeeping;
	}

	public void setUseHousekeeping(Boolean useHousekeeping) {
		this.useHousekeeping = useHousekeeping;
	}

	public Integer getHousekeepingPdf() {
		return housekeepingPdf;
	}

	public void setHousekeepingPdf(Integer housekeepingPdf) {
		this.housekeepingPdf = housekeepingPdf;
	}

	public Integer getHousekeepingContracts() {
		return housekeepingContracts;
	}

	public void setHousekeepingContracts(Integer housekeepingContracts) {
		this.housekeepingContracts = housekeepingContracts;
	}

	public Boolean getIsOnlineShop() {
		return isOnlineShop;
	}

	public void setIsOnlineShop(Boolean isOnlineShop) {
		this.isOnlineShop = isOnlineShop;
	}

	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}

}
