import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Results 
{

	static void show_browse(String adding)
	{
		 
        try
        { //проверка на то, поддерживается ли класс и возможно ли открытие браузера
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) 
            {
            	//возвращает экземпляр контекста браузера с указанным URI
                Desktop.getDesktop().browse(new URI("https://ru.wikipedia.org/w/index.php?curid="+URLEncoder.encode(adding, StandardCharsets.UTF_8)));
            }
        } 
        catch (IOException e) 
        {
            throw new RuntimeException(e);
        } 
        catch (URISyntaxException e) 
        {
            throw new RuntimeException(e);
        }
	}
}
