package dbs_fussball.model;

import active_record.ActiveRecord;

public class Stadium extends ActiveRecord {

	private String city, country, name;
	private Double geoLong, geoLat;
	private Integer maximumOccupancy;

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Double getGeoLat() {
		return geoLat;
	}

	public Double getGeoLong() {
		return geoLong;
	}
	public Integer getMaximumOccupancy() {
		return maximumOccupancy;
	}

	public String getName() {
		return name;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public void setGeoLat(Double geoLat) {
		this.geoLat = geoLat;
	}

	public void setGeoLong(Double geoLong) {
		this.geoLong = geoLong;
	}
	public void setMaximumOccupancy(Integer maximumOccupancy) {
		this.maximumOccupancy = maximumOccupancy;
	}
	public void setName(String name) {
		this.name = name;
	}

}
