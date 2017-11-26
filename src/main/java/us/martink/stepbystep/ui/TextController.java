package us.martink.stepbystep.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import us.martink.stepbystep.services.TextProcessor;
import us.martink.stepbystep.ui.model.Text;
import us.martink.stepbystep.ui.model.TextRequestForm;
import us.martink.stepbystep.ui.utils.ValidationUtils;

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
        String validation = ValidationUtils.validateBeforeTextEncoding(vectorRequest);
        if (validation != null) {
            model.addAttribute("validation", validation);
            model.addAttribute("requestForm", vectorRequest);
            return "text";
        }

        //Tekstas siunciamas su kodavimu
        String withEncoding = TextProcessor.withEncoding(vectorRequest.getMatrix(), vectorRequest.getP(), vectorRequest.getEnteredText().getTextValue());
        vectorRequest.setEncodedText(new Text(withEncoding));

        //Tekstas siunciamas be kodavimo
        String withoutEncoding = TextProcessor.withoutEncoding(vectorRequest.getMatrix(), vectorRequest.getP(), vectorRequest.getEnteredText().getTextValue());
        vectorRequest.setPlainText(new Text(withoutEncoding));

        model.addAttribute("requestForm", vectorRequest);
        return "text";
    }
}
