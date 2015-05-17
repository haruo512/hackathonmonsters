package com.zeroone_creative.basicapplication.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.android.volley.toolbox.ImageLoader;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.provider.CustomImageLoader;

public class RemindLaterReceiver extends BroadcastReceiver implements CustomImageLoader.ImageListener {
	//Intent key
	public static final String IntentRecipeJSON ="string_RecipeJSON";
	private Context context;
	private PendingIntent sender;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		//intentからレシピ情報を受け取る
		String recipeJSON = intent.getStringExtra(IntentRecipeJSON);


		Intent sendIntent;
		if(recipeJSON==null){
			sendIntent = new Intent(context, MainActivity.class);
			sender = PendingIntent.getActivity(context, 0, sendIntent, 0);
			//通知オブジェクトの生成
			Notification noti = new NotificationCompat.Builder(context)
	            .setTicker("レシピのお届け")
	            .setContentTitle("レシピのお届け")
	            .setContentText("タップで確認")
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
	            .setAutoCancel(true)
	            .setContentIntent(sender)
	            .build();
			NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	        manager.notify(0, noti);
		}else{
			//通知がクリックされた時に発行されるIntentの生成
			sendIntent = new Intent(context, DetailsActivity.class);
			sendIntent.putExtra(DetailsActivity.IntentRecipeJSON, recipeJSON);
			try {
				recipe = TranslateJSON.recipeParse(new JSONObject(recipeJSON));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sender = PendingIntent.getActivity(context, 0, sendIntent, 0);
		if(recipe!=null){
			ImageLoader imageLoader = new ImageLoader(VolleyHelper.getRequestQueue(context),new BitmapNormalCache());
			Log.d("RemindLaterReceiver",recipe.foodImageUrl);
			imageLoader.get(recipe.foodImageUrl,this);
		}
	}
	
	@Override
	public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
    	StringBuilder materialsBuilder = new StringBuilder();
		for(String material : recipe.recipeMaterial){
			materialsBuilder.append(material);
			materialsBuilder.append("\n");
		}
    	//通知オブジェクトの生成
		Notification noti = new NotificationCompat.Builder(context)
            .setTicker("レシピのお届け")
            .setContentTitle(recipe.recipeTitle)
            .setContentText(materialsBuilder.toString())
            .setSmallIcon(R.drawable.ic_launcher)
            .setLargeIcon(response.getBitmap())
            .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
            .setAutoCancel(true)
            .setContentIntent(sender)
            .build();
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, noti);
	}
	@Override
	public void onErrorResponse(VolleyError error) {
	}

}


/**
 /*-----------後で通知するようにインテントを設定-----------*/
//受けっとった値で何分後に通知かを設定
//呼び出す日時を設定する
		/*
Calendar triggerTime = Calendar.getInstance();
switch(position) {
		case 0:
		//"30分後"
		triggerTime.add(Calendar.MINUTE, 30);
		break;
		case 1:
		//"１時間後"
		triggerTime.add(Calendar.HOUR, 1);
		break;
		case 2:
		//"２時間後"
		triggerTime.add(Calendar.HOUR, 2);
		break;
		case 3:
		//"４時間後"
		triggerTime.add(Calendar.HOUR, 4);
		break;
		case 4:
		//"６時間後"
		triggerTime.add(Calendar.HOUR, 6);
		break;
		}
		//設定した日時で発行するIntentを生成
		Intent intent = new Intent(getApplicationContext(), RemindLaterReceiver.class);
		intent.putExtra(RemindLaterReceiver.IntentRecipeJSON, mRecipeJSON);
		PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//日時と発行するIntentをAlarmManagerにセットします
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);
*/