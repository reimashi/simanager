package weathercool.proyectosi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alert {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int temperature_high;
	private int temperature_half;
	private int temperature_low;
	private int rain_high;
	private int rain_half;
	private int rain_low;

	public int getId() {
		return id;
	}

	public int getTemperatureHigh() {
		return temperature_high;
	}

	public int getTemperatureHalf() {
		return temperature_half;
	}
	public int getTemperatureLow() {
		return temperature_low;
	}
	
	public int getRainHigh() {
		return rain_high;
	}

	public int getRainHalf() {
		return rain_half;
	}
	public int getRainLow() {
		return rain_low;
	}

	public void setTemperatureHigh(int temperature_high) {
		this.temperature_high = temperature_high;
	}

	public void setTemperatureHalf(int temperature_half) {
		this.temperature_half = temperature_half;
	}
	
	public void setTemperatureLow(int temperature_low) {
		this.temperature_low = temperature_low;
	}
	
	public void setRainHigh(int rain_high) {
		this.rain_high = rain_high;
	}

	public void setRainHalf(int rain_half) {
		this.rain_half = rain_half;
	}
	
	public void setRainLow(int rain_low) {
		this.rain_low = rain_low;
	}
	
}