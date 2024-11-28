package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.TestCases;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class Wrappers {
    ChromeDriver driver = TestCases.driver;
    WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
    ObjectMapper mapper = new ObjectMapper();


    public void ScrapingHomePage(){
        driver.get("https://www.scrapethissite.com/pages/");
    }

    public void ClickHockeyTeams(){
        WebElement HockeyLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Hockey Teams: Forms, Searching and Pagination']")));
        HockeyLink.click();
    }

    public long EpochTime(){
        long epoch = System.currentTimeMillis()/1000;
        return epoch;
    }

    public void ClickPageNo(int PageNo){
       WebElement Pageno = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class=\"pagination\"]/li/a[normalize-space(text())='"+PageNo+"']")));
       Pageno.click();
    }

    public List<Map<String,Object>> GetTeamNameYearAndWinPercent(float LessThanPercent,int NoOfPages){
        List<Map<String,Object>> TeamsDetails = new ArrayList<Map<String,Object>>();
        for(int page=1;page<=NoOfPages;page++){
            ClickPageNo(page);
            List<WebElement> TeamWinPercentage  = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tbody/tr/td[6][text()<"+LessThanPercent+"]")));
            for(int i = 0 ; i<TeamWinPercentage.size(); i++){
                HashMap<String,Object> TeamDetail = new HashMap<String,Object>();
                TeamDetail.put("Epoch Time of Scrape", EpochTime());
                //get team name
                WebElement GetTeamName = TeamWinPercentage.get(i).findElement(By.xpath("./preceding-sibling::td[5]"));
                TeamDetail.put("Team Name", GetTeamName.getText());
                //get year
                WebElement GetYear = TeamWinPercentage.get(i).findElement(By.xpath("./preceding-sibling::td[4]"));
                TeamDetail.put("Year",GetYear.getText());
                TeamDetail.put("Win %", TeamWinPercentage.get(i).getText());
                
                TeamsDetails.add(TeamDetail);
            }
        }
       return TeamsDetails;
    }

    public void ConvertToJsonAndPrint(Object Object) throws JsonProcessingException{
        String JsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Object);
        System.out.println(JsonString);
    }

    public void CreateJsonFile(Object Object,String FileName) throws StreamWriteException, DatabindException, IOException{
        String userDir = System.getProperty("user.dir");
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userDir +"\\Output\\"+FileName+""), Object);
    }

    public void ClickOscarWinningFilms(){
        WebElement OscarWinningFilms = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Oscar Winning Films: AJAX and Javascript']")));
        OscarWinningFilms.click();
    }

    public List<Map<String,Object>> GetTopMoviesDetailsInEachYear(int TopNoOfMoviesInEachYear) throws InterruptedException{
        List<Map<String,Object>> MoviesDetails = new ArrayList<Map<String,Object>>();

        //Get Years
        List<WebElement> Years = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//a[@class='year-link']")));

        //Each Year
        for(int i=0;i<Years.size();i++){
            Years.get(i).click();
            Thread.sleep(3000);

            //Get Table 
            List<WebElement> MovieList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tbody/tr")));

            //Each Row
            for(int row=0;row<TopNoOfMoviesInEachYear;row++){
                HashMap<String,Object> Movie = new HashMap<String,Object>();
                
                //Epoch Time of Scrape
                Movie.put("Epoch Time of Scrape", EpochTime());

                //Year
                Movie.put("Year", Years.get(i).getText());

                //Title
                WebElement Title = MovieList.get(row).findElement(By.xpath("./td[1]"));
                Movie.put("Title", Title.getText());

                //Nomination
                WebElement Nomination = MovieList.get(row).findElement(By.xpath("./td[2]"));
                Movie.put("Nomination", Nomination.getText());

                //Awards
                WebElement Awards = MovieList.get(row).findElement(By.xpath("./td[3]"));
                Movie.put("Awards", Awards.getText());
                
                //isWinner
                List<WebElement> winner = MovieList.get(row).findElements(By.xpath("./td[4][i]"));
                if(winner.size()==0){
                    Movie.put("isWinner", false);
                }else if(winner.size()==1){
                    Movie.put("isWinner", true);
                }

                MoviesDetails.add(Movie);
            }
            
        }

        return MoviesDetails;

    }

    
}
