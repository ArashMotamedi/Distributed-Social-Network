package dsn.scheduler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import dsn.bots.facebook.FacebookBot;
import dsn.bots.flickr.FlickrBot;
import dsn.bots.twitter.TwitterBot;
import dsn.bots.linkedin.LinkedinBot;
import dsn.scheduler.Scheduler;
import dsn.scheduler.SchedulerTask;
import dsn.scheduler.iterator.DailyIterator;

public class DSNJob {
	public static FlickrBot flickrBot;
    public static TwitterBot twitterBot;
    public static FacebookBot facebookBot;
    public static LinkedinBot linkedinBot;
    public static DOMXML domxml;
    public static DSNJobHelper dsnJobHelper;
    public static DBInfo dbinfo;
    public static CalendarHelper calendarHelper;
    public static UpdateIndexHelper updateIndexHelper;
    
    private final Scheduler scheduler = new Scheduler();
    private final SimpleDateFormat dateFormat =
        new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
    private final int hourOfDay, minute, second;
	private int minInterval;
	public String user_id, status, sp_id;

    public DSNJob(int hourOfDay, int minute, int second) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.second = second;
        this.minInterval = minInterval;
    }

    public void start(final String[] users, final String[] tokens, final String[] secrets, final Integer instant, 
			final Integer[] currentDateTime, final int intervalInMin) throws IOException {

    	facebookBot = new FacebookBot();
		flickrBot = new FlickrBot();
        twitterBot = new TwitterBot();
        linkedinBot = new LinkedinBot();
        dsnJobHelper = new DSNJobHelper();
        domxml = new DOMXML();
        dbinfo = new DBInfo();
        updateIndexHelper = new UpdateIndexHelper();
   
        scheduler.schedule(new SchedulerTask() {
            public void run() {
            	this.DSNJobMessageStart();
            	
            	System.out.println(users[1]);
            	System.out.println(users[2]);
            	System.out.println(users[3]);
            	System.out.println(users[4]);
            	
                if(!users[1].equals("0")) { 
                	try {
                		try {
                			System.out.println("FACEBOOK=USER:" +users[1]+ "-TOKEN:" +tokens[1]+ "SECRETS:" +secrets[1]);
							facebookBot.runAll(users[1], tokens[1], secrets[1]);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						
						//dbinfo.insertNewStatus(users[1], spId, status, timestamp, sm_sp_id);
                		//dbinfo.insertNewStatus(users[1], facebookBot.getStatus(facebookBot.getXmlFilename(users[1])), 
                		//"fb", facebookBot.getStatusId(facebookBot.getXmlFilename(users[1])));
					} catch (IOException e) { e.printStackTrace();
					} catch (ParserConfigurationException e) { e.printStackTrace();
					} catch (SAXException e) { e.printStackTrace();
					} catch (TransformerException e) { e.printStackTrace();
					}
				}
                
				if (!users[2].equals("0")) {
					try {
						System.out.println("FLICKR=USER:" +users[2]+ "-TOKEN:" +tokens[2]+ "SECRETS:" +secrets[2]);
						flickrBot.runAll(users[2], tokens[2]);
					} catch (IOException e) { e.printStackTrace();
					} catch (ParserConfigurationException e) { e.printStackTrace();
					} catch (SAXException e) { e.printStackTrace();
					} catch (TransformerException e) { e.printStackTrace();
					}
				}
                
				if (!users[3].equals("0")) {
					try { 
						try {
							System.out.println("TWITTER=USER:" +users[3]+ "-TOKEN:" +tokens[3]+ "SECRETS:" +secrets[3]);
							twitterBot.runAll(users[3], tokens[3], secrets[3]);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//dbinfo.insertNewStatus(users[0], twitterBot.getStatus(twitterBot.getXmlFilename(users[3])), 
							//	"tw", null, twitterBot.getStatusId(twitterBot.getXmlFilename(users[3])));
					} catch (IOException e) { e.printStackTrace();
					} catch (ParserConfigurationException e) { e.printStackTrace();
					} catch (SAXException e) { e.printStackTrace();
					} catch (TransformerException e) { e.printStackTrace();
					}
				}
				
				if (!users[4].equals("0")) {
					try { 
						System.out.println("LINKEDIN=USER:" +users[4]+ "-TOKEN:" +tokens[4]+ "SECRETS:" +secrets[4]);
						linkedinBot.runAll(users[4],tokens[4], secrets[4]);
						//System.out.println("LINKEDIN=USER:" +users[4]+ "-TOKEN:" +tokens[4]+ "SECRETS:" +secrets[4]);
						//linkedinBot.runAll(users[4]);
						//dbinfo.insertNewStatus(users[4], linkedinBot(linkedinBot.getXmlFilename(users[4])), 
						//"li", linkedinBot.getStatusId(linkedinBot.getXmlFilename(users[4])));
					} catch (IOException e) { e.printStackTrace();
					} catch (ParserConfigurationException e) { e.printStackTrace();
					} catch (SAXException e) { e.printStackTrace();
					} catch (TransformerException e) { e.printStackTrace();
					}
				}
              
				/*
				try { domxml.DOMXML(users);
				} catch (IOException e) { e.printStackTrace(); }
				*/
				String thistime;
				try {
					thistime = updateIndexHelper.lastIndexRun();
					//System.out.println(thistime);
				} catch (IOException e1) { e1.printStackTrace();
				}
				
				try {
					//DataMiningInterface.main(updateIndexHelper.lastIndexRun());
				//} catch (IOException e1) { e1.printStackTrace();
				} catch (Exception e1) { e1.printStackTrace();
				}
			
				/*
				try { dsnJobHelper.deleteRawData(users);
				} catch (IOException e) { e.printStackTrace();
				}
				*/
		        
				updateIndexHelper.newIndexRun();
				
				dbinfo.updateDateTime(users[0], intervalInMin);
				this.DSNJobMessageEnd();

				if (instant == 1) { scheduler.cancel(); }
            } 
            
			private void DSNJobMessageStart() {
				System.out.println("----------------------------------------");
				System.out.println("DSNJob started: "+ dateFormat.format(new Date()));
                System.out.println("----------------------------------------");
            }
			
			private void DSNJobMessageEnd() {
				System.out.println("----------------------------------------");
                System.out.println("DSNJob ended: "+ dateFormat.format(new Date()));
                System.out.println("----------------------------------------");
            }
        }, new DailyIterator(hourOfDay, minute, second));
        
    }
	
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

    }
}
