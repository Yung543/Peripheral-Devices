package dick;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DiskInfo {

    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec(
                "wmic diskdrive get Model,InterfaceType,Size"
            );

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}