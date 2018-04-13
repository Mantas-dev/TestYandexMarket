import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private final WebDriver mainPageDriver;

    public MainPage(WebDriver driver) {
        mainPageDriver = driver;
    }

    public void get(String url){
        mainPageDriver.get(url);
    }

    public List<Item> search(String query){
        List<Item> result = new ArrayList<Item>();

        WebElement inputSearch = ((FirefoxDriver) mainPageDriver).findElementById("header-search"); //Найти строку поиска и ввести запрос
        inputSearch.sendKeys(query);

        WebElement button = ((FirefoxDriver) mainPageDriver).findElementByCssSelector(".search2__button"); //Найти кнопку отправки запроса и кликнуть
        button.click();

        List<WebElement> names  = mainPageDriver.findElements(By.cssSelector("div[class*=\"__header\"] > div[class^=\"n-snippet-\"] > a")); //Получить список товаров
        List<WebElement> prices = mainPageDriver.findElements(By.cssSelector("div[class*=\"__main-price\"]")); //Получить список цен

        for (int i = 0; i < names.size(); i++){
            result.add(new Item(names.get(i).getText(), prices.get(i).getText())); //Заполнить список значениями Имя товара - Цена
        }

        return result; //Вернуть список
    }

}
