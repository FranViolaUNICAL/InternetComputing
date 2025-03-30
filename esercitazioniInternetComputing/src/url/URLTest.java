package url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URLTest {
    public static void main(String[] args) {
        try{
            URL url = new URL("http://www.w3.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            boolean more = true;
            while(more){
                String line = in.readLine();
                if(line == null){
                    more = false;
                }else{
                    System.out.println(line);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
