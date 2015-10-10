package module1;

import processing.core.PApplet;

import java.util.*;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;


public class LifeExpectancy extends PApplet {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;

	HashMap<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
 
	 public void setup(){
		 size(800,600,OPENGL);
		 map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		 MapUtils.createDefaultEventDispatcher(this, map);
		 lifeExpByCountry = loadLifeExpectancyFromCSV("../data/LifeExpectancyWorldBank.csv");
		 countries = GeoJSONReader.loadData(this, "countries.geo.json");
		 countryMarkers = MapUtils.createSimpleMarkers(countries);
		 map.addMarkers(countryMarkers);
		 
		 shadeCountries();
	 }
	 private void shadeCountries() {
			for (Marker marker : countryMarkers) {
				// Find data for country of the current marker
				String countryId = marker.getId();
				if (lifeExpByCountry.containsKey(countryId)) {
					float lifeExp = lifeExpByCountry.get(countryId);
					int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
					marker.setColor(color(255-colorLevel, 100, colorLevel));
				} else {
					marker.setColor(color(150,150,150));
				}
			}
		
	}
	public void draw(){
		 map.draw();
	 }
 
	 private HashMap<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		 HashMap<String,Float> lifeExpMap = new HashMap<String,Float>();
		 String rows[] =  loadStrings(fileName);;
		 for(String row : rows){
			 String[] columns = row.split(",");
			 if (columns.length == 18 && !columns[5].equals("..")) {
				 float value = Float.parseFloat(columns[5]);
				 lifeExpMap.put(columns[4], value);
			 }
		 }
		 return lifeExpMap;
	}

}