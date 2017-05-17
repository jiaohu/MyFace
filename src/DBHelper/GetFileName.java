package DBHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class GetFileName
{
    public static String [] getFileName(String path)
    {
        File file = new File(path);
        String [] fileName = file.list();
        return fileName;
    }
    public static void getAllFileName(String path,ArrayList<String> fileName)
    {
        File file = new File(path);
        File [] files = file.listFiles();
        String [] names = file.list();
        if(names != null)
        fileName.addAll(Arrays.asList(names));
        for(File a:files)
        {
            if(a.isDirectory())
            {
                getAllFileName(a.getAbsolutePath(),fileName);
            }
        }
    }
    public static void main(String[] args) throws IOException
    {
        String [] fileName = getFileName("./Yale");
        for(String name:fileName)
        {
            System.out.println(name);
        }
        System.out.println("--------------------------------");
        ArrayList<String> listFileName = new ArrayList<String>(); 
        getAllFileName("./Yale",listFileName);
        int id =1;
        String url = "./data/Yaleurl.txt";
        BufferedWriter res = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(url, true)));
        for(String name:listFileName)
        {
        	res.append(id + " " + name+"\n");
            System.out.println(name);
            id++;
        }
         res.close();
    }
}
