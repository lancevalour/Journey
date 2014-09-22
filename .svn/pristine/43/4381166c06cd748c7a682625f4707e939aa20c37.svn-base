package app.googlemaptestapp;

import java.io.IOException;








import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ShowLargePhotoActivity extends Activity{
	private ImageView large_photo_imageView;
	private RelativeLayout show_large_photo_layout;
	
	private String photoPath;
	
	private int photo_width = 600;
	private int photo_height = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_large_photo_activity_layout);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		
		large_photo_imageView = (ImageView) findViewById(R.id.large_photo_imageView);
		show_large_photo_layout = (RelativeLayout) findViewById(R.id.show_large_photo_layout);
		
		
		Intent intent = getIntent();
		Bundle dataBundle = intent.getBundleExtra("photoInfo");
		photoPath = dataBundle.getString("photoFilePath");
		
		
		loadLargePhoto();
		
		setOnTouchControl();
		
	}
	
	private void loadLargePhoto(){
		Bitmap bm = decodeSampledBitmapFromUri(photoPath, photo_width);
		
		
		large_photo_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		large_photo_imageView.setImageBitmap(bm);
	}
	
	private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth) {
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
				
				double width_height_ratio = (double)options.outWidth / (double)options.outHeight;
				
				if (width_height_ratio > 1){
					width_height_ratio = 1 / width_height_ratio;
				}

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, reqWidth, (int)((double)reqWidth / width_height_ratio));

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

	
	private void setOnTouchControl(){
		show_large_photo_layout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);			
				return false;
			}
		});
		
		large_photo_imageView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);			
				return false;
			}
		});
	}
	
	
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
