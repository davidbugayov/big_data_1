package generator;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;

import java.io.*;
public class log {

    /*
    * Генератор рандомного лога apache сервера
    * Если не указано в параметрах запуска, имя выходного файла по умолчания <p>apachelog.log</p>
    * */
    private String filename = "apachelog.log";


    /*
    * Количество строк в логе apache
    * Если не указано в параметрах запуска, значение по умолчанию <p>100</p>
    * */
    private int countline = 100;

    /*
    * Шанс генерации в % невалидной строки в логе apache
    * Значение <p>0</p> - нет невалидных строк
    * Значение <p>100</p> - все строки невалидны
    * */
    private int errorchance = 5;


    /*
    * Юзер агенты для генерации лога apache
    * */
    private String[] useragents = {
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)",
            "Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.50 Safari/534.24",
            "Mozilla/5.0 (X11; Linux x86_64; rv:6.0a1) Gecko/20110421 Firefox/6.0a1"
    };

    /*
    * Массив со списком url, который генерируются в логе apache
    * */
    private String[] url = {"/wp-amin", "/wp-content", "/wp-includes", "/index.php", "/readme.html", "/robots.txt", "/wp-cron.php"};

    /*
    * Коды ответа каждого запроса и их шанс генерации в логе apache
    * */
    private int[]response_codes = {200,404,503};

    /*
    * Методы обращения к страницам веб-сервера в логе
    * */
    private String[] methods = {"GET","POST","PUT","DELETE"};

    /*
    * Рефереры с которых обращаются к страницам в логе
    * */
    private String[] referers = {"https://google.ru/", "https://yandex.ru/", "https://rkn.gov.ru/"};

    /*
    * Функция генерации ip адреса из определенного диапазона
    * Диапазон от <p>192.168.1.200</p> до <p>192.168.1.254</p>
    * @return ip адрес
    * */

    private String generateip(){
        String ip = "192.168.1.";
        Random rand = new Random();
        int random = 200+rand.nextInt(55);
        return ip + random;
    }
    /*
    * Формат времени для генерации в логе
    * */

    SimpleDateFormat dateFormat = new SimpleDateFormat("d/W/y:k:m:ss Z");

    /*
    * Формат лога <p>ip - - [число/месяц/год:час:минута:сек +utc] "матод_запроса путь_запроса HTTP/1.1" код_ответа количество_байт "реферер" "юзерагент"<p>
    * */
    public String generatelog()
    {
        String badline = "BADLINE\n";
        String result = "";
        while(this.countline!=0)
        {
            Random rand = new Random();
            if(rand.nextInt(100)<5) {
                    result+=badline;
            }else{
                String ip = this.generateip();
                String method = this.methods[rand.nextInt(this.methods.length)];
                String path = this.url[rand.nextInt(this.url.length)];
                String referer = this.referers[rand.nextInt(this.referers.length)];
                String useragent = this.useragents[rand.nextInt(this.useragents.length)];
                int response = this.response_codes[rand.nextInt(this.response_codes.length)];
                int count = rand.nextInt(65537);
                result += ip + " - - [" + dateFormat.format(new Date()) + "] \"" + method + " " + path + " HTTP/1.1\" " + response + " " + count + " " + referer + " " + useragent + " \n";
            }
            this.countline--;
        }
        //System.out.println(result);
        this.writelog(result);
        return result;
    }

    /*
    * @param result - переменная с содержимым файла
    * */
    private void writelog(String result)
    {
        try(FileWriter writer = new FileWriter(this.filename, false))
        {
            writer.write(result);
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
