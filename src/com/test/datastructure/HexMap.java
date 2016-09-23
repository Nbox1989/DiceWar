package com.test.datastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

@SuppressLint("WorldReadableFiles")
public class HexMap {
	public List<HexRegion> HexRegionList=new ArrayList<HexRegion>();
	
	public HexRegion findRegionByTileId(int hexTileId)
	{
		for (HexRegion hexRegion : HexRegionList) {
			for (HexTile hexTile : hexRegion.HexTileList) {
				if(hexTile.index==hexTileId)
				{
					return hexRegion;
				}
			}
		}
		return null;
	}
	
	public List<Integer> findHexTileIdList(int RegionIndex)
	{
		List<Integer> idList=new ArrayList<Integer>();
		for(HexRegion hr:HexRegionList)
		{
			if(hr.index==RegionIndex)
			{
				for(HexTile ht :hr.HexTileList)
				{
					idList.add(Integer.valueOf(ht.index));
				}
			}
		}
		return idList;
	}
	
	public boolean regionsAdjacent(int index1,int index2)
	{
		HexRegion hr1=findRegionByIndex(index1);
		HexRegion hr2=findRegionByIndex(index2);
		for(HexTile ht1:hr1.HexTileList)
		{
			for(HexTile ht2:hr2.HexTileList)
			{
				if(ht1.isAjacentTo(ht2))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean regionsAdjacent(HexRegion hr1,HexRegion hr2)
	{
		for(HexTile ht1:hr1.HexTileList)
		{
			for(HexTile ht2:hr2.HexTileList)
			{
				if(ht1.isAjacentTo(ht2))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void getAdjacentRegionList()
	{
		for(HexRegion hr1 :HexRegionList)
		{
			for(HexRegion hr2:HexRegionList)
			{
				if(hr1==hr2)
				{
					continue;
				}
				if(hr1.adjacentRegionList.contains(hr2))
				{
					continue;
				}
				if(regionsAdjacent(hr1.index, hr2.index))
				{
					hr1.adjacentRegionList.add(hr2);
					hr2.adjacentRegionList.add(hr1);
				}
			}
		}
	}
	
	public HexRegion findRegionByIndex(int index)
	{
		for(HexRegion hr :HexRegionList)
		{
			if( hr.index==index)
			{
				return hr;
			}
		}
		return null;
	}
	
	//广度优先遍历 Orz。。。。。如果用队列实现是个好办法，但是懒得写了
	public int getMaxAdjacentRegions(int color)
	{
		List<HexRegion> hrList=getRegionListByColor(color);
		List<HexRegion> curhrList=new ArrayList<HexRegion>(); 
		int maxNum=0;
		for(HexRegion hr :hrList)
		{
			curhrList.removeAll(curhrList);
			curhrList.add(hr);
			for(HexRegion temphr:hr.adjacentRegionList)
			{
				if(hrList.contains(temphr))
				{
					curhrList.add(temphr);
				}
			}
			int index=0;
			while (index<curhrList.size())
			{
				for(HexRegion temphr:curhrList.get(index).adjacentRegionList)
				{
					if(hrList.contains(temphr))
					{
						if(!curhrList.contains(temphr))
						{
							curhrList.add(temphr);
						}
					}
				}
				index++;
			}
			maxNum=Math.max(maxNum, curhrList.size());
			
		}
		return maxNum;
	}
	
	public List<HexRegion> getRegionListByColor(int color)
	{
		List<HexRegion> hrList=new ArrayList<HexRegion>();
		for(HexRegion hr :this.HexRegionList)
		{
			if(hr.color==color)
			{
				hrList.add(hr);
			}
		}
		return hrList;
	}
	
	public static boolean MapToFile(Context context,HexMap hexmap ,String outputFileName) throws FileNotFoundException
	{
		File file = new File(outputFileName);   
		@SuppressWarnings("deprecation")
		FileOutputStream fos =context.openFileOutput(outputFileName,Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
	    XStream xstream = new XStream(new DomDriver());
	    xstream.alias("HexMap", HexMap.class);      		     
        xstream.alias("HexRegion", HexRegion.class);
        xstream.alias("HexTile", HexTile.class); 
	    xstream.toXML(hexmap, fos);  
	    	    
		return true;
	}
	
	public static HexMap FileToMap(Context context,String inputFileName) throws IOException 
	{
		FileInputStream is = context.openFileInput("tempmap.xml"); 
	    XStream xstream = new XStream(new DomDriver());
	    xstream.alias("HexMap", HexMap.class);      		     
        xstream.alias("HexRegion", HexRegion.class);
        xstream.alias("HexTile", HexTile.class);
        return (HexMap)xstream.fromXML(is);
	}
	
	public static HexMap FileToMap(InputStream is)
	{
		XStream xstream = new XStream(new DomDriver());
	    xstream.alias("HexMap", HexMap.class);      		     
        xstream.alias("HexRegion", HexRegion.class);
        xstream.alias("HexTile", HexTile.class);
        return (HexMap)xstream.fromXML(is);
	}
}
