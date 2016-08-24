package de.rwthaachen.cbmb.FormValidator;


import de.rwthaachen.cbmb.Beans.ScannedFileBean;
import de.rwthaachen.cbmb.Utility.ApplicationHelpers;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@ManagedBean
@ViewScoped
public class ScannedFileValidator {

    public void validateForm (ComponentSystemEvent event) throws IOException {
        String alphaNumeric = "^[a-zA-Z0-9]*$";
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        UIInput uiInputImage = (UIInput) components
                .findComponent("scannedImage");
        Part file;
        file = (Part)uiInputImage.getLocalValue();
        String scannedImageId = uiInputImage.getClientId();


        UIInput uiInputfallnummer = (UIInput) components
                .findComponent("fallnummer");
        String fallnummer = uiInputfallnummer.getLocalValue() == null ? ""
                : uiInputfallnummer.getLocalValue().toString();
        String fallnummerId = uiInputImage.getClientId();

        //validate fallnummer
        if (!(fallnummer.matches(alphaNumeric))){
            ApplicationHelpers.setErrorMessage("Only numers and letters are allowed", fallnummerId);
            fc.renderResponse();
        }

        //validate uploaded image
        try (InputStream input = file.getInputStream()) {

            int maxFileSize = 2*1024*1024;
            if (!(file.getSize()<maxFileSize)){
                ApplicationHelpers.setErrorMessage("File size must be less than 2Mb", scannedImageId);
                fc.renderResponse();
            }

            try {
                ImageIO.read(input).toString();
            }
            catch (Exception e) {
                ApplicationHelpers.setErrorMessage("Only BMP, GIF, JPG or PNG type images are allowed", scannedImageId);
                fc.renderResponse();
            }
        }
    }

}
