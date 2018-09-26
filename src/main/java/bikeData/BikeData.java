package bikeData;

/** to hold the bike data values of the contract */
public class BikeData {

	private String date, seller, frameNumber, frameHeight, mechanic, dateOfAppointment, timeOfAppointment;

	private int delivery, collection, takeAway, booked, new_Bike, used_Bike, trekking, cityTour, mountainBike,
			childsBike, racingCycle, cross, bmx, eBike, others, year;

	/**
	 * Constructor.
	 * 
	 * @param date
	 *            - the date
	 * @param seller
	 *            - the seller
	 * @param frameNumber
	 *            - the frame number
	 * 
	 * @param frameHeight
	 *            - the frame height
	 * 
	 * @param mechanic
	 *            - the mechanic
	 * @param delivery
	 *            - the delivery
	 * @param collection
	 *            - the collection
	 * @param takeAway
	 *            - the take away
	 * @param booked
	 *            - booked
	 * @param new_Bike
	 *            - bike is new
	 * @param used_Bike
	 *            - bike is used
	 * @param trekking
	 *            - trekking
	 * @param cityTour
	 *            - city tour
	 * @param mountainBike
	 *            - mountainbike
	 * @param childsBike
	 *            - childBike
	 * @param racingCycle
	 *            - racing cycle
	 * @param cross
	 *            - cross
	 * @param bmx
	 *            - bmx
	 * @param eBike
	 *            - eBike
	 * @param others
	 *            - others
	 * @param year
	 *            - years
	 * @param dateOfAppointment
	 *            - the date of the appointment
	 * @param timeOfAppointment
	 *            - the time of the appointment
	 */
	public BikeData(String date, String seller, String frameNumber, String frameHeight, String mechanic, int delivery,
			int collection, int takeAway, int booked, int new_Bike, int used_Bike, int trekking, int cityTour,
			int mountainBike, int childsBike, int racingCycle, int cross, int bmx, int eBike, int others, int year,
			String dateOfAppointment, String timeOfAppointment) {

		this.date = date;
		this.seller = seller;
		this.frameNumber = frameNumber;
		this.frameHeight = frameHeight;
		this.mechanic = mechanic;
		this.delivery = delivery;
		this.collection = collection;
		this.takeAway = takeAway;
		this.booked = booked;
		this.new_Bike = new_Bike;
		this.used_Bike = used_Bike;
		this.trekking = trekking;
		this.cityTour = cityTour;
		this.mountainBike = mountainBike;
		this.childsBike = childsBike;
		this.racingCycle = racingCycle;
		this.cross = cross;
		this.bmx = bmx;
		this.eBike = eBike;
		this.others = others;
		this.year = year;
		this.dateOfAppointment = dateOfAppointment;
		this.timeOfAppointment = timeOfAppointment;
	}

	// set & get follows below here

	public String getDate() {
		return date;
	}

	public String getSeller() {
		return seller;
	}

	public String getFrameNumber() {
		return frameNumber;
	}

	public String getFrameHeight() {
		return frameHeight;
	}

	public String getMechanic() {
		return mechanic;
	}

	public int getDelivery() {
		return delivery;
	}

	public int getCollection() {
		return collection;
	}

	public int getTakeAway() {
		return takeAway;
	}

	public int getBooked() {
		return booked;
	}

	public int getNew_Bike() {
		return new_Bike;
	}

	public int getUsed_Bike() {
		return used_Bike;
	}

	public int getTrekking() {
		return trekking;
	}

	public int getCityTour() {
		return cityTour;
	}

	public int getMountainBike() {
		return mountainBike;
	}

	public int getChildsBike() {
		return childsBike;
	}

	public int getRacingCycle() {
		return racingCycle;
	}

	public int getCross() {
		return cross;
	}

	public int getBmx() {
		return bmx;
	}

	public int geteBike() {
		return eBike;
	}

	public int getOthers() {
		return others;
	}

	public int getYear() {
		return year;
	}

	public String getTimeOfAppointment() {
		return timeOfAppointment;
	}

	public String getDateOfAppointment() {
		return dateOfAppointment;
	}

}
