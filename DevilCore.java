package net.jb.devilcore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author JavaBeast
 * @version 1.0
 */
public class DevilCore
{
    private final String version = "v1.0";

    /**
     * the prefix of the system using the DEVILCORE
     */
    private final String prefix;

    /**
     * the folder of the 'devilcore.yml' file.
     */
    private final String dataFolder;


    /**
     * the content which will be added to the 'devilcore.yml' file when the system is getting initialized at the first
     * time.
     */
    private final String fileContent = "# DEVILCORE " + version + "\n" +
            "# @author Java3east \n" +
            "# contact JavaBeast#3129 \n" +
            "# DEVILCORE is made for a simple data down / upload. This data can contain performance or update \n" +
            "# information, as well as  datasets used by the program. \n" +
            "# To allow this data up / download you have to set the following to 'true'.\n" +
            "use=false\n" +
            "\n" +
            "# if the following option is set to 'true' the program will be able to upload any data.\n" +
            "# HANDLE WITH CARE.\n" +
            "upload=false\n" +
            "\n" +
            "# if 'allow download' is set to 'true' the program will be able to download content from any website,\n" +
            "# as long as the file is smaller than 'maximum file size'.\n" +
            "download=true\n" +
            "\n" +
            "# This value contains the maximum size of a file to down / upload in bytes.\n" +
            "size=512\n" +
            "\n" +
            "# If you have any questions about this system contact JavaBeast#3129 on discord.\n" +
            "# You can find the src code on github : https://github.com/java3east. \n"
            ;

    private final boolean use;
    private final boolean allowUpload;
    private final boolean allowDownload;
    private final int maxSize;


    /**
     * Init the DEVILCORE system
     * @param prefix the prefix of the executing program
     * @param dataFolder the folder where the 'devilcore.yml' file will be created.
     */
    public DevilCore(String prefix, String dataFolder)
    {
        boolean use = false;
        boolean allowUpload = false;
        boolean allowDownload = false;
        int maxSize = 0;

        // set values
        this.prefix = prefix;
        this.dataFolder = dataFolder;

        // check if the data folder exists if not -> create it
        File folder = new File(dataFolder);
        if(!folder.exists())
            if(!folder.mkdirs())
                Print("failed to create data folder.");
            else
                Print("failed to create data folder.");

        // check if there is a devilcore.yml file if not -> create it and write default content.
        File dcf = new File(dataFolder + "/devilcore.yml");
        if(!dcf.exists())
            try
            {
                if (!dcf.createNewFile())
                    Print("failed to create 'devilcore.yml'.");
                else
                    Print("created 'devilcore.yml'.");


                FileWriter writer = null;

                try{
                    writer = new FileWriter(dcf);

                    writer.write(fileContent);
                    writer.flush();
                    writer.close();

                    Print("added content to 'devilcore.yml'.");
                }catch (IOException ex)
                {
                    Print("caught error while writing to 'devilcore.yml' : " +
                            ex.getMessage());
                }
            }catch (IOException ex)
            {
                Print("caught error while creating 'devilcore.yml' : " +
                        ex.getMessage());
            }

        try
        {
            Scanner scanner = new Scanner(dcf);

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if(line.startsWith("use="))
                {
                    use = Boolean.parseBoolean(line.split("=")[1].replace(" ", ""));
                }else if(line.startsWith("upload="))
                {
                    allowUpload = Boolean.parseBoolean(line.split("=")[1].replace(" ", ""));
                }else if(line.startsWith("download="))
                {
                    allowDownload = Boolean.parseBoolean(line.split("=")[1].replace(" ", ""));
                }else if(line.startsWith("size="))
                {
                    try{
                        maxSize = Integer.parseInt(line.split("=")[1].replace(" ", ""));
                    }catch (NumberFormatException exception)
                    {
                        Print("unable to read maximum file size ->" +
                                "0 bytes");
                    }
                }
            }
        }catch (IOException exception)
        {
            Print("failed to read file 'devilcore.yml'.");
        }


        this.use = use;
        this.allowUpload = allowUpload;
        this.allowDownload = allowDownload;
        this.maxSize = maxSize;

        Print("use="+use);
        Print("upload="+allowUpload);
        Print("download="+allowDownload);
        Print("size="+maxSize);
    }

    public String Download(String http)
    {

        if(!use || !allowDownload)
        {
            Print( "downloading is disabled.");
            return null;
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(http).openStream())) {
             byte[] dataBuffer = new byte[maxSize];
             Print("receiving data from "+http + ". (" + in.read(dataBuffer) + " bytes).");
            return new String(dataBuffer).trim();
        } catch (IOException e)
        {
            Print("error while receiving data : " + e.getMessage());
        }

        return null;
    }

    private void Print(String s)
    {
        System.out.println(prefix + "[DEVILCORE] [" + version + "] "+s);
    }

}
