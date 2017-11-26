package us.martink.stepbystep.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import us.martink.stepbystep.services.ByteProcessor;
import us.martink.stepbystep.ui.model.PhotoRequestForm;
import us.martink.stepbystep.ui.utils.ValidationUtils;

import java.io.IOException;

/**
 * Created by tadas.
 */

@Controller
public class PhotoController {

    private static byte[] image;
    private static PhotoRequestForm photoRequestForm;

    @RequestMapping("/photo")
    public String vector(Model model) {
        model.addAttribute("requestForm", new PhotoRequestForm());
        return "photo";
    }

    @RequestMapping(value = "/photoUpload", method = RequestMethod.POST)
    public String send(@ModelAttribute PhotoRequestForm photoRequest, @RequestParam("myFile") MultipartFile myFile, Model model) {
        String validation = ValidationUtils.validateBeforePhotoSend(photoRequest);
        if (validation != null) {
            model.addAttribute("validation", validation);
            model.addAttribute("requestForm", photoRequest);
            return "photo";
        }

        photoRequestForm = photoRequest;
        try {
            image = myFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoRequest.setDone(true);

        model.addAttribute("requestForm", photoRequest);
        return "photo";
    }

    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") int imageId) throws IOException {

        byte[] imageContent = null;
        switch (imageId) {
            case 1:
                imageContent = image;
                break;
            case 2:
                imageContent = ByteProcessor.withEncoding(photoRequestForm.getMatrix(), photoRequestForm.getP(), image);
                break;
            case 3:
                imageContent = ByteProcessor.withoutEncoding(photoRequestForm.getMatrix(), photoRequestForm.getP(), image);
                break;
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }
}
