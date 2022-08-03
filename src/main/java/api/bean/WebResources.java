package api.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Component;

@Component
public class WebResources extends WebProperties.Resources {

    @Autowired
    public WebResources() {
        super();
    }
    
}
