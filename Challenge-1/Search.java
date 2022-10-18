import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class Search {

  public static void main(String[] args) throws IOException {
    String emailID;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the email ID: ");
    emailID = reader.readLine();

    URL url = new URL("https://www.ecs.soton.ac.uk/people/" + emailID);
    URLConnection urlConnection = url.openConnection();
    BufferedReader webReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

    String contents = "";
    String name = "";
    String webpage = "";
    String description = "";
    String email = "";
    String jobTitle = "";

    while ((contents = webReader.readLine()) != null) {

      if (contents.contains("\"@type\": \"Person\"")) {
        //gets name
        contents = webReader.readLine();
        contents = contents.replaceAll("\"", "").replaceAll(",", "");
        name = contents.split(":")[1];
        //gets webpage url
        contents = webReader.readLine();
        contents = contents.replaceAll("\"", "").replaceAll(",", "");
        webpage = contents.split(":")[1]+contents.split(":")[2];
        //gets description
        contents = webReader.readLine();
        contents = contents.replaceAll("\"", "").replaceAll(",", "");
        description = contents.split(":")[1];
        //gets email
        contents = webReader.readLine();
        contents = contents.replaceAll("\"", "").replaceAll(",", "");
        email = contents.split(":")[1];
        //gets job title
        contents = webReader.readLine();
        contents = contents.replaceAll("\"", "").replaceAll(",", "");
        jobTitle = contents.split(":")[1];
        break;
      }
    }
    webReader.close();
    System.out.println("Name:" + name);
    System.out.println("Job Title:" + jobTitle);
    System.out.println(description);
    System.out.println("Contact email:" + email);
    System.out.println("More info:" + webpage);
  }
}