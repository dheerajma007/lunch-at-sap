package dheeraj.lunchsap;

import android.content.Context;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by I320939 on 22/07/2016.
 */

public class FileAccess {
    Context currentContext;
    File workFile;
    final String fileName = "Menu.xlsx";
    final String urlStr = "https://portal.wdf.sap.corp/irj/go/km/docs/corporate_portal/Human Resources for SAP/Benefits/Labs India/MENU.xls.xlsx/";
    final int SYNC_SUCCESS = 0;
    final int SYNC_FAIL = -1;
    public FileAccess(Context context) {
        currentContext = context;
        workFile = new File(context.getFilesDir() + "/Menu.xlsx");
    }

    int syncFile()
    {
        URLConnection urlConnection = null;

        try{
            URL url = new URL(urlStr);
            urlConnection = url.openConnection();
            int length = urlConnection.getContentLength();
            InputStream in = urlConnection.getInputStream();
            workFile.createNewFile();
            FileOutputStream out = new FileOutputStream(workFile);
            int bytesRead =  -1;
            byte buffer[] = new byte[4096];
            while((bytesRead = in.read(buffer)) != -1)
                out.write(buffer, 0, bytesRead);
            out.close();
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return SYNC_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return SYNC_FAIL;
        }
        return SYNC_SUCCESS;
    }
}
