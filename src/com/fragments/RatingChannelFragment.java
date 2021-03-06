package com.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import source.APListAdapter;
import source.ChListAdapter;
import source.MyFragment;
import source.MyView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wifi_zombie.R;

public class RatingChannelFragment extends MyFragment {
	private View thisView;
	private ArrayList<int[]> chlist;
	public static final int maxRating = 100;
	public static final double overlappingRate = 5.5;
	public static final int listItemNum = 4;
	private ListView chlistview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.ratingchannel, null);
		chlistview = (ListView)thisView.findViewById(R.id.ratingchannel_channellist);
        return thisView;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	public void updateWifiData() {
		super.updateWifiData();
		chlist = new ArrayList<int[]>();
		calcChannel();
		ChListAdapter adapter = new ChListAdapter(getActivity(), R.layout.chlist_item, chlist);
		chlistview.setAdapter(adapter);
		chlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
			}
			
		});
	}
	
	void calcChannel()
	{
		// 戚暗 蟹掻拭 郊荷切 ばばばばばばばばばば
		int[] ch = new int[MyView.chMax5G+1];
		int[] chOverlapping = new int[MyView.chMax5G+1];
		int[] chRating = new int[MyView.chMax5G+1];
		// 段奄鉢
		for(int i=0 ; i<=MyView.chMax5G ; i++) ch[i] = 0;
		for(int i=0 ; i<=MyView.chMax5G ; i++) chOverlapping[i] = 0;
		for(int i=0 ; i<=MyView.chMax5G ; i++) chRating[i] = 0;
		
		for(int i=0 ; i<super.wifidata.getSize() ; i++)
		{
			ch[super.wifidata.getWifiInfoData(i).getChannel()]++;	// 廃 channel拭 違帖澗 AP
			// channel overlapping
			if(super.wifidata.getWifiInfoData(i).getChannel()+1 <= MyView.chMax5G)
				chOverlapping[super.wifidata.getWifiInfoData(i).getChannel()+1]++;
			if(super.wifidata.getWifiInfoData(i).getChannel()+2 <= MyView.chMax5G)
				chOverlapping[super.wifidata.getWifiInfoData(i).getChannel()+2]++;
			if(super.wifidata.getWifiInfoData(i).getChannel()-1 >= 1)
				chOverlapping[super.wifidata.getWifiInfoData(i).getChannel()-1]++;
			if(super.wifidata.getWifiInfoData(i).getChannel()-2 >= 1)
				chOverlapping[super.wifidata.getWifiInfoData(i).getChannel()-2]++;
			
			// rating 域至
			chRating[super.wifidata.getWifiInfoData(i).getChannel()]+=((3*(-1)*super.wifidata.getWifiInfoData(i).getStrength())/100);
			if(super.wifidata.getWifiInfoData(i).getChannel()+1 <= MyView.chMax5G)
				chRating[super.wifidata.getWifiInfoData(i).getChannel()+1]+=((2*(-1)*super.wifidata.getWifiInfoData(i).getStrength())/100);
			if(super.wifidata.getWifiInfoData(i).getChannel()+2 <= MyView.chMax5G)
				chRating[super.wifidata.getWifiInfoData(i).getChannel()+2]+=((1*(-1)*super.wifidata.getWifiInfoData(i).getStrength())/100);
			if(super.wifidata.getWifiInfoData(i).getChannel()-1 >= 1)
				chRating[super.wifidata.getWifiInfoData(i).getChannel()-1]+=((2*(-1)*super.wifidata.getWifiInfoData(i).getStrength())/100);
			if(super.wifidata.getWifiInfoData(i).getChannel()-2 >= 1)
				chRating[super.wifidata.getWifiInfoData(i).getChannel()-2]+=((1*(-1)*super.wifidata.getWifiInfoData(i).getStrength())/100);
		}
		// sorting遂 var
		ArrayList<int[]> list = new ArrayList<int[]>();	
		ArrayList<Integer> perList = new ArrayList<Integer>();	
		for(int i=1 ; i<MyView.chMax5G ; i++)
		{
			if ((i <= 14) || (i == 36) || (i == 40) || (i == 44) || (i == 48) || (i == 52)
					|| (i == 56) || (i == 60) || (i == 64) || (i == 100)
					|| (i == 104) || (i == 108) || (i == 112) || (i == 116)
					|| (i == 120) || (i == 124) || (i == 128) || (i == 132)
					|| (i == 136) || (i == 140) || (i == 149) || (i == 153)
					|| (i == 157) || (i == 161) || (i == 165)) 
			{
				// channel num, %, aps, overlapping
				int per = (int)(maxRating - chRating[i]*overlappingRate);
				list.add(new int[] {i, per >= 0 ? per : 0, ch[i], chOverlapping[i]});
//				Log.i("wifi zombie", i+" : "+(int)(maxRating - chRating[i]*overlappingRate)+"% "+ch[i]+"aps "+chOverlapping[i]+"over");
				perList.add(per);
			}
		}
		//sorting!!
		Collection col = perList;
		int max = list.size();
		for(int i=0 ; i<max ; i++)
		{
			int num = perList.indexOf((Integer)Collections.min(col));
			chlist.add(list.get(num));
			list.remove(num);
			perList.remove(num);
		}
	}
}
