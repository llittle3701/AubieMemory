package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class HighScoreManager {

    private String scoreFile = "high_score.xml";
    private final String ERROR_TAG = "error";
    private final String DEBUG_TAG = "debug";

    private Integer getHighScore(Context context) {
        String rawXML = "";
        ArrayList<String> data = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(scoreFile);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            rawXML = new String(inputBuffer);
            isr.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            Log.e(ERROR_TAG, "FileNotFoundException in getHighScore : \n" + e.getMessage());
            return 0;
        }
        catch (IOException e) {
            Log.e(ERROR_TAG, "IOException in getHighScore: \n" + e.getMessage());
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        try {
            xpp.setInput(new StringReader(rawXML));
        }
        catch(XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(DEBUG_TAG, "Start document");
            }
            else if (eventType == XmlPullParser.START_TAG) {
                Log.d(DEBUG_TAG, "Start tag: " + xpp.getName());
            }
            else if (eventType == XmlPullParser.END_TAG) {
                Log.d(DEBUG_TAG, "End tag: " + xpp.getName());
            }
            else if (eventType == XmlPullParser.TEXT) {
                data.add(xpp.getText());
            }
            try {
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
            }
            catch (IOException e) {
                Log.e(ERROR_TAG, "IOException in getHighScore: \n" + e.getMessage());
            }
        }

        String highScore = data.get(0);
        return Integer.parseInt(highScore);
    }

    private void setHighScore(Context context, Integer score) {
        try{
            FileOutputStream fos = context.openFileOutput(scoreFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "data");
            xmlSerializer.startTag(null, "highScore");
            xmlSerializer.text(score.toString());
            xmlSerializer.endTag(null, "highScore");
            xmlSerializer.endTag(null, "data");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fos.write(dataWrite.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.e(ERROR_TAG, "FileNotFoundException in setHighScore : \n" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.e(ERROR_TAG, "IllegalArgumentException in setHighScore : \n" + e.getMessage());
        }
        catch (IllegalStateException e) {
            Log.e(ERROR_TAG, "IllegalStateException in setHighScore : \n" + e.getMessage());
        }
        catch (IOException e) {
            Log.e(ERROR_TAG, "IOException in setHighScore : \n" + e.getMessage());
        }
    }

}
