package app.googlemaptestapp;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class MainActivity extends Activity {
	private GoogleMap map;
	private LocationManager locationManager;
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener; 

	/*	private TextView location_name_textView;*/
	private Button begin_journey_button, end_journey_button, add_place_button;

	private ToggleButton  place_display_toggleButton;

	private Geocoder geocoder;

	private LatLng currentLocationLatLng;
	
	public static SharedPreferences local_user_information;
	private SharedPreferences.Editor local_user_editor;
	private String PREFS_NAME = "LocalUserInfo";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity_layout);
		/*		location_name_textView = (TextView) findViewById(R.id.location_name_textView);*/
		begin_journey_button = (Button) findViewById(R.id.begin_journey_button);
		end_journey_button = (Button) findViewById(R.id.end_journey_button);
		add_place_button = (Button) findViewById(R.id.add_place_button);

		place_display_toggleButton = (ToggleButton) findViewById(R.id.place_display_toggleButton);

		

		local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
		local_user_editor = local_user_information.edit();
		local_user_editor.remove("photoGalleryPath");
		local_user_editor.commit();
		

		begin_journey_button.setVisibility(View.VISIBLE);
		end_journey_button.setVisibility(View.INVISIBLE);


		setGoogleMap();
		setBeginJourneyButtonControl();
		setEndJourneyButtonControl();
		setAddPlaceButtonControl();


		place_display_toggleButton.setChecked(false);


		loadDataBase();
		setPlaceDisplayToggleButtonControl();
		setMarkerActionControl();



















	}

	private ArrayList<LatLng> directionPoint;

	private PolylineOptions directionPolyLineOption;

	private Polyline directionPolyLine;

	private HashMap<Integer, Polyline> markerDirectionPointMap = new HashMap<Integer, Polyline>();

	private Handler handler;


	private void getDirectionToPlace(final int placeListIndex, String mode){

		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GMapV2Direction md = new GMapV2Direction();
						Document doc = md.getDocument(new LatLng(currentLocationLatLng.latitude, currentLocationLatLng.longitude),
								new LatLng(places.get(placeListIndex).getLatitude(), places.get(placeListIndex).getLongitude()),
								GMapV2Direction.MODE_DRIVING);
						directionPoint = md.getDirection(doc);



						Message msg = Message.obtain();
						msg.what = 1;
						handler.sendMessage(msg);
					}

				}).start();
			}

		});



	}




	private double positionLatLngAccuracy = 0.0001;





	private void setMarkerActionControl(){
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub

				for (int i = 0; i < placeMarkerList.size(); i++){
					if (marker.equals(placeMarkerList.get(i))){

						Bundle dataBundle = new Bundle();

						String title = places.get(i).getTitle();
						String description = places.get(i).getDescription();
						String pathString = places.get(i).getPhotoPath();

						dataBundle.putString("myPlaceTitle", title);
						dataBundle.putString("myPlaceDescription", description);
						dataBundle.putString("myPlacePhotoPath", pathString);

						Intent goToShowLargePhotoIntent = new Intent("app.googlemaptestapp.SHOWPLACEACTIVITY");
						goToShowLargePhotoIntent.putExtra("myPlaceInfo", dataBundle);
						startActivity(goToShowLargePhotoIntent);

						break;
					}
					/*	else if (marker.equals(journeyEndMarker)){
						map.clear();
					}*/
				}
			}

		});


		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				for(int i = 0; i < placeMarkerList.size(); i++) {
					if(Math.abs(placeMarkerList.get(i).getPosition().latitude - latLng.latitude) < positionLatLngAccuracy && Math.abs(placeMarkerList.get(i).getPosition().longitude - latLng.longitude) < positionLatLngAccuracy) {
						// Toast.makeText(MapActivity.this, "got clicked", Toast.LENGTH_SHORT).show(); //do some stuff


						if (placeMarkerList.get(i).isVisible()){


							final int index = i;

							if (markerDirectionPointMap.containsKey(index)){
								markerDirectionPointMap.get(index).remove();
								markerDirectionPointMap.remove(index);

								/*	directionPolyLineOption = null;
							directionPolyLine = null;
							directionPoint = null;*/
							}
							else{
								AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

								builder.setTitle("How to get here")
								.setItems(new CharSequence []{"Drive", "Walk", "Cancel"}, new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int which) {
										if (which == 0){

											getDirectionToPlace(index, GMapV2Direction.MODE_DRIVING);

											handler = new Handler(){
												@Override
												public void handleMessage(Message msg) {

													if(msg.what == 1){
														directionPolyLineOption = new PolylineOptions().width(10).color(Color.rgb(66, 103, 128));

														for(int i = 0 ; i < directionPoint.size() ; i++) {          
															directionPolyLineOption.add(directionPoint.get(i));
														}

														directionPolyLine = map.addPolyline(directionPolyLineOption);

														markerDirectionPointMap.put(index, directionPolyLine);
													}
												}

											};




										}

										if (which == 1){
											getDirectionToPlace(index, GMapV2Direction.MODE_WALKING);

											handler = new Handler(){
												@Override
												public void handleMessage(Message msg) {

													if(msg.what == 1){
														directionPolyLineOption = new PolylineOptions().width(10).color(Color.rgb(66, 103, 128));

														for(int i = 0 ; i < directionPoint.size() ; i++) {          
															directionPolyLineOption.add(directionPoint.get(i));
														}

														directionPolyLine = map.addPolyline(directionPolyLineOption);

														markerDirectionPointMap.put(index, directionPolyLine);
													}
												}

											};


										}

										if (which == 2){
											dialog.dismiss();
										}

									}
								}).create().show();

							}

						}

						break;


					}
				}







				for(int i = 0; i < journeyEndMarkerList.size(); i++) {
					if(Math.abs(journeyEndMarkerList.get(i).getPosition().latitude - latLng.latitude) < positionLatLngAccuracy && Math.abs(journeyEndMarkerList.get(i).getPosition().longitude - latLng.longitude) < positionLatLngAccuracy) {

						final int index = i;

						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("Delete journey");
						builder.setMessage("Do you want to delete this journey?");
						builder.setPositiveButton("Yes", new OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								journeyPolylineList.get(index).remove();
								journeyPolylineList.remove(index);

								journeyStartMarekerList.get(index).remove();
								journeyStartMarekerList.remove(index);

								journeyEndMarkerList.get(index).remove();
								journeyEndMarkerList.remove(index);

								journeyDistanceList.remove(index);

							}

						});
						builder.setNegativeButton("No", new OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}

						});

						builder.create().show();

					}

				}
			}
		});











	}




	private void setPlaceDisplayToggleButtonControl(){

		place_display_toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					showPlacesOnMap();
				}
				else{
					hidePlacesOnMap();
				}
			}

		});



	}







	public static List<Place> places;

	private void loadDataBase(){
		Log.d("Reading: ", "Reading all places.."); 

		SQLiteHelper sqliteHelper = new SQLiteHelper(getBaseContext(), SQLiteHelper.TABLE_PLACE);
		places = sqliteHelper.getAllPlaces();       

		for (Place place : places) {
			String log = "Id: "+place.getId()+", latitude: " + place.getLatitude() + ", longtitude: " + place.getLongitude()
					+ ", date: " + place.getDate() + ", content: " + place.getDescription() + ", title: " + place.getTitle() + 
					", photoPath: " + place.getPhotoPath();
			// Writing Contacts to log
			Log.d("Name: ", log);
		}

	}

	private ArrayList<Marker> placeMarkerList = new ArrayList<Marker>();


	private void showPlacesOnMap(){

		if (placeMarkerList.size() < 1){

			for (int i = 0; i < places.size(); i++){

				Place place = places.get(i);

				final Marker currentMarker = map.addMarker(new MarkerOptions()

				.position(new LatLng(place.getLatitude(), place.getLongitude()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker))
				.title(place.getTitle())
				.snippet(place.getDescription()));

				placeMarkerList.add(currentMarker);

			}
		}
		else if (placeMarkerList.size() == places.size() - 1){

			Place place = places.get(places.size() - 1);

			final Marker currentMarker = map.addMarker(new MarkerOptions()

			.position(new LatLng(place.getLatitude(), place.getLongitude()))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker))
			.title(place.getTitle())
			.snippet(place.getDescription()));

			placeMarkerList.add(currentMarker);
		}
		else{

			for (Marker marker : placeMarkerList){
				marker.setVisible(true);
			}

		}
	}



	private void hidePlacesOnMap(){
		for (Marker marker : placeMarkerList){
			marker.setVisible(false);
			//marker.remove();


		}

		for (Integer i : markerDirectionPointMap.keySet()){
			markerDirectionPointMap.get(i).remove();
		}

		markerDirectionPointMap.clear();


	}



	private void setGoogleMap(){
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setBuildingsEnabled(true);



		geocoder = new Geocoder(getBaseContext(), Locale.US);

		myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

				if(map != null){
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							currentLocationLatLng, 10));
				}

				/*if (currentLocationLatLng != null){*/
				myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

					@Override
					public void onMyLocationChange(Location location) {
						// TODO Auto-generated method stub
						currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
					}
				};

				map.setOnMyLocationChangeListener(myLocationChangeListener);
				/*	}*/

			}
		};


		map.setOnMyLocationChangeListener(myLocationChangeListener);



	}


	private void setBeginJourneyButtonControl(){
		begin_journey_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_journey_button.setVisibility(View.VISIBLE);
				begin_journey_button.setVisibility(View.INVISIBLE);
				setLocationChangeListner(true);

			}
		});
	}



	private ArrayList<Marker> journeyStartMarekerList = new ArrayList<Marker>();
	private ArrayList<Marker> journeyEndMarkerList = new ArrayList<Marker>();
	private ArrayList<Polyline> journeyPolylineList = new ArrayList<Polyline>();
	private ArrayList<Double> journeyDistanceList = new ArrayList<Double>();



	private Marker journeyStartMarker;
	private boolean isJourneyStarted = false;
	private Marker journeyEndMarker;
	private PolylineOptions journeyPolylineOption;
	private LatLng preLocationLatLng;
	private double journeyDistance = 0;
	private Polyline tempPolyline;

	private void setLocationChangeListner(boolean isJourneyBegun){
		if (isJourneyBegun){
			myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location location) {
					if (preLocationLatLng == null){
						preLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
					}

					Location preLocation = new Location("pre");
					preLocation.setLatitude(preLocationLatLng.latitude);
					preLocation.setLongitude(preLocationLatLng.longitude);


					currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
					Location curLocation = new Location("cur");
					curLocation.setLatitude(currentLocationLatLng.latitude);
					curLocation.setLongitude(currentLocationLatLng.longitude);

					journeyDistance += preLocation.distanceTo(curLocation);
					/*	journeyDistance += */

					preLocationLatLng = new LatLng(currentLocationLatLng.latitude, currentLocationLatLng.longitude);


				//	Toast.makeText(getBaseContext(), String.valueOf(journeyDistance), Toast.LENGTH_LONG).show();



					if (!isJourneyStarted){
						journeyStartMarker  = map.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker_journey))
						.position(currentLocationLatLng));
						
						journeyStartMarekerList.add(journeyStartMarker);
						isJourneyStarted = true;

					}





					if (journeyPolylineOption == null){
						journeyPolylineOption = new PolylineOptions().width(10).color(Color.rgb(18, 161, 103));
						//journeyPolylineOption.add(currentLocationLatLng);
					}

					journeyPolylineOption.add(currentLocationLatLng);

					if (tempPolyline != null){
						tempPolyline.remove();
					}

					tempPolyline = map.addPolyline(journeyPolylineOption);

					/*	for(int i = 0 ; i < directionPoint.size() ; i++) {          
						rectLine.add(directionPoint.get(i));
					}*/


					if(map != null){
						map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 19.0f));
					}
				}
			};

			map.setOnMyLocationChangeListener(myLocationChangeListener);

		}
		else{
/*
			if (journeyEndMarker == null){
				journeyEndMarker = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker_journey_end))
				.title("Distance")
				.snippet(String.valueOf(journeyDistance))
				.position(currentLocationLatLng));
			}*/

			preLocationLatLng = null;

			isJourneyStarted = false;

	/*		journeyStartMarker.remove();
			Marker tempStartMarker = map.addMarker(new MarkerOptions()
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker_journey))
			.position(currentLocationLatLng));

			journeyStartMarekerList.add(tempStartMarker);
			journeyStartMarker = null;*/

			/*journeyEndMarker.remove();*/
			Marker tempEndMarker = map.addMarker(new MarkerOptions()
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_marker_journey_end))
			.title("Distance")
			.snippet(String.valueOf(journeyDistance / 1000) + "km")
			.position(currentLocationLatLng));
			journeyEndMarkerList.add(tempEndMarker);
			/*journeyEndMarker = null;*/

			journeyDistanceList.add(journeyDistance);
			journeyDistance = 0;

			journeyPolylineList.add(tempPolyline);

			journeyPolylineOption = null;



			myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location location) {
					currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());				
				}

			};
			map.setOnMyLocationChangeListener(myLocationChangeListener);











		}



	}




	private void setEndJourneyButtonControl(){
		end_journey_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				begin_journey_button.setVisibility(View.VISIBLE);
				end_journey_button.setVisibility(View.INVISIBLE);
				setLocationChangeListner(false);
			}
		});
	}



	private int isLoactionInDatabase(LatLng location){
		for (int i = 0; i < places.size(); i++){
			if (places.get(i).getLatitude() == location.latitude && places.get(i).getLongitude() == location.longitude){
				return i;
			}
		}

		return -1;
	}



	private void setAddPlaceButtonControl(){
		add_place_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentLocationLatLng != null){
					//Toast.makeText(getBaseContext(), String.valueOf(currentLocationLatLng.latitude) + String.valueOf(currentLocationLatLng.longitude), Toast.LENGTH_LONG).show();
					if (isLoactionInDatabase(currentLocationLatLng) != -1){
						//Toast.makeText(getBaseContext(), String.valueOf(isLoactionInDatabase(currentLocationLatLng)), Toast.LENGTH_LONG).show();
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setPositiveButton("Ok", new OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Bundle dataBundle = new Bundle();
								dataBundle.putDouble("currentLat", currentLocationLatLng.latitude);
								dataBundle.putDouble("currentLong", currentLocationLatLng.longitude);
								dataBundle.putInt("updatePlaceIndex", isLoactionInDatabase(currentLocationLatLng));


								Intent intent = new Intent("app.googlemaptestapp.ADDPLACEACTIVITY");

								intent.putExtra("locationInfo", dataBundle);
								startActivity(intent);
							}

						});
						builder.setNegativeButton("No", new OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}

						});
						builder.setTitle("Add place").setMessage("This place already exists, do you want to replace it?").create().show();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setPositiveButton("Ok", new OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								Bundle dataBundle = new Bundle();
								dataBundle.putDouble("currentLat", currentLocationLatLng.latitude);
								dataBundle.putDouble("currentLong", currentLocationLatLng.longitude);
								dataBundle.putInt("updatePlaceIndex", isLoactionInDatabase(currentLocationLatLng));


								Intent intent = new Intent("app.googlemaptestapp.ADDPLACEACTIVITY");

								intent.putExtra("locationInfo", dataBundle);
								startActivity(intent);

							}

						});
						builder.setNegativeButton("No", new OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}

						});
						builder.setTitle("Add place").setMessage("Do you want to add this place?").create().show();
					}

				}
				else{
					Toast.makeText(getBaseContext(), "Getting your current location, please wait.", Toast.LENGTH_LONG).show();
				}

			}
		});
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		setPlaceDisplayToggleButtonControl();
		setMarkerActionControl();

	}



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			//preventing default implementation previous to 
			//android.os.Build.VERSION_CODES.ECLAIR
			moveTaskToBack(true);
			return false;
		}     
		return super.onKeyDown(keyCode, event);    
	}





}