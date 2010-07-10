package dbs_fussball.model;

import active_record.ActiveRecord;

public class Stadium extends ActiveRecord {

	private Integer maximumOccupancy;
	private String city, country;
	private Double geo_long, geo_lat;
	
	public Integer getMaximumOccupancy() {
		return maximumOccupancy;
	}

	public void setMaximumOccupancy(Integer maximumOccupancy) {
		this.maximumOccupancy = maximumOccupancy;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getGeo_long() {
		return geo_long;
	}

	public void setGeo_long(Double geoLong) {
		geo_long = geoLong;
	}

	public Double getGeo_lat() {
		return geo_lat;
	}
	
	public void setGeo_lat(Double geoLat) {
		geo_lat = geoLat;
	}
	
}
