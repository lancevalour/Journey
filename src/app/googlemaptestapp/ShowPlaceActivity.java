package app.googlemaptestapp;

import java.io.IOException;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowPlaceActivity extends Activity{

	private TextView my_place_title_textView, my_place_description_textView;
	private LinearLayout my_place_horizontalScrollView_layout;

	private String title, description, photoPathConcatString;

	private String[] photoPathArray;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_place_activity_layout);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


		my_place_title_textView = (TextView) findViewById(R.id.my_place_title_textView);
		my_place_description_textView = (TextView) findViewById(R.id.my_place_description_textView);

		my_place_horizontalScrollView_layout = (LinearLayout) findViewById(R.id.my_place_horizontalScrollView_layout);

		Intent intent = getIntent();
		Bundle dataBundle = intent.getBundleExtra("myPlaceInfo");
		title = dataBundle.getString("myPlaceTitle");
		description = dataBundle.getString("myPlaceDescription");
		photoPathConcatString = dataBundle.getString("myPlacePhotoPath");

		photoPathArray = photoPathConcatString.split("\n");

		my_place_title_textView.setText(title);
		my_place_description_textView.setText(description);


		if (photoPathConcatString.length() >= 1){
			setGalleryPhoto();
		}



	}

	private void setGalleryPhoto(){
		for (int i = 0; i < photoPathArray.length; i++){
			my_place_horizontalScrollView_layout.addView(insertPhoto(photoPathArray[i]));
		}
	}


	private View insertPhoto(final String path){

		Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);

		final LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getApplicationContext());
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

		//return bm;  
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


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
