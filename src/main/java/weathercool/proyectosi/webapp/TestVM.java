package weathercool.proyectosi.webapp;

import static weathercool.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Location;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.util.DesktopEntityManagerManager;

public class TestVM {

	private double latitude = 0.0;
	private double longitude = 0.0;

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public int getLocationCount() {
		return getDesktopEntityManager().createQuery("SELECT e FROM Employee e", Location.class).getResultList().size();
	}
	
	@Command
	@NotifyChange("locationCount")
	public void submitLocation() {
		Location loc = new Location();
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		
		TransactionUtils.doTransaction(
				getDesktopEntityManager(), 
				lo->{ lo.persist(loc);}
		);
	}
}
