package weathercool.proyectosi;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@IdClass(Measurement.MeasurementKey.class)
public class Measurement {
	@Id
	@ManyToOne
	private Alerts alert;
	
	@Id
	@ManyToOne
	private Time time;
	
	@Id
	@ManyToOne
	private Location location;
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;*/
	
	private double elevation;
	private double temperature_surface;
	private double temperature_500mb;
	private double temperature_850mb;
	private double temperature_sea_level;
	private double cloud_cover_high;
	private double cloud_cover_half;
	private double cloud_cover_low;
	private double visibility;
	private double salinity;
	private double water_speed_eastward;
	private double water_speed_northward;
	private double wave_direction_mean;
	private double wave_direction_peak;
	private double wave_period_absolute;
	private double wave_period_peak;
	private double snow_level;
	private double snow_precipitation;
	private double rain_pecipitation;
	private double humidity;
	private double wind_direction;
	private double wind_lon;
	private double wind_lat;
	private double wind_gust;

	/*public int getId() {
		return id;
	}*/

	public double getElevation() {
		return elevation;
	}

	public double getTemperatureSurface() {
		return temperature_half;
	}
	public double getTemperature500mb() {
		return temperature_500mb;
	}
	
	public double getTemperature850mb() {
		return temperature_850mb;
	}
	public double getTemperatureSeaLevel() {
		return temperature_sea_level;
	}
	
	public double getCloudCoverHigh() {
		return cloud_cover_high;
	}
	
	public double getCloudCoverHalf() {
		return cloud_cover_half;
	}
	
	public double getCloudCoverLow() {
		return cloud_cover_low;
	}

	public double getVisibility() {
		return visibility;
	}
	
	public double getSalinity() {
		return salinity;
	}
	
	public double getWaterSpeedEastward() {
		return water_speed_eastward;
	}
	
	public double getWaterSpeedNorthward() {
		return water_speed_northward;
	}
	
	public double getWaterDirectionMean() {
		return water_direction_mean;
	}
	
	public double getWaterDirectionPeak() {
		return water_direction_peak;
	}
	
	public double getWaterPeriodAbsolute() {
		return water_period_absolute;
	}
	
	public double getWaterPeriodPeak() {
		return water_period_peak;
	}
	
	public double getSnowLevel() {
		return snow_level;
	}
	
	public double getSnowPrecipitation() {
		return snow_precipitation;
	}
	
	public double getHumidity() {
		return humidity;
	}
	
	public double getWindDirection() {
		return wind_direction;
	}
	
	public double getWindLon() {
		return wind_lon;
	}
	
	public double getWindLat() {
		return wind_lat;
	}
	
	public double getWindGust() {
		return wind_gust;
	}

	public void setElevation(double elevation) {
		this.elevation=elevation;
	}

	public void setTemperatureSurface(double temperature_half) {
		this.temperature_half=temperature_half;
	}
	public void setTemperature500mb(double temperature_500mb) {
		this.temperature_500mb=temperature_500mb;
	}
	
	public void setTemperature850mb(double temperature_850mb) {
		this.temperature_850mb=temperature_850mb;
	}
	public void setTemperatureSeaLevel(double temperature_sea_level) {
		this.temperature_sea_level=temperature_sea_level;
	}
	
	public void setCloudCoverHigh(double cloud_cover_high) {
		this.cloud_cover_high=cloud_cover_high;
	}
	
	public void setCloudCoverHalf(double cloud_cover_half) {
		this.cloud_cover_half=cloud_cover_half;
	}
	
	public void setCloudCoverLow(double cloud_cover_low) {
		this.cloud_cover_low=cloud_cover_low;
	}

	public void setVisibility(double visibility) {
		this.visibility=visibility;
	}
	
	public void setSalinity(double salinity) {
		this.salinity=salinity;
	}
	
	public void setWaterSpeedEastward(double water_speed_eastward) {
		this.water_speed_eastward=water_speed_eastward;
	}
	
	public void setWaterSpeedNorthward(double water_speed_northward) {
		this.water_speed_northward=water_speed_northward;
	}
	
	public void setWaterDirectionMean(double water_direction_mean) {
		this.water_direction_mean= water_direction_mean;
	}
	
	public void setWaterDirectionPeak(double water_direction_peak) {
		this.water_direction_peak=water_direction_peak;
	}
	
	public void setWaterPeriodAbsolute(double water_period_absolute) {
		this.water_period_absolute=water_period_absolute;
	}
	
	public void setWaterPeriodPeak(double water_period_peak) {
		this.water_period_peak=water_period_peak;
	}
	
	public void setSnowLevel(double snow_level) {
		this.snow_level=snow_level;
	}
	
	public void setSnowPrecipitation(double snow_precipitation) {
		this.snow_precipitation=snow_precipitation;
	}
	
	public void setHumidity(double humidity) {
		this.humidity=humidity;
	}
	
	public void setWindDirection(double wind_direction) {
		this.wind_direction=wind_direction;
	}
	
	public void setWindLon(double wind_lon) {
		this.wind_lon=wind_lon;
	}
	
	public void setWindLat(double wind_lat) {
		this.wind_lat=wind_lat;
	}
	
	public void setWindGust(double wind_gust) {
		this.wind_gust=wind_gust;
	}
	
	public static class MeasurementKey
		implements Serializable {
		
		private int alert;
		
		private int time;
		
		private int location;
		
		public MeasurementKey() {
			// TODO Auto-generated constructor stub
		}

		public MeasurementKey(int alert, int time, int location) {
			super();
			this.alert = alert;
			this.time = time;
			this.location = location;
		}
	}
	
}