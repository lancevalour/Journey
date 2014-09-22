package app.googlemaptestapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AddPlaceActivity extends Activity{
	private EditText add_place_content_editText, add_place_title_editText;
	private Button add_place_confirm_button, add_photo_button, add_place_back_button;

	private HorizontalScrollView add_place_horizontalScrollView;

	private RelativeLayout add_place_layout;

	private LinearLayout add_place_horizontalScrollView_layout;

	private String photoPathConcatString = "";

	private ArrayList<String> photoPathList = new ArrayList<String>();

	private double currentLat, currentLong;

	private int updatePlaceIndex;
	

	public static SharedPreferences local_user_information;
	private SharedPreferences.Editor local_user_editor;
	private String PREFS_NAME = "LocalUserInfo";
	
	private Set<String> photoPathSet;
	private String[] phtotPathArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_place_activity_layout);
		add_place_content_editText = (EditText) findViewById(R.id.add_place_content_editText);
		add_place_title_editText = (EditText) findViewById(R.id.add_place_title_editText);
		add_place_confirm_button = (Button) findViewById(R.id.add_place_confirm_button);
		add_photo_button = (Button) findViewById(R.id.add_photo_button);
		add_place_back_button = (Button) findViewById(R.id.add_place_back_button);

		add_place_layout = (RelativeLayout) findViewById(R.id.add_place_layout);
		add_place_horizontalScrollView_layout = (LinearLayout) findViewById(R.id.add_place_horizontalScrollView_layout);
		add_place_horizontalScrollView_layout.setGravity(Gravity.CENTER_VERTICAL);

		add_place_horizontalScrollView = (HorizontalScrollView) findViewById(R.id.add_place_horizontalScrollView);

		
		local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
		
		if (!local_user_information.getString("photoGalleryPath", "").equals("")){
			updateGalleryDisplay();
		}
		


		Intent intent = getIntent();
		Bundle dataBundle = intent.getBundleExtra("locationInfo");
		currentLat = dataBundle.getDouble("currentLat");
		currentLong = dataBundle.getDouble("currentLong");
		updatePlaceIndex = dataBundle.getInt("updatePlaceIndex");


		//Toast.makeText(getBaseContext(), String.valueOf(currentLat) + String.valueOf(currentLong), Toast.LENGTH_LONG).show();

		setHideKeyBoardControl();

		setAddPhotoButtonControl();

		setConfirmButtonControl();

		setBackButtonControl();


	}
	
	
	

	private void setBackButtonControl(){
		add_place_back_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
				builder.setTitle("Go back");
				builder.setMessage("Going back will lose the information you have entered, are you sure?");
				builder.setPositiveButton("Yes", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						local_user_editor = local_user_information.edit();
						local_user_editor.remove("photoGalleryPath");
						
						local_user_editor.commit();
						finish();
					}

				});

				builder.setNegativeButton("No", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}

				});

				builder.create().show();

			}
		});
	}

	private void hideKeyboard(){
		if(getCurrentFocus()!=null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	private void setHideKeyBoardControl(){
		add_place_layout.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideKeyboard();
				return false;
			}
		});

		add_photo_button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideKeyboard();
				return false;
			}
		});

		add_place_confirm_button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideKeyboard();
				return false;
			}
		});

		add_place_horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideKeyboard();
				return false;
			}
		});


		add_place_back_button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideKeyboard();
				return false;
			}
		});


	}





	private final int REQUEST_CAMERA = 1;
	private final int SELECT_FILE = 2;

	private final String photoFileDirectory = android.os.Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "Journey" + File.separator + "myJouneyPhoto" + File.separator;

	private void setAddPhotoButtonControl(){
		add_photo_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
				builder.setTitle("Upload your image")
				.setItems(new CharSequence []{"Choose from gallery", "Take a photo", "Cancel"}, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0){
							Intent intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							intent.setType("image/*");
							startActivityForResult(
									Intent.createChooser(intent, "Select Image"),
									SELECT_FILE);

						}

						if (which == 1){
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
						


							File f = new File(photoFileDirectory, timeStamp + ".jpg");
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
							
							
							startActivityForResult(intent, REQUEST_CAMERA);



							local_user_editor = local_user_information.edit();
							local_user_editor.putString("PhotoPath", photoFileDirectory + timeStamp + ".jpg");
							local_user_editor.commit();


							Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
							Uri contentUri = Uri.fromFile(f);
							mediaScanIntent.setData(contentUri);
							sendBroadcast(mediaScanIntent);





							/*	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
							startActivityForResult(intent, REQUEST_CAMERA);*/


							/*photoPathConcatString += photoFileDirectory + timeStamp + ".jpg";*/

							/*	local_user_editor = local_user_information.edit();
							local_user_editor.putString("PhotoPath", photoFileDirectory + timeStamp + ".jpg");
							local_user_editor.commit();*/







						}

						if (which == 2){
							dialog.dismiss();
						}



					}



				}).create().show();

			}
		});
	}
	
	
	private void updateGalleryDisplay(){
		local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
		String photoGalleryPath = local_user_information.getString("photoGalleryPath", "");
		
		//Toast.makeText(getBaseContext(), photoGalleryPath, Toast.LENGTH_LONG).show();
		
		String[] paths = photoGalleryPath.split("\n");
		
		photoPathList = new ArrayList<String>();
		add_place_horizontalScrollView_layout = (LinearLayout) findViewById(R.id.add_place_horizontalScrollView_layout);
		add_place_horizontalScrollView_layout.setGravity(Gravity.CENTER_VERTICAL);
		/*add_place_horizontalScrollView_layout.removeAllViews();*/
		
		add_place_horizontalScrollView_layout.removeViews(0, add_place_horizontalScrollView_layout.getChildCount() - 1);
		
		
		for (int i = 0; i < paths.length; i++){
			photoPathList.add(paths[i]);
		}
		
		for (int i = 0; i < photoPathList.size(); i++){		
			add_place_horizontalScrollView_layout.addView(insertPhoto(photoPathList.get(i)), 0);
		}

		
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {



				local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
				//local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
				String currentPhotoPath = local_user_information.getString("PhotoPath", "default");
				
				String catStringPath = local_user_information.getString("photoGalleryPath", "");
				
				
				local_user_editor = local_user_information.edit();		
				local_user_editor.putString("photoGalleryPath", catStringPath + currentPhotoPath + "\n");
				local_user_editor.commit();


			/*	photoPathList.add(currentPhotoPath);

				Toast.makeText(getBaseContext(), photoPathList.get(photoPathList.size() - 1), Toast.LENGTH_LONG).show();
				
				Log.d("photoPathList size", String.valueOf(photoPathList.size()));
				
				
				Log.d("photoPathList size", String.valueOf(photoPathList.get(photoPathList.size()-1)));
				
				

				//Bitmap bitmap = decodeSampledBitmapFromUri(photoFileDirectory + timeStamp + ".jpg", 220, 220);
		
				View view = insertPhoto(currentPhotoPath);
				Log.d("view size", String.valueOf(	view.getWidth()));
				

				add_place_horizontalScrollView_layout.addView(insertPhoto(currentPhotoPath), 0);*/
				


			} else if (requestCode == SELECT_FILE) {

				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, AddPlaceActivity.this);

				local_user_information =  this.getSharedPreferences(PREFS_NAME,0);
				String catStringPath = local_user_information.getString("photoGalleryPath", "");
				
				
				local_user_editor = local_user_information.edit();		
				local_user_editor.putString("photoGalleryPath", catStringPath + tempPath + "\n");
				local_user_editor.commit();
				
				updateGalleryDisplay();
				
				/*photoPathList.add(tempPath);

				Log.d("photoPathList size", String.valueOf(photoPathList.size()));

				add_place_horizontalScrollView_layout.addView(insertPhoto(photoPathList.get(photoPathList.size()-1)), 0);*/
				
				
				


			}
		}


	}



	private String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	



	private View insertPhoto(final String path){
		Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);

		final LinearLayout layout = new LinearLayout(getBaseContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getBaseContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bm);

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Bundle dataBundle = new Bundle();
				dataBundle.putString("photoFilePath", path);
				
				Intent goToShowLargePhotoIntent = new Intent("app.googlemaptestapp.SHOWLARGEPHOTOACTIVITY");
				goToShowLargePhotoIntent.putExtra("photoInfo", dataBundle);
				startActivity(goToShowLargePhotoIntent);

			}
		});

		imageView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub


				add_place_horizontalScrollView_layout.removeView(layout);

				/*photoPathList.remove(path);*/
				local_user_information =  getBaseContext().getSharedPreferences(PREFS_NAME,0);
				String catStringPath = local_user_information.getString("photoGalleryPath", "");
				
				String[] paths = catStringPath.split("\n");
				
				for (int i = 0; i < paths.length; i++){
					if (paths[i].equals(path)){
						paths[i] = "";
					}
				}
				
				String result = "";
				
				for (int i = 0 ; i < paths.length; i++){
					if(paths[i].equals("")){
						result += "";
					}
					else{
						result += paths[i] + "\n";
					}
				}
				
				local_user_editor = local_user_information.edit();		
				
				local_user_editor.putString("photoGalleryPath", result);
				local_user_editor.commit();

				//Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
				
				return false;
			}
		});


		layout.addView(imageView);
		return layout;
	}


	private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);


		ExifInterface exif = null;
		try {
			exif = new ExifInterface(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 

		int rotationInDegrees = exifToDegrees(rotation);

		Matrix matrix = new Matrix();
		if (rotation != 0f) {
			matrix.preRotate(rotationInDegrees);
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options); 

		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		/*return bm;  */
	}

	private int exifToDegrees(int exifOrientation) {        
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } 
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; } 
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }            
		return 0;    
	}


	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);   
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);   
			}   
		}

		return inSampleSize;   
	}



	private void setConfirmButtonControl(){
		add_place_confirm_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);

				builder.setPositiveButton("Ok", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (add_place_title_editText.getText().toString().length() < 1 || add_place_content_editText.getText().toString().length() < 1){
							Toast.makeText(getBaseContext(), "One or more of your info is empty, please check.", Toast.LENGTH_LONG).show();
						}
						else{
							SQLiteHelper sqliteHelper = new SQLiteHelper(getBaseContext(), SQLiteHelper.TABLE_PLACE);

							String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH/mm").format(new Date());

							for (String path : photoPathList){
								photoPathConcatString += path + "\n";
							}

							//Toast.makeText(getBaseContext(), photoPathConcatString, Toast.LENGTH_LONG).show();

							
							local_user_editor = local_user_information.edit();
							local_user_editor.remove("photoGalleryPath");
							
							local_user_editor.commit();

							if (updatePlaceIndex == -1){

								sqliteHelper.addPlace(new Place(currentLat, currentLong, 
										timeStamp, 
										add_place_title_editText.getText().toString(),
										add_place_content_editText.getText().toString(), 
										photoPathConcatString));

							}
							else{
								sqliteHelper.updatePlace(updatePlaceIndex, new Place(currentLat, 
										currentLong, 
										timeStamp,
										add_place_title_editText.getText().toString(), 
										add_place_content_editText.getText().toString(), photoPathConcatString));
							}

							MainActivity.places = sqliteHelper.getAllPlaces();       

							finish();
						}

					}

				});

				builder.setNegativeButton("No", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}

				});

				builder.setTitle("Save Place").setMessage("Do you want to save this place?").create().show();


				/*SQLiteHelper sqliteHelper = new SQLiteHelper(getBaseContext(), SQLiteHelper.TABLE_FOOD);

				for (int i = 1; i <= 9; i++){
					sqliteHelper.deletePlace(i);
				}

				sqliteHelper.addPlace(new Place(39.31, -76.60, "date1", "title1", "content1", "path1"));
				sqliteHelper.addPlace(new Place(39.31, -76.64, "date2", "title2", "content2", "path2"));
				sqliteHelper.addPlace(new Place(39.29, -76.60, "date3", "title3", "content3", "path3"));*/


			}
		});
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
