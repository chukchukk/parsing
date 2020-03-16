import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;

//Все сохраняется в папке photos на диске D
public class parsMirea {
    public static void main(String[] args) throws IOException {
        String mainUrl="https://student.mirea.ru/media/photo";
        String toUrl="https://student.mirea.ru";
        Document document=Jsoup.connect(mainUrl).get(); //Подключаемся к начальной ссылке
        //Ищем все нужные нам a-элементы
        Elements aElements=document.getElementsByAttributeValue("class","u-link-v2");
        aElements.forEach(aElement->{
            //получили ссылку на альбом
            String albom_url=toUrl+aElement.attr("href");
            System.out.println(albom_url);
            try {
                //переходим по ссылке в альбом
                Document second_document=Jsoup.connect(albom_url).get();
                //название альбома
                Elements h2Element=second_document.getElementsByAttributeValue("class","text-uppercase u-heading-v2__title g-mb-5");
                //ссылка на картинку
                Elements imgElements=second_document.getElementsByAttributeValue("class","img-fluid u-block-hover__main--grayscale u-block-hover__img");
                imgElements.forEach(imgElement->{
                    //конечная +ссылка на на фото
                    String finalUrl=imgElement.attr("src");
                    //удаление из названия альбома кавычек
                    String h2=h2Element.text().replace('\"','_');
                    //имя папки
                    String folderName="d:\\photos\\"+h2;
                    File file = new File(folderName);
                    if(!file.exists())
                    {
                        file.mkdir();
                    }
                    download(h2,0,finalUrl);
                });
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
    }

    private static void download(String h2, Integer i, String url_plus){
        try {
            BufferedImage image =null;


            URL url = new URL("https://student.mirea.ru"+url_plus);
            image = ImageIO.read(url);

            if (image != null){
                ImageIO.write(image, "jpg", new File("d:\\photos\\"+h2+"\\"+i+".jpg"));
                i++;
            }
        }
        catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
