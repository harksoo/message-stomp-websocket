package com.telcoware.omp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.telcoware.omp.monitor.model.Item;
import com.telcoware.omp.monitor.model.Lower;
import com.telcoware.omp.monitor.model.Upper;

@Controller
public class MonitorController
{

	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;

	@MessageMapping({"/chat"})
	@SendTo({"/topic/message"})
	public Message sendMessage ( Message message )
	{
		System.out.println( "sysout message :: " + message );

		Message outMsg = new Message();
		outMsg.setMessage( message.getMessage() );
		outMsg.setId( message.getId() );
		outMsg.setTime( new Date() );

		return outMsg;
	}

	@MessageMapping({"/getAll"})
	@SendTo({"/topic/all"})
	public ArrayList<Upper> getAll ( Message message )
	{
		System.out.println( "getAll() :: " + message);
		return getUpperSampe();
	}

	@MessageMapping({"/mask"})
	@SendTo({"/topic/masked"})
	public ArrayList<Upper> mask ( Message message )
	{
		System.out.println( "mask() :: " + message);
		return getUpperSampe2();
	}

	@Scheduled(fixedRate=10000L)
	public void scheduledSystemAlarm()
			throws Exception
	{	
//		DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		Item item = new Item();
		item.setSystem("SW");
		item.setUpper("EAM");
		item.setLower("HELLO");
		item.setItem("HLRCS152A");

		item.setLog("A1363 IPSP CONNECTION STATUS ALARM OCURRED");
		
		long sTime, eTime; 
		double diffTime;
		Calendar today1 = Calendar.getInstance();
		System.out.println( "Start Time :: " 
				+ today1.get(Calendar.HOUR_OF_DAY) + ":"  
				+ today1.get(Calendar.MINUTE) + ":"
				+ today1.get(Calendar.SECOND) + ":"
				+ today1.get(Calendar.MILLISECOND));
		
		sTime = today1.getTimeInMillis();
//		System.out.println("ssTime :: " + sTime);
		
		int count = 0;
		Calendar today = null;
		String currentTime = null;
		
		
//		Date date = new Date();
//		String d = dateFormat.format(date);
		//item.setItem(item);
		
		for ( int i=0; i<1000; i++ ) {
			today = Calendar.getInstance();
			currentTime = today.get(Calendar.YEAR)
					+ "-" + ( today.get(Calendar.MONTH) + 1 ) 
					+ "-" + today.get(Calendar.DATE) 
					+ " " + today.get(Calendar.HOUR_OF_DAY) 
					+ ":" + today.get(Calendar.MINUTE)
					+ ":" + today.get(Calendar.SECOND)
					+ ":" + today.get(Calendar.MILLISECOND);
			item.setDate(currentTime);
			item.setAlarm(AlarmGrade.getRandomGrade());
			this.brokerMessagingTemplate.convertAndSend("/topic/smRealtimeAlarm", item);
			
			count++;
		}

		Calendar today2 = Calendar.getInstance();
		System.out.println( "End Time :: " 
				+ today2.get(Calendar.HOUR_OF_DAY) + ":"  
				+ today2.get(Calendar.MINUTE) + ":"
				+ today2.get(Calendar.SECOND) + ":"
				+ today2.get(Calendar.MILLISECOND));
		
		eTime = today2.getTimeInMillis();
//		System.out.println("eeTime :: " + eTime);
		
		System.out.println("Total count : " + count);
		double diff = 1000.0;
		
		diffTime = (eTime - sTime ) /diff;
		System.out.println("소요 시간 : " + diffTime + " 초");
	}

	@Scheduled(fixedRate=10L)
	public void scheduledSystemAlarmTwo() throws Exception
	{	
		Calendar today1 = Calendar.getInstance();
		String currentTime = today1.get(Calendar.HOUR_OF_DAY) 
				+ ":" + today1.get(Calendar.MINUTE)
				+ ":" + today1.get(Calendar.SECOND)
				+ ":" + today1.get(Calendar.MILLISECOND);
		
		Item item = new Item();
		item.setSystem("SW");
		item.setUpper("EAM");
		item.setLower("HELLO");
		item.setItem("HLRCS152A");
		item.setLog("A1363 IPSP CONNECTION STATUS ALARM OCURRED");
		item.setDate(currentTime);
		item.setAlarm(AlarmGrade.getRandomGrade());
		//item.setItem(item);
		this.brokerMessagingTemplate.convertAndSend("/topic/smRealtimeAlarmTwo", item);
	}

//	@Scheduled(fixedRate=1000L)
//	public void scheduledSystemAlarmVe() throws Exception {		
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		Date date = new Date();
//
//		ArrayList<String> arrd1 = new ArrayList<String>();
//		ArrayList<String> arrd2 = new ArrayList<String>();
//		ArrayList<String> arrd3 = new ArrayList<String>();
//		arrd1.add(dateFormat.format(date));
//		arrd1.add("20");
//		arrd1.add("30");
//		arrd2.add(dateFormat.format(date));
//		arrd2.add("20");
//		arrd2.add("30");
//		arrd3.add(dateFormat.format(date));
//		arrd3.add("20");
//		arrd3.add("30");
//		Monitor a = new Monitor();
//		a.setD1(arrd1);
//		a.setD2(arrd2);
//		a.setD3(arrd3);
//
//		this.brokerMessagingTemplate.convertAndSend("/topic/smRealtimeAlarmVe", a);
//	}
	
	/**
	 * 시스템 모니터링
	 * @throws Exception
	 */
	@Scheduled(fixedRate=10L)
	public void scheduledSystemMonitoring()
			throws Exception
	{
		ArrayList<Upper> uppArr = getUpperSampe();
		this.brokerMessagingTemplate.convertAndSend("/topic/systemMonitoring", uppArr);
	}

	/**
	 * 그래프
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 10L)
	public void scheduledResourceGraph() throws Exception {

		ResourceMonitor resourceMonitor = new ResourceMonitor();

		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		resourceMonitor.setTimestamp(dateFormat.format(date));

		// random 으로 해달 값을 설정한다.
		Random generator = new Random();   

		int random = generator.nextInt(100);


		resourceMonitor.setCpuMax(50);
		resourceMonitor.setCpuAverage(random);

		random = generator.nextInt(100);
		resourceMonitor.setRamMax(80);
		resourceMonitor.setRamAverage(random);

		random = generator.nextInt(100);
		resourceMonitor.setDiskMax(60);
		resourceMonitor.setDiskAverage(random);

		this.brokerMessagingTemplate.convertAndSend("/topic/smResourceGraph", resourceMonitor);
	}

	private ArrayList<Upper> getUpperSampe()
	{
		ArrayList<Upper> uppArr = new ArrayList<Upper>();
		
		for ( int i = 0; i < 2; ++i ) {
			Upper upp = new Upper();
			upp.setUpper("UPP" + i);
			ArrayList<Lower> lowArr = new ArrayList<Lower>();
			for ( int j = 0; j < 3; ++j ) {
				Lower low = new Lower();
				low.setLower("LOW" + i + "_" + j);
				ArrayList<Item> itemArr = new ArrayList<Item>();
				for ( int k = 0; k < 3; ++k ) {
					Item item1 = new Item();
					item1.setItem("ITEM" + i + "_" + j + "_" + k);
					item1.setGrade("" + getRandomGrade2());
					item1.setMask("0");

					itemArr.add(item1);
				}
				low.setValue(itemArr);
				lowArr.add(low);
			}
			upp.setValue(lowArr);
			uppArr.add(upp);
		}
		return uppArr;
	}
	
	// masked request
	private ArrayList<Upper> getUpperSampe2()
	{
		ArrayList<Upper> uppArr = new ArrayList<Upper>();
		for (int i = 0; i < 1; ++i) {
			Upper upp = new Upper();
			upp.setUpper("UPP" + i);
			ArrayList<Lower> lowArr = new ArrayList<Lower>();
			for (int j = 0; j < 3; ++j) {
				Lower low = new Lower();
				low.setLower("LOW" + i + "_" + j);
				ArrayList<Item> itemArr = new ArrayList<Item>();
				for (int k = 0; k < 3; ++k) {
					Item item1 = new Item();
					item1.setItem("ITEM" + i + "_" + j + "_" + k);
					//					if ((i == 2) && (j == 0) && (k == 1)) {
					//						item1.setGrade("0");
					//						item1.setMask("1");
					//					} else if ((i == 3) || (i == 4)) {
					//						item1.setGrade(getRandomGrade2());
					//						item1.setMask("0");
					//					} else {
					//						item1.setGrade("0");
					//						item1.setMask("0");
					//					}
					item1.setGrade(""+getRandomGrade2());
					item1.setMask("0");

					itemArr.add(item1);
				}
				low.setValue(itemArr);
				lowArr.add(low);
			}
			upp.setValue(lowArr);
			uppArr.add(upp);
		}
		return uppArr;
	}

	private int getRandomGrade2(){

		Random generator = new Random();   

		int random = generator.nextInt(4);

		return random;
	}
	
}