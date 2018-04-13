
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;


public class SearchingTest {
    WebDriver driver;
    MainPage mainPage;

    @BeforeTest
    public void Initialize(){
        driver = new FirefoxDriver();
        mainPage = new MainPage(driver);
        mainPage.get("https://market.yandex.ru/");
        Assert.assertEquals(driver.getTitle(),"Яндекс.Маркет — выбор и покупка товаров из проверенных интернет-магазинов");
    }

    @Test
    public void CheckSearching() {
        int falseResults = 0;
        String shopName = "", shopPrice = "";
        String query = "Xiaomi Redmi Note 4X";
        List<Item> results = mainPage.search(query); //Получить список найденных товаров

        for (Item value : results) { //Обход каждого элемента в списке
            if (value.getName().contains(query.toLowerCase())) break; //Если запрос встречается в названии товара - выйти из цикла
            falseResults++; //Считать количество неподходящих запросу товаров
        }

        if (falseResults == results.size()){ //Если все товаров не подходят - вывести сообщение
            System.out.println("Запрашиваемый продукт не найден в списке найденных товаров.");
        }

        WebElement sortPrice = driver.findElement(By.cssSelector("div[data-bem*=\"aprice\"]"));//Найти ссылку сортировки по цене и кликнуть на нее
        sortPrice.click();

        (new WebDriverWait(driver,10)) //Ожидать загрузки таблицы отсортированных товаров
                .until(ExpectedConditions.attributeContains(By.cssSelector("div[class *= \"n-filter-applied-results__content preloadable i-bem preloadable_js_inited\"]"),"style","height: auto;"));

        WebElement firstElementAfterSort = driver.findElement(By.cssSelector("a[class^=\"link n-link_theme_blue\"]"));//Выбрать первый товар
        firstElementAfterSort.click();

        try{
            (new WebDriverWait(driver,10)) //Ожидать отображения таблицы с магазинами
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".n-product-top-offers-list__body > div")));
        }
        catch (TimeoutException Ignore){ //Игнорировать исключение таймаута
        }

        List<WebElement> shopList = driver.findElements(By.cssSelector(".n-product-top-offers-list__body > div")); //Получить список магазинов

        if (shopList.size() == 0) {
            System.out.println("Магазины не найдены");
        }
        else{
            System.out.println("Список магазинов:");
            for (WebElement ShopInfo : shopList) //Обход каждого элемента в списке
            {
                //Имеется ситуация, когда название магазина и цена не являются ссылками. Необходимо отловить исключение и изменить критерий поиска
                try{
                    shopName = ShopInfo.findElement(By.cssSelector("div[class^=\"n-product-top-offer__item n-product-top-offer__item_type_shop\"]  a")).getText();
                }
                catch (NoSuchElementException e){
                    shopName = ShopInfo.findElement(By.cssSelector("div[class^=\"n-product-top-offer__item n-product-top-offer__item_type_shop\"]  span")).getText();
                }

                try {
                    shopPrice = ShopInfo.findElement(By.cssSelector("div[class^=\"n-product-top-offer__item n-product-top-offer__item_type_price\"]  a")).getText();
                }
                catch (NoSuchElementException Ignore){
                    shopPrice = ShopInfo.findElement(By.cssSelector("div[class^=\"n-product-top-offer__item n-product-top-offer__item_type_price\"]  span")).getText();
                }

                System.out.format("%35s %16s", shopName, shopPrice); //Вывод пар Название магазина - Цена
                System.out.println();
            }
        }

    }

    @AfterTest
    public void Shutdown()
    {
        driver.close();
    }
}
