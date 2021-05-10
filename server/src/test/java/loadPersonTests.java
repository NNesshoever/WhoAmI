import dtos.PersonDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import server.JsonService;

import java.util.List;

public class loadPersonTests {


    @Test
    public void readJson(){
        List<PersonDto> person = JsonService.loadJson();
        Assert.assertTrue(person.size()>1);
    }

    @Test
    public void getPersonsInfo(){
        List<PersonDto> person = JsonService.loadJson();
        Assert.assertTrue(person.get(0).getId()==1);
    }

}