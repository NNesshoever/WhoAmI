import models.Person;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import utils.JsonService;

import java.util.List;

public class loadPersonTests {


    @Test
    public void readJson(){
        List<Person> person = JsonService.loadJson();
        Assert.assertTrue(person.size()>1);
    }

    @Test
    public void getPersonsInfo(){
        List<Person> person = JsonService.loadJson();
        Assert.assertTrue(person.get(0).getId()==1);
    }

}