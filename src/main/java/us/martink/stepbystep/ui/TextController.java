package us.martink.stepbystep.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import us.martink.stepbystep.ui.model.TextRequestForm;

/**
 * Created by tadas.
 */

@Controller
public class TextController {

    @RequestMapping("/text")
    public String vector (Model model) {
        model.addAttribute("requestForm", new TextRequestForm());
        return "text";
    }

    @RequestMapping(value="/text", method= RequestMethod.POST)
    public String send(@ModelAttribute TextRequestForm vectorRequest, Model model) {

        return "text";
    }
}
